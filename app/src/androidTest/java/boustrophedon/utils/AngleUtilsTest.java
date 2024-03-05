package boustrophedon.utils;

import static org.junit.Assert.*;

import static boustrophedon.constants.AngleConstants.FORTY_FIVE_DEGREES;
import static boustrophedon.constants.AngleConstants.HUNDRED_AND_EIGHTY_DEGREES;
import static boustrophedon.constants.AngleConstants.HUNDRED_AND_TWENTY_DEGREES;
import static boustrophedon.constants.AngleConstants.NINETY_DEGREES;
import static boustrophedon.constants.AngleConstants.ZERO_DEGREES;

import org.junit.Test;

import boustrophedon.constants.PrecisionConstants;
import boustrophedon.provider.primitives.Point;

public class AngleUtilsTest {
    @Test
    public void testCalcAngle() {
        assertEquals(ZERO_DEGREES, AngleUtils.calcAngle(0, 0), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(FORTY_FIVE_DEGREES, AngleUtils.calcAngle(1, 1), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(NINETY_DEGREES, AngleUtils.calcAngle(0, 1), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(ZERO_DEGREES, AngleUtils.calcAngle(1, 0), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(HUNDRED_AND_TWENTY_DEGREES, AngleUtils.calcAngle(-1, 1), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(-FORTY_FIVE_DEGREES, AngleUtils.calcAngle(1, -1), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(-NINETY_DEGREES, AngleUtils.calcAngle(0, -1), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(HUNDRED_AND_EIGHTY_DEGREES, AngleUtils.calcAngle(-1, 0), PrecisionConstants.ANGLE_PRECISION);
    }
    @Test
    public void testCalcAngleWithCoordinates() {
        assertEquals(ZERO_DEGREES, AngleUtils.calcAngle(0, 0, 0, 0), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(FORTY_FIVE_DEGREES, AngleUtils.calcAngle(0, 0, 1, 1), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(NINETY_DEGREES, AngleUtils.calcAngle(0, 0, 0, 1), PrecisionConstants.ANGLE_PRECISION);
    }
    @Test
    public void testCalcAngleWithPoints() {
        assertEquals(ZERO_DEGREES, AngleUtils.calcAngle(new Point(0, 0), new Point(0, 0)), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(FORTY_FIVE_DEGREES, AngleUtils.calcAngle(new Point(0, 0), new Point(1, 1)), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(NINETY_DEGREES, AngleUtils.calcAngle(new Point(0, 0), new Point(0, 1)), PrecisionConstants.ANGLE_PRECISION);
    }

    @Test
    public void testCGetPositiveAngle() {
        assertEquals(ZERO_DEGREES, AngleUtils.getPositiveAngle(ZERO_DEGREES), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(HUNDRED_AND_EIGHTY_DEGREES, AngleUtils.getPositiveAngle(HUNDRED_AND_EIGHTY_DEGREES), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(NINETY_DEGREES, AngleUtils.getPositiveAngle(-NINETY_DEGREES), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(HUNDRED_AND_TWENTY_DEGREES, AngleUtils.getPositiveAngle(-FORTY_FIVE_DEGREES), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(ZERO_DEGREES, AngleUtils.getPositiveAngle(-HUNDRED_AND_EIGHTY_DEGREES), PrecisionConstants.ANGLE_PRECISION);
    }
    @Test
    public void testCGetFirstHalfAngle() {
        assertEquals(ZERO_DEGREES, AngleUtils.getFirstHalfAngle(ZERO_DEGREES), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(ZERO_DEGREES, AngleUtils.getFirstHalfAngle(HUNDRED_AND_EIGHTY_DEGREES), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(NINETY_DEGREES, AngleUtils.getFirstHalfAngle(-NINETY_DEGREES), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(HUNDRED_AND_TWENTY_DEGREES, AngleUtils.getFirstHalfAngle(-FORTY_FIVE_DEGREES), PrecisionConstants.ANGLE_PRECISION);
        assertEquals(ZERO_DEGREES, AngleUtils.getFirstHalfAngle(-HUNDRED_AND_EIGHTY_DEGREES), PrecisionConstants.ANGLE_PRECISION);
    }
}