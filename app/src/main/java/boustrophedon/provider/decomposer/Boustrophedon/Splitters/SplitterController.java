package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.decomposer.Boustrophedon.Events;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;

public class SplitterController {
    private final ArrayList<CriticalPoint> criticalPoints;
    private ArrayList<CriticalPoint> remainingPoints;
    private Map<IPoint, Integer> splitMap;

    private MatrixAdjacency<Node<ICell>> matrixAdjacency;

    public SplitterController(ArrayList<CriticalPoint> criticalPoints) {
        this.criticalPoints = criticalPoints;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MatrixAdjacency<Node<ICell>> execute() throws ExceedNumberOfAttempts {
        this.remainingPoints = this.criticalPoints;
        this.splitMap = new HashMap<>();
        this.matrixAdjacency = new MatrixAdjacency<>();

        while (this.remainingPoints != null && !this.remainingPoints.isEmpty()) {
            ArrayList<CriticalPoint> sortedCP = CriticalPointerHelper.sort(this.criticalPoints);
            this.remainingPoints = this.splitNextEvent(sortedCP);
        }

        return this.matrixAdjacency;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<CriticalPoint> splitNextEvent(ArrayList<CriticalPoint> sortedCP) throws ExceedNumberOfAttempts {
        CriticalPoint nextCP = this.findNextEvent(sortedCP);

        ISplitter splitter = createSplitter(nextCP);

        splitter.split(nextCP);
        IPoint vertices = nextCP != null ? nextCP.getVertices() : null;
        this.addCellsToMatrix(splitter.getCells(), vertices);
        return splitter.getRemainingPoints();
    }

    private ISplitter createSplitter(CriticalPoint criticalPoint) {
        if (criticalPoint == null) {
            return new NoneSplitter(this.remainingPoints);
        }
        switch (criticalPoint.getEvent()) {
            case MIDDLE:
            case IN:
                return new MiddleSplitter(this.remainingPoints);
            case OUT:
                return new OutSplitter(this.remainingPoints);
            default:
                return new NoneSplitter(this.remainingPoints);
        }
    }

    private void addCellsToMatrix(ICell cell, IPoint splitPoint) {
        int indexCell = this.matrixAdjacency.addNode(new Node<>(cell));

        for (IPoint p : cell.getPolygon().getPoints()) {
            Integer indexSource = this.splitMap.get(p);
            if (indexSource != null) {
                this.matrixAdjacency.addAdjacency(indexSource, indexCell);
            }
        }

        if (splitPoint != null)
            this.splitMap.put(splitPoint, indexCell);
    }

    private void addCellsToMatrix(ArrayList<ICell> cells, IPoint point) {
        for (ICell cell : cells) addCellsToMatrix(cell, point);
    }

    private CriticalPoint findNextEvent(ArrayList<CriticalPoint> sortedCP) {
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
