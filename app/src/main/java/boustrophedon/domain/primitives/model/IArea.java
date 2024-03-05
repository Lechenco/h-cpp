package boustrophedon.domain.primitives.model;

import java.util.ArrayList;

public interface IArea {
    int getNumberOfSubAreas();
    void add(ISubarea sub);
    ArrayList<ISubarea> getSubareas();
    IPolygon getGeometry();
}
