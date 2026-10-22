/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.musicolet.misc.pairip

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patches.shared.misc.generated.addDefaultConstructor
import app.morphe.patches.shared.misc.generated.replaceMethod
import app.morphe.patches.shared.misc.pairip.removePairipVirtualizationPatch
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private const val VM_RUNNER = "Lcom/pairip/VMRunner;"
private const val APP_PACKAGE = "Lin/krosbits/musicolet"
private const val DO_IN_BACKGROUND = "doInBackground"
private const val ON_RECEIVE = "onReceive"

private class RestoredMethod(val type: String, val name: String, val resource: String)

private val RESTORED_METHODS = listOf(
    RestoredMethod("La61;", DO_IN_BACKGROUND, "a61DoInBackground"),
    RestoredMethod("Lon1;", DO_IN_BACKGROUND, "on1DoInBackground"),
    RestoredMethod("Lnn1;", DO_IN_BACKGROUND, "nn1DoInBackground"),
    RestoredMethod("Lht0;", DO_IN_BACKGROUND, "ht0DoInBackground"),
    RestoredMethod("Lkb;", DO_IN_BACKGROUND, "kbDoInBackground"),
    RestoredMethod("Lc51;", DO_IN_BACKGROUND, "c51DoInBackground"),
    RestoredMethod("Lez0;", DO_IN_BACKGROUND, "ez0DoInBackground"),
    RestoredMethod("Ljm1;", DO_IN_BACKGROUND, "jm1DoInBackground"),
    RestoredMethod("$APP_PACKAGE/fh;", ON_RECEIVE, "onReceiveFh"),
    RestoredMethod("$APP_PACKAGE/RemoteMediaControlReceiver;", ON_RECEIVE, "onReceiveRemoteMediaControlReceiver"),
    RestoredMethod("$APP_PACKAGE/MediaMountedReceiver;", ON_RECEIVE, "onReceiveMediaMountedReceiver"),
)

private val UNRESTORED_STUB_TYPES = setOf(
    "Lca2;",
    "Lci2;",
    "Ld9;",
    "Lov;",
    "Landroidx/mediarouter/media/MediaTransferReceiver;",
    "Landroidx/work/impl/background/systemalarm/RescheduleReceiver;",
    "Landroidx/work/impl/diagnostics/DiagnosticsReceiver;",
    "Landroidx/work/impl/utils/ForceStopRunnable\$BroadcastReceiver;",
    "Lcom/google/android/datatransport/runtime/scheduling/jobscheduling/AlarmManagerSchedulerBroadcastReceiver;",
)

private fun Method.callsVmRunner() = implementation?.instructions?.any { instruction ->
    ((instruction as? ReferenceInstruction)?.reference as? MethodReference)?.definingClass == VM_RUNNER
} == true

val restoreVirtualizedMethodsPatch = bytecodePatch {
    compatibleWith(AppCompatibilities.MUSICOLET)

    dependsOn(removePairipVirtualizationPatch)

    execute {
        RESTORED_METHODS.forEach { restored ->
            mutableClassDefBy(restored.type).apply {
                check(methods.single { it.name == restored.name }.callsVmRunner()) {
                    "${restored.type}->${restored.name} is not a PairIP stub in this build"
                }
                replaceMethod(restored.name, "musicolet/restored", restored.resource)
            }
        }
        mutableClassDefBy("Lhm1;").addDefaultConstructor()

        val leftover = mutableSetOf<String>()
        classDefForEach { classDef ->
            if (!classDef.type.startsWith("Lcom/pairip/") && classDef.methods.any { it.callsVmRunner() }) {
                leftover += classDef.type
            }
        }
        check(leftover == UNRESTORED_STUB_TYPES) {
            "PairIP stubs changed: unexpected ${leftover - UNRESTORED_STUB_TYPES}, missing ${UNRESTORED_STUB_TYPES - leftover}"
        }
    }
}
