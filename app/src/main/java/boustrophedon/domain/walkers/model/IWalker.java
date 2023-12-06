package boustrophedon.domain.walkers.model;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;

public interface IWalker {
    IPolyline walk(IPoint initialPoint);
    IPolyline walk(IPolygon polygon, IPoint initialPoint);
}
