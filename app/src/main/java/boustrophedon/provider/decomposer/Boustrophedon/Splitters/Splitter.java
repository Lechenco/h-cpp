package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import java.util.ArrayList;
import java.util.function.Predicate;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.primitives.Border;

public abstract class Splitter implements ISplitter {
    protected ArrayList<ICell> cells;
    protected final ArrayList<ICriticalPoint> criticalPoints;
    protected ArrayList<ICriticalPoint> remainingPoints;
    public Splitter(ArrayList<ICriticalPoint> criticalPoints) {
        this.criticalPoints = criticalPoints;
    }
    @Override
    public void split(ICriticalPoint splitPoint, SubareaTypes subareaType) throws ExceedNumberOfAttempts {
        this.cells = new ArrayList<>();

        this.adjustEdges(splitPoint);
        ArrayList<ICriticalPoint> cellPoints = calcCellPoints(splitPoint);
        this.remainingPoints = this.calcRemainingPoints(splitPoint);

        if (this.remainingPoints.size() < 3) {
            cellPoints.addAll(this.remainingPoints);
            this.remainingPoints.clear();
        }

        this.populateCells(cellPoints, splitPoint);

        this.cells.forEach(cell -> cell.getSubarea().setSubareaType(subareaType));
    }

    abstract void populateCells(ArrayList<ICriticalPoint> cellPoints, ICriticalPoint splitPoint) throws ExceedNumberOfAttempts;

    protected ArrayList<ICriticalPoint> calcCellPoints(ICriticalPoint splitPoint) {
        IPoint vertices = splitPoint.getVertices();

        Predicate<ICriticalPoint> beforeX = criticalPoint -> criticalPoint.getVertices().getX() <= vertices.getX();
        return  CriticalPointerHelper.filter(this.criticalPoints, beforeX);
    }

    protected ArrayList<ICriticalPoint> calcRemainingPoints(ICriticalPoint splitPoint) {
        IPoint vertices = splitPoint.getVertices();
        Predicate<ICriticalPoint> afterX = criticalPoint -> criticalPoint.getVertices().getX() >= vertices.getX();
        return CriticalPointerHelper.filter(this.criticalPoints, afterX);
    }
    protected void adjustEdges(ICriticalPoint splitPoint) {
        this.criticalPoints.stream()
                .filter(rp -> splitPoint.getIntersectionsInNormal().contains(rp))
                .forEach(rp -> {
                    addSplitEdge(rp, splitPoint);
                    CriticalPointerHelper.populateIntersectionEdges(rp, this.criticalPoints);
                });
    }

    protected void addSplitEdge(ICriticalPoint rp, ICriticalPoint splitPoint) {
        IBorder newEdge = new Border(rp.getVertices(), splitPoint.getVertices());
        rp.getEdges().add(newEdge);
        splitPoint.getEdges().add(newEdge);
    }

    @Override
    public ArrayList<ICell> getCells() {
        return cells;
    }

    @Override
    public ArrayList<ICriticalPoint> getRemainingPoints() {
        return remainingPoints;
    }
}
