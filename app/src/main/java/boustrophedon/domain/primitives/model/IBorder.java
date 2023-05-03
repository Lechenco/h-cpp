package boustrophedon.domain.primitives.model;

public interface IBorder {
    IPoint getFirstVertice();
    IPoint getSecondVertice();
    double getLength();
    double getAngle();
    double getPositiveAngle();
    double[] getCoefficients();
    boolean isOnBorder(IPoint point);

    double getAngleFirstHalf();

    boolean isParallelToY();
    double[] getParallelLineCoefficients(IPoint point);
    double getAngleDiff(double angle);
    double getDistanceToPoint(IPoint point);
}
