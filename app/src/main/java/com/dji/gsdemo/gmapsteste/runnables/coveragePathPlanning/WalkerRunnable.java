package com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning;

import static boustrophedon.constants.AngleConstants.NINETY_DEGREES;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.runnables.RunnableWithCallback;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.domain.walkers.error.AngleOffLimitsException;
import boustrophedon.provider.walkers.Walker;

public class WalkerRunnable extends RunnableWithCallback<ICell, IPolyline> {
    public static double DEFAULT_DISTANCE_BETWEEN_PATHS = 0.0006;
    public static double DEFAULT_DIRECTION = NINETY_DEGREES;
    private final IPoint startPoint;

    public WalkerRunnable(ICell input, Handler handler, RunnableCallback<IPolyline> callback) {
        super(input, handler, callback);
        this.startPoint = input.getPolygon().getPoints().get(0);
    }

    public IPolyline walk(IPolygon polygon) throws AngleOffLimitsException {
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(DEFAULT_DISTANCE_BETWEEN_PATHS).atDirection(DEFAULT_DIRECTION).build();
        return walker.walk(
            polygon,
            this.startPoint
        );
    }

    public IPolyline walk(ICell cell) throws AngleOffLimitsException {
        return this.walk(cell.getPolygon());
    }

    @Override
    public void run() {
        try {
            IPolyline polyline= this.walk(this.getInput());

            this.getHandler().post(() -> this.getCallback().onComplete(polyline));
        } catch (AngleOffLimitsException e) {
            this.getHandler().post(() -> this.getCallback().onError(e));
        }
    }
}
