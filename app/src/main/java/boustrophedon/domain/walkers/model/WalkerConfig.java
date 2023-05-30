package boustrophedon.domain.walkers.model;

import boustrophedon.domain.walkers.error.AngleOffLimitsException;

public class WalkerConfig {
    public static double ANGLE_PRECISION = Math.PI / 180;
    public static double DEFAULT_DISTANCE_BETWEEN_PATHS = 0.00009; // ~10 meters
    private double distanceBetweenPaths;

    private double direction = 0;

    public WalkerConfig() {
        this.distanceBetweenPaths =DEFAULT_DISTANCE_BETWEEN_PATHS;
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
