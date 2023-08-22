package boustrophedon.provider.decomposer.Boustrophedon.Cell;

import java.util.ArrayList;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.primitives.Polygon;

public class CellHelper {
    public static ICell createCell(ArrayList<CriticalPoint> criticalPoints) {
        ArrayList<IPoint> polygonPoints = criticalPoints
                .stream()
                .map(CriticalPoint::getVertices)
                .collect(Collectors.toCollection(ArrayList::new));

        return new Cell(new Polygon(polygonPoints));
    }

}
