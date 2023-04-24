package boustrophedon.utils;

import static org.junit.Assert.*;

import org.junit.Test;

import boustrophedon.provider.Point;

public class GATest {
    private static final double DOUBLE_DELTA = 0.00001;
    @Test
    public void testConstructor() {
        GATest gaTest = new GATest();

        assertNotEquals(null, gaTest);
    }

    @Test
    public void testCalcYPoint() {
        assertEquals(0, GA.calcYPoint(0, 0, 0), DOUBLE_DELTA);
        assertEquals(0, GA.calcYPoint(0, 0, 1), DOUBLE_DELTA);
        assertEquals(0, GA.calcYPoint(0, 0, -1), DOUBLE_DELTA);

        assertEquals(0, GA.calcYPoint(1, 0, 0), DOUBLE_DELTA);
        assertEquals(1, GA.calcYPoint(1, 0, 1), DOUBLE_DELTA);
        assertEquals(-1, GA.calcYPoint(1, 0, -1), DOUBLE_DELTA);

        assertEquals(0, GA.calcYPoint(5, 0, 0), DOUBLE_DELTA);
        assertEquals(5, GA.calcYPoint(5, 0, 1), DOUBLE_DELTA);
        assertEquals(-5, GA.calcYPoint(5, 0, -1), DOUBLE_DELTA);

        assertEquals(1, GA.calcYPoint(1, 1, 0), DOUBLE_DELTA);
        assertEquals(2, GA.calcYPoint(1, 1, 1), DOUBLE_DELTA);
        assertEquals(0, GA.calcYPoint(1, 1, -1), DOUBLE_DELTA);
    }
    @Test
    public void testCalcYPointWithVector() {
        assertEquals(0, GA.calcYPoint(new double[]{0, 0}, 0), DOUBLE_DELTA);
        assertEquals(0, GA.calcYPoint(new double[]{0, 0}, 1), DOUBLE_DELTA);
        assertEquals(0, GA.calcYPoint(new double[]{0, 0}, -1), DOUBLE_DELTA);

        assertEquals(0, GA.calcYPoint(new double[]{1, 0}, 0), DOUBLE_DELTA);
        assertEquals(1, GA.calcYPoint(new double[]{1, 0}, 1), DOUBLE_DELTA);
        assertEquals(-1, GA.calcYPoint(new double[]{1, 0}, -1), DOUBLE_DELTA);

        assertEquals(0, GA.calcYPoint(new double[]{5, 0}, 0), DOUBLE_DELTA);
        assertEquals(5, GA.calcYPoint(new double[]{5, 0}, 1), DOUBLE_DELTA);
        assertEquals(-5, GA.calcYPoint(new double[]{5, 0}, -1), DOUBLE_DELTA);

        assertEquals(1, GA.calcYPoint(new double[]{1, 1}, 0), DOUBLE_DELTA);
        assertEquals(2, GA.calcYPoint(new double[]{1, 1}, 1), DOUBLE_DELTA);
        assertEquals(0, GA.calcYPoint(new double[]{1, 1}, -1), DOUBLE_DELTA);
    }

    @Test
    public void testCalcDistance() {
        assertEquals(0, GA.calcDistance(0,0,0,0), DOUBLE_DELTA);
        assertEquals(1, GA.calcDistance(0,0,0,1), DOUBLE_DELTA);
        assertEquals(Math.sqrt(2), GA.calcDistance(0,0,1,1), DOUBLE_DELTA);
        assertEquals(Math.sqrt(2), GA.calcDistance(0,0,-1,-1), DOUBLE_DELTA);

        assertEquals(1, GA.calcDistance(1,0,0,0), DOUBLE_DELTA);
        assertEquals(Math.sqrt(2), GA.calcDistance(1,1,0,0), DOUBLE_DELTA);
        assertEquals(Math.sqrt(2), GA.calcDistance(-1,-1,0,0), DOUBLE_DELTA);
    }
    @Test
    public void testCalcDistanceWithPoint() {
        assertEquals(0, GA.calcDistance(new Point(0, 0), new Point(0,0)), DOUBLE_DELTA);
        assertEquals(1, GA.calcDistance(new Point(0, 0), new Point(1,0)), DOUBLE_DELTA);
        assertEquals(Math.sqrt(2), GA.calcDistance(new Point(0, 0), new Point(1,1)), DOUBLE_DELTA);

        assertEquals(1, GA.calcDistance(new Point(-1, 0), new Point(0,0)), DOUBLE_DELTA);
        assertEquals(1, GA.calcDistance(new Point(1, 0), new Point(0,0)), DOUBLE_DELTA);
        assertEquals(Math.sqrt(2), GA.calcDistance(new Point(1, 1), new Point(0,0)), DOUBLE_DELTA);
    }

    @Test
    public void testGetAngularCofficient() throws Exception {
        assertThrows("deltaX can't be equals to zero", Exception.class, () -> GA.calcAngularCoefficient(0, 0));
        assertThrows("deltaX can't be equals to zero", Exception.class,() -> GA.calcAngularCoefficient(0, 5));

        assertEquals(0, GA.calcAngularCoefficient(1, 0), DOUBLE_DELTA);
        assertEquals(0, GA.calcAngularCoefficient(-1, 0), DOUBLE_DELTA);

        assertEquals(1, GA.calcAngularCoefficient(1, 1), DOUBLE_DELTA);
        assertEquals(-1, GA.calcAngularCoefficient(-1, 1), DOUBLE_DELTA);
    }
}