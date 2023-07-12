package boustrophedon.provider.decomposer.Boustrophedon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.DecomposerConfig;
import boustrophedon.domain.decomposer.model.IPolygonDecomposer;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;

public class Decomposer implements IPolygonDecomposer {
    private DecomposerConfig config;
    private ArrayList<CriticalPoint> criticalPoints;

    public Decomposer() {
        this.setConfig(new DecomposerConfig());
    }

    @Override
    public void setConfig(DecomposerConfig config) {
        this.config = config;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void decompose(IPolygon polygon) {
        this.criticalPoints = this.getCriticalPoints(polygon); // sort by x

        for (CriticalPoint criticalPoint : criticalPoints) {
            criticalPoint.detectPointEvent(polygon);
        }

    }

    protected IPolygon getCellBetweenXValues (double minX, double maxX) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected ArrayList<CriticalPoint> getCriticalPoints(IPolygon polygon) {
        ArrayList<CriticalPoint> criticalPoints = new ArrayList<>();

        for (IPoint point : polygon.getPoints()) {
            List<IBorder> pointBorders = polygon.getBorders()
                    .stream().filter((b) -> b.isOnBorder(point)).collect(Collectors.toList());
            criticalPoints.add(new CriticalPoint(point, new ArrayList<>(pointBorders)));
        }

        return criticalPoints;
    }
}
