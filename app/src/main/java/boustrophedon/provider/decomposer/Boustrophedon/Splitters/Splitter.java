package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.function.Predicate;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.domain.graph.IMatrixAdjacency;
import boustrophedon.domain.graph.INode;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.primitives.Border;

public abstract class Splitter implements ISplitter {
    protected IMatrixAdjacency<INode<ICell>> matrixAdjacency;
    protected ArrayList<ICell> cells;
    protected final ArrayList<CriticalPoint> criticalPoints;
    protected ArrayList<CriticalPoint> remainingPoints;
    public Splitter(ArrayList<CriticalPoint> criticalPoints) {
        this.criticalPoints = criticalPoints;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void split(ICriticalPoint splitPoint) {
        this.cells = new ArrayList<>();
        this.matrixAdjacency = new MatrixAdjacency<>();

        ArrayList<CriticalPoint> cellPoints = calcCellPoints((CriticalPoint) splitPoint);
        this.remainingPoints = this.calcRemainingPoints((CriticalPoint) splitPoint);
        this.adjustEdges((CriticalPoint) splitPoint);

        this.populateCells(cellPoints, (CriticalPoint) splitPoint);
    }

    abstract void populateCells(ArrayList<CriticalPoint> cellPoints, CriticalPoint splitPoint);

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected ArrayList<CriticalPoint> calcCellPoints(CriticalPoint splitPoint) {
        IPoint vertices = splitPoint.getVertices();

        Predicate<CriticalPoint> beforeX = criticalPoint -> criticalPoint.getVertices().getX() <= vertices.getX();
        return  CriticalPointerHelper.filter(this.criticalPoints, beforeX);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected ArrayList<CriticalPoint> calcRemainingPoints(CriticalPoint splitPoint) {
        IPoint vertices = splitPoint.getVertices();
        Predicate<CriticalPoint> afterX = criticalPoint -> criticalPoint.getVertices().getX() >= vertices.getX();
        return CriticalPointerHelper.filter(this.criticalPoints, afterX);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void adjustEdges(CriticalPoint splitPoint) {
        splitPoint.getEdges().clear();
        this.remainingPoints.stream()
                .filter(rp -> splitPoint.getIntersectionsInNormal().contains(rp))
                .forEach(rp -> addSplitEdge(rp, splitPoint));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void addSplitEdge(CriticalPoint rp, CriticalPoint splitPoint) {
        IBorder newEdge = new Border(rp.getVertices(), splitPoint.getVertices());
        rp.getEdges().add(newEdge);
        splitPoint.getEdges().add(newEdge);
    }
    @Override
    public IMatrixAdjacency<INode<ICell>> getMatrixAdjacency() {
        return matrixAdjacency;
    }

    @Override
    public ArrayList<ICell> getCells() {
        return cells;
    }

    @Override
    public ArrayList<CriticalPoint> getRemainingPoints() {
        return remainingPoints;
    }
}
