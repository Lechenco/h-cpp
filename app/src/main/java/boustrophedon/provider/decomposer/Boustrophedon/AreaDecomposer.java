package boustrophedon.provider.decomposer.Boustrophedon;

import java.util.ArrayList;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.DecomposerConfig;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IDecomposer;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.ISubarea;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;

public class AreaDecomposer implements IDecomposer<IArea> {

    private AdjacencyMatrix<Node<ICell>> adjacencyMatrix;

    @Override
    public ArrayList<ICell> getCells() {
        return this.adjacencyMatrix.getNodes()
                .stream().map(Node::getObject)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void setConfig(DecomposerConfig config) {

    }

    @Override
    public AdjacencyMatrix<Node<ICell>> decompose(IArea area) throws ExceedNumberOfAttempts {
        this.adjacencyMatrix = new AdjacencyMatrix<>();

        PolygonDecomposer polygonDecomposer = new PolygonDecomposer();
        for (ISubarea subarea : area.getSubareas()) {
            IAdjacencyMatrix<Node<ICell>> matrix = polygonDecomposer.decompose(subarea.getPolygon());
            this.adjacencyMatrix.concat(matrix);
        }

        return this.adjacencyMatrix;
    }
}
