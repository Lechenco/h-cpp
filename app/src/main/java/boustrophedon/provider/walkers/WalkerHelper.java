package boustrophedon.provider.walkers;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.primitives.BorderHelper;
import boustrophedon.provider.primitives.Border;
import boustrophedon.utils.GA;

public class WalkerHelper {
    static protected IPoint calcIntersectionToWall(IPoint currentPoint, IBorder wall, double angle) {
        return BorderHelper.calcIntersectionToWall(currentPoint, wall, angle);
    }

    static protected ArrayList<IBorder> findWalls(ArrayList<IBorder> borders, double angle) {
        return BorderHelper.findWalls(borders, angle);
    }

    static protected ArrayList<IBorder> getPolygonBorders(IPolygon polygon) {
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

    static protected boolean isPointInsidePolygon(IPoint point, IPolygon polygon) {
        ArrayList<IBorder> polygonBorders = polygon.getBorders();

        return  isPointInsidePolygonBorders(point, polygonBorders);
    }
    static protected boolean isPointInsidePolygonBorders(IPoint point, ArrayList<IBorder> polygonBorders) {
        int countIntersection = 0;

        for(IBorder border : polygonBorders) {
            if (border.isOnBorder(point)) return true;

            IPoint intersection = calcIntersectionToWall(point, border, 0);

            if (border.isOnBorder(intersection) && intersection.getX() > point.getX())
                countIntersection++;
        }

        return countIntersection == 1;
    }

    static protected IPoint walkAside(
            IPoint currentPoint,
            double distanceToWalk,
            double currentWallAngle,
            double absDiffCosGoalAndWall,
            double absDiffCosGoalAndWall180
    ) {
        if (absDiffCosGoalAndWall < absDiffCosGoalAndWall180)
            return currentPoint.walk(distanceToWalk, currentWallAngle);

        return currentPoint.walk(distanceToWalk, currentWallAngle + Math.PI);
    }

    static protected IPoint walkAsideWallAndGoalOrthogonal(
            IPoint currentPoint,
            double distanceToWalk,
            double currentWallAngle,
            double absDiffCosGoalAndWall,
            double absDiffCosGoalAndWall180
    ) {
        if (absDiffCosGoalAndWall > absDiffCosGoalAndWall180)
            return currentPoint.walk(distanceToWalk, currentWallAngle);

        return currentPoint.walk(distanceToWalk, currentWallAngle + Math.PI);
    }

    static protected IPoint walkAsideWallOrGoalParallelToXAxis(
            IPoint currentPoint,
            double distanceToWalk,
            double currentWallAngle,
            double sinStartToGoal
    ) {
        if (Math.abs(sinStartToGoal - Math.sin(currentWallAngle)) <
                Math.abs(sinStartToGoal - Math.sin(currentWallAngle + Math.PI)))
            return currentPoint.walk(distanceToWalk, currentWallAngle);

        return currentPoint.walk(distanceToWalk, currentWallAngle + Math.PI);
    }

    static protected  IPoint getClosestWallVertices(IPoint point, IBorder wall) {
        return Math.abs(
                GA.calcDistance(point, wall.getFirstVertice())
        ) < Math.abs(
                GA.calcDistance(point, wall.getSecondVertice())
        ) ? wall.getFirstVertice() : wall.getSecondVertice();
    }
}
