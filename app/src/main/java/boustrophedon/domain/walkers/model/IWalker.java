package boustrophedon.domain.walkers.model;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;

public interface IWalker {
    void setConfig(WalkerConfig config);
    IPolyline generatePath(IPolygon polygon);
    IPolyline generatePath(IPolygon polygon, IPoint initialPoint);
}
