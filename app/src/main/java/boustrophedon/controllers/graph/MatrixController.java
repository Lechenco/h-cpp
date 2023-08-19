package boustrophedon.controllers.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;

public class MatrixController {
    private final Map<IPoint, Integer> splitMap;

    private final MatrixAdjacency<Node<ICell>> matrixAdjacency;

    public MatrixController(Map<IPoint, Integer> splitMap, MatrixAdjacency<Node<ICell>> matrixAdjacency) {
        this.splitMap = splitMap;
        this.matrixAdjacency = matrixAdjacency;
    }

    public MatrixController() {
        this.matrixAdjacency = new MatrixAdjacency<>();
        this.splitMap = new HashMap<>();
    }

    public MatrixAdjacency<Node<ICell>> getMatrixAdjacency() {
        return matrixAdjacency;
    }

    public void addCellsToMatrix(ICell cell, IPoint splitPoint) {
        int indexCell = this.matrixAdjacency.addNode(new Node<>(cell));

        for (IPoint p : cell.getPolygon().getPoints()) {
            Integer indexSource = this.splitMap.get(p);
            if (indexSource != null) {
                this.matrixAdjacency.addAdjacency(indexSource, indexCell);
            }
        }

        if (splitPoint != null)
            this.splitMap.put(splitPoint, indexCell);
    }

    public void addCellsToMatrix(ArrayList<ICell> cells, IPoint point) {
        for (ICell cell : cells) addCellsToMatrix(cell, point);
    }
}
