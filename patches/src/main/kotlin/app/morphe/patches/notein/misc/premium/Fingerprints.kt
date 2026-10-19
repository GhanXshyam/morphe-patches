/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.notein.misc.premium

import app.morphe.patcher.Fingerprint

internal object MembershipToStringFingerprint : Fingerprint(
    name = "toString",
    returnType = "Ljava/lang/String;",
    parameters = emptyList(),
    strings = listOf("Membership(uid="),
)

internal object MembershipGetTierFingerprint : Fingerprint(
    classFingerprint = MembershipToStringFingerprint,
    name = "getTier",
    parameters = emptyList(),
)
