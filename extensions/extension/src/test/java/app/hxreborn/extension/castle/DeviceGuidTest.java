/*
 * Copyright (C) 2026 hxreborn
 * SPDX-License-Identifier: GPL-3.0-only
 */
package app.hxreborn.extension.castle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class DeviceGuidTest {

    @Test
    public void createsA36CharacterHexGuid() {
        // when
        String guid = DeviceGuid.newGuid();

        // then
        assertTrue(guid.matches("[0-9a-f]{36}"));
    }

    @Test
    public void createsADifferentGuidEachTime() {
        // when
        String first = DeviceGuid.newGuid();
        String second = DeviceGuid.newGuid();

        // then
        assertNotEquals(first, second);
    }

    @Test
    public void keepsTheResponseWhenTheDeviceIsNotBlacklisted() {
        // when
        Boolean result = DeviceGuid.switchGuidIfBlacklisted(Boolean.FALSE);

        // then
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void keepsAMissingFlagMissing() {
        // when
        Boolean result = DeviceGuid.switchGuidIfBlacklisted(null);

        // then
        assertNull(result);
    }

    @Test
    public void clearsTheFlagAndSwitchesGuidWhenTheDeviceIsBlacklisted() {
        // when
        Boolean result = DeviceGuid.switchGuidIfBlacklisted(Boolean.TRUE);

        // then
        assertEquals(Boolean.FALSE, result);
        assertTrue(DeviceGuid.switchedGuid().matches("[0-9a-f]{36}"));
    }

}
