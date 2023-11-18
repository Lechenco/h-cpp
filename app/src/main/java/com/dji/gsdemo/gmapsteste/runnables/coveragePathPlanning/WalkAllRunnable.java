package com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.runnables.RunnableWithCallback;

import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Collection;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.domain.walkers.error.AngleOffLimitsException;
import boustrophedon.domain.walkers.model.WalkerConfig;
import boustrophedon.provider.primitives.Polyline;
import boustrophedon.provider.walkers.Walker;

public class WalkAllRunnable extends RunnableWithCallback<Triple<ArrayList<ICell>, Collection<Integer>, IPoint>, IPolyline> {
    public static double DEFAULT_DISTANCE_BETWEEN_PATHS = 0.0006;
    public static double DEFAULT_DIRECTION = Math.PI / 2;
    public WalkAllRunnable(
            Triple<ArrayList<ICell>, Collection<Integer>, IPoint> input, Handler handler,
                           RunnableCallback<IPolyline> callback)
    {
        super(input, handler, callback);
    }

    private IPolyline walk(IPolygon polygon, IPoint initialPoint) throws AngleOffLimitsException {
        Walker walker = new Walker(
                new WalkerConfig(DEFAULT_DISTANCE_BETWEEN_PATHS, DEFAULT_DIRECTION)
        );
        return walker.generatePath(
                polygon,
                initialPoint != null ? initialPoint : polygon.getPoints().get(0)
        );
    }
    public IPolyline walkAll(ArrayList<ICell> cells, Collection<Integer> cellsOrder, IPoint starterPoint) throws AngleOffLimitsException {
        IPolyline path = new Polyline();
        if (starterPoint != null) path.add(starterPoint);

        for (int index: cellsOrder) {
            ICell cell = cells.get(index);

            IPolyline polyline = this.walk(cell.getPolygon(), path.getNumberOfPoints() > 0 ? path.getLastPoint() : null);

            path.add(polyline.getPoints());
        }

        return path;
    }

    @Override
    public void run() {
        try {
            IPolyline polyline= this.walkAll(
                    this.getInput().getLeft(),
                    this.getInput().getMiddle(),
                    this.getInput().getRight()
            );

            this.getHandler().post(() -> this.getCallback().onComplete(polyline));
        } catch (AngleOffLimitsException e) {
            this.getHandler().post(() -> this.getCallback().onError(e));
        }
    }
}
