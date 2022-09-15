package boustrophedon.model;

import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;

public interface ICell {
    IPolygon polygon = null;
    ArrayList<IPoint> points = null;
    ArrayList<ICell> neighbors = null;
    boolean visited = false;

    IPolygon getPolygon();
    ArrayList<IPoint> getPoints();
    ArrayList<ICell> getNeighbors();
    boolean getVisited();
    void setPolygon(IPolygon polygon);
    void setNeighbors(ArrayList<ICell> neighbors);
    void setVisited(boolean visited);

    abstract void insertPointOnEdge(IPoint point);
    abstract boolean isPointOnEdge(IPoint point);
    abstract boolean isAdjacentTo(ICell other);
    abstract boolean canMergeWith(ArrayList<ICell> cells);

    abstract IPolygon toPolygon();
}
