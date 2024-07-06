package boustrophedon.controllers.decomposer.Boustrophedon.Splitters;

import java.util.ArrayList;

import boustrophedon.controllers.graph.MatrixController;
import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.MiddleSplitter;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.NoneSplitter;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.OutSplitter;

public class SplitterController {
    private final ArrayList<ICriticalPoint> criticalPoints;
    private ArrayList<ICriticalPoint> remainingPoints;
    private MatrixController matrixController;
    private final SubareaTypes subareaType;
    private final boolean clipMode;

    public SplitterController(ArrayList<ICriticalPoint> criticalPoints, SubareaTypes subareaType) {
        this.criticalPoints = criticalPoints;
        this.subareaType = subareaType;
        clipMode = criticalPoints.stream().anyMatch(cp -> cp.getEvent() == Events.CLIP);
    }

    public IAdjacencyMatrix<INode<ICell>> execute() throws ExceedNumberOfAttempts {
        this.remainingPoints = this.criticalPoints;
        this.matrixController = new MatrixController();

        while (this.remainingPoints != null && !this.remainingPoints.isEmpty()) {
            ArrayList<ICriticalPoint> sortedCP = CriticalPointerHelper.sort(this.criticalPoints);
            this.remainingPoints = this.splitNextEvent(sortedCP);
        }

        return this.matrixController.getMatrixAdjacency();
    }

    protected ArrayList<ICriticalPoint> splitNextEvent(ArrayList<ICriticalPoint> sortedCP) throws ExceedNumberOfAttempts {
        ICriticalPoint nextCP = this.findNextEvent(sortedCP);

        ISplitter splitter = createSplitter(nextCP);

        splitter.split(nextCP, subareaType);
        IPoint vertices = nextCP != null ? nextCP.getVertices() : null;
        this.matrixController.addCellsToMatrix(splitter.getCells(), vertices);
        return splitter.getRemainingPoints();
    }

    protected ISplitter createSplitter(ICriticalPoint criticalPoint) {
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

    protected ICriticalPoint findNextEvent(ArrayList<ICriticalPoint> sortedCP) {
        for (ICriticalPoint cp : sortedCP) {
            if (cp.isSplit())
                continue;

            cp.setSplit(true);

            if (clipMode && cp.getEvent() != Events.CLIP)
                continue;
            if (cp.getEvent() != Events.NONE && cp.getEvent() != Events.UNKNOWN)
                return cp;
        }
        return null;
    }
}
