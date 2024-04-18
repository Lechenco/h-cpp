package boustrophedon.utils;

import static boustrophedon.constants.AngleConstants.HUNDRED_AND_EIGHTY_DEGREES;
import static boustrophedon.constants.AngleConstants.NINETY_DEGREES;
import static boustrophedon.constants.PrecisionConstants.ANGLE_PRECISION;

import boustrophedon.domain.primitives.model.IPoint;

public final class AngleUtils {
    public static double calcAngle(IPoint p1, IPoint p2) {
        return calcAngle(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public static double calcAngle(double x1, double y1, double x2, double y2) {
        return calcAngle(x2 - x1, y2 - y1);
    }

    public static double calcAngle(double deltaX, double deltaY) {
        return Math.atan2(deltaY, deltaX);
    }

    public static double getPositiveAngle(double angle) {
        return angle < 0 ? angle + Math.PI : angle;
    }

    public static double getFirstHalfAngle(double angle) {
        double anglePositive = getPositiveAngle(angle);
        double angleMinus180 = anglePositive - HUNDRED_AND_EIGHTY_DEGREES;
        return Math.abs(angleMinus180) <= ANGLE_PRECISION
                ? angleMinus180
                : anglePositive;
    }

    public static boolean checkAngles(double angle1, double angle2) {
        double angle1FirstHalf = getFirstHalfAngle(angle1);
        double angle2FirstHalf = getFirstHalfAngle(angle2);
        return Math.abs(angle1FirstHalf - angle2FirstHalf) <= ANGLE_PRECISION;
    }

    public static boolean checkOrthogonality(double angle1, double angle2) {
        double angleDiff = Math.abs(getFirstHalfAngle(angle1) - getFirstHalfAngle(angle2));

        return AngleUtils.checkAngles(angleDiff, NINETY_DEGREES);
    }
    public static boolean isParallelToX(double angle) {
        return Math.abs(Math.sin(angle)) <= ANGLE_PRECISION;
    }
    public static boolean isParallelToY(double angle) {
        return Math.abs(Math.cos(angle)) <= ANGLE_PRECISION;
    }

    public static double add180Degrees(double angle) {
        return angle + HUNDRED_AND_EIGHTY_DEGREES;
    }
    public static double add90Degrees(double angle) {
        return angle + NINETY_DEGREES;
    }

    public static double calcXRotation(IPoint point, double angle) {
        return AngleUtils.calcXRotation(point.getX(), point.getY(), angle);
    }

    public static double calcXRotation(double x, double y, double angle) {
        return x * Math.cos(angle) + y * Math.sin(angle);
    }

    private AngleUtils() {
    }
}
