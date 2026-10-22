/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.shared.misc.pairip

import app.morphe.patcher.util.proxy.mutableTypes.MutableClass
import app.morphe.patches.shared.misc.smali.replaceMethod
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private const val VM_RUNNER = "Lcom/pairip/VMRunner;"

private fun Method.callsVmRunner() = implementation?.instructions?.any { instruction ->
    ((instruction as? ReferenceInstruction)?.reference as? MethodReference)?.definingClass == VM_RUNNER
} == true

internal fun MutableClass.replaceVirtualizedMethod(name: String, registers: Int, smali: String) {
    check(methods.single { it.name == name }.callsVmRunner()) {
        "$type->$name is not a PairIP stub in this build"
    }
    replaceMethod(name, registers, smali)
}
