package boustrophedon.model;

public interface IBorder {
    IPoint getFirstVertice();
    IPoint getSecondVertice();
    double getLength();
    double getAngle();
    double[] getCoefficients();
    boolean isOnBorder(IPoint point);
    boolean isParallelToY();
    double[] parallelLineCoefficients(IPoint point);
}
