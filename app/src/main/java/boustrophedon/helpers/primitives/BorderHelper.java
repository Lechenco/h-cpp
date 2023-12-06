package boustrophedon.helpers.primitives;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.walkers.Walker;
import boustrophedon.utils.AngleUtils;
import boustrophedon.utils.GA;

public class BorderHelper {
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
        if (refBorder.isParallelToY())
            return calcIntersectionWithRefParallelToY(currentPoint, wall);
        double intersectionX, intersectionY;
        double[] refBorderCoefficients = refBorder.getParallelLineCoefficients(currentPoint);
        intersectionX = wall.getFirstVertice().getX();
        intersectionY = GA.calcYPoint(refBorderCoefficients, intersectionX);

        return new Point(intersectionX, intersectionY);
    }

    static public IPoint calcIntersectionWithRefParallelToY(IPoint currentPoint, IBorder wall) {
        if (wall.isParallelToY())
            return new Point(wall.getFirstVertice().getX(), currentPoint.getY());

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

        intersectionX = Math.abs(wall.getAngleFirstHalf() - refBorder.getAngleFirstHalf()) < Walker.ANGLE_PRECISION ?
                currentPoint.getX() :
                (refBorderCoefficients[1] - wallCoefficients[1]) /
                        (wallCoefficients[0] - refBorderCoefficients[0]);
        intersectionY = GA.calcYPoint(wallCoefficients, intersectionX);

        return new Point(intersectionX, intersectionY);
    }

    static public ArrayList<IBorder> findWalls(ArrayList<IBorder> borders, double angle) {
        ArrayList<IBorder> walls = new ArrayList<>();

        double angleSanitized = AngleUtils.getFirstHalfAngle(angle);

        for (IBorder border : borders) {
            if (Math.abs(angleSanitized - border.getAngleFirstHalf()) > Walker.ANGLE_PRECISION)
                walls.add(border);
        }
        return walls;
    }

}
