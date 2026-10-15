/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.perplexity.misc.upselling

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.perplexity.misc.returnUnitEarly
import app.morphe.patches.shared.compat.AppCompatibilities
import app.morphe.util.getFreeRegisterProvider
import app.morphe.util.matchSingle
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

@Suppress("unused")
val hideUpgradePromotionsPatch = bytecodePatch(
    name = "Hide upgrade promotions",
    description = "Hides the subscription offer after signing in, the Upgrade plan banner and home screen " +
        "promotions. Plan limits still apply.",
) {
    compatibleWith(AppCompatibilities.PERPLEXITY)

    execute {
        val userDataType = SignInFunnelUserDataToStringFingerprint.matchSingle().classDef.type
        skipPaywallAfterSignInFingerprint(userDataType).matchSingle().method.returnEarly(true)

        returnUnitEarly(HomeUpsellFetchFingerprint.matchSingle().method)

        if (UpgradePlanBannerToStringFingerprint.matchOrNull() != null) {
            UpgradePlanBannerFlagFingerprint.matchSingle().apply {
                val index = instructionMatches.last().index
                val bannerFlag = method.getInstruction<ReferenceInstruction>(index).reference
                val homeStateRegister = method.getInstruction<TwoRegisterInstruction>(index).registerB
                val falseRegister = method.getFreeRegisterProvider(index + 1, 1).getFreeRegister4Bit()

                method.addInstructions(
                    index + 1,
                    """
                        const/4 v$falseRegister, 0x0
                        iput-boolean v$falseRegister, v$homeStateRegister, $bannerFlag
                    """,
                )
            }
        }
    }
}
