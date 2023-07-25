package boustrophedon.provider.decomposer.Boustrophedon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;

public class CriticalPointerHelper {
    protected static boolean isPointOnEdges(IPoint point, ArrayList<IBorder> edges) {
        return edges.get(0).isOnBorder(point) ||
                edges.get(1).isOnBorder(point);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected static ArrayList<CriticalPoint> unsorted(
            ArrayList<CriticalPoint> sortedPoints,
            ArrayList<CriticalPoint> criticalPoints
    ) {
        return criticalPoints
                .stream()
                .filter(sortedPoints::contains)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected static void addIntersections(CriticalPoint intersection, ArrayList<CriticalPoint> criticalPoints) {
        for (int i = 0; i < criticalPoints.size(); i++) {
            CriticalPoint current = criticalPoints.get(i);
            if (current.getEdges().size() > 0 &&
                    current.getEdges().get(0).isOnBorder(intersection.getVertices())
            ) {
                criticalPoints.add(i, intersection);
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected static ArrayList<CriticalPoint> filter(
            ArrayList<CriticalPoint> criticalPoints,
            Predicate<CriticalPoint> condition
    ) {
        return (ArrayList<CriticalPoint>) criticalPoints
                .stream()
                .filter(condition)
                .collect(Collectors.toList());
    }
}
