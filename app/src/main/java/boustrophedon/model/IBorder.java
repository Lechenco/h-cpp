package boustrophedon.model;

public interface IBorder {
    IPoint getFirstVertice();
    IPoint getSecondVertice();
    double getLenght();
    double getAngle();
    double[] getCoeficients();
    boolean isOnBorder(IPoint point);
}
