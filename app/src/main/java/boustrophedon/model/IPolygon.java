package boustrophedon.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public interface IPolygon {
    int getNumberOfPoints();
    ArrayList<IPoint> getPoints();
    void setPoints(ArrayList<IPoint> points);
    LatLng[] toLatLngArray();
}
