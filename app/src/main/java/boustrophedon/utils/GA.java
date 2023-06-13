package boustrophedon.utils;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;

public class GA {
    public static double calcYPoint(double[] coefficients, double x) {
        return calcYPoint(coefficients[0], coefficients[1], x);
    }

    public static double calcYPoint(double a, double b, double x) {
        return a * x + b;
    }

    public static double calcDistance(IPoint p1, IPoint p2) {
        return calcDistance(p1.getX(), p1.getY(),
                p2.getX(), p2.getY());
    }

    public static double calcDistance(IBorder border, IPoint point) {
        // calc distance of the point to the hole line
        try {
            double[] borderCoefficients = border.getCoefficients();
            double a = borderCoefficients[0], b = -1, c = borderCoefficients[1];

            return (Math.abs(a * point.getX() + b * point.getY() + c)) / (Math.sqrt(a * a + b * b));
        } catch (Exception exception) {
            return Math.abs(point.getX() - border.getFirstVertice().getX());
        }
    }

    public static double calcDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(
                Math.pow(x1 - x2, 2) +
                        Math.pow(y1 - y2, 2)
        );
    }

    public static double calcDistanceWithDirection(IPoint p1, IPoint p2, double direction) {
        return calcDistanceWithDirection(p1.getX(), p1.getY(),
                p2.getX(), p2.getY(), direction);
    }
    public static double calcDistanceWithDirection(double x1, double y1, double x2, double y2, double direction) {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        return deltaX * Math.cos(direction) + deltaY * Math.sin(direction);
    }

    public static double[] getCoefficients(IPoint p1, IPoint p2) throws Exception {
        return getCoefficients(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    public static double[] getCoefficients(double x1, double y1, double x2, double y2) throws Exception {
        double a = calcAngularCoefficient(x2 - x1, y2 - y1);
        double b = calcLinearCoefficient(a, x1, y1);
        return new double[]{a, b};
    }

    public static double calcAngularCoefficient(double deltaX, double deltaY) throws Exception {
        if (deltaX == 0) throw new Exception("deltaX can't be equals to zero");

        return deltaY / deltaX;
    }

    public static double calcLinearCoefficient(double a, double x, double y) {
        return -1 * a * x + y;
    }

    public static double[] calcParallelLineCoefficients(double a, IPoint p) {
        return calcParallelLineCoefficients(a, p.getX(), p.getY());
    }

    public static double[] calcParallelLineCoefficients(double a, double x, double y) {
        return new double[]{
                a,
                y - a * x
        };
    }

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
        return anglePositive >= Math.PI ? anglePositive - Math.PI : anglePositive;
    }
}
