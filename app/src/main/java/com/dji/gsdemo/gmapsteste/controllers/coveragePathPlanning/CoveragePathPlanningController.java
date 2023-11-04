package com.dji.gsdemo.gmapsteste.controllers.coveragePathPlanning;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.CalcObjectiveMatrixRunnable;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.DecomposerRunnable;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.WalkerRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.graph.model.IObjectiveFunction;
import boustrophedon.domain.graph.model.IObjectiveMatrix;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.CenterOfMassFunction;
import boustrophedon.provider.graph.Node;

public class CoveragePathPlanningController {
    private AdjacencyMatrix<Node<ICell>> adjacencyMatrix;
    private IObjectiveMatrix<IPolygon> objectiveMatrix;
    private final Handler handler;

    public CoveragePathPlanningController(Handler handler) {
        this.handler = handler;
    }

    public void decompose(IPolygon polygon, RunnableCallback<ArrayList<ICell>> callback) {
        DecomposerRunnable decomposerRunnable = new DecomposerRunnable(polygon, handler, new RunnableCallback<AdjacencyMatrix<Node<ICell>>>() {
            @Override
            public void onComplete(AdjacencyMatrix<Node<ICell>> result) {
                adjacencyMatrix = result;
                ArrayList<ICell> cells = result.getNodes()
                        .stream()
                        .map(Node<ICell>::getObject)
                                .collect(Collectors.toCollection(ArrayList::new));
                callback.onComplete(cells);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });

        new Thread(decomposerRunnable).start();
    }

    public void walk(ICell cell, RunnableCallback<IPolyline> callback) {
        WalkerRunnable walkerRunnable = new WalkerRunnable(cell, this.handler, new RunnableCallback<IPolyline>() {
            @Override
            public void onComplete(IPolyline result) {
                callback.onComplete(result);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });

        new Thread(walkerRunnable).start();
    }
    public void walkAll(RunnableCallback<ArrayList<IPolyline>> callback) {
        Collection<ICell> cells = this.adjacencyMatrix
                .getNodes().stream().map(Node::getObject)
                .collect(Collectors.toCollection(ArrayList::new));
        walkAll(cells, callback);
    }
    public void walkAll(Collection<ICell> cells, RunnableCallback<ArrayList<IPolyline>> callback) {
        Runnable runnable = () -> {
            ArrayList<IPolyline> polylines = new ArrayList<>();
            for (ICell cell : cells) {
                WalkerRunnable walkerRunnable = new WalkerRunnable(cell, handler, new RunnableCallback<IPolyline>() {
                    @Override
                    public void onComplete(IPolyline result) {
                        polylines.add(result);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                });
                walkerRunnable.run();
            }
            callback.onComplete(polylines);
        };

        new Thread(runnable).start();
    }

    public void calcObjective(RunnableCallback<IObjectiveMatrix<IPolygon>> callback) {
        ArrayList<IPolygon> polygons = this.adjacencyMatrix
                .getNodes().stream().map(node -> node.getObject().getPolygon())
                .collect(Collectors.toCollection(ArrayList::new));

        calcObjective(polygons, callback);
    }

    public void calcObjective(ArrayList<IPolygon> polygons, RunnableCallback<IObjectiveMatrix<IPolygon>> callback) {
        IObjectiveFunction<IPolygon> function = new CenterOfMassFunction();
        CalcObjectiveMatrixRunnable matrixRunnable = new CalcObjectiveMatrixRunnable(polygons, function, handler, new RunnableCallback<IObjectiveMatrix<IPolygon>>() {
            @Override
            public void onComplete(IObjectiveMatrix<IPolygon> result) {
                objectiveMatrix = result;
                callback.onComplete(result);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });

        new Thread(matrixRunnable).start();
    }
}
