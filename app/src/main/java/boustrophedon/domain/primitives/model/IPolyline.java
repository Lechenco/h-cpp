package boustrophedon.domain.primitives.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;

public interface IPolyline {
    int getNumberOfPoints();
    ArrayList<IPoint> getPoints();
    void setPoints(ArrayList<IPoint> points);
    void add(IPoint point);
    void add(IPoint... points);
    void add(Collection<IPoint> points);
    LatLng[] toLatLngArray();
}
