package boustrophedon.controllers.decomposer.Boustrophedon.Splitters;

import java.util.ArrayList;

import boustrophedon.controllers.graph.MatrixController;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.MiddleSplitter;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.NoneSplitter;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.OutSplitter;

public class SplitterController {
    private final ArrayList<CriticalPoint> criticalPoints;
    private ArrayList<CriticalPoint> remainingPoints;

    private MatrixController matrixController;

    public SplitterController(ArrayList<CriticalPoint> criticalPoints) {
        this.criticalPoints = criticalPoints;
    }

    public IAdjacencyMatrix<INode<ICell>> execute() throws ExceedNumberOfAttempts {
        this.remainingPoints = this.criticalPoints;
        this.matrixController = new MatrixController();

        while (this.remainingPoints != null && !this.remainingPoints.isEmpty()) {
            ArrayList<CriticalPoint> sortedCP = CriticalPointerHelper.sort(this.criticalPoints);
            this.remainingPoints = this.splitNextEvent(sortedCP);
        }

        return this.matrixController.getMatrixAdjacency();
    }

    protected ArrayList<CriticalPoint> splitNextEvent(ArrayList<CriticalPoint> sortedCP) throws ExceedNumberOfAttempts {
        CriticalPoint nextCP = this.findNextEvent(sortedCP);

        ISplitter splitter = createSplitter(nextCP);

        splitter.split(nextCP);
        IPoint vertices = nextCP != null ? nextCP.getVertices() : null;
        this.matrixController.addCellsToMatrix(splitter.getCells(), vertices);
        return splitter.getRemainingPoints();
    }

    protected ISplitter createSplitter(CriticalPoint criticalPoint) {
        if (criticalPoint == null) {
            return new NoneSplitter(this.remainingPoints);
        }
        switch (criticalPoint.getEvent()) {
            case MIDDLE:
            case IN:
            case CLIP:
                return new MiddleSplitter(this.remainingPoints);
            case OUT:
                return new OutSplitter(this.remainingPoints);
            default:
                return new NoneSplitter(this.remainingPoints);
        }
    }

    protected CriticalPoint findNextEvent(ArrayList<CriticalPoint> sortedCP) {
        for (CriticalPoint cp : sortedCP) {
            if (cp.isSplit())
                continue;

            cp.setSplit(true);

            if (cp.getEvent() != Events.NONE && cp.getEvent() != Events.UNKNOWN)
                return cp;
        }
        return null;
    }
}
