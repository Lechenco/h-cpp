package boustrophedon.provider.primitives;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.primitives.BorderHelper;
import boustrophedon.utils.GA;

public class Polygon implements IPolygon {

    public int numberOfPoints = 0;
    public ArrayList<IPoint> points = null;
    public ArrayList<IBorder> borders = null;

    public Polygon(IPoint... points) throws NullPointerException {
        ArrayList<IPoint> p = new ArrayList<>(Arrays.asList(points));
        setPoints(p);
    }

    public Polygon(ArrayList<IPoint> points) throws NullPointerException {
        setPoints(points);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public LatLng[] toLatLngArray() {
        return points.stream()
                .map(IPoint::toLatLng).toArray(LatLng[]::new);
    }

    @Override
    public IPoint getOutsiderPointInDirection(IPoint startPoint, double direction) {
        IPoint outsiderPoint = null;
        for (IBorder border : borders) {
            IPoint intersection = BorderHelper.calcIntersectionToWall(startPoint, border, direction);

            if (outsiderPoint == null ||
                    (startPoint.calcDistance(intersection) > startPoint.calcDistance(outsiderPoint) &&
                            Math.abs(border.getAngleDiff(direction)) > 0.001
                    )
            )
                outsiderPoint = intersection;

        }
        return outsiderPoint;
    }
    @Override
    public IPoint getClosestVertices(IPoint point) {
        IPoint closest = this.points.get(0);

        for (int i = 1; i < this.numberOfPoints; i++) {
            IPoint p = this.points.get(i);
            if (point.calcDistance(p) < point.calcDistance(closest))
                closest = p;
        }

        return closest;
    }

    @Override
    public IPoint getFarthestVertices(IPoint point) {
        IPoint farthest = this.points.get(0);

        for (int i = 1; i < this.numberOfPoints; i++) {
            IPoint p = this.points.get(i);
            if (point.calcDistance(p) > point.calcDistance(farthest))
                farthest = p;
        }

        return farthest;
    }

    @Override
    public IPoint getFarthestVertices(IPoint point, double direction) {
        IPoint farthest = this.points.get(0);

        for (int i = 1; i < this.numberOfPoints; i++) {
            IPoint p = this.points.get(i);
            if (
                    GA.calcDistanceWithDirection(point, p, direction)
                            > GA.calcDistanceWithDirection(point, farthest, direction)
            )
                farthest = p;
        }

        return farthest;
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
        this.borders = getBorders();
    }

    private ArrayList<IBorder> getBorders() {
        ArrayList<IBorder> polygonBorders = new ArrayList<>();

        for (int i = 0; i < this.getPoints().size(); i++) {
            IBorder border = i != this.getNumberOfPoints() - 1
                    ? new Border(this.getPoints().get(i), this.getPoints().get(i + 1))
                    : new Border(
                    this.getPoints().get(this.getNumberOfPoints() - 1),
                    this.getPoints().get(0)
            );
            polygonBorders.add((border));
        }
        return polygonBorders;
    }
}
