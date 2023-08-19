package boustrophedon.provider.decomposer.Boustrophedon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.DecomposerConfig;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IPolygonDecomposer;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;

public class Decomposer implements IPolygonDecomposer {
    private DecomposerConfig config;
    private ArrayList<CriticalPoint> criticalPoints;

    private MatrixAdjacency<Node<ICell>> matrixAdjacency;

    Queue<Integer> inOutQueue;
    Stack<Integer> sourceStack;
    Stack<Integer> destinationStack;

    public Decomposer() {
        this.setConfig(new DecomposerConfig());
    }

    @Override
    public void setConfig(DecomposerConfig config) {
        this.config = config;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<ICell> decompose(IPolygon polygon) {
        this.criticalPoints = this.getCriticalPoints(polygon);
        this.matrixAdjacency = new MatrixAdjacency<>();

        this.inOutQueue = new PriorityQueue<>();
        this.sourceStack = new Stack<>();
        this.destinationStack = new Stack<>();
        for (CriticalPoint criticalPoint : criticalPoints) {
            criticalPoint.detectPointEvent(polygon);
        }

        addIntersections();
        ArrayList<CriticalPoint> sortedCP = CriticalPointerHelper.sort(this.criticalPoints);
// TODO Integrate With SplitterController

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<CriticalPoint> getAllIntersections() {
        return (ArrayList<CriticalPoint>) this.criticalPoints
                .stream()
                .map(CriticalPoint::getIntersectionsInNormal)
                .flatMap(ArrayList::stream)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addIntersections() {
        ArrayList<CriticalPoint> intersections = getAllIntersections();

        intersections.forEach(element -> CriticalPointerHelper.addIntersections(element, this.criticalPoints));
    }
    @Override
    public MatrixAdjacency<Node<ICell>> getMatrixAdjacency() {
        return this.matrixAdjacency;
    }

    @Override
    public ArrayList<ICell> getCells() {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected ArrayList<CriticalPoint> getCriticalPoints(IPolygon polygon) {
        ArrayList<CriticalPoint> criticalPoints = new ArrayList<>();

        for (IPoint point : polygon.getPoints()) {
            List<IBorder> pointBorders = polygon.getBorders()
                    .stream().filter((b) -> b.isOnBorder(point)).collect(Collectors.toList());
            criticalPoints.add(new CriticalPoint(point, new ArrayList<>(pointBorders)));
        }

        return criticalPoints;
    }
}
