package boustrophedon.provider.decomposer.Boustrophedon.Clippers;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import boustrophedon.constants.PrecisionConstants;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.primitives.BorderHelper;
import boustrophedon.helpers.walkers.WalkerHelper;
import boustrophedon.provider.primitives.Polygon;

public class WeilerAthertonClippingAlgorithm {
    public enum WeilerAthertonModes {
        CLIP, MERGE, DIFFERENCE
    }

    public enum IntersectionStatus {
        ENTERING, LEAVING, NONE, INTERSECTION
    }
    private static class Intersection {
        IPoint point;
        IntersectionStatus status;

        public Intersection(IPoint point, IntersectionStatus status) {
            this.point = point;
            this.status = status;
        }
        @SuppressLint("DefaultLocale")
        @NonNull
        @Override
        public String toString() {
            return String.format("Intersection{ x=%f, y=%f, status=%s }", point.getX(), point.getY(), status);
        }
    }

    int directionA;
    int directionB = 1;
    IntersectionStatus startStatus;

    public IPolygon execute(IPolygon A, IPolygon B) {
        return this.execute(A, B, WeilerAthertonModes.CLIP);
    }

    public IPolygon execute(IPolygon A, IPolygon B, WeilerAthertonModes mode) {
        this.setMode(mode);

        Map<IPolygon,ArrayList<Intersection>> pointsMap = this.calcPointsAAndB(A, B);

        return this.mountResult(Objects.requireNonNull(pointsMap.get(A)), Objects.requireNonNull(pointsMap.get(B)));
    }

    private Map<IPolygon,ArrayList<Intersection>> calcPointsAAndB(IPolygon A, IPolygon B) {
        ArrayList<Intersection> pointsA = new ArrayList<>();
        Map<IPolygon,ArrayList<Intersection>> res = new HashMap<>();
        Map<IBorder, ArrayList<Intersection>> intersectionsInBMap = new HashMap<>();

        for (IBorder borderB : B.getBorders()) {
            intersectionsInBMap.put(borderB, new ArrayList<>());
        }

        for (IBorder borderA : A.getBorders()) {
            if (pointsA.size() == 0 || pointsA.get(pointsA.size() -1).point != borderA.getFirstVertice())
                pointsA.add(new Intersection(borderA.getFirstVertice(), IntersectionStatus.NONE));

            for (IBorder borderB : B.getBorders()) {
                IPoint firstVertices = borderB.getFirstVertice();

                IPoint intersect = this.calcIntersection(firstVertices, borderB, borderA);
                if (
                        intersect != null && !onPolygon(intersect, B) &&
                        borderA.isOnBorder(intersect) && borderB.isOnBorder(intersect)
                ) {
                    Intersection intersection = new Intersection(intersect, IntersectionStatus.INTERSECTION);
                    pointsA.add(intersection);

                    Objects.requireNonNull(intersectionsInBMap.get(borderB)).add(intersection);
                }
            }
        }

        res.put(A, pointsA);
        res.put(B, this.mountPointsInB(B, intersectionsInBMap));
        this.calcStatus(res.get(B), A);

        return res;
    }
    private boolean onPolygon(IPoint point, IPolygon polygon) {
        return polygon.getPoints().stream().anyMatch(p -> p.equals(point));
    }

    private ArrayList<Intersection> mountPointsInB(IPolygon B, Map<IBorder, ArrayList<Intersection>> intersectionsInBMap) {
        ArrayList<Intersection> pointsB = new ArrayList<>();

        for (IBorder borderB : B.getBorders()) {
            pointsB.add(new Intersection(borderB.getFirstVertice(), IntersectionStatus.NONE));
            pointsB.addAll(Objects.requireNonNull(intersectionsInBMap.get(borderB)));
        }
        return pointsB;
    }

    private IPolygon mountResult(ArrayList<Intersection> pointsA, ArrayList<Intersection> pointsB) {
        ArrayList<IPoint> resultPoints = new ArrayList<>();

        Optional<Intersection> startOptional = pointsB.stream().filter(i -> i.status == startStatus).findFirst();
        if (!startOptional.isPresent()) return null;

        Intersection start = startOptional.get();
        Intersection current = start;

        do {
            current = getNextIntersection(current, pointsB, directionB, resultPoints);
            current = getNextIntersection(current, pointsA, directionA, resultPoints);
        } while(current != start);
        return new Polygon(resultPoints);
    }

    private Intersection getNextIntersection(Intersection current, ArrayList<Intersection> points, int direction, ArrayList<IPoint> resultPoints) {
        if (!points.contains(current)) return current;

        Intersection aux = current;
       do {
            resultPoints.add(aux.point);
            aux = points.get(this.nextIndex(aux, points, direction));
        }  while (aux.status == IntersectionStatus.NONE);

        return aux;
    }

    private int nextIndex(Intersection current, ArrayList<Intersection> points, int direction) {
        int pos = points.indexOf(current) + direction;
        pos = pos == points.size() ? 0 : pos;
        pos = pos < 0 ? points.size() -1 : pos;
        return pos;
    }

    private void setMode(WeilerAthertonModes mode) {
        switch (mode) {
            default:
            case CLIP:
                this.directionA = 1;
                this.startStatus = IntersectionStatus.ENTERING;
                break;
            case MERGE:
                this.directionA = 1;
                this.startStatus = IntersectionStatus.LEAVING;
                break;
            case DIFFERENCE:
                this.directionA = -1;
                this.startStatus = IntersectionStatus.LEAVING;
                break;
        }
    }

    protected IPoint calcIntersection(IPoint vertices, IBorder borderB, IBorder borderA) {
        if (Math.abs(borderA.getAngleFirstHalf() - borderB.getAngleFirstHalf()) > PrecisionConstants.ANGLE_PRECISION) {
            IPoint intersect = BorderHelper.calcIntersectionToWall(vertices, borderA, borderB.getAngleFirstHalf());

            if (intersect.equals(borderA.getFirstVertice())) return null;
            return intersect;
        }


        if (borderB.isOnBorder(borderA.getSecondVertice())) {
            return borderA.getSecondVertice();
        }
        return null;
    }

    private void calcStatus(ArrayList<Intersection> intersectionsB, IPolygon A) {
        ArrayList<Intersection> copyIntersections = new ArrayList<>(intersectionsB);

        while (copyIntersections.get(0).status != IntersectionStatus.NONE) {
            Collections.rotate(copyIntersections, -1);
        }

        IntersectionStatus currentStatus = WalkerHelper.isPointInsidePolygon(copyIntersections.get(0).point, A) ?
                IntersectionStatus.LEAVING :
                IntersectionStatus.ENTERING;

        for (Intersection intersection : copyIntersections) {
            if (intersection.status != IntersectionStatus.NONE) {
                intersection.status = currentStatus;
                currentStatus = currentStatus == IntersectionStatus.ENTERING ?
                        IntersectionStatus.LEAVING :
                        IntersectionStatus.ENTERING;
            }
        }

    }
    public int getDirectionA() {
        return directionA;
    }

    public int getDirectionB() {
        return directionB;
    }

    public IntersectionStatus getStartStatus() {
        return startStatus;
    }
}
