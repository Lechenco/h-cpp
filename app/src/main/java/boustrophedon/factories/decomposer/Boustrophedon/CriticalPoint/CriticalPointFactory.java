package boustrophedon.factories.decomposer.Boustrophedon.CriticalPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.primitives.Border;

public class CriticalPointFactory {
    public static ArrayList<CriticalPoint> execute(IPolygon polygon) {
        ArrayList<CriticalPoint> criticalPoints = createCriticalPointsFromPolygon(polygon);

        criticalPoints.forEach(cp -> cp.detectPointEvent(polygon));

        ArrayList<CriticalPoint> intersections = getAllIntersections(criticalPoints);

        addIntersections(criticalPoints, intersections);

        return  criticalPoints;
    }

    public static ArrayList<ICriticalPoint> execute(IPolygon polygon, ArrayList<ICriticalPoint> criticalPoints ) {
        ArrayList<CriticalPoint> _criticalPoints = createCriticalPointsFromPolygon(polygon);

        ArrayList<CriticalPoint> intersections = getAllIntersections(criticalPoints.stream()
                .map(p -> (CriticalPoint) p).collect(Collectors.toCollection(ArrayList::new)));

        addIntersections(_criticalPoints, intersections);

        return  _criticalPoints.stream()
                .map(p -> (ICriticalPoint) p).collect(Collectors.toCollection(ArrayList::new));
    }

    static private ArrayList<CriticalPoint> createCriticalPointsFromPolygon(IPolygon polygon) {
        ArrayList<CriticalPoint> criticalPoints = new ArrayList<>();

        for (IPoint point : polygon.getPoints()) {
            List<IBorder> pointBorders = polygon.getBorders()
                    .stream().filter((b) -> b.isOnBorder(point)).collect(Collectors.toList());
            criticalPoints.add(new CriticalPoint(point, new ArrayList<>(pointBorders)));
        }

        Collections.swap(criticalPoints.get(0).getEdges(), 0, 1);
        return criticalPoints;
    }

    static private ArrayList<CriticalPoint> getAllIntersections(ArrayList<CriticalPoint> criticalPoints) {
        return criticalPoints
                .stream()
                .map(CriticalPoint::getIntersectionsInNormal)
                .flatMap(ArrayList::stream)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    static protected void addIntersections(ArrayList<CriticalPoint> criticalPoints, ArrayList<CriticalPoint> intersections) {
        for (CriticalPoint intersection: intersections) {
            for (int i = 0; i < criticalPoints.size(); i++) {
                CriticalPoint current = criticalPoints.get(i);
                if (current.getEdges().size() > 0 &&
                        current.getEdges().get(0).isOnBorder(intersection.getVertices())
                ) {
                    criticalPoints.add(i, intersection);
                    updateBorders(criticalPoints, i);
                    break;
                }
            }
        }
    }

    static protected void updateBorders(ArrayList<CriticalPoint> criticalPoints, int index) {
        int indexBefore = index != 0 ? index -1 : criticalPoints.size() -1;
        int indexAfter = index != criticalPoints.size() -1 ? index +1 : 0;

        CriticalPoint criticalPoint = criticalPoints.get(index);
        CriticalPoint criticalPointBefore = criticalPoints.get(indexBefore);
        CriticalPoint criticalPointAfter = criticalPoints.get(indexAfter);

        IBorder borderBefore = new Border(criticalPointBefore.getVertices(), criticalPoint.getVertices());
        IBorder borderAfter = new Border(criticalPointAfter.getVertices(), criticalPoint.getVertices());

        criticalPoint.getEdges().addAll(Arrays.asList(borderBefore, borderAfter));
        criticalPointBefore.getEdges().add(borderBefore);
        criticalPointAfter.getEdges().add(0, borderAfter);

        criticalPointBefore.getEdges().removeIf(
                b -> b.getFirstVertice().equals(criticalPointAfter.getVertices())
                || b.getSecondVertice().equals(criticalPointAfter.getVertices())
        );
        criticalPointAfter.getEdges().removeIf(
                b -> b.getFirstVertice().equals(criticalPointBefore.getVertices())
                        || b.getSecondVertice().equals(criticalPointBefore.getVertices())
        );
    }
}
