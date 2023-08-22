package boustrophedon.provider.primitives;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolyline;

public class Polyline implements IPolyline {

    private int numberOfPoints = 0;
    private ArrayList<IPoint> points = null;

    public Polyline(IPoint... points) throws NullPointerException {
        ArrayList<IPoint> p = new ArrayList<>(Arrays.asList(points));
        setPoints(p);
    }

    public Polyline(ArrayList<IPoint> points) throws NullPointerException {
        setPoints(points);
    }

    public LatLng[] toLatLngArray() {
        return points.stream()
                .map(IPoint::toLatLng).toArray(LatLng[]::new);
    }

    @Override
    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    @Override
    public ArrayList<IPoint> getPoints() {
        return points;
    }

    @Override
    public IPoint getLastPoint() {
        return this.points.get(this.numberOfPoints -1);
    }

    @Override
    public void setPoints(ArrayList<IPoint> points) throws NullPointerException {
        if (points == null)
            throw new NullPointerException("No points founded");

        this.points = points;
        this.numberOfPoints = this.points.size();
    }

    @Override
    public void add(IPoint point) throws NullPointerException{
        if (point == null)
            throw new NullPointerException("No point");

        this.points.add(point);
        this.numberOfPoints = this.points.size();
    }

    @Override
    public void add(IPoint... points) throws NullPointerException{
        if (points == null)
            throw new NullPointerException("No point");


        this.points.addAll(Arrays.asList(points));
        this.numberOfPoints = this.points.size();

    }

    @Override
    public void add(Collection<IPoint> points) throws NullPointerException{
        if (points == null)
            throw new NullPointerException("No point");

        this.points.addAll(points);
        this.numberOfPoints = this.points.size();

    }
}
