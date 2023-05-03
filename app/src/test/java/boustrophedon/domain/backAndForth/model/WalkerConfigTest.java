package boustrophedon.domain.backAndForth.model;

import static org.junit.Assert.*;

import org.junit.Test;

import boustrophedon.domain.backAndForth.error.AngleOffLimitsException;

public class WalkerConfigTest {
    @Test
    public void testConstructorAndDefaultConfig() {
        WalkerConfig config = new WalkerConfig();

        assertNotNull(config);
        assertEquals(0.00009, config.getDistanceBetweenPaths(), 0);
    }

    @Test
    public void testConstructorWithDistance() {
        WalkerConfig config = new WalkerConfig(5);

        assertNotNull(config);
        assertEquals(5, config.getDistanceBetweenPaths(), 0);
    }

    @Test
    public void testConstructorWithDistanceAndDirection() {
        try {
            WalkerConfig config = new WalkerConfig(5, Math.PI / 2);
            assertNotNull(config);
            assertEquals(5, config.getDistanceBetweenPaths(), 0);
            assertEquals(Math.PI / 2, config.getDirection(), 0);
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetDirection() {
        WalkerConfig config = new WalkerConfig();

        assertThrows(AngleOffLimitsException.class, () -> config.setDirection(4));
        assertThrows(AngleOffLimitsException.class, () -> config.setDirection(-4));
    }
}