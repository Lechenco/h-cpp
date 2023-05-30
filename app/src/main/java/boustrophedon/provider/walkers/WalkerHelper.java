package boustrophedon.provider.walkers;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.primitives.BorderHelper;
import boustrophedon.provider.primitives.Border;

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
        ArrayList<IBorder> polygonBorders = getPolygonBorders(polygon);

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
}