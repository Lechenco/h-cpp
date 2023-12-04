package boustrophedon.domain.decomposer.model;

import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.ISubarea;

public interface ICell {
    void visit();
    boolean getVisited();
    IPolygon getPolygon();
    ISubarea getSubarea();
}
