package boustrophedon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import boustrophedon.model.IBorder;
import boustrophedon.model.ICell;
import boustrophedon.model.IPoint;
import boustrophedon.model.IPolygon;
import boustrophedon.provider.Border;
import boustrophedon.provider.Point;
import boustrophedon.provider.Polyline;

public class Boustrophedon {
    double LENGTH_OF_PATH = 0.001;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public Polyline generatePath(IPolygon polygon) {
        Polyline line = new Polyline();

        IBorder largestBorder = calcLargestBorderAxis(polygon);

        // TODO calculate most distance point from other side
        IPoint end = polygon.getPoints().stream()
                .min(Comparator.comparingDouble(IPoint::getY))
                .orElseThrow(NoSuchElementException::new);

        IPoint currentPoint = largestBorder.getFirstVertice();

        // Initial
        line.add(currentPoint);

        do {
            // Walk to the next border
            currentPoint = getNextPoint(polygon, currentPoint, largestBorder);
            line.add(currentPoint);

            // Walk to the side
            currentPoint = calcPointToSide(polygon, currentPoint, largestBorder, end);
            line.add(currentPoint);
        } while (currentPoint.calcDistance(end) > LENGTH_OF_PATH);
        // End of path
        line.add(end);

        return line;
    }

    private IBorder calcLargestBorderAxis(IPolygon polygon) {
        IBorder largestBorder = new Border(
                polygon.getPoints().get(polygon.getNumberOfPoints() -1),
                polygon.getPoints().get(0)
        );
        double largestDistance = largestBorder.getLenght(), aux = 0;

        for (int i = 0; i < polygon.getPoints().size() -1; i++) {
            IBorder border = new Border(polygon.getPoints().get(i), polygon.getPoints().get(i +1));
            aux = border.getLenght();
            if (aux > largestDistance) {
                largestDistance = aux;
                largestBorder = border;
            }
        }

        return largestBorder;
    }

    private IPoint calcPointToSide(IPolygon polygon, IPoint currentPoint, IBorder refBorder, IPoint end) {
        IPoint anticlockwisePoint = currentPoint.walk(LENGTH_OF_PATH, refBorder.getAngle() + Math.PI / 2);
        IPoint clockwisePoint = currentPoint.walk(LENGTH_OF_PATH, refBorder.getAngle() - Math.PI / 2);

        IPoint stepPoint =  (end.calcDistance(anticlockwisePoint) < end.calcDistance(clockwisePoint))
                ? anticlockwisePoint : clockwisePoint;

        // get current border
        // point in the border with

        return stepPoint;
    }

    private IPoint getNextPoint(IPolygon polygon, IPoint currentPoint, IBorder refBorder) {
        double[] currentCoef = refBorder.getCoeficients();
        // TODO: calculate paralell coeficients
        double[] paralellCoef = new double[]{
                currentCoef[0],
                currentCoef[1] //- LENGTH_OF_PATH*Math.sin(refBorder.getAngle())
        };

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            IBorder border = i != polygon.getNumberOfPoints() - 1
                    ? new Border(polygon.getPoints().get(i), polygon.getPoints().get(i +1))
                    : new Border(
                        polygon.getPoints().get(polygon.getNumberOfPoints() -1),
                        polygon.getPoints().get(0)
                    );
            if (border.getAngle() != refBorder.getAngle()) {
                double[] borderCoef = border.getCoeficients();
                double intersectionX = (borderCoef[0] - paralellCoef[0]) == 0 ?
                        currentPoint.getX() :
                        (paralellCoef[1] - borderCoef[1]) / (borderCoef[0] - paralellCoef[0]);
                double intersectionY = borderCoef[0] * intersectionX + borderCoef[1];

                IPoint intersection = new Point(intersectionX, intersectionY);
                if (border.isOnBorder(intersection) && !intersection.equals(currentPoint) ) {
                    return intersection;
                }
            }
        }

        return currentPoint;
    }
}
