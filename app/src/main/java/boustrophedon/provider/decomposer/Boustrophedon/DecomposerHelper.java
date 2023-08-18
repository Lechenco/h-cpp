package boustrophedon.provider.decomposer.Boustrophedon;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;

public class DecomposerHelper {
    static protected void addCellToMatrix(ICell cell, MatrixAdjacency<Node<ICell>> matrixAdjacency) {
        Node<ICell> node = new Node<>(cell);
        int index = matrixAdjacency.addNode(node);
        node.setIndex(index);
    }
}
