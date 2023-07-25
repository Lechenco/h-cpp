package boustrophedon.provider.decomposer.Boustrophedon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.DecomposerConfig;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IPolygonDecomposer;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;
import boustrophedon.provider.primitives.Polygon;

public class Decomposer implements IPolygonDecomposer {
    private DecomposerConfig config;
    private ArrayList<CriticalPoint> criticalPoints;

    private MatrixAdjacency<Node<ICell>> matrixAdjacency;

    Queue<Integer> inOutQueue;
    Stack<Integer> sourceStack;
    Stack<Integer> destinationStack;

    Map<Events, Integer> eventsCount = new HashMap<Events, Integer>() {{
        put(Events.IN, 0);
        put(Events.MIDDLE, 0);
        put(Events.OUT, 0);
        put(Events.NONE, 0);
        put(Events.UNKNOWN, 0);
    }};

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
            addEventToCount(criticalPoint.getEvent());
        }

        addIntersections();
        ArrayList<CriticalPoint> sortedCP = criticalPoints
                .stream()
                .sorted(Comparator.comparingDouble(criticalPoint -> criticalPoint.getVertices().getX()))
                .collect(Collectors.toCollection(ArrayList::new));

        this.splitCells(sortedCP);

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<CriticalPoint> getAllIntersections() {
        return (ArrayList<CriticalPoint>) this.criticalPoints
                .stream()
                .map(CriticalPoint::getCpFromIntersections)
                .flatMap(ArrayList::stream)
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addIntersections() {
        ArrayList<CriticalPoint> intersections = getAllIntersections();

        intersections.forEach(element -> CriticalPointerHelper.addIntersections(element, this.criticalPoints));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitCells(ArrayList<CriticalPoint> criticalPoints) {
        if (criticalPoints.size() == 0) return;

        for (int i = 0; i < criticalPoints.size(); i++) {
            CriticalPoint cp = criticalPoints.get(i);

            if (cp.isSplit())
                continue;

            cp.setSplit(true);

            switch (cp.getEvent()) {
                case MIDDLE:
                    ArrayList<CriticalPoint> remainingCPMiddle = this.splitCellMiddleEvent(criticalPoints, i);
                    this.splitCells(remainingCPMiddle);
                    return;
                case IN:

                    ArrayList<CriticalPoint> remainingCPIn = this.splitCellInEvent(criticalPoints, i);

                    int splitIndex = remainingCPIn.indexOf(cp);
                    this.splitCells(new ArrayList<>(remainingCPIn.subList(0, splitIndex +1)));
                    this.splitCells(new ArrayList<>(remainingCPIn.subList(splitIndex, remainingCPIn.size())));
                    return;
                case OUT:

                    // from new border top: run over edges until find x >=  currentX
                    // recursive top initialX_1 -> currentX
                    // from new border bottom: run over edges until find find x >=  currentX
                    // recursive bottom initialX_2 -> currentX
                    break;
            }
        }

        ICell cell = createCell(criticalPoints);
        this.addCellToMatrix(cell);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<CriticalPoint> splitCellInEvent(ArrayList<CriticalPoint> sortedPoints, int cpIndex) {
        IPoint splitPoint = sortedPoints.get(cpIndex).getVertices();

        Predicate<CriticalPoint> beforeY = criticalPoint -> criticalPoint.getVertices().getY() <= splitPoint.getY();
        Predicate<CriticalPoint> afterY = criticalPoint -> criticalPoint.getVertices().getY() >= splitPoint.getY();

        ArrayList<CriticalPoint> middleCellCP = CriticalPointerHelper.filter(sortedPoints, beforeY);
        ArrayList<CriticalPoint> criticalPoints = CriticalPointerHelper.unsorted(middleCellCP, this.criticalPoints);

        ICell cell = createCell(criticalPoints);
        this.addCellToMatrix(cell);

        ArrayList<CriticalPoint> remainingCP = CriticalPointerHelper.unsorted(
                CriticalPointerHelper.filter(sortedPoints, afterY),
                this.criticalPoints
        );

        return remainingCP;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<CriticalPoint> splitCellMiddleEvent(ArrayList<CriticalPoint> sortedPoints, int cpIndex) {
        IPoint splitPoint = sortedPoints.get(cpIndex).getVertices();

        Predicate<CriticalPoint> beforeY = criticalPoint -> criticalPoint.getVertices().getY() <= splitPoint.getY();
        Predicate<CriticalPoint> afterY = criticalPoint -> criticalPoint.getVertices().getY() >= splitPoint.getY();

        ArrayList<CriticalPoint> middleCellCP = CriticalPointerHelper.filter(sortedPoints, beforeY);

        ArrayList<CriticalPoint> criticalPoints = CriticalPointerHelper.unsorted(middleCellCP, this.criticalPoints);

        ICell cell = createCell(criticalPoints);
        this.addCellToMatrix(cell);

        ArrayList<CriticalPoint> remainingCP = CriticalPointerHelper.filter(sortedPoints, afterY);
        return remainingCP;
    }

    private ICell createCell(ArrayList<CriticalPoint> sortedPoints) {
        ArrayList<IPoint> polygonPoints = new ArrayList<>();

        for (CriticalPoint cp : this.criticalPoints) {
            if (sortedPoints.contains(cp)) {
                polygonPoints.add(cp.getVertices());
            }
        }

        return new Cell(new Polygon(polygonPoints));
    }

    private void swapStacks() {
        this.sourceStack = this.destinationStack;
        this.destinationStack = new Stack<>();
    }

    private void addCellToMatrix(ICell cell) {
        Node<ICell> node = new Node<>(cell);
        int index = this.matrixAdjacency.addNode(node);
        node.setIndex(index);
    }

    private void addEventToCount(Events event) {
        try {
            Integer lastCount = eventsCount.get(event);
            eventsCount.put(event, lastCount + 1);
        } catch (NullPointerException exception) {
        }
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

    public Stack<Integer> copyStack(Stack<Integer> source) {
        Stack<Integer> destination =  new Stack<>();
        Stack<Integer> temp =  new Stack<>();

        while(!source.empty()) { temp.add(source.pop()); }

        while(!temp.empty()) {
            Integer value = temp.pop();
            source.add(value);
            destination.add(value);
        }

        return destination;
    }
}
