package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;

public interface IPolygonDecomposer {
    void setConfig(DecomposerConfig config);
    ArrayList<ICell> decompose(IPolygon polygon);
    MatrixAdjacency<Node<ICell>> getMatrixAdjacency();
    ArrayList<ICell> getCells();
}
