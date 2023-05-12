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
    private IBorder currentWall;

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

    protected IPoint walkToFront(IPoint currentPoint) {
        return this.walkToFront(this.polygon, currentPoint);
    }

    protected IPoint walkToFront(IPolygon polygon, IPoint currentPoint) {
        double angleBetweenBorders = this.config.getDirection() - this.currentWall.getPositiveAngle();
        double distanceToWalk = config.getDistanceBetweenPaths()/ Math.sin(angleBetweenBorders);
        IPoint anticlockwisePoint = currentPoint.walk(distanceToWalk, this.currentWall.getAngle());
        IPoint clockwisePoint = currentPoint.walk(distanceToWalk, this.currentWall.getAngle() + Math.PI);

        return clockwisePoint;
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
