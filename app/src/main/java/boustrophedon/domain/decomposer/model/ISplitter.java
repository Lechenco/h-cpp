package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.graph.IMatrixAdjacency;
import boustrophedon.domain.graph.INode;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public interface ISplitter {
    void split(ICriticalPoint splitPoint);
    IMatrixAdjacency<INode<ICell>> getMatrixAdjacency();
    ArrayList<ICell> getCells();

    ArrayList<CriticalPoint> getRemainingPoints();
}
