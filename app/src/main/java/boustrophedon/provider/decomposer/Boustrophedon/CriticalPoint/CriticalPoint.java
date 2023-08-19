package boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.primitives.BorderHelper;
import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.provider.primitives.Border;
import boustrophedon.utils.GA;

public class CriticalPoint implements ICriticalPoint {
    private final IPoint vertices;
    private Events event = Events.UNKNOWN;
    private final ArrayList<IBorder> edges;

    private boolean split = false;

    private final ArrayList<CriticalPoint> intersectionsInNormal;

    public ArrayList<CriticalPoint> getIntersectionsInNormal() {
        return intersectionsInNormal;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    protected void addIntersectionsInNormalPoints(IBorder border, IPoint intersectionPoint) {
        ArrayList<IBorder> borders = new ArrayList<>(Arrays
                .asList(new Border(intersectionPoint, border.getFirstVertice()),
                        new Border(intersectionPoint, border.getSecondVertice()))
        );
        this.intersectionsInNormal.add(new CriticalPoint(intersectionPoint, borders));
    }

    public CriticalPoint(IPoint vertices, ArrayList<IBorder> edges) {
        this.edges = edges;
        this.vertices = vertices;
        this.intersectionsInNormal = new ArrayList<>();
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

    public ArrayList<IPoint> getEdgesPoints() {
        ArrayList<IPoint> points = new ArrayList<>();

        for (IBorder b : this.edges) {
            points.add(this.getEdgeFarEnd(b));
        }

        return points;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void detectPointEvent(IPolygon polygon) {
        double normalAngle = Math.PI / 2; // TODO: calc angle dynamically
        ArrayList<IPoint> intersectionNormalPoints = this.calcIntersectionsInAngle(polygon, normalAngle);
        ArrayList<IPoint> intersectionTangentPoints = this.calcIntersectionsInAngle(polygon, normalAngle + Math.PI / 2);

        int countOfIntersectionsTangent = intersectionTangentPoints.size();
        int countOfIntersectionsNormal = intersectionNormalPoints.size();

        if (isAConvexPoint(countOfIntersectionsTangent, countOfIntersectionsNormal)) {
            this.setEvent(Events.NONE);
            return;
        }

        this.validateEventWithIntersections(intersectionNormalPoints);
        this.populateIntersectionNormalPoints(intersectionNormalPoints, polygon);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateIntersectionNormalPoints(ArrayList<IPoint> intersectionNormalPoints, IPolygon polygon) {
        intersectionNormalPoints.forEach(p -> {
            Optional<IBorder> border = polygon.getBorders().stream().filter(b -> b.isOnBorder(p)).findFirst();
            border.ifPresent(iBorder -> addIntersectionsInNormalPoints(iBorder, p));
        });
    }

    private void validateEventWithIntersections(ArrayList<IPoint> intersectionNormalPoints) {
        if (intersectionNormalPoints.size() == 1) {
            this.setEvent(Events.MIDDLE);
        } else {
            this.setEvent( this.isInEvent() ? Events.IN : Events.OUT);
        }
    }

    protected boolean isAConvexPoint(int countOfIntersectionsTangent, int countOfIntersectionsNormal) {
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

    public IPoint getEdgeFarEnd(IBorder edge) {
        return edge.getFirstVertice().equals(this.vertices) ? edge.getSecondVertice() : edge.getFirstVertice();
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
