package boustrophedon.provider.decomposer.Boustrophedon.Cell;

import java.util.ArrayList;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.primitives.Polygon;
import boustrophedon.utils.GA;

public class CellHelper {
    public static ICell createCell(ArrayList<CriticalPoint> criticalPoints) {
        ArrayList<IPoint> polygonPoints = criticalPoints
                .stream()
                .map(CriticalPoint::getVertices)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Cell(new Polygon(polygonPoints));
    }

    public static ICell getClosestCellToPoint(ArrayList<ICell> cells, IPoint point) {
        int closestIndex = -1;
        double closestDistance = Double.MAX_VALUE;

        for (int i = 0; i < cells.size();i ++) {
            double distance = Math.abs(GA.calcDistance(cells.get(i).getPolygon().getClosestVertices(point), point));

            if (distance < closestDistance) {
                closestDistance = distance;
                closestIndex = i;
            }
        }
        return cells.get(closestIndex);
    }
}
