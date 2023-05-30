package boustrophedon.provider.walkers;

import java.util.ArrayList;

import boustrophedon.domain.walkers.model.IWalker;
import boustrophedon.domain.walkers.model.WalkerConfig;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.provider.primitives.Polyline;

public class Walker implements IWalker {
    private WalkerConfig config;
    private IPolyline path;
    private IPolygon polygon;
    private ArrayList<IBorder> polygonBorders;
    private ArrayList<IBorder> walls;
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

        if (Math.abs(Math.abs(currentWallAngle - this.directionStartToGoal) - Math.PI / 2) < 0.001) {
            if (Math.abs(Math.cos(this.directionStartToGoal) - Math.cos(currentWallAngle)) >
                    Math.abs(Math.cos(this.directionStartToGoal) - Math.cos(currentWallAngle + Math.PI))
            )
                return currentPoint.walk(distanceToWalk, currentWallAngle);

            return currentPoint.walk(distanceToWalk, currentWallAngle + Math.PI);
        }

        if (Math.abs(Math.cos(currentWallAngle)) < 0.001 ||
                (Math.abs(Math.cos(this.directionStartToGoal)) < 0.001)
        ) {
            if (Math.abs(Math.sin(this.directionStartToGoal) - Math.sin(currentWallAngle)) <
                    Math.abs(Math.sin(this.directionStartToGoal) - Math.sin(currentWallAngle + Math.PI)))
                return currentPoint.walk(distanceToWalk, currentWallAngle);

            return currentPoint.walk(distanceToWalk, currentWallAngle + Math.PI);
        }

        else if (Math.abs(Math.cos(this.directionStartToGoal) - Math.cos(currentWallAngle)) <
                Math.abs(Math.cos(this.directionStartToGoal) - Math.cos(currentWallAngle + Math.PI))
        )
            return currentPoint.walk(distanceToWalk, currentWallAngle);

        return currentPoint.walk(distanceToWalk, currentWallAngle + Math.PI);
    }

    protected IPoint walkAside(IPolygon polygon, IPoint currentPoint) {
        this.polygon = polygon;
//        this.goal = polygon.getOutsiderPointInDirection(currentPoint, this.config.getDirection() + Math.PI /2);
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

    @Override
    public void setConfig(WalkerConfig config) {
        this.config = config;
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