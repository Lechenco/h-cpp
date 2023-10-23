package boustrophedon.controllers.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;

public class MatrixController {
    private final Map<IPoint, Integer> splitMap;

    private final AdjacencyMatrix<Node<ICell>> adjacencyMatrix;

    public MatrixController(Map<IPoint, Integer> splitMap, AdjacencyMatrix<Node<ICell>> adjacencyMatrix) {
        this.splitMap = splitMap;
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public MatrixController() {
        this.adjacencyMatrix = new AdjacencyMatrix<>();
        this.splitMap = new HashMap<>();
    }

    public AdjacencyMatrix<Node<ICell>> getMatrixAdjacency() {
        return adjacencyMatrix;
    }

    public void addCellsToMatrix(ICell cell, IPoint splitPoint) {
        int indexCell = this.adjacencyMatrix.addNode(new Node<>(cell));

        for (IPoint p : cell.getPolygon().getPoints()) {
            Integer indexSource = this.splitMap.get(p);
            if (indexSource != null) {
                this.adjacencyMatrix.addAdjacency(indexSource, indexCell);
            }
        }

        if (splitPoint != null)
            this.splitMap.put(splitPoint, indexCell);
    }

    public void addCellsToMatrix(ArrayList<ICell> cells, IPoint point) {
        for (ICell cell : cells) addCellsToMatrix(cell, point);
    }
}
