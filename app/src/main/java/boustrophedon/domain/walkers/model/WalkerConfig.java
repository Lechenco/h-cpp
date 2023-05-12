package boustrophedon.domain.walkers.model;

import boustrophedon.domain.walkers.error.AngleOffLimitsException;

public class WalkerConfig {
    public static double ANGLE_PRECISION = Math.PI / 180;
    private double distanceBetweenPaths;

    private double direction = 0;

    public WalkerConfig() {
        this.distanceBetweenPaths = 0.00009; // 10 meters
    }

    public WalkerConfig(double distanceBetweenPaths) {
        this.distanceBetweenPaths = distanceBetweenPaths;
    }

    public WalkerConfig(double distanceBetweenPaths, double direction) throws AngleOffLimitsException {
        this.distanceBetweenPaths = distanceBetweenPaths;
        setDirection(direction);
    }

    public double getDistanceBetweenPaths() {
        return distanceBetweenPaths;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) throws AngleOffLimitsException {
        if (direction > Math.PI || direction < -Math.PI)
            throw new AngleOffLimitsException();
        this.direction = direction;
    }
}
