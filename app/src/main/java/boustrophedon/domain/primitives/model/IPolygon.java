package boustrophedon.domain.primitives.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;

public interface IPolygon {
    int getNumberOfPoints();
    ArrayList<IPoint> getPoints();
    void setPoints(ArrayList<IPoint> points);
    LatLng[] toLatLngArray();
    IPoint getOutsiderPointInDirection(IPoint startPoint, double direction);
    IPoint getClosestVertices(IPoint point);
    IPoint getFarthestVertices(IPoint point);
    IPoint getFarthestVertices(IPoint point, double direction);
    boolean isAdjacentTo(IPolygon polygon);
    ArrayList<IBorder> getBorders();
    boolean contains(IPoint point);

    boolean containsAll(Collection<IPoint> points);
}
