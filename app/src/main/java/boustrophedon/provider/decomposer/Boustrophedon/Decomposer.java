package boustrophedon.provider.decomposer.Boustrophedon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boustrophedon.controllers.decomposer.Boustrophedon.Splitters.SplitterController;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
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

    public Decomposer() {
        this.setConfig(new DecomposerConfig());
    }

    @Override
    public void setConfig(DecomposerConfig config) {
        this.config = config;
    }

    @Override
    public ArrayList<ICell> decompose(IPolygon polygon) throws ExceedNumberOfAttempts {
        this.criticalPoints = this.getCriticalPoints(polygon);
        for (CriticalPoint criticalPoint : criticalPoints) {
            criticalPoint.detectPointEvent(polygon);
        }

        addIntersections();

        SplitterController splitterController = new SplitterController(this.criticalPoints);
        this.matrixAdjacency = splitterController.execute();
        return null;
    }

    private ArrayList<CriticalPoint> getAllIntersections() {
        return (ArrayList<CriticalPoint>) this.criticalPoints
                .stream()
                .map(CriticalPoint::getIntersectionsInNormal)
                .flatMap(ArrayList::stream)
                .collect(Collectors.toList());
    }

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
