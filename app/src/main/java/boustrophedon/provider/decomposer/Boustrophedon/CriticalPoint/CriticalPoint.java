package boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint;

import static boustrophedon.utils.AngleUtils.add90Degrees;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.ArrayList;


import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.primitives.BorderHelper;
import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.utils.AngleUtils;

public class CriticalPoint implements ICriticalPoint {
    private final IPoint vertices;
    private Events event = Events.UNKNOWN;
    private final ArrayList<IBorder> edges;

    private boolean split = false;

    private final ArrayList<CriticalPoint> intersectionsInNormal;

    @Override
    public ArrayList<CriticalPoint> getIntersectionsInNormal() {
        return intersectionsInNormal;
    }

    @Override
    public boolean isSplit() {
        return split;
    }

    @Override
    public void setSplit(boolean split) {
        this.split = split;
    }

    public CriticalPoint(IPoint vertices) {
        this.edges = new ArrayList<>();
        this.vertices = vertices;
        this.intersectionsInNormal = new ArrayList<>();
    }

    public CriticalPoint(IPoint vertices, ArrayList<IBorder> edges) {
        this.edges = edges;
        this.vertices = vertices;
        this.intersectionsInNormal = new ArrayList<>();
    }

    @Override
    public void setEvent(Events event) {
        this.event = event;
    }

    @Override
    public IPoint getVertices() {
        return vertices;
    }

    @Override
    public Events getEvent() {
        return event;
    }

    @Override
    public ArrayList<IBorder> getEdges() {
        return edges;
    }

    @Override
    public ArrayList<IPoint> getEdgesPoints() {
        ArrayList<IPoint> points = new ArrayList<>();

        for (IBorder b : this.edges) {
            points.add(this.getEdgeFarEnd(b));
        }

        return points;
    }

    @Override
    public void detectPointEvent(IPolygon polygon) {
        double normalAngle = Math.PI / 2; // TODO: calc angle dynamically
        ArrayList<IPoint> intersectionNormalPoints = this.calcIntersectionsInAngle(polygon, normalAngle);
        ArrayList<IPoint> intersectionTangentPoints = this.calcIntersectionsInAngle(polygon,  add90Degrees(normalAngle));

        int countOfIntersectionsTangent = intersectionTangentPoints.size();
        int countOfIntersectionsNormal = intersectionNormalPoints.size();

        if (this.event == Events.UNKNOWN) {
            if (isAConvexPoint(countOfIntersectionsTangent, countOfIntersectionsNormal)) {
                this.setEvent(Events.NONE);
                return;
            }

            this.validateEventWithIntersections(intersectionNormalPoints);
        }

        this.populateIntersectionNormalPoints(intersectionNormalPoints, polygon);
    }

    private void populateIntersectionNormalPoints(ArrayList<IPoint> intersectionNormalPoints, IPolygon polygon) {
        intersectionNormalPoints.forEach(p -> this.intersectionsInNormal.add(new CriticalPoint(p)));
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
        if (this.getEdges().size() < 2) return false;
        IPoint point = this.getVertices();
        double pointX = point.getX();
        double firstEdgePointX = getEdgeFarEnd(this.getEdges().get(0)).getX();
        double secondEdgePointX  = getEdgeFarEnd(this.getEdges().get(1)).getX();

        return pointX <= firstEdgePointX && pointX <= secondEdgePointX;
    }

    @Override
    public IPoint getEdgeFarEnd(IBorder edge) {
        return edge.getFirstVertice().equals(this.vertices) ? edge.getSecondVertice() : edge.getFirstVertice();
    }

    protected ArrayList<IPoint> calcIntersectionsInAngle(IPolygon polygon, double angle) {
        ArrayList<IPoint> intersectionPoints = new ArrayList<>();

        for (IBorder border : polygon.getBorders()) {
            if (border.isOnBorder(this.getVertices()))
                continue;

            IPoint intersection = BorderHelper.calcIntersectionToWall(this.getVertices(), border, angle);
            boolean duplicated = intersectionPoints
                    .stream().anyMatch(intersection::equals);

            if (
                    border.isOnBorder(intersection) &&
                    AngleUtils.checkAngles(angle, AngleUtils.calcAngle(this.vertices, intersection)) &&
                    !duplicated
            ) {
                intersectionPoints.add(intersection);
            }
        }
        return intersectionPoints;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("CriticalPoint{ Vertices={ x=%f, y=%f }, Event=%s }",
                vertices.getX(),
                vertices.getY(),
                event.toString()
                );
    }
}
