package boustrophedon.domain.backAndForth.useCases;

import boustrophedon.domain.backAndForth.model.WalkerConfig;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;

public interface IBackAndForthUseCase {
    void setup(WalkerConfig config);
    IPolyline run(IPolygon polygon);
    IPolyline run(IPolygon polygon, IPoint initialPoint);
}
