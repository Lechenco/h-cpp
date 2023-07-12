package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;
import java.util.Iterator;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.walkers.model.WalkerConfig;
import boustrophedon.model.ICell;

public interface IPolygonDecomposer {
    void setConfig(DecomposerConfig config);
    void decompose(IPolygon polygon);
}
