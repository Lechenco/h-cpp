package boustrophedon.utils;

import static org.junit.Assert.*;

import static boustrophedon.constants.AngleConstants.NINETY_DEGREES;
import static boustrophedon.constants.AngleConstants.ZERO_DEGREES;
import static boustrophedon.constants.PrecisionConstants.DISTANCE_PRECISION;

import org.junit.Test;

import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;

public class GATest {
    @Test
    public void testConstructor() {
        GATest gaTest = new GATest();

        assertNotEquals(null, gaTest);
    }

    @Test
    public void testCalcYPoint() {
        assertEquals(0, GA.calcYPoint(0, 0, 0), DISTANCE_PRECISION);
        assertEquals(0, GA.calcYPoint(0, 0, 1), DISTANCE_PRECISION);
        assertEquals(0, GA.calcYPoint(0, 0, -1), DISTANCE_PRECISION);

        assertEquals(0, GA.calcYPoint(1, 0, 0), DISTANCE_PRECISION);
        assertEquals(1, GA.calcYPoint(1, 0, 1), DISTANCE_PRECISION);
        assertEquals(-1, GA.calcYPoint(1, 0, -1), DISTANCE_PRECISION);

        assertEquals(0, GA.calcYPoint(5, 0, 0), DISTANCE_PRECISION);
        assertEquals(5, GA.calcYPoint(5, 0, 1), DISTANCE_PRECISION);
        assertEquals(-5, GA.calcYPoint(5, 0, -1), DISTANCE_PRECISION);

        assertEquals(1, GA.calcYPoint(1, 1, 0), DISTANCE_PRECISION);
        assertEquals(2, GA.calcYPoint(1, 1, 1), DISTANCE_PRECISION);
        assertEquals(0, GA.calcYPoint(1, 1, -1), DISTANCE_PRECISION);
    }
    @Test
    public void testCalcYPointWithVector() {
        assertEquals(0, GA.calcYPoint(new double[]{0, 0}, 0), DISTANCE_PRECISION);
        assertEquals(0, GA.calcYPoint(new double[]{0, 0}, 1), DISTANCE_PRECISION);
        assertEquals(0, GA.calcYPoint(new double[]{0, 0}, -1), DISTANCE_PRECISION);

        assertEquals(0, GA.calcYPoint(new double[]{1, 0}, 0), DISTANCE_PRECISION);
        assertEquals(1, GA.calcYPoint(new double[]{1, 0}, 1), DISTANCE_PRECISION);
        assertEquals(-1, GA.calcYPoint(new double[]{1, 0}, -1), DISTANCE_PRECISION);

        assertEquals(0, GA.calcYPoint(new double[]{5, 0}, 0), DISTANCE_PRECISION);
        assertEquals(5, GA.calcYPoint(new double[]{5, 0}, 1), DISTANCE_PRECISION);
        assertEquals(-5, GA.calcYPoint(new double[]{5, 0}, -1), DISTANCE_PRECISION);

        assertEquals(1, GA.calcYPoint(new double[]{1, 1}, 0), DISTANCE_PRECISION);
        assertEquals(2, GA.calcYPoint(new double[]{1, 1}, 1), DISTANCE_PRECISION);
        assertEquals(0, GA.calcYPoint(new double[]{1, 1}, -1), DISTANCE_PRECISION);
    }

    @Test
    public void testCalcDistance() {
        assertEquals(0, GA.calcDistance(0,0,0,0), DISTANCE_PRECISION);
        assertEquals(1, GA.calcDistance(0,0,0,1), DISTANCE_PRECISION);
        assertEquals(Math.sqrt(2), GA.calcDistance(0,0,1,1), DISTANCE_PRECISION);
        assertEquals(Math.sqrt(2), GA.calcDistance(0,0,-1,-1), DISTANCE_PRECISION);

        assertEquals(1, GA.calcDistance(1,0,0,0), DISTANCE_PRECISION);
        assertEquals(Math.sqrt(2), GA.calcDistance(1,1,0,0), DISTANCE_PRECISION);
        assertEquals(Math.sqrt(2), GA.calcDistance(-1,-1,0,0), DISTANCE_PRECISION);
    }
    @Test
    public void testCalcDistanceWithPoint() {
        assertEquals(0, GA.calcDistance(new Point(0, 0), new Point(0,0)), DISTANCE_PRECISION);
        assertEquals(1, GA.calcDistance(new Point(0, 0), new Point(1,0)), DISTANCE_PRECISION);
        assertEquals(Math.sqrt(2), GA.calcDistance(new Point(0, 0), new Point(1,1)), DISTANCE_PRECISION);

        assertEquals(1, GA.calcDistance(new Point(-1, 0), new Point(0,0)), DISTANCE_PRECISION);
        assertEquals(1, GA.calcDistance(new Point(1, 0), new Point(0,0)), DISTANCE_PRECISION);
        assertEquals(Math.sqrt(2), GA.calcDistance(new Point(1, 1), new Point(0,0)), DISTANCE_PRECISION);
    }
    @Test
    public void testCalcDistanceWithBorderAndPoint() {
        Border border = new Border(new Point(0, 0), new Point(5, 5));
        assertEquals(0, GA.calcDistance(border, new Point(0,0)), DISTANCE_PRECISION);
        assertEquals(0, GA.calcDistance(border, new Point(1,1)), DISTANCE_PRECISION);
        assertEquals(Math.sqrt(0.5), GA.calcDistance(border, new Point(2,1)), DISTANCE_PRECISION);
        assertEquals(Math.sqrt(0.5), GA.calcDistance(border, new Point(0,1)), DISTANCE_PRECISION);
        assertEquals(0, GA.calcDistance(border, new Point(-1,-1)), DISTANCE_PRECISION);

        Border border1 = new Border(new Point(0, 0), new Point(0, 5));
        assertEquals(0, GA.calcDistance(border1, new Point(0,0)), DISTANCE_PRECISION);
        assertEquals(1, GA.calcDistance(border1, new Point(1,0)), DISTANCE_PRECISION);
        assertEquals(0, GA.calcDistance(border1, new Point(0,1)), DISTANCE_PRECISION);
    }

    @Test
    public void testGetAngularCoefficient() throws Exception {
        assertThrows("deltaX can't be equals to zero", Exception.class, () -> GA.calcAngularCoefficient(0, 0));
        assertThrows("deltaX can't be equals to zero", Exception.class,() -> GA.calcAngularCoefficient(0, 5));

        assertEquals(0, GA.calcAngularCoefficient(1, 0), DISTANCE_PRECISION);
        assertEquals(0, GA.calcAngularCoefficient(-1, 0), DISTANCE_PRECISION);

        assertEquals(1, GA.calcAngularCoefficient(1, 1), DISTANCE_PRECISION);
        assertEquals(-1, GA.calcAngularCoefficient(-1, 1), DISTANCE_PRECISION);
    }
    @Test
    public void testCalcLinearCoefficient() {
        assertEquals(0, GA.calcLinearCoefficient(0, 0, 0), DISTANCE_PRECISION);
        assertEquals(0, GA.calcLinearCoefficient(0, 1, 0), DISTANCE_PRECISION);
        assertEquals(-1, GA.calcLinearCoefficient(1, 1, 0), DISTANCE_PRECISION);
        assertEquals(1, GA.calcLinearCoefficient(1, 0, 1), DISTANCE_PRECISION);
    }
    @Test
    public void testGetCoefficients() throws Exception {
        double[] coefficients;
        assertThrows("deltaX can't be equals to zero", Exception.class, () -> GA.getCoefficients(0, 0, 0, 0));

        coefficients = GA.getCoefficients(0,0,1,1);
        assertEquals(1, coefficients[0], DISTANCE_PRECISION);
        assertEquals(0, coefficients[1], DISTANCE_PRECISION);
    }
    @Test
    public void testGetCoefficientsWithPoints() throws Exception {
        double[] coefficients;
        assertThrows("deltaX can't be equals to zero", Exception.class, () -> GA.getCoefficients(new Point(0,0),new Point(0,0)));

        coefficients = GA.getCoefficients(new Point(0,0),new Point(1,1));
        assertEquals(1, coefficients[0], DISTANCE_PRECISION);
        assertEquals(0, coefficients[1], DISTANCE_PRECISION);
    }
    @Test
    public void testCalcParallelLineCoefficients() {
        double[] coefficients;

        coefficients = GA.calcParallelLineCoefficients(0, 0,0);
        assertEquals(0, coefficients[0], DISTANCE_PRECISION);
        assertEquals(0, coefficients[1], DISTANCE_PRECISION);

        coefficients = GA.calcParallelLineCoefficients(1, 0,5);
        assertEquals(1, coefficients[0], DISTANCE_PRECISION);
        assertEquals(5, coefficients[1], DISTANCE_PRECISION);

        coefficients = GA.calcParallelLineCoefficients(0, 0,5);
        assertEquals(0, coefficients[0], DISTANCE_PRECISION);
        assertEquals(5, coefficients[1], DISTANCE_PRECISION);
    }
    @Test
    public void testCalcParallelLineCoefficientsWithPoint() {
        double[] coefficients;

        coefficients = GA.calcParallelLineCoefficients(0, new Point(0,0));
        assertEquals(0, coefficients[0], DISTANCE_PRECISION);
        assertEquals(0, coefficients[1], DISTANCE_PRECISION);

        coefficients = GA.calcParallelLineCoefficients(1, new Point(0,5));
        assertEquals(1, coefficients[0], DISTANCE_PRECISION);
        assertEquals(5, coefficients[1], DISTANCE_PRECISION);

        coefficients = GA.calcParallelLineCoefficients(0, new Point(0,5));
        assertEquals(0, coefficients[0], DISTANCE_PRECISION);
        assertEquals(5, coefficients[1], DISTANCE_PRECISION);
    }

    @Test
    public void testCalcDistanceWithAngle() {
        assertEquals(0, GA.calcDistanceWithDirection(0,0,0,0, ZERO_DEGREES), DISTANCE_PRECISION);
        assertEquals(0, GA.calcDistanceWithDirection(0,0,0,1, ZERO_DEGREES), DISTANCE_PRECISION);
        assertEquals(1, GA.calcDistanceWithDirection(0,0,0,1, NINETY_DEGREES), DISTANCE_PRECISION);
        assertEquals(-1, GA.calcDistanceWithDirection(0,0,0,1, -NINETY_DEGREES), DISTANCE_PRECISION);
    }
    @Test
    public void testCalcDistanceWithAnglePoint() {
        assertEquals(0, GA.calcDistanceWithDirection(new Point(0,0), new Point(0,0), ZERO_DEGREES), DISTANCE_PRECISION);
        assertEquals(0, GA.calcDistanceWithDirection(new Point(0,0), new Point(0,1), ZERO_DEGREES), DISTANCE_PRECISION);
        assertEquals(1, GA.calcDistanceWithDirection(new Point(0,0), new Point(0,1),NINETY_DEGREES), DISTANCE_PRECISION);
        assertEquals(-1, GA.calcDistanceWithDirection(new Point(0,0), new Point(0,1),-NINETY_DEGREES), DISTANCE_PRECISION);
    }
}