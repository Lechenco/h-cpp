package boustrophedon.provider.walkers;

import static boustrophedon.constants.AngleConstants.HUNDRED_AND_EIGHTY_DEGREES;
import static boustrophedon.constants.AngleConstants.NINETY_DEGREES;
import static boustrophedon.utils.AngleUtils.add180Degrees;
import static boustrophedon.utils.AngleUtils.add90Degrees;

import java.util.ArrayList;
import java.util.Optional;

import boustrophedon.domain.walkers.error.AngleOffLimitsException;
import boustrophedon.domain.walkers.model.IWalker;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.helpers.walkers.WalkerHelper;
import boustrophedon.provider.primitives.Polyline;
import boustrophedon.utils.AngleUtils;
import boustrophedon.utils.GA;

public class Walker implements IWalker {
    private IPolyline path;
    protected IPolygon polygon;
    protected ArrayList<IBorder> polygonBorders;
    protected ArrayList<IBorder> walls;
    protected IBorder currentWall;
    protected IPoint goal;
    protected IPoint start;
    protected double directionStartToGoal;

    private final double distanceBetweenPaths;

    private final double direction;

    private Walker(WalkerBuilder walkerBuilder) {
        this.direction = walkerBuilder.direction;
        this.distanceBetweenPaths = walkerBuilder.distanceBetweenPaths;
        path = new Polyline();
    }

    protected IPoint walkToTheOtherSide(IPoint currentPoint) {
        if (this.walls == null)
            setUpWalls();

        if (!WalkerHelper.isPointInsidePolygonBorders(currentPoint, this.polygonBorders))
            return null;

        for (IBorder wall : this.walls) {
            IPoint intersection = WalkerHelper.calcIntersectionToWall(currentPoint, wall, this.direction);

            if (wall.isOnBorder(intersection) && !intersection.equals(currentPoint)) {
                this.currentWall = wall;
                return intersection;
            }
        }

        return null;
    }

    protected IPoint walkToTheOtherSide(IPolygon polygon, IPoint currentPoint) {
        this.polygon = polygon;
        this.polygonBorders = null;
        this.walls = null;
        this.path = new Polyline();

        return this.walkToTheOtherSide(currentPoint);
    }

    protected IPoint walkAside(IPoint currentPoint) {
        return this.walkAside(currentPoint, this.distanceBetweenPaths);
    }

    private double calcDistanceToWalk(double distance) {
        double angleBetweenBorders = this.currentWall.getAngleFirstHalf() - this.direction;
        return Math.abs(distance / Math.sin(angleBetweenBorders));
    }
    private double calcDistance(double distanceToWalk) {
        double angleBetweenBorders = this.currentWall.getAngleFirstHalf() - this.direction;
        return Math.abs(distanceToWalk * Math.sin(angleBetweenBorders));
    }

    protected IPoint walkAside(IPoint currentPoint, double distance) {
        if (this.currentWall == null) return null;

        double angleBetweenBorders = this.currentWall.getAngleFirstHalf() - this.direction; // Theta
        double normalizedDirectionToGoal = this.directionStartToGoal - this.direction; //Beta

        double distanceToWalk = this.calcDistanceToWalk(distance);
        double currentWallAngle = this.currentWall.getAngleFirstHalf();

        // TODO: case Theta or Beta parallel to X
        if (Math.signum(Math.sin(angleBetweenBorders)) == Math.signum(Math.sin(normalizedDirectionToGoal))) {
            return currentPoint.walk(distanceToWalk, currentWallAngle);
        }

        return currentPoint.walk(distanceToWalk, add180Degrees(currentWallAngle));
    }

    private boolean walkedOutOfWall(IPoint currentPoint) {
        return !this.currentWall.isOnBorder(currentPoint);
    }

    protected IPoint walkAsideAndValidate(IPoint currentPoint) {
        IPoint nextPoint = walkAside(currentPoint);

        if (walkedOutOfWall(nextPoint)) {
            IPoint closestWallPoint = WalkerHelper.getClosestWallVertices(nextPoint, this.currentWall);

            IBorder nextWall = this.walls.stream()
                    .filter(w -> w.isOnBorder(closestWallPoint) && w != this.currentWall).findFirst().orElse(null);

            if (nextWall == null) {
                nextWall = WalkerHelper.getClosestWallExcept(nextPoint, this.walls, this.direction, this.currentWall);
                nextPoint = WalkerHelper.getClosestWallVertices(nextPoint, this.currentWall);
            }

            if (nextWall != null) {
                double distanceOutOfWall = closestWallPoint.calcDistance(nextPoint);

                double remainDistance = this.calcDistance(distanceOutOfWall);

                this.currentWall = nextWall;
                nextPoint = walkAside(closestWallPoint, remainDistance);
            }
        }

        return nextPoint;
    }
    protected IPoint walkAside(IPolygon polygon, IPoint currentPoint) {
        this.polygon = polygon;
        return this.walkAside(currentPoint);
    }

    @Override
    public IPolyline walk(IPolygon polygon, IPoint initialPoint) {
        this.setPolygon(polygon);
        return this.walk(initialPoint);
    }

    protected IPoint calcStartPoint(IPoint currentPoint) {
        return this.polygon.getClosestVertices(currentPoint);
    }

    protected void calcGoal(IPoint startPoint) {
        this.start = startPoint;
        double anglePlus90 = add90Degrees(this.direction);
        double angleMinus90 = this.direction - NINETY_DEGREES;
        IPoint goalClockWise = this.polygon.getFarthestVertices(startPoint, angleMinus90);
        IPoint goalAntiClockWise = this.polygon.getFarthestVertices(startPoint, anglePlus90);
        this.goal =
                Math.abs(GA.calcDistanceWithDirection(startPoint, goalClockWise, anglePlus90))
                        > Math.abs(GA.calcDistanceWithDirection(startPoint, goalAntiClockWise, anglePlus90))
                    ? goalClockWise : goalAntiClockWise;
        this.directionStartToGoal = AngleUtils.calcAngle(startPoint, this.goal);
    }

    public void setPolygon(IPolygon polygon) {
        this.polygon = polygon;
        this.populatePolygonBorders();
        this.setUpWalls();
    }

    @Override
    public IPolyline walk(IPoint initialPoint) {
        IPoint currentPoint = this.calcStartPoint(initialPoint);

        this.calcGoal(currentPoint);
        this.path = new Polyline();

        long numberOfIterations =
                Math.round(Math.abs(GA.calcDistanceWithDirection(currentPoint, this.goal, add90Degrees(this.direction))
                / this.distanceBetweenPaths)) +1;

        while(numberOfIterations-- != 0) {
            if (currentPoint == null ||
                    (this.path.getNumberOfPoints() > 0 && currentPoint == this.path.getLastPoint())
            )
                break;

            this.path.add(currentPoint);
            currentPoint = this.walkToTheOtherSide(currentPoint);

            if (currentPoint == null && this.path.getNumberOfPoints() == 1) {
                IPoint finalCurrentPoint = this.path.getLastPoint();
                this.currentWall = this.walls
                        .stream()
                        .filter(w -> w.isOnBorder(finalCurrentPoint))
                        .findFirst().orElse(null);

                currentPoint = this.walkAsideAndValidate(finalCurrentPoint);
                continue;
            }

            if (currentPoint == null || currentPoint == this.path.getLastPoint())
                break;

            this.path.add(currentPoint);
            currentPoint = this.walkAsideAndValidate(currentPoint);
        }

        return this.path;
    }

    public IPolyline getPath() {
        return path;
    }

    private void setUpWalls() {
        populatePolygonBorders();

        this.walls = WalkerHelper.findWalls(this.polygonBorders, this.direction);
    }

    private void populatePolygonBorders() {
        this.polygonBorders = WalkerHelper.getPolygonBorders(this.polygon);
    }
    
    public static class WalkerBuilder {
        public static double DEFAULT_DISTANCE_BETWEEN_PATHS = 0.00009; // ~10 meters
        private double distanceBetweenPaths = DEFAULT_DISTANCE_BETWEEN_PATHS;

        private double direction = 0;

        public WalkerBuilder() {}
        
        public WalkerBuilder atDirection(double direction) throws AngleOffLimitsException {
            if (direction > HUNDRED_AND_EIGHTY_DEGREES || direction < -HUNDRED_AND_EIGHTY_DEGREES)
                throw new AngleOffLimitsException();
            this.direction = direction;
            
            return this;
        }

        public WalkerBuilder withDistanceBetweenPaths(double distanceBetweenPaths) {
            this.distanceBetweenPaths = distanceBetweenPaths;

            return this;
        }
        
        public Walker build() {
            return new Walker(this);
        }
    }
}
