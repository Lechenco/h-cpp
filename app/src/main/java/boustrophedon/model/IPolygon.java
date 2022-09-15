package boustrophedon.model;

import java.util.ArrayList;

public interface IPolygon {
    int getNumberOfPoints();
    ArrayList<IPoint> getPoints();
    void setPoints(ArrayList<IPoint> points);
}
