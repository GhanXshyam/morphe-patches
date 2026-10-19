/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.notein.misc.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.patches.shared.misc.pairip.removePairipProtectionPatch
import app.morphe.util.matchSingle

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock premium",
    description = "Removes the notebook limit and unlocks paid features the app checks locally. " +
        "AI and cloud sync are controlled by the server.",
) {
    compatibleWith(AppCompatibilities.NOTEIN)
    dependsOn(removePairipProtectionPatch)

    execute {
        MembershipGetTierFingerprint.matchSingle().method.apply {
            addInstructions(
                0,
                """
                    sget-object p0, $returnType->PRO:$returnType
                    return-object p0
                """,
            )
        }
    }
}
