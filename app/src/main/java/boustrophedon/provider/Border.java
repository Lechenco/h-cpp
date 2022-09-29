package boustrophedon.provider;

import boustrophedon.model.IBorder;
import boustrophedon.model.IPoint;

public class Border implements IBorder {
    private IPoint firstVertice;
    private IPoint secondVertice;

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
        return  Math.sqrt(
                    Math.pow(firstVertice.getX() - secondVertice.getX(), 2) +
                    Math.pow(firstVertice.getY() - secondVertice.getY(), 2)
        );
    }

    @Override
    public double getAngle() {
        return Math.atan2(secondVertice.getY() - firstVertice.getY(), secondVertice.getX() - firstVertice.getX());
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
        return new double[]{
                currentCoefficients[0],
                point.getY() - currentCoefficients[0] * point.getX()
        };
    }

    @Override
    public double angleDiff(double angle) {
        return angle - this.getPositiveAngle();
    }

    @Override
    public double distanceToPoint(IPoint point) {
        double[] coefficients = this.getCoefficients();
        double a = coefficients[0], b = -1, c = coefficients[1];

        return (Math.abs(a * point.getX() + b * point.getY() + c)) / (Math.sqrt(a * a + b * b));
    }

    public double[] getCoefficients() {
        double deltaX = secondVertice.getX() - firstVertice.getX();
        double deltaY = secondVertice.getY() - firstVertice.getY();
        double a = deltaX != 0 ? deltaY / deltaX : 0;
        double b = - 1 * a * firstVertice.getX() + firstVertice.getY();


        return new double[]{a, b};
    }

    @Override
    public boolean isOnBorder(IPoint point) {
        double[] coef = getCoefficients();
        if ( this.isParallelToY() || point.getX()*coef[0] + coef[1] - point.getY() < 0.01) {
            if (
                    (firstVertice.getX() <= point.getX() && point.getX() <= secondVertice.getX() &&
                        firstVertice.getY() <= point.getY() && point.getY() <= secondVertice.getY()) ||
                    (firstVertice.getX() <= point.getX() && point.getX() <= secondVertice.getX() &&
                        secondVertice.getY() <= point.getY() && point.getY() <= firstVertice.getY()) ||
                    (firstVertice.getX() >= point.getX() && point.getX() >= secondVertice.getX() &&
                            firstVertice.getY() >= point.getY() && point.getY() >= secondVertice.getY()) ||
                    (firstVertice.getX() >= point.getX() && point.getX() >= secondVertice.getX() &&
                            secondVertice.getY() >= point.getY() && point.getY() >= firstVertice.getY())
            ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Border)) return false;

        Border p = (Border) other;

        return p.getFirstVertice().equals(this.getFirstVertice()) && p.getSecondVertice().equals(this.getSecondVertice());
    }
}
