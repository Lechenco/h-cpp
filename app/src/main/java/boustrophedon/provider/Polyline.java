package boustrophedon.provider;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import boustrophedon.model.IPoint;
import boustrophedon.model.IPolyline;

public class Polyline implements IPolyline {

    private int numberOfPoints = 0;
    private ArrayList<IPoint> points = null;

    public Polyline(IPoint... points) throws NullPointerException {
        try {
            ArrayList<IPoint> p = new ArrayList<>(Arrays.asList(points));
            setPoints(p);
        } catch (NullPointerException error) {
            throw error;
        }
    }

    public Polyline(ArrayList<IPoint> points) throws NullPointerException {
        try {
            setPoints(points);
        } catch (NullPointerException error) {
            throw error;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LatLng[] toLatLngArray() {
        return points.stream()
                .map(point -> point.toLatLng())
                .collect(Collectors.toList())
                .toArray(new LatLng[0]);
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
