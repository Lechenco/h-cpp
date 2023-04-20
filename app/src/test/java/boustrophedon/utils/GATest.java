package boustrophedon.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class GATest {

    @Test
    public void constructorTest() {
        GATest gaTest = new GATest();

        assertNotEquals(null, gaTest);
    }
}