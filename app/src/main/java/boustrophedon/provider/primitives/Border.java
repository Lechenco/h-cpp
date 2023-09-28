package boustrophedon.provider.primitives;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.utils.GA;

public class Border implements IBorder {
    private final double PRECISION = 0.000001;
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
    public double getAngleFirstHalf() {
        double angle = this.getPositiveAngle();
        return angle >= Math.PI ? angle - Math.PI : angle;
    }


    @Override
    public boolean isParallelToY() {
        return Math.abs(firstVertice.getX() - secondVertice.getX()) <= PRECISION;
    }

    @Override
    public double[] getParallelLineCoefficients(IPoint point) {
        double[] currentCoefficients = this.getCoefficients();
        return GA.calcParallelLineCoefficients(currentCoefficients[0], point);
    }

    @Override
    public double getAngleDiff(double angle) {
        return angle - this.getPositiveAngle();
    }

    @Override
    public double getDistanceToPoint(IPoint point) {
        return GA.calcDistance(this, point);
    }

    public double[] getCoefficients() throws RuntimeException {
        try {
            return GA.getCoefficients(firstVertice, secondVertice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isOnBorder(IPoint point) {
        if ( this.isParallelToY() || Math.abs(this.getDistanceToPoint(point)) < PRECISION) {
            return this.isPointInsideLimitations(point);
        }
        return false;
    }

    private boolean isPointInsideLimitations(@NonNull IPoint point) {
        double maxX = Math.max(firstVertice.getX(), secondVertice.getX()) + PRECISION;
        double minX = Math.min(firstVertice.getX(), secondVertice.getX()) - PRECISION;
        double maxY = Math.max(firstVertice.getY(), secondVertice.getY()) + PRECISION;
        double minY = Math.min(firstVertice.getY(), secondVertice.getY()) - PRECISION;

        return minX <= point.getX() && point.getX() <= maxX &&
                minY <= point.getY() && point.getY() <= maxY;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Border)) return false;

        Border p = (Border) other;

        return p.getFirstVertice().equals(this.getFirstVertice()) &&
                p.getSecondVertice().equals(this.getSecondVertice());
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("Border{ v1=%s, v2=%s }", this.firstVertice, this.secondVertice);
    }

}
