package boustrophedon.provider.walkers;

import java.util.ArrayList;
import java.util.Optional;

import boustrophedon.domain.walkers.model.IWalker;
import boustrophedon.domain.walkers.model.WalkerConfig;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.provider.primitives.Polyline;
import boustrophedon.utils.GA;

public class Walker implements IWalker {
    private WalkerConfig config;
    private IPolyline path;
    protected IPolygon polygon;
    protected ArrayList<IBorder> polygonBorders;
    protected ArrayList<IBorder> walls;
    protected IBorder currentWall;
    protected IPoint goal;
    protected IPoint start;
    protected double directionStartToGoal;
    public Walker() {
        this.setConfig(new WalkerConfig());
        path = new Polyline();
    }

    public Walker(WalkerConfig config) {
        this.setConfig(config);
        path = new Polyline();
    }

    protected IPoint walkToTheOtherSide(IPoint currentPoint) {
        if (this.walls == null)
            setUpWalls();

        if (!WalkerHelper.isPointInsidePolygonBorders(currentPoint, this.polygonBorders))
            return null;

        for (IBorder wall : this.walls) {
            IPoint intersection = WalkerHelper.calcIntersectionToWall(currentPoint, wall, this.config.getDirection());

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
        return this.walkAside(currentPoint, this.config.getDistanceBetweenPaths());
    }

    private double calcDistanceToWalk(double distance) {
        double angleBetweenBorders = this.currentWall.getAngleFirstHalf() - this.config.getDirection();
        return Math.abs(distance / Math.sin(angleBetweenBorders));
    }
    private double calcDistance(double distanceToWalk) {
        double angleBetweenBorders = this.currentWall.getAngleFirstHalf() - this.config.getDirection();
        return Math.abs(distanceToWalk * Math.sin(angleBetweenBorders));
    }

    protected IPoint walkAside(IPoint currentPoint, double distance) {
        if (this.currentWall == null) return null;

        double angleBetweenBorders = this.currentWall.getAngleFirstHalf() - this.config.getDirection(); // Theta
        double normalizedDirectionToGoal = this.directionStartToGoal - this.config.getDirection(); //Beta

        double distanceToWalk = this.calcDistanceToWalk(distance);
        double currentWallAngle = this.currentWall.getAngleFirstHalf();

        // TODO: case Theta or Beta parallel to X
        if (Math.signum(Math.sin(angleBetweenBorders)) == Math.signum(Math.sin(normalizedDirectionToGoal))) {
            return currentPoint.walk(distanceToWalk, currentWallAngle);
        }

        return currentPoint.walk(distanceToWalk, currentWallAngle + Math.PI);
    }

    private boolean walkedOutOfWall(IPoint currentPoint) {
        return !this.currentWall.isOnBorder(currentPoint);
    }

    protected IPoint walkAsideAndValidate(IPoint currentPoint) {
        IPoint nextPoint = walkAside(currentPoint);

        if (walkedOutOfWall(nextPoint)) {
            IPoint closestWallPoint = WalkerHelper.getClosestWallVertices(nextPoint, this.currentWall);

            Optional<IBorder> nextWall = this.walls.stream()
                    .filter(w -> w.isOnBorder(closestWallPoint) && w != this.currentWall).findFirst();
            if (nextWall.isPresent()) {
                double distanceOutOfWall = closestWallPoint.calcDistance(nextPoint);

                double remainDistance = this.calcDistance(distanceOutOfWall);

                this.currentWall = nextWall.get();
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
    public IPolyline generatePath(IPolygon polygon, IPoint initialPoint) {
        this.setPolygon(polygon);
        return this.generatePath(initialPoint);
    }

    public WalkerConfig getConfig() {
        return config;
    }

    protected IPoint calcStartPoint(IPoint currentPoint) {
        return this.polygon.getClosestVertices(currentPoint);
    }

    protected IPoint calcGoal(IPoint startPoint) {
        this.start = startPoint;
        IPoint goalClockWise = this.polygon.getFarthestVertices(startPoint, this.config.getDirection() - Math.PI / 2);
        IPoint goalAntiClockWise = this.polygon.getFarthestVertices(startPoint, this.config.getDirection() + Math.PI / 2);
        this.goal =
                Math.abs(GA.calcDistanceWithDirection(startPoint, goalClockWise, this.config.getDirection() + Math.PI / 2))
                        > Math.abs(GA.calcDistanceWithDirection(startPoint, goalAntiClockWise, this.config.getDirection() + Math.PI / 2))
                    ? goalClockWise : goalAntiClockWise;
        this.directionStartToGoal = GA.calcAngle(startPoint, this.goal);
        return this.goal;
    }

    public void setPolygon(IPolygon polygon) {
        this.polygon = polygon;
        this.populatePolygonBorders();
        this.setUpWalls();
    }

    @Override
    public void setConfig(WalkerConfig config) {
        this.config = config;
    }

    @Override
    public IPolyline generatePath(IPoint initialPoint) {
        IPoint currentPoint = this.calcStartPoint(initialPoint);

        this.calcGoal(currentPoint);
        this.path = new Polyline();

        long numberOfIterations =
                Math.round(Math.abs(GA.calcDistanceWithDirection(currentPoint, this.goal,this.config.getDirection() + Math.PI / 2)
                / this.config.getDistanceBetweenPaths())) +1;

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

        this.walls = WalkerHelper.findWalls(this.polygonBorders, this.config.getDirection());
    }

    private void populatePolygonBorders() {
        this.polygonBorders = WalkerHelper.getPolygonBorders(this.polygon);
    }
}
