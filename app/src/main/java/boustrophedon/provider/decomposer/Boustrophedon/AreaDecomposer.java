package boustrophedon.provider.decomposer.Boustrophedon;

import java.util.ArrayList;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IClipper;
import boustrophedon.domain.decomposer.model.IDecomposer;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.ISubarea;
import boustrophedon.provider.decomposer.Boustrophedon.Clippers.NormalSubareaClipper;
import boustrophedon.provider.graph.AdjacencyMatrix;

public class AreaDecomposer implements IDecomposer<IArea> {

    private IAdjacencyMatrix<INode<ICell>> adjacencyMatrix;

    @Override
    public ArrayList<ICell> getCells() {
        return this.adjacencyMatrix.getNodes()
                .stream().map(INode::getObject)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public IAdjacencyMatrix<INode<ICell>> decompose(IArea area) throws ExceedNumberOfAttempts {
        this.adjacencyMatrix = new AdjacencyMatrix<>();

        IClipper clipper = new NormalSubareaClipper();
        clipper.clip(area.getSubareas());

        for (ISubarea subarea : clipper.getResult()) {
            IAdjacencyMatrix<INode<ICell>> matrix = this.decompose(subarea);
            this.concatToAdjacencyMatrix(matrix);
        }

        return this.adjacencyMatrix;
    }

    private IAdjacencyMatrix<INode<ICell>> decompose(ISubarea subarea) throws ExceedNumberOfAttempts {
        PolygonDecomposer polygonDecomposer = new PolygonDecomposer();
        return polygonDecomposer.decompose(subarea.getPolygon());
    }

    private void concatToAdjacencyMatrix(IAdjacencyMatrix<INode<ICell>> matrix) {
        for (INode<ICell> node : matrix.getNodes()) {
            int source = this.adjacencyMatrix.addNode(node);

            for (int destination = 0; destination < source; destination++) {
                ICell destinationCell = this.adjacencyMatrix.getNodes().get(destination).getObject();

                if (node.getObject().isAdjacent(destinationCell)) {
                    this.adjacencyMatrix.addAdjacency(source, destination);
                }
            }
        }
    }

}
