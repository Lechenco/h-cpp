package boustrophedon.domain.backAndForth.model;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;

public interface IWalker {
    IPoint walkToTheOtherSide(IPoint currentPoint);
    IPoint walkToTheOtherSide(IPolygon polygon, IPoint currentPoint);
    IPoint walkToFront(IPoint currentPoint);
    IPoint walkToFront(IPolygon polygon, IPoint currentPoint);
    void setConfig(WalkerConfig config);
    IPolyline generatePath(IPolygon polygon);
    IPolyline generatePath(IPolygon polygon, IPoint initialPoint);
}
