/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.shared.misc.secshell

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.ResourcePatchContext
import com.android.tools.smali.dexlib2.Opcodes
import com.android.tools.smali.dexlib2.dexbacked.DexBackedDexFile
import java.security.MessageDigest
import java.util.Base64

private const val PAYLOAD_JAR = "assets/classes0.jar"
private const val SHELL_DEX = "classes.dex"
private const val EXTENSION_DEX = "extensions/extension.mpe"
private const val JAR_MANIFEST = "META-INF/MANIFEST.MF"

internal interface SecShellContainer {
    fun dexEntries(): List<String>

    fun read(name: String): ByteArray

    fun withEntries(replacements: Map<String, ByteArray>): ByteArray
}

internal class SecShellPayload(private val container: SecShellContainer) {
    private val editors = LinkedHashMap<String, SecShellDex>()

    fun dexContaining(classType: String) = singleDex(classType) { it.hasClass(classType) }

    fun replaceBody(classType: String, name: String, registers: Int, smali: String) =
        editor(dexContaining(classType)).replaceBody(classType, name, registers, smali)

    fun method(classType: String, name: String) = editor(dexContaining(classType)).method(classType, name)

    fun prependToMethodUsingString(string: String, firstParameter: String, smali: String): String {
        val entry = singleDex("\"$string\"") { it.hasString(string) }
        val method = editor(entry).methodUsingString(string, firstParameter)
        method.addInstructions(0, smali)
        return method.definingClass
    }

    fun addExtensionPackage(packageType: String, besideClass: String) {
        val extension = SecShellPayload::class.java.classLoader.getResourceAsStream(EXTENSION_DEX)
            ?.use { DexBackedDexFile.fromInputStream(Opcodes.getDefault(), it.buffered()) }
            ?: throw PatchException("$EXTENSION_DEX is missing from the patch bundle")
        val classes = extension.classes.filter { it.type.startsWith(packageType) }
        if (classes.isEmpty()) throw PatchException("$EXTENSION_DEX has no class in $packageType")

        val editor = editor(dexContaining(besideClass))
        classes.forEach(editor::addClass)
    }

    fun sealed(): ByteArray =
        container.withEntries(editors.filterValues { it.isEdited }.mapValues { it.value.toBytes() })

    private fun singleDex(subject: String, holds: (SecShellDex) -> Boolean): String {
        val owners = container.dexEntries().filter { holds(editor(it)) }
        return owners.singleOrNull()
            ?: throw PatchException("Expected $subject in one payload dex, found ${owners.size}")
    }

    private fun editor(entry: String) = editors.getOrPut(entry) { SecShellDex(container.read(entry)) }
}

internal fun ResourcePatchContext.editSecShellPayload(block: SecShellPayload.() -> Unit) {
    val jar = get(PAYLOAD_JAR)
    val entryName = if (jar.exists()) PAYLOAD_JAR else SHELL_DEX
    val file = get(entryName)
    val bytes = file.readBytes()
    val container = when {
        jar.exists() -> SecShellJar.of(bytes)
        SecShellEmbeddedDex.isEmbedded(bytes) -> SecShellEmbeddedDex(bytes)
        else -> throw PatchException("Neither $PAYLOAD_JAR nor a payload in $SHELL_DEX, this is not a SecShell build")
    }

    val payload = SecShellPayload(container)
    payload.block()
    val sealed = payload.sealed()
    file.writeBytes(sealed)
    updateManifestDigest(entryName, sealed)
}

private fun ResourcePatchContext.updateManifestDigest(entryName: String, bytes: ByteArray) {
    val manifest = get(JAR_MANIFEST)
    if (!manifest.exists()) return

    val text = manifest.readText()
    val entryDigest = Regex("\\r?\\nName: ${Regex.escape(entryName)}\\r?\\n([A-Za-z0-9-]+)-Digest: ([A-Za-z0-9+/=]+)")
    val match = entryDigest.find(text) ?: throw PatchException("$JAR_MANIFEST has no digest for $entryName")
    val algorithm = match.groupValues[1].replace("SHA1", "SHA-1")
    val digest = Base64.getEncoder().encodeToString(MessageDigest.getInstance(algorithm).digest(bytes))
    manifest.writeText(text.replaceRange(match.groups[2]!!.range, digest))
}
