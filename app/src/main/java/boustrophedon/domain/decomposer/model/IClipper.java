package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IArea;

public interface IClipper {
    void clip(IArea area);
    ArrayList<ICell> getCells();
}
