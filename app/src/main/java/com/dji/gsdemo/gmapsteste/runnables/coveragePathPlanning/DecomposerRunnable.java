package com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.runnables.RunnableWithCallback;


import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.decomposer.Boustrophedon.Decomposer;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;

public class DecomposerRunnable extends RunnableWithCallback<IPolygon, AdjacencyMatrix<Node<ICell>>> {

    public DecomposerRunnable(IPolygon input, Handler handler, RunnableCallback<AdjacencyMatrix<Node<ICell>>> callback) {
        super(input, handler, callback);
    }

    private AdjacencyMatrix<Node<ICell>> decompose(IPolygon polygon) throws ExceedNumberOfAttempts {
        Decomposer decomposer = new Decomposer();
        return decomposer.decompose(polygon);
    }

    @Override
    public void run() {
        try {
            AdjacencyMatrix<Node<ICell>> adjacencyMatrix = decompose(this.getInput());

            this.getHandler().post(() -> this.getCallback().onComplete(adjacencyMatrix));
        } catch (ExceedNumberOfAttempts e) {
            this.getHandler().post(() -> this.getCallback().onError(e));
        }
    }
}
