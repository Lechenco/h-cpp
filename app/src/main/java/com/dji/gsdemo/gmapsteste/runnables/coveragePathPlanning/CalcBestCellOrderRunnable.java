package com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.runnables.RunnableWithCallback;

import java.util.ArrayList;
import java.util.Collection;

import boustrophedon.domain.graph.error.ElementNotFoundedException;
import boustrophedon.domain.graph.model.IObjectiveMatrix;
import boustrophedon.domain.primitives.model.IPolygon;



public class CalcBestCellOrderRunnable extends RunnableWithCallback<IObjectiveMatrix<IPolygon>, Collection<Integer>> {
    int startIndex;
    public CalcBestCellOrderRunnable(IObjectiveMatrix<IPolygon> input, int startIndex, Handler handler, RunnableCallback<Collection<Integer>> callback) {
        super(input, handler, callback);
        this.startIndex = startIndex;
    }

    @Override
    public void run() {
        Collection<Integer> indexes = new ArrayList<>();
        indexes.add(startIndex);

        int currentIndex = startIndex;
        while(indexes.size() < this.getInput().getNodes().size()) {
            try {
                currentIndex = this.getInput().getBestObjectiveIndexExcept(currentIndex, indexes);
                indexes.add(currentIndex);
            } catch (ElementNotFoundedException e) {
                this.getHandler().post(() -> this.getCallback().onError(e));
            }
        }
        this.getHandler().post(() -> this.getCallback().onComplete(indexes));
    }
}
