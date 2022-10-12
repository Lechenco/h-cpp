package boustrophedon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import boustrophedon.model.IBorder;
import boustrophedon.model.IPoint;
import boustrophedon.model.IPolygon;
import boustrophedon.model.IPolyline;
import boustrophedon.provider.Border;
import boustrophedon.provider.Point;
import boustrophedon.provider.Polyline;
import boustrophedon.utils.GA;

public class Boustrophedon {
    double LENGTH_OF_PATH = 0.001;
    double ANGLE_PRECISION = Math.PI / 180;

    private final IPolygon polygon;
    private final IBorder refBorder;
    private final IPoint startPoint;
    private final IPoint endpoint;
    private ArrayList<IBorder> polygonBorders;

    public Boustrophedon(IPolygon polygon) {
        this.polygon = polygon;
        populatePolygonBorders();

        this.refBorder = calcLargestBorderAxis();
        this.startPoint = this.refBorder.getFirstVertice();
        this.endpoint = getFinalPoint();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public IPolyline generatePath() {
        IPolyline line = new Polyline();

        line.add(this.startPoint);

        walkBackAndForth(line, this.startPoint);
        line.add(this.endpoint);

        return line;
    }

    private void walkBackAndForth(IPolyline polyline, IPoint startPoint) {
        IPoint currentPoint = walkToTheOtherSide(polyline, startPoint);

        int i = 0, iterations = calcNumberOfIterations();
        while (i < iterations) {
            currentPoint = walkAStepToFront(polyline, currentPoint);
            currentPoint = walkToTheOtherSide(polyline, currentPoint);
            i++;
        }
    }

    private int calcNumberOfIterations() {
        return (int) Math.floor(this.refBorder.distanceToPoint(this.endpoint) / LENGTH_OF_PATH);
    }

    private IPoint walkToTheOtherSide(IPolyline polyline, IPoint currentPoint) {
        IPoint otherSidePoint= getNextPoint(currentPoint);
        polyline.add(otherSidePoint);
        return otherSidePoint;
    }

    private IPoint walkAStepToFront(IPolyline polyline, IPoint currentPoint) {
        IPoint stepFrontPoint = calcPointToFront(currentPoint);
        polyline.add(stepFrontPoint);
        return stepFrontPoint;
    }

    private void populatePolygonBorders() {
        this.polygonBorders = new ArrayList<>();


        for (int i = 0; i < polygon.getPoints().size(); i++) {
            IBorder border = i != polygon.getNumberOfPoints() - 1
                    ? new Border(polygon.getPoints().get(i), polygon.getPoints().get(i +1))
                    : new Border(
                    polygon.getPoints().get(polygon.getNumberOfPoints() -1),
                    polygon.getPoints().get(0)
            );
            this.polygonBorders.add((border));
        }
    }

    private IPoint getFinalPoint() {
        IPoint finalPoint = null;
        double maxDistance = 0;

        for (IPoint point : this.polygon.getPoints()) {
            if (this.startPoint.calcDistance(point) > maxDistance && !this.refBorder.isOnBorder(point)) {
                maxDistance = this.startPoint.calcDistance(point);
                finalPoint = point;
            }
        }

        return finalPoint;
    }

    private IBorder calcLargestBorderAxis() {
        IBorder largestBorder = null;
        double largestDistance = 0, aux;

        for (IBorder border : this.polygonBorders) {
            aux = border.getLength();
            if (aux > largestDistance) {
                largestDistance = aux;
                largestBorder = border;
            }
        }

        return largestBorder;
    }

    private IPoint calcPointToFront(IPoint currentPoint) {
        for (IBorder border : this.polygonBorders) {
            if (!border.equals(refBorder) && border.isOnBorder(currentPoint)) {
                double angleBetweenBorders = refBorder.angleDiff(border.getPositiveAngle());
                double distanceToWalk = LENGTH_OF_PATH / Math.sin(angleBetweenBorders);
                IPoint anticlockwisePoint = currentPoint.walk(distanceToWalk, border.getAngle());
                IPoint clockwisePoint = currentPoint.walk(distanceToWalk, border.getAngle() + Math.PI);

                return (this.endpoint.calcDistance(anticlockwisePoint) < this.endpoint.calcDistance(clockwisePoint))
                        ? anticlockwisePoint : clockwisePoint;
            }
        }

        return currentPoint;
    }

    private IPoint getNextPoint(IPoint currentPoint) {
        for (IBorder border : this.polygonBorders) {
            if (!isTheSameAngleOfRefBorder(border) && !border.isOnBorder(currentPoint)) {
                IPoint intersection = calcNextPointParallelToRef(border, currentPoint);
                if (border.isOnBorder(intersection) && !intersection.equals(currentPoint) ) {
                    return intersection;
                }
            }
        }

        return currentPoint;
    }

    private IPoint calcNextPointParallelToRef(IBorder border, IPoint currentPoint) {
        double intersectionX, intersectionY;
        double[] parallelCoefficients = this.refBorder.parallelLineCoefficients(currentPoint);
        double[] borderCoefficients = border.getCoefficients();

        if (border.isParallelToY()) {
            intersectionX = border.getFirstVertice().getX();
            intersectionY = GA.calcYPoint(parallelCoefficients, intersectionX);
        } else if (this.refBorder.isParallelToY()) {
            intersectionX = currentPoint.getX();
            intersectionY = GA.calcYPoint(borderCoefficients, intersectionX);
        } else {
            intersectionX = (borderCoefficients[0] - parallelCoefficients[0]) == 0 ?
                    currentPoint.getX() :
                    (parallelCoefficients[1] - borderCoefficients[1]) /
                            (borderCoefficients[0] - parallelCoefficients[0]);
            intersectionY = GA.calcYPoint(borderCoefficients, intersectionX);
        }

        return new Point(intersectionX, intersectionY);
    }

    private boolean isTheSameAngleOfRefBorder(IBorder border) {
        return Math.abs(this.refBorder.angleDiff(border.getPositiveAngle())) <= ANGLE_PRECISION;
    }
}
