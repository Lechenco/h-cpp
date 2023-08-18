package boustrophedon.domain.decomposer.model;

import boustrophedon.domain.primitives.model.IPolygon;

public interface ICell {
    void visit();
    boolean getVisited();
    IPolygon getPolygon();
}
