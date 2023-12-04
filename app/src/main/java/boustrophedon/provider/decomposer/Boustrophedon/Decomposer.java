package boustrophedon.provider.decomposer.Boustrophedon;

import java.util.ArrayList;
import java.util.stream.Collectors;

import boustrophedon.controllers.decomposer.Boustrophedon.Splitters.SplitterController;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.DecomposerConfig;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IPolygonDecomposer;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.factories.decomposer.Boustrophedon.CriticalPoint.CriticalPointFactory;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;

public class Decomposer implements IPolygonDecomposer {
    private DecomposerConfig config;

    private AdjacencyMatrix<Node<ICell>> adjacencyMatrix;

    public Decomposer() {
        this.setConfig(new DecomposerConfig());
    }

    @Override
    public void setConfig(DecomposerConfig config) {
        this.config = config;
    }

    @Override
    public AdjacencyMatrix<Node<ICell>> decompose(IPolygon polygon) throws ExceedNumberOfAttempts {
        ArrayList<CriticalPoint> criticalPoints = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(criticalPoints);
        this.adjacencyMatrix = splitterController.execute();
        return this.adjacencyMatrix;
    }

    @Override
    public ArrayList<ICell> getCells() {
        return this.adjacencyMatrix.getNodes()
                .stream().map(Node::getObject)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
