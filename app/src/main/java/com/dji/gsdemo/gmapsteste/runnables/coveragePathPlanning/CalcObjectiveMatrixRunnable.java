package com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.runnables.RunnableWithCallback;

import java.util.ArrayList;

import boustrophedon.domain.graph.model.IObjectiveFunction;
import boustrophedon.domain.graph.model.IObjectiveMatrix;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.graph.ObjectiveMatrix;

public class CalcObjectiveMatrixRunnable extends RunnableWithCallback<ArrayList<IPolygon>, IObjectiveMatrix<IPolygon>> {
    private final IObjectiveFunction<IPolygon> objectiveFunction;
    public CalcObjectiveMatrixRunnable(
            ArrayList<IPolygon> input,
            IObjectiveFunction<IPolygon> objectiveFunction,
            Handler handler,
            RunnableCallback<IObjectiveMatrix<IPolygon>> callback
    ) {
        super(input, handler, callback);
        this.objectiveFunction = objectiveFunction;
    }

    private IObjectiveMatrix<IPolygon> calcObjective(ArrayList<IPolygon> polygons) {
        IObjectiveMatrix<IPolygon> matrix = new ObjectiveMatrix<>(polygons, this.objectiveFunction);
        matrix.calcObjective();

        return matrix;
    }

    @Override
    public void run() {
        try {
            IObjectiveMatrix<IPolygon> matrix = calcObjective(this.getInput());

            this.getHandler().post(() -> this.getCallback().onComplete(matrix));
        } catch (Exception e) {
            this.getHandler().post(() -> this.getCallback().onError(e));
        }
    }
}
