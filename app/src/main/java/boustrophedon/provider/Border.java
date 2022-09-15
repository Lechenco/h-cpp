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
    public double getLenght() {
        return  Math.sqrt(
                    Math.pow(firstVertice.getX() - secondVertice.getX(), 2) +
                    Math.pow(firstVertice.getY() - secondVertice.getY(), 2)
        );
    }

    @Override
    public double getAngle() {
        return Math.atan2(secondVertice.getY() - firstVertice.getY(), secondVertice.getX() - firstVertice.getX());
    }

    public double[] getCoeficients() {
        double deltaX = secondVertice.getX() - firstVertice.getX();
        double deltaY = secondVertice.getY() - firstVertice.getY();
        double a = deltaX != 0 ? deltaY / deltaX : 0;
        double b = - 1 * a * firstVertice.getX() + firstVertice.getY();


        return new double[]{a, b};
    }

    @Override
    public boolean isOnBorder(IPoint point) {
        double[] coef = getCoeficients();
        if (point.getX()*coef[0] + coef[1] == point.getY()) {
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
}
