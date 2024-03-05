package com.dji.gsdemo.gmapsteste.controllers.coveragePathPlanning;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.CalcBestCellOrderRunnable;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.CalcObjectiveMatrixRunnable;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.DecomposerRunnable;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.WalkAllRunnable;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.WalkerRunnable;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.graph.model.IObjectiveFunction;
import boustrophedon.domain.graph.model.IObjectiveMatrix;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.graph.CenterOfMassFunction;


public class CoveragePathPlanningController {
    private IAdjacencyMatrix<INode<ICell>> adjacencyMatrix;
    private IObjectiveMatrix<IPolygon> objectiveMatrix;

    private Collection<Integer> cellsOrder;
    private IPolyline finalPath;
    private final Handler handler;
    private IPoint startPoint;

    public CoveragePathPlanningController(Handler handler) {
        this.handler = handler;
    }

    public Thread decompose(IArea area, RunnableCallback<ArrayList<ICell>> callback) {
        DecomposerRunnable decomposerRunnable = new DecomposerRunnable(area, handler, new RunnableCallback<IAdjacencyMatrix<INode<ICell>>>() {
            @Override
            public void onComplete(IAdjacencyMatrix<INode<ICell>> result) {
                adjacencyMatrix = result;
                ArrayList<ICell> cells = result.getNodes()
                        .stream()
                        .map(INode::getObject)
                                .collect(Collectors.toCollection(ArrayList::new));
                callback.onComplete(cells);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });

        Thread thread = new Thread(decomposerRunnable);
        thread.start();
        return thread;
    }

    public Thread walk(ICell cell, RunnableCallback<IPolyline> callback) {
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

        Thread thread = new Thread(walkerRunnable);
        thread.start();
        return thread;
    }

    public Thread calcObjective(RunnableCallback<IObjectiveMatrix<IPolygon>> callback) {
        ArrayList<IPolygon> polygons = this.adjacencyMatrix
                .getNodes().stream().map(node -> node.getObject().getPolygon())
                .collect(Collectors.toCollection(ArrayList::new));

        return calcObjective(polygons, callback);
    }

    public Thread calcObjective(ArrayList<IPolygon> polygons, RunnableCallback<IObjectiveMatrix<IPolygon>> callback) {
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

        Thread thread = new Thread(matrixRunnable);
        thread.start();
        return thread;
    }

    public Thread calcBestCellOrder(RunnableCallback<Collection<Integer>> callback) {
        ArrayList<ICell> cells = this.adjacencyMatrix.getNodes().stream()
                .map(INode::getObject).collect(Collectors.toCollection(ArrayList::new));
        ICell starterCell = CellHelper.getClosestCellToPoint(cells, this.startPoint);
        return calcBestCellOrder(cells.indexOf(starterCell), callback);
    }
    public Thread calcBestCellOrder(int starterCell, RunnableCallback<Collection<Integer>> callback) {
        CalcBestCellOrderRunnable calcBestCellOrderRunnable = new CalcBestCellOrderRunnable(this.objectiveMatrix, starterCell, handler, new RunnableCallback<Collection<Integer>>() {
            @Override
            public void onComplete(Collection<Integer> result) {
                cellsOrder = result;
                callback.onComplete(result);
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }
        });

        Thread thread = new Thread(calcBestCellOrderRunnable);
        thread.start();
        return thread;
    }

    public Thread generateFinalPath(RunnableCallback<IPolyline> callback) {
        ArrayList<ICell> cells = this.adjacencyMatrix.getNodes().stream()
                .map(INode::getObject).collect(Collectors.toCollection(ArrayList::new));
        WalkAllRunnable walkAllRunnable = new WalkAllRunnable(
                new ImmutableTriple<>(cells, this.cellsOrder, this.startPoint),
                this.handler,
                new RunnableCallback<IPolyline>() {
                    @Override
                    public void onComplete(IPolyline result) {
                        finalPath = result;
                        callback.onComplete(result);
                    }

                    @Override
                    public void onError(Exception e) {
                        callback.onError(e);
                    }
                }
        );

        Thread thread = new Thread(walkAllRunnable);
        thread.start();
        return thread;
    }

    public IPolyline generateFinalPathSync(IArea area) throws InterruptedException {
        this.decompose(area, new RunnableCallback<ArrayList<ICell>>() {
            @Override
            public void onComplete(ArrayList<ICell> result) {

            }

            @Override
            public void onError(Exception e) {

            }
        }).join(0);
        this.calcObjective(new RunnableCallback<IObjectiveMatrix<IPolygon>>() {
            @Override
            public void onComplete(IObjectiveMatrix<IPolygon> result) {

            }

            @Override
            public void onError(Exception e) {

            }
        }).join(0);
        this.calcBestCellOrder(new RunnableCallback<Collection<Integer>>() {
            @Override
            public void onComplete(Collection<Integer> result) {

            }

            @Override
            public void onError(Exception e) {

            }
        }).join(0);
        this.generateFinalPath(new RunnableCallback<IPolyline>() {
            @Override
            public void onComplete(IPolyline result) {

            }

            @Override
            public void onError(Exception e) {

            }
        }).join(0);

        return  this.finalPath;
    }

    public void setStartPoint(IPoint startedPoint) {
        this.startPoint = startedPoint;
    }
}
