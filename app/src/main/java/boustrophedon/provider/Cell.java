package boustrophedon.provider;

import java.util.ArrayList;

import boustrophedon.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;

public class Cell implements ICell {
    IPolygon polygon = null;
    ArrayList<IPoint> points = null;
    ArrayList<ICell> neighbors = null;
    boolean visited = false;

    public Cell(IPolygon polygon) throws NullPointerException {
        try {
            setPolygon(polygon);
        } catch (NullPointerException error) {
            throw error;
        }
    }

    @Override
    public IPolygon getPolygon() {
        return this.polygon;
    }

    @Override
    public ArrayList<IPoint> getPoints() {
        return this.points;
    }

    @Override
    public ArrayList<ICell> getNeighbors() {
        return this.neighbors;
    }

    @Override
    public boolean getVisited() {
        return this.visited;
    }

    @Override
    public void setPolygon(IPolygon polygon) throws NullPointerException {
        if (polygon == null)
            throw new NullPointerException("No polygon founded");

        this.polygon = polygon;
        this.points = polygon.getPoints();
    }

    @Override
    public void setNeighbors(ArrayList<ICell> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    @Override
    public void insertPointOnEdge(IPoint point) {

    }

    @Override
    public boolean isPointOnEdge(IPoint point) {
        return false;
    }

    @Override
    public boolean isAdjacentTo(ICell other) {
        return false;
    }

    @Override
    public boolean canMergeWith(ArrayList<ICell> cells) {
        return false;
    }

    @Override
    public IPolygon toPolygon() {
        return null;
    }
}
