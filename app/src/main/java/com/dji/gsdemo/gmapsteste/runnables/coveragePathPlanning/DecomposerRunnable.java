package com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.runnables.RunnableWithCallback;


import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IDecomposer;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.provider.decomposer.Boustrophedon.AreaDecomposer;

public class DecomposerRunnable extends RunnableWithCallback<IArea, IAdjacencyMatrix<INode<ICell>>> {

    public DecomposerRunnable(IArea input, Handler handler, RunnableCallback<IAdjacencyMatrix<INode<ICell>>> callback) {
        super(input, handler, callback);
    }

    private IAdjacencyMatrix<INode<ICell>> decompose(IArea area) throws ExceedNumberOfAttempts {
        IDecomposer<IArea> decomposer = new AreaDecomposer();
        return decomposer.decompose(area);
    }

    @Override
    public void run() {
        try {
            Log.i("Boustrophedon", "Starting Decomposer " + this.getInput());
            IAdjacencyMatrix<INode<ICell>> adjacencyMatrix = decompose(this.getInput());

            this.getHandler().post(() -> {
                this.getCallback().onComplete(adjacencyMatrix);
                this.complete();
            });
            this.getHandler().sendMessage(new Message());
            Log.i("Boustrophedon", "Completed Decomposer " + adjacencyMatrix.getNodes());
        } catch (ExceedNumberOfAttempts e) {
            this.getHandler().post(() -> this.getCallback().onError(e));
        }
    }
}
