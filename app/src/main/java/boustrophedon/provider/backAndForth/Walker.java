package boustrophedon.provider.backAndForth;

import java.util.ArrayList;

import boustrophedon.domain.backAndForth.model.IWalker;
import boustrophedon.domain.backAndForth.model.WalkerConfig;
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


    public Walker() {
        this.setConfig(new WalkerConfig());
        path = new Polyline();
    }

    public Walker(WalkerConfig config) {
        this.setConfig(config);
        path = new Polyline();
    }

    @Override
    public IPoint walkToTheOtherSide(IPoint currentPoint) {
        if (this.walls == null)
            setUpWalls();

        for(IBorder wall : this.walls) {
            IPoint intersection = WalkerHelper.calcIntersectionToWall(currentPoint, wall, this.config.getDirection());

            if (wall.isOnBorder(intersection) && !intersection.equals(currentPoint) ) {
                return intersection;
            }
        }

        return null;
    }

    @Override
    public IPoint walkToTheOtherSide(IPolygon polygon, IPoint currentPoint) {
        this.polygon = polygon;
        this.polygonBorders = null;
        this.walls = null;
        this.path = new Polyline();

        return this.walkToTheOtherSide(currentPoint);
    }

    @Override
    public IPoint walkToFront(IPoint currentPoint) {
        return this.walkToFront(this.polygon, currentPoint);
    }

    @Override
    public IPoint walkToFront(IPolygon polygon, IPoint currentPoint) {
        return null;
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
