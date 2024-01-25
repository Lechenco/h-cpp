package boustrophedon.provider.decomposer.Boustrophedon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.decomposer.model.IDecomposer;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.ISubarea;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
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

        for (ISubarea subarea : area.getSubareas()) {
            IAdjacencyMatrix<INode<ICell>> matrix = this.decompose(subarea);
            this.adjacencyMatrix.concat(matrix);
        }

        return this.adjacencyMatrix;
    }

    private IAdjacencyMatrix<INode<ICell>> decompose(ISubarea subarea) throws ExceedNumberOfAttempts {
        PolygonDecomposer polygonDecomposer = new PolygonDecomposer();
        return polygonDecomposer.decompose(subarea.getPolygon());
    }


}
