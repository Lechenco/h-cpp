package boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.provider.primitives.Border;

public class CriticalPointerHelper {
    public static ArrayList<ICriticalPoint> sort(
            ArrayList<ICriticalPoint> criticalPoints
    ) {
        return criticalPoints
                .stream()
                .sorted(Comparator.comparingDouble(criticalPoint -> criticalPoint.getVertices().getX()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ICriticalPoint> unsorted(
            ArrayList<ICriticalPoint> sortedPoints,
            ArrayList<ICriticalPoint> criticalPoints
    ) {
        return criticalPoints
                .stream()
                .filter(sortedPoints::contains)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void addIntersections(ICriticalPoint intersection, ArrayList<ICriticalPoint> criticalPoints) {
        for (int i = 0; i < criticalPoints.size(); i++) {
            ICriticalPoint current = criticalPoints.get(i);
            if (current.getEdges().size() > 0 &&
                    current.getEdges().get(0).isOnBorder(intersection.getVertices())
            ) {
                criticalPoints.add(i, intersection);
                break;
            }
        }
    }
    public static void populateIntersectionEdges(ICriticalPoint intersection, ArrayList<ICriticalPoint> criticalPoints) {
        ArrayList<ICriticalPoint> cps = criticalPoints
                .stream()
                .filter(cp -> intersection.getEdges().stream().anyMatch(e -> e.isOnBorder(cp.getVertices())))
                .collect(Collectors.toCollection(ArrayList::new));

        cps.forEach(cp -> addIntersectionBorderToEdges(intersection, cp));
    }
    private static void addIntersectionBorderToEdges(ICriticalPoint intersection, ICriticalPoint cp) {
        Optional<IBorder> border = intersection.getEdges().stream().filter(
                e -> e.getFirstVertice() == cp.getVertices()
                || e.getSecondVertice() == cp.getVertices()
        ).findFirst();

        border.ifPresent(iBorder -> cp.getEdges().add(iBorder));
    }

    public static ArrayList<ICriticalPoint> filter(
            ArrayList<ICriticalPoint> criticalPoints,
            Predicate<ICriticalPoint> condition
    ) {
        return (ArrayList<ICriticalPoint>) criticalPoints
                .stream()
                .filter(condition)
                .collect(Collectors.toList());
    }
}
