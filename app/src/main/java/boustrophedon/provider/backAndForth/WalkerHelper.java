package boustrophedon.provider.backAndForth;

import java.util.ArrayList;

import boustrophedon.domain.backAndForth.model.WalkerConfig;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;
import boustrophedon.utils.GA;

public class WalkerHelper {
    static public IPoint calcIntersectionToWall(IPoint currentPoint, IBorder wall, double angle) {
        if (wall.isOnBorder(currentPoint)) return currentPoint;

        IBorder refBorder = new Border(new Point(0, 0), new Point(Math.cos(angle), Math.sin(angle)));

        if (wall.isParallelToY()) {
            return calcIntersectionWithWallParallelToY(currentPoint, wall, refBorder);
        } else if (refBorder.isParallelToY()) {
            return calcIntersectionWithRefParallelToY(currentPoint, wall);
        } else {
            return calcIntersection(currentPoint, refBorder, wall);
        }
    }

    static public IPoint calcIntersectionWithWallParallelToY(IPoint currentPoint, IBorder wall, IBorder refBorder) {
        double intersectionX, intersectionY;
        double[] refBorderCoefficients = refBorder.getParallelLineCoefficients(currentPoint);
        intersectionX = wall.getFirstVertice().getX();
        intersectionY = GA.calcYPoint(refBorderCoefficients, intersectionX);

        return new Point(intersectionX, intersectionY);
    }

    static public IPoint calcIntersectionWithRefParallelToY(IPoint currentPoint, IBorder wall) {
        double intersectionX, intersectionY;
        double[] wallCoefficients = wall.getCoefficients();

        intersectionX = currentPoint.getX();
        intersectionY = GA.calcYPoint(wallCoefficients, intersectionX);

        return new Point(intersectionX, intersectionY);
    }

    static public IPoint calcIntersection(IPoint currentPoint, IBorder refBorder, IBorder wall) {
        double intersectionX, intersectionY;
        double[] refBorderCoefficients = refBorder.getParallelLineCoefficients(currentPoint);
        double[] wallCoefficients = wall.getCoefficients();

        intersectionX = (wallCoefficients[0] - refBorderCoefficients[0]) == 0 ?
                currentPoint.getX() :
                (refBorderCoefficients[1] - wallCoefficients[1]) /
                        (wallCoefficients[0] - refBorderCoefficients[0]);
        intersectionY = GA.calcYPoint(wallCoefficients, intersectionX);

        return new Point(intersectionX, intersectionY);
    }

    static public ArrayList<IBorder> findWalls(ArrayList<IBorder> borders, double angle) {
        ArrayList<IBorder> walls = new ArrayList<>();

        double angleSanitized = GA.getFirstHalfAngle(angle);

        for (IBorder border : borders) {
            if (Math.abs(angleSanitized - border.getAngleFirstHalf()) > WalkerConfig.ANGLE_PRECISION)
                walls.add(border);
        }
        return walls;
    }

    static public ArrayList<IBorder> getPolygonBorders(IPolygon polygon) {
        ArrayList<IBorder> polygonBorders = new ArrayList<>();

        for (int i = 0; i < polygon.getPoints().size(); i++) {
            IBorder border = i != polygon.getNumberOfPoints() - 1
                    ? new Border(polygon.getPoints().get(i), polygon.getPoints().get(i + 1))
                    : new Border(
                    polygon.getPoints().get(polygon.getNumberOfPoints() - 1),
                    polygon.getPoints().get(0)
            );
            polygonBorders.add((border));
        }
        return polygonBorders;
    }
}
