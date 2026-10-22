import java.security.MessageDigest
import java.util.zip.ZipFile

group = "app.hxreborn"

patches {
    about {
        name = "hxreborn’s patches"
        description = "Patches for the apps I actually use"
        source = "git@github.com:hxreborn/morphe-patches.git"
        author = "hxreborn"
        contact = "https://github.com/hxreborn/morphe-patches/issues"
        website = "https://github.com/hxreborn/morphe-patches"
        license = "GPLv3"
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

// Matches the plugin's targetCompatibility 11 for this module's Java sources
java {
    sourceCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.morphe.patches.library)
    implementation(libs.pngj)
    testImplementation(kotlin("test"))
}

tasks {
    jar {
        exclude("ar/com/hjg/pngj/pixels/ImageLine*.class")
        exclude("LICENSE.txt", "NOTICE.txt")
        from(rootProject.file("LICENSE"))
        from(rootProject.file("NOTICE"))
        from(rootProject.file("third-party/licenses")) { into("META-INF/third-party/licenses") }

        doLast {
            val desktopOnlyPackages = listOf("java/awt/", "javax/imageio/", "javax/swing/")
            val offenders = ZipFile(archiveFile.get().asFile).use { zip ->
                zip.entries().asSequence()
                    .filter { it.name.endsWith(".class") }
                    .filter { entry ->
                        val text = zip.getInputStream(entry).readBytes().toString(Charsets.ISO_8859_1)
                        desktopOnlyPackages.any(text::contains)
                    }
                    .map { it.name }
                    .toList()
            }
            if (offenders.isNotEmpty()) {
                throw GradleException("Android has no desktop JDK, referenced from: $offenders")
            }

            val binaryMagics = listOf(
                "dex\n".toByteArray(),
                byteArrayOf(0x7F, 0x45, 0x4C, 0x46),
                byteArrayOf(0x50, 0x4B, 0x03, 0x04),
                "MDLT".toByteArray(),
                byteArrayOf(0x00, 0x49, 0x41, 0x50, 0x02),
                byteArrayOf(0x28, 0xB5.toByte(), 0x2F, 0xFD.toByte()),
                byteArrayOf(0x1F, 0x8B.toByte()),
                "SQLite format 3\u0000".toByteArray(),
                byteArrayOf(0x02, 0x00, 0x0C, 0x00),
            )
            fun sha256(bytes: ByteArray) = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
            val registeredHashes = rootProject.file(".github/vendor-content-allowlist.txt").readLines()
                .filter { it.startsWith("provenance ") }
                .flatMap { rootProject.file(it.split(" ")[1]).readLines() }
                .mapNotNull { Regex("^([0-9a-f]{64})  ").find(it)?.groupValues?.get(1) }
            val builtExtension = layout.buildDirectory.file("resources/main/extensions/extension.mpe").get().asFile
            val ownHashes = registeredHashes.toSet() + listOfNotNull(builtExtension.takeIf { it.exists() }?.let { sha256(it.readBytes()) })
            val unregistered = ZipFile(archiveFile.get().asFile).use { zip ->
                zip.entries().asSequence()
                    .filter { !it.isDirectory && !it.name.endsWith(".class") }
                    .map { it.name to zip.getInputStream(it).readBytes() }
                    .filter { (_, bytes) -> binaryMagics.any { magic -> bytes.size >= magic.size && magic.indices.all { bytes[it] == magic[it] } } }
                    .filter { (_, bytes) -> sha256(bytes) !in ownHashes }
                    .toList()
            }
            val dependencyHashes = if (unregistered.isEmpty()) emptySet() else configurations.runtimeClasspath.get().files
                .filter { it.extension == "jar" }
                .flatMapTo(mutableSetOf()) { jar ->
                    ZipFile(jar).use { zip -> zip.entries().asSequence().filter { !it.isDirectory }.map { sha256(zip.getInputStream(it).readBytes()) }.toList() }
                }
            val unregisteredNames = unregistered.filter { (_, bytes) -> sha256(bytes) !in dependencyHashes }.map { it.first }
            if (unregisteredNames.isNotEmpty()) {
                throw GradleException("Unregistered binary resource in the bundle: $unregisteredNames")
            }
        }
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("proton-bundle-version.txt") {
            expand("version" to project.version)
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("app.morphe.util.PatchListGeneratorKt")
    }

    publish {
        dependsOn("generatePatchesList")
    }
}
