package boustrophedon.model;

import boustrophedon.domain.primitives.model.IPoint;

public interface IBorder {
    IPoint getFirstVertice();
    IPoint getSecondVertice();
    double getLength();
    double getAngle();
    double getPositiveAngle();
    double[] getCoefficients();
    boolean isOnBorder(IPoint point);
    boolean isParallelToY();
    double[] parallelLineCoefficients(IPoint point);
    double angleDiff(double angle);
    double distanceToPoint(IPoint point);
}
