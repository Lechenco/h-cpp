package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IPolygon;

public interface IPolygonDecomposer extends IDecomposer<IPolygon> {
    ArrayList<ICell> getCells();
}
