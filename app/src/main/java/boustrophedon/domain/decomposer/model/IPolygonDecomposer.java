package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;

public interface IPolygonDecomposer {
    void setConfig(DecomposerConfig config);
    AdjacencyMatrix<Node<ICell>> decompose(IPolygon polygon) throws ExceedNumberOfAttempts;
    AdjacencyMatrix<Node<ICell>> getMatrixAdjacency();
    ArrayList<ICell> getCells();
}
