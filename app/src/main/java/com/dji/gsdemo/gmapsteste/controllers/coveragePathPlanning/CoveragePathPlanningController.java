package com.dji.gsdemo.gmapsteste.controllers.coveragePathPlanning;

import android.os.Handler;
import android.util.Log;

import com.dji.gsdemo.gmapsteste.R;
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

    public RunnableCallback<ArrayList<ICell>> onDecomposeCallback;
    public DecomposerRunnable decompose(IArea area) {
        return new DecomposerRunnable(area, handler, new RunnableCallback<IAdjacencyMatrix<INode<ICell>>>() {
            @Override
            public void onComplete(IAdjacencyMatrix<INode<ICell>> result) {
                adjacencyMatrix = result;
                ArrayList<ICell> cells = result.getNodes()
                        .stream()
                        .map(INode::getObject)
                                .collect(Collectors.toCollection(ArrayList::new));

                if (onDecomposeCallback != null)
                    onDecomposeCallback.onComplete(cells);
            }

            @Override
            public void onError(Exception e) {
                if (onDecomposeCallback != null)
                    onDecomposeCallback.onError(e);
            }
        });
    }

    public RunnableCallback<IPolyline> onWalkCallback;
    public WalkerRunnable walk(ICell cell) {
        return new WalkerRunnable(cell, this.handler, new RunnableCallback<IPolyline>() {
            @Override
            public void onComplete(IPolyline result) {

                if (onWalkCallback != null) onWalkCallback.onComplete(result);
            }

            @Override
            public void onError(Exception e) {
                if (onWalkCallback != null) onWalkCallback.onError(e);
            }
        });
    }

    RunnableCallback<IObjectiveMatrix<IPolygon>> onCalcObjectiveCallback;
    public CalcObjectiveMatrixRunnable calcObjective() {
        ArrayList<IPolygon> polygons = this.adjacencyMatrix
                .getNodes().stream().map(node -> node.getObject().getPolygon())
                .collect(Collectors.toCollection(ArrayList::new));

        return calcObjective(polygons);
    }

    public CalcObjectiveMatrixRunnable calcObjective(ArrayList<IPolygon> polygons) {
        IObjectiveFunction<IPolygon> function = new CenterOfMassFunction();
        return new CalcObjectiveMatrixRunnable(polygons, function, handler, new RunnableCallback<IObjectiveMatrix<IPolygon>>() {
            @Override
            public void onComplete(IObjectiveMatrix<IPolygon> result) {
                objectiveMatrix = result;
                if (onCalcObjectiveCallback != null)
                    onCalcObjectiveCallback.onComplete(result);
            }

            @Override
            public void onError(Exception e) {
                if (onCalcObjectiveCallback != null) onCalcObjectiveCallback.onError(e);
            }
        });
    }

    RunnableCallback<Collection<Integer>> onCalcBestCellOrderCallback;
    public CalcBestCellOrderRunnable calcBestCellOrder() {
        ArrayList<ICell> cells = this.adjacencyMatrix.getNodes().stream()
                .map(INode::getObject).collect(Collectors.toCollection(ArrayList::new));
        ICell starterCell = CellHelper.getClosestCellToPoint(cells, this.startPoint);
        return calcBestCellOrder(cells.indexOf(starterCell) );
    }
    public CalcBestCellOrderRunnable calcBestCellOrder(int starterCell) {
        return  new CalcBestCellOrderRunnable(this.objectiveMatrix, starterCell, handler, new RunnableCallback<Collection<Integer>>() {
            @Override
            public void onComplete(Collection<Integer> result) {
                cellsOrder = result;
                if (onCalcBestCellOrderCallback != null)
                    onCalcBestCellOrderCallback.onComplete(result);
            }

            @Override
            public void onError(Exception e) {
                if (onCalcBestCellOrderCallback != null)
                    onCalcBestCellOrderCallback.onError(e);
            }
        });
    }

    RunnableCallback<IPolyline> onGenerateFinalPathCallback;
    public WalkAllRunnable generateFinalPath() {
        ArrayList<ICell> cells = this.adjacencyMatrix.getNodes().stream()
                .map(INode::getObject).collect(Collectors.toCollection(ArrayList::new));
        return new WalkAllRunnable(
                new ImmutableTriple<>(cells, this.cellsOrder, this.startPoint),
                this.handler,
                new RunnableCallback<IPolyline>() {
                    @Override
                    public void onComplete(IPolyline result) {
                        finalPath = result;
                        if (onGenerateFinalPathCallback != null)
                            onGenerateFinalPathCallback.onComplete(result);
                    }

                    @Override
                    public void onError(Exception e) {
                        if (onGenerateFinalPathCallback != null)
                            onGenerateFinalPathCallback.onError(e);
                    }
                }
        );
    }

    public IPolyline generateFinalPathSync(IArea area) throws InterruptedException {
        DecomposerRunnable decomposerRunnable = this.decompose(area);
        decomposerRunnable.run();
        while (true) {
            if (decomposerRunnable.isCompleted()) break;
        }

        CalcObjectiveMatrixRunnable calcObjectiveMatrixRunnable = this.calcObjective();
        calcObjectiveMatrixRunnable.run();
        while (true) {
            if (calcObjectiveMatrixRunnable.isCompleted()) break;
        }

        CalcBestCellOrderRunnable calcBestCellOrderRunnable = this.calcBestCellOrder();
        calcBestCellOrderRunnable.run();
        while (true) {
            if (calcBestCellOrderRunnable.isCompleted()) break;
        }

        WalkAllRunnable walkAllRunnable = this.generateFinalPath();
        walkAllRunnable.run();
        while (true) {
            if (walkAllRunnable.isCompleted()) break;
        }

        return  this.finalPath;
    }

    public void setStartPoint(IPoint startedPoint) {
        this.startPoint = startedPoint;
    }
}
