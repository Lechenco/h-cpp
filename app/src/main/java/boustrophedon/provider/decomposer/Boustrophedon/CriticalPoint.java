package boustrophedon.provider.decomposer.Boustrophedon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.primitives.BorderHelper;
import boustrophedon.utils.GA;

public class CriticalPoint {
    private IPoint vertices;
    private Events event = Events.UNKNOWN;
    private ArrayList<IBorder> edges;

    private ArrayList<IPoint> intersectionsInY;

    public ArrayList<IPoint> getIntersectionsInY() {
        return intersectionsInY;
    }

    public void setIntersectionsInY(ArrayList<IPoint> intersectionsInY) {
        this.intersectionsInY = intersectionsInY;
    }

    public void setIntersectionsInY(IPoint... intersectionsInY) {
        ArrayList<IPoint> i = new ArrayList<>(Arrays.asList(intersectionsInY));
        setIntersectionsInY(i);
    }

    public CriticalPoint(IPoint vertices, ArrayList<IBorder> edges) {
        this.edges = edges;
        this.vertices = vertices;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    public IPoint getVertices() {
        return vertices;
    }

    public Events getEvent() {
        return event;
    }

    public ArrayList<IBorder> getEdges() {
        return edges;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CriticalPoint detectPointEvent(IPolygon polygon) {
        double normalAngle = Math.PI / 2; // TODO: calc angle dynamically
        ArrayList<IPoint> intersectionNormalPoints = this.calcIntersectionsInAngle(polygon, normalAngle);
        ArrayList<IPoint> intersectionTangentPoints = this.calcIntersectionsInAngle(polygon, normalAngle + Math.PI / 2);

        int countOfIntersectionsTangent = intersectionTangentPoints.size();
        int countOfIntersectionsNormal = intersectionNormalPoints.size();

        if (isAConvexPoint(countOfIntersectionsTangent, countOfIntersectionsNormal)) {
            this.setEvent(Events.NONE);
            return this;
        } else if (intersectionNormalPoints.size() == 1) {
            this.setEvent(Events.MIDDLE);
        } else {
            this.setEvent( this.isInEvent() ? Events.IN : Events.OUT);
        }

        this.setIntersectionsInY(intersectionNormalPoints);

        return this;
    }

    private boolean isAConvexPoint(int countOfIntersectionsTangent, int countOfIntersectionsNormal) {
        return (countOfIntersectionsTangent <= 1 && countOfIntersectionsNormal <= 1) ||
                (countOfIntersectionsTangent > 1 && countOfIntersectionsTangent % 2 == 1) ||
                (countOfIntersectionsNormal > 1 && countOfIntersectionsNormal % 2 == 1);
    }

    protected boolean isInEvent() {
        IPoint point = this.getVertices();
        double pointX = point.getX();
        double firstEdgePointX = getEdgeFarEnd(this.getEdges().get(0)).getX();
        double secondEdgePointX  = getEdgeFarEnd(this.getEdges().get(1)).getX();

        return pointX <= firstEdgePointX && pointX <= secondEdgePointX;
    }

    private IPoint getEdgeFarEnd(IBorder edge) {
        return edge.getFirstVertice() == this.vertices ? edge.getSecondVertice() : edge.getFirstVertice();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected ArrayList<IPoint> calcIntersectionsInAngle(IPolygon polygon, double angle) {
        ArrayList<IPoint> intersectionPoints = new ArrayList<>();

        for (IBorder border : polygon.getBorders()) {
            if (border.isOnBorder(this.getVertices()))
                continue;

            IPoint intersection = BorderHelper.calcIntersectionToWall(this.getVertices(), border, angle);
            boolean duplicated = intersectionPoints
                    .stream().anyMatch(iPoint -> intersection == iPoint);

            if (
                    border.isOnBorder(intersection) &&
                    GA.checkAngles(angle, GA.calcAngle(this.vertices, intersection)) &&
                    !duplicated
            ) {
                intersectionPoints.add(intersection);
            }
        }
        return intersectionPoints;
    }
}
