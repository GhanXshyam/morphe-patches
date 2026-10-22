/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.shared.misc.revenuecat

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.BytecodePatchContext
import app.morphe.util.getFreeRegisterProvider
import app.morphe.util.matchSingle

private const val EXTENSION_CLASS = "Lapp/hxreborn/extension/shared/RevenueCatUnlock;"

internal fun BytecodePatchContext.grantRevenueCatEntitlements(vararg entitlements: Pair<String, String>) {
    BuildCustomerInfoFingerprint.matchSingle().method.apply {
        val registers = getFreeRegisterProvider(0, 3)
        val jsonRegister = registers.getFreeRegister4Bit()
        val entitlementRegister = registers.getFreeRegister4Bit()
        val productRegister = registers.getFreeRegister4Bit()

        val grants = entitlements.joinToString("\n") { (entitlement, product) ->
            """
                const-string v$entitlementRegister, "$entitlement"
                const-string v$productRegister, "$product"
                invoke-static { v$jsonRegister, v$entitlementRegister, v$productRegister }, $EXTENSION_CLASS->grantEntitlement(Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/String;)V
            """
        }

        addInstructions(0, "move-object/from16 v$jsonRegister, ${customerInfoJsonParameter()}\n$grants")
    }
}
