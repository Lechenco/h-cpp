package boustrophedon.provider.primitives;

import static boustrophedon.constants.PrecisionConstants.DISTANCE_PRECISION;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;

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
    public boolean isAdjacentTo(IPolygon polygon) {

        for (IPoint point : this.points) {
            for (IPoint p : polygon.getPoints()) {
                if (point.equals(p)) return true;
            }
        }

        return false;
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

        if (!isClockwise(points)) {
            Collections.reverse(points);
        }

        this.points = points;
        this.numberOfPoints = this.points.size();
        this.borders = populateBorders();
    }

    private ArrayList<IBorder> populateBorders() {
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

    public ArrayList<IBorder> getBorders() {
        return this.borders;
    }

    private double[] getXLimits() {
        double min = this.points.stream().map(IPoint::getX).min(
                Comparator.comparing(aDouble -> aDouble)
        ).orElseThrow(NoSuchElementException::new);
        double max = this.points.stream().map(IPoint::getX).max(
                Comparator.comparing(aDouble -> aDouble)
        ).orElseThrow(NoSuchElementException::new);;

        return new double[]{min, max};
    }
    private double[] getYLimits() {
        double min = this.points.stream().map(IPoint::getY).min(
                Comparator.comparing(aDouble -> aDouble)
        ).orElseThrow(NoSuchElementException::new);
        double max = this.points.stream().map(IPoint::getY).max(
                Comparator.comparing(aDouble -> aDouble)
        ).orElseThrow(NoSuchElementException::new);;

        return new double[]{min, max};
    }

    private boolean contains(IPoint point, double[] xLimits, double[] yLimits) {
        return xLimits[0] - DISTANCE_PRECISION < point.getX() &&
                point.getX() < xLimits[1] + DISTANCE_PRECISION &&
                yLimits[0] - DISTANCE_PRECISION < point.getY() &&
                point.getY() < yLimits[1] + DISTANCE_PRECISION;
    }

    @Override
    public boolean contains(IPoint point) {
        double[] xLimits = getXLimits();
        double[] yLimits = getYLimits();

        return contains(point, xLimits, yLimits);
    }

    @Override
    public boolean containsAll(Collection<IPoint> points) {
        double[] xLimits = getXLimits();
        double[] yLimits = getYLimits();

        return points.stream()
                .map(point -> contains(point, xLimits, yLimits))
                .reduce(true,
                (a, b) -> a && b
        );
    }

    private boolean isClockwise(Collection<IPoint> points) {
        ArrayList<IPoint> array = new ArrayList<>(points);
        double total = array.get(array.size() -1).getX() * array.get(0).getY() -
                array.get(0).getX() * array.get(array.size() -1).getY();
        for (int i = 0; i < array.size() -1; i++){
            total += array.get(i).getX() * array.get(i + 1).getY() -
                    array.get(i + 1).getX() * array.get(i).getY();
        }

        return total <= 0;
    }
}
