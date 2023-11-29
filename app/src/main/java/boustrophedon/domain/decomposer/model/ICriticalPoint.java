package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public interface ICriticalPoint {
    boolean isSplit();
    void setSplit(boolean value);
    IPoint getVertices();

    ArrayList<IBorder> getEdges();
    ArrayList<IPoint> getEdgesPoints();

    void detectPointEvent(IPolygon polygon);
    void setEvent(Events event);
    Events getEvent();
    IPoint getEdgeFarEnd(IBorder edge);
    ArrayList<CriticalPoint> getIntersectionsInNormal();
}
