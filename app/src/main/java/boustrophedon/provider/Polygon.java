package boustrophedon.provider;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import boustrophedon.model.IBorder;
import boustrophedon.model.IPoint;
import boustrophedon.model.IPolygon;

public class Polygon implements IPolygon {

    public int numberOfPoints = 0;
    public ArrayList<IPoint> points = null;

    public Polygon(IPoint... points) throws NullPointerException {
        try {
            ArrayList<IPoint> p = new ArrayList<>(Arrays.asList(points));
            setPoints(p);
        } catch (NullPointerException error) {
            throw error;
        }
    }

    public Polygon(ArrayList<IPoint> points) throws NullPointerException {
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
}
