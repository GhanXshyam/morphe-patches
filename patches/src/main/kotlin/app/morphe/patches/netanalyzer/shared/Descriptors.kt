/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.morphe.patches.netanalyzer.shared


private const val CLASS_DESCRIPTOR_PREFIX = "L"
private const val CLASS_DESCRIPTOR_SUFFIX = ";"

internal fun String.toJavaName() =
    removePrefix(CLASS_DESCRIPTOR_PREFIX).removeSuffix(CLASS_DESCRIPTOR_SUFFIX).replace('/', '.')
