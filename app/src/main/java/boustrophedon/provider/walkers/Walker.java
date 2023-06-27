package boustrophedon.provider.walkers;

import java.util.ArrayList;

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
        double angleBetweenBorders = Math.abs(this.config.getDirection() - this.currentWall.getPositiveAngle());
        double distanceToWalk = Math.abs(config.getDistanceBetweenPaths() / Math.sin(angleBetweenBorders));
        double currentWallAngle = this.currentWall.getAngle();
        double cosStartToGoal = Math.cos(this.directionStartToGoal);
        double absDiffCosGoalAndWall = Math.abs(cosStartToGoal - Math.cos(currentWallAngle));
        double absDiffCosGoalAndWall180 = Math.abs(cosStartToGoal - Math.cos(currentWallAngle + Math.PI));

        IPoint nextPoint;

        if (Math.abs(Math.abs(currentWallAngle - this.directionStartToGoal) - Math.PI / 2) < 0.001) {
            nextPoint = WalkerHelper.walkAsideWallAndGoalOrthogonal(
             currentPoint, distanceToWalk, currentWallAngle, absDiffCosGoalAndWall, absDiffCosGoalAndWall180
            );
        }

        else if (Math.abs(Math.cos(currentWallAngle)) < 0.001 || (Math.abs(cosStartToGoal) < 0.001)) {
            double sinStartToGoal = Math.sin(this.directionStartToGoal);
            nextPoint = WalkerHelper.walkAsideWallOrGoalParallelToXAxis(
                    currentPoint, distanceToWalk, currentWallAngle, sinStartToGoal
            );
        } else {
            nextPoint = WalkerHelper.walkAside(
                    currentPoint, distanceToWalk, currentWallAngle, absDiffCosGoalAndWall, absDiffCosGoalAndWall180
            );
        }


        return nextPoint;
    }

    protected IPoint walkAside(IPolygon polygon, IPoint currentPoint) {
        this.polygon = polygon;
        return this.walkAside(currentPoint);
    }

    @Override
    public IPolyline generatePath(IPolygon polygon) {
        return null;
    }

    @Override
    public IPolyline generatePath(IPolygon polygon, IPoint initialPoint) {
        return null;
    }

    public WalkerConfig getConfig() {
        return config;
    }

    protected IPoint calcStartPoint(IPoint currentPoint) {
        return this.polygon.getClosestVertices(currentPoint);
    }

    protected IPoint calcGoal(IPoint startPoint) {
        IPoint goalClockWise = this.polygon.getFarthestVertices(startPoint, this.config.getDirection() - Math.PI / 2);
        IPoint goalAntiClockWise = this.polygon.getFarthestVertices(startPoint, this.config.getDirection() + Math.PI / 2);
        this.goal =
                startPoint.calcDistance(goalClockWise) > startPoint.calcDistance(goalAntiClockWise)
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
            if (currentPoint == null || currentPoint == this.path.getLastPoint())
                break;

            this.path.add(currentPoint);
            currentPoint = this.walkAside(currentPoint);
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
