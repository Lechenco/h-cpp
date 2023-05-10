package boustrophedon.domain.primitives.model;

import com.google.android.gms.maps.model.LatLng;

public interface IPoint {
    LatLng toLatLng();
    double getX();
    double getY();
    IPoint walkXY(double deltaX, double deltaY);
    IPoint walk(double delta, double angle);
    double calcDistance(IPoint point);
}
