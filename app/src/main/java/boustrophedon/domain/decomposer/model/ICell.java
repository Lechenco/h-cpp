package boustrophedon.domain.decomposer.model;

import boustrophedon.domain.graph.model.INodeChildrenObject;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.ISubarea;

public interface ICell extends INodeChildrenObject {
    void visit();
    boolean getVisited();
    IPolygon getPolygon();
    ISubarea getSubarea();
}
