package boustrophedon.provider.decomposer.Boustrophedon;

import java.util.ArrayList;
import java.util.stream.Collectors;

import boustrophedon.controllers.decomposer.Boustrophedon.Splitters.SplitterController;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.decomposer.model.IDecomposer;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.factories.decomposer.Boustrophedon.CriticalPoint.CriticalPointFactory;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class PolygonDecomposer implements IDecomposer<IPolygon> {

    private IAdjacencyMatrix<INode<ICell>> adjacencyMatrix;

    public PolygonDecomposer() {

    }

    @Override
    public IAdjacencyMatrix<INode<ICell>> decompose(IPolygon polygon) throws ExceedNumberOfAttempts {
        ArrayList<ICriticalPoint> criticalPoints = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(criticalPoints);
        this.adjacencyMatrix = splitterController.execute();
        return this.adjacencyMatrix;
    }

    @Override
    public ArrayList<ICell> getCells() {
        return this.adjacencyMatrix.getNodes()
                .stream().map(INode::getObject)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
