package boustrophedon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Comparator;
import java.util.NoSuchElementException;

import boustrophedon.model.IBorder;
import boustrophedon.model.IPoint;
import boustrophedon.model.IPolygon;
import boustrophedon.provider.Border;
import boustrophedon.provider.Point;
import boustrophedon.provider.Polyline;

public class Boustrophedon {
    double LENGTH_OF_PATH = 0.001;
    double ANGLE_PRECISION = Math.PI / 180;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public Polyline generatePath(IPolygon polygon) {
        Polyline line = new Polyline();

        IBorder largestBorder = calcLargestBorderAxis(polygon);

        IPoint end = getFinalPoint(polygon, largestBorder.getFirstVertice(), largestBorder);

        IPoint currentPoint = largestBorder.getFirstVertice();

        // Initial
        line.add(currentPoint);

        // Walk to the next border
        currentPoint = getNextPoint(polygon, currentPoint, largestBorder);
        line.add(currentPoint);

        int i = 0, iterations = (int) Math.floor(largestBorder.distanceToPoint(end) / LENGTH_OF_PATH);
        while (i < iterations) {
            // Walk to the side
            currentPoint = calcPointToSide(polygon, currentPoint, largestBorder, end);
            line.add(currentPoint);

            // Walk to the next border
            currentPoint = getNextPoint(polygon, currentPoint, largestBorder);
            line.add(currentPoint);

            i++;
        }
        // End of path
        line.add(end);

        return line;
    }

    private IPoint getFinalPoint(IPolygon polygon, IPoint initialPoint, IBorder borderRef) {
        IPoint finalPoint = null;
        double maxDistance = 0;

        for (IPoint point : polygon.getPoints()) {
            if (initialPoint.calcDistance(point) > maxDistance && !borderRef.isOnBorder(point)) {
                maxDistance = initialPoint.calcDistance(point);
                finalPoint = point;
            }
        }

        return finalPoint;
    }

    private IBorder calcLargestBorderAxis(IPolygon polygon) {
        IBorder largestBorder = new Border(
                polygon.getPoints().get(polygon.getNumberOfPoints() -1),
                polygon.getPoints().get(0)
        );
        double largestDistance = largestBorder.getLength(), aux = 0;

        for (int i = 0; i < polygon.getPoints().size() -1; i++) {
            IBorder border = new Border(polygon.getPoints().get(i), polygon.getPoints().get(i +1));
            aux = border.getLength();
            if (aux > largestDistance) {
                largestDistance = aux;
                largestBorder = border;
            }
        }

        return largestBorder;
    }

    private IPoint calcPointToSide(IPolygon polygon, IPoint currentPoint, IBorder refBorder, IPoint end) {
        for (int i = 0; i < polygon.getPoints().size(); i++) {
            IBorder border = i != polygon.getNumberOfPoints() - 1
                    ? new Border(polygon.getPoints().get(i), polygon.getPoints().get(i + 1))
                    : new Border(
                    polygon.getPoints().get(polygon.getNumberOfPoints() - 1),
                    polygon.getPoints().get(0)
            );

            if (!border.equals(refBorder) && border.isOnBorder(currentPoint)) {
                double angleBetweenBorders = refBorder.angleDiff(border.getPositiveAngle());
                IPoint anticlockwisePoint = currentPoint.walk(LENGTH_OF_PATH / Math.sin(angleBetweenBorders), border.getAngle());
                IPoint clockwisePoint = currentPoint.walk(LENGTH_OF_PATH / Math.sin(angleBetweenBorders), border.getAngle() + Math.PI);

                return (end.calcDistance(anticlockwisePoint) < end.calcDistance(clockwisePoint))
                        ? anticlockwisePoint : clockwisePoint;
            }
        }

        return currentPoint;
    }

    private IPoint getNextPoint(IPolygon polygon, IPoint currentPoint, IBorder refBorder) {
        double intersectionX = 0, intersectionY = 0;
        double[] parallelCoefficients = refBorder.parallelLineCoefficients(currentPoint);

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            IBorder border = i != polygon.getNumberOfPoints() - 1
                    ? new Border(polygon.getPoints().get(i), polygon.getPoints().get(i +1))
                    : new Border(
                        polygon.getPoints().get(polygon.getNumberOfPoints() -1),
                        polygon.getPoints().get(0)
                    );
            if (Math.abs(refBorder.angleDiff(border.getPositiveAngle())) > ANGLE_PRECISION && !border.isOnBorder(currentPoint)) {
                double[] borderCoefficients = border.getCoefficients();
                if (border.isParallelToY()) {
                    intersectionX = border.getFirstVertice().getX();
                    intersectionY = parallelCoefficients[0] * intersectionX + parallelCoefficients[1];
                } else if (refBorder.isParallelToY()) {
                    intersectionX = currentPoint.getX();
                    intersectionY = borderCoefficients[0] * intersectionX + borderCoefficients[1];
                } else {
                    intersectionX = (borderCoefficients[0] - parallelCoefficients[0]) == 0 ?
                            currentPoint.getX() :
                            (parallelCoefficients[1] - borderCoefficients[1]) /
                                    (borderCoefficients[0] - parallelCoefficients[0]);
                    intersectionY = borderCoefficients[0] * intersectionX + borderCoefficients[1];
                }

                IPoint intersection = new Point(intersectionX, intersectionY);
                if (border.isOnBorder(intersection) && !intersection.equals(currentPoint) ) {
                    return intersection;
                }
            }
        }

        return currentPoint;
    }
}
