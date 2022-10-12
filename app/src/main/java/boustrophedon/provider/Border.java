package boustrophedon.provider;

import boustrophedon.model.IBorder;
import boustrophedon.model.IPoint;
import boustrophedon.utils.GA;

public class Border implements IBorder {
    private final IPoint firstVertice;
    private final IPoint secondVertice;

    public Border(IPoint firstVertice, IPoint secondVertice) {
        this.firstVertice = firstVertice;
        this.secondVertice = secondVertice;
    }

    @Override
    public IPoint getFirstVertice() {
        return firstVertice;
    }

    @Override
    public IPoint getSecondVertice() {
        return secondVertice;
    }

    @Override
    public double getLength() {
        return  GA.calcDistance(firstVertice, secondVertice);
    }

    @Override
    public double getAngle() {
        return GA.calcAngle(firstVertice, secondVertice);
    }

    @Override
    public double getPositiveAngle() {
        double angle = this.getAngle();
        return angle < 0 ? angle + Math.PI : angle;
    }

    @Override
    public boolean isParallelToY() {
        return firstVertice.getX() == secondVertice.getX();
    }

    @Override
    public double[] parallelLineCoefficients(IPoint point) {
        double[] currentCoefficients = this.getCoefficients();
        return GA.calcParallelLineCoefficients(currentCoefficients[0], point);
    }

    @Override
    public double angleDiff(double angle) {
        return angle - this.getPositiveAngle();
    }

    @Override
    public double distanceToPoint(IPoint point) {
        return GA.calcDistance(this, point);
    }

    public double[] getCoefficients() {
        return GA.getCoefficients(firstVertice, secondVertice);
    }

    @Override
    public boolean isOnBorder(IPoint point) {
        double[] coefficients = getCoefficients();
        if ( this.isParallelToY() || Math.abs(point.getX()*coefficients[0] + coefficients[1] - point.getY()) < 0.01) {
            return (firstVertice.getX() <= point.getX() && point.getX() <= secondVertice.getX() &&
                    firstVertice.getY() <= point.getY() && point.getY() <= secondVertice.getY()) ||
                    (firstVertice.getX() <= point.getX() && point.getX() <= secondVertice.getX() &&
                            secondVertice.getY() <= point.getY() && point.getY() <= firstVertice.getY()) ||
                    (firstVertice.getX() >= point.getX() && point.getX() >= secondVertice.getX() &&
                            firstVertice.getY() >= point.getY() && point.getY() >= secondVertice.getY()) ||
                    (firstVertice.getX() >= point.getX() && point.getX() >= secondVertice.getX() &&
                            secondVertice.getY() >= point.getY() && point.getY() >= firstVertice.getY());
        }
        return false;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Border)) return false;

        Border p = (Border) other;

        return p.getFirstVertice().equals(this.getFirstVertice()) &&
                p.getSecondVertice().equals(this.getSecondVertice());
    }
}
