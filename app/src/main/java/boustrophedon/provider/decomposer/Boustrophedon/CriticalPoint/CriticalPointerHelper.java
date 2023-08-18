package boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CriticalPointerHelper {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<CriticalPoint> sort(
            ArrayList<CriticalPoint> criticalPoints
    ) {
        return criticalPoints
                .stream()
                .sorted(Comparator.comparingDouble(criticalPoint -> criticalPoint.getVertices().getX()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<CriticalPoint> unsorted(
            ArrayList<CriticalPoint> sortedPoints,
            ArrayList<CriticalPoint> criticalPoints
    ) {
        return criticalPoints
                .stream()
                .filter(sortedPoints::contains)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addIntersections(CriticalPoint intersection, ArrayList<CriticalPoint> criticalPoints) {
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
    public static ArrayList<CriticalPoint> filter(
            ArrayList<CriticalPoint> criticalPoints,
            Predicate<CriticalPoint> condition
    ) {
        return (ArrayList<CriticalPoint>) criticalPoints
                .stream()
                .filter(condition)
                .collect(Collectors.toList());
    }
}
