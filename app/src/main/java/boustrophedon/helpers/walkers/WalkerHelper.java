package boustrophedon.helpers.walkers;

import static boustrophedon.utils.AngleUtils.add180Degrees;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.primitives.BorderHelper;
import boustrophedon.provider.primitives.Border;
import boustrophedon.utils.GA;

public class WalkerHelper {
    static public IPoint calcIntersectionToWall(IPoint currentPoint, IBorder wall, double angle) {
        return BorderHelper.calcIntersectionToWall(currentPoint, wall, angle);
    }

    static public ArrayList<IBorder> findWalls(ArrayList<IBorder> borders, double angle) {
        return BorderHelper.findWalls(borders, angle);
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

    static public boolean isPointInsidePolygon(IPoint point, IPolygon polygon) {
        ArrayList<IBorder> polygonBorders = polygon.getBorders();

        return  isPointInsidePolygonBorders(point, polygonBorders);
    }
    static public boolean isPointInsidePolygonBorders(IPoint point, ArrayList<IBorder> polygonBorders) {
        int countIntersection = 0;

        for(IBorder border : polygonBorders) {
            if (border.isOnBorder(point)) return true;

            IPoint intersection = calcIntersectionToWall(point, border, 0);

            if (border.isOnBorder(intersection) && intersection.getX() > point.getX())
                countIntersection++;
        }

        return countIntersection == 1;
    }

    static public IPoint walkAside(
            IPoint currentPoint,
            double distanceToWalk,
            double currentWallAngle,
            double absDiffCosGoalAndWall,
            double absDiffCosGoalAndWall180
    ) {
        if (absDiffCosGoalAndWall < absDiffCosGoalAndWall180)
            return currentPoint.walk(distanceToWalk, currentWallAngle);

        return currentPoint.walk(distanceToWalk, add180Degrees(currentWallAngle));
    }

    static public IPoint walkAsideWallAndGoalOrthogonal(
            IPoint currentPoint,
            double distanceToWalk,
            double currentWallAngle,
            double absDiffCosGoalAndWall,
            double absDiffCosGoalAndWall180
    ) {
        if (absDiffCosGoalAndWall > absDiffCosGoalAndWall180)
            return currentPoint.walk(distanceToWalk, currentWallAngle);

        return currentPoint.walk(distanceToWalk, add180Degrees(currentWallAngle));
    }

    static public IPoint walkAsideWallOrGoalParallelToXAxis(
            IPoint currentPoint,
            double distanceToWalk,
            double currentWallAngle,
            double sinStartToGoal
    ) {
        if (Math.abs(sinStartToGoal - Math.sin(currentWallAngle)) <
                Math.abs(sinStartToGoal - Math.sin(add180Degrees(currentWallAngle))))
            return currentPoint.walk(distanceToWalk, currentWallAngle);

        return currentPoint.walk(distanceToWalk, add180Degrees(currentWallAngle));
    }

    static public  IPoint getClosestWallVertices(IPoint point, IBorder wall) {
        return Math.abs(
                GA.calcDistance(point, wall.getFirstVertice())
        ) < Math.abs(
                GA.calcDistance(point, wall.getSecondVertice())
        ) ? wall.getFirstVertice() : wall.getSecondVertice();
    }
}
