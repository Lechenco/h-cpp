package boustrophedon.provider.decomposer.Boustrophedon.Clippers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        ENTERING, LEAVING, NONE
    }
    private static class Intersection {
        IPoint point;
        IntersectionStatus status;

        public Intersection(IPoint point, IntersectionStatus status) {
            this.point = point;
            this.status = status;
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
            pointsA.add(new Intersection(borderA.getFirstVertice(), IntersectionStatus.NONE));
            for (IBorder borderB : B.getBorders()) {
                IPoint firstVertices = borderB.getFirstVertice();

                IPoint intersect = BorderHelper.calcIntersection(firstVertices, borderB, borderA);
                if (borderA.isOnBorder(intersect)) {
                    IntersectionStatus status = WalkerHelper.isPointInsidePolygon(firstVertices, A) ?
                            IntersectionStatus.LEAVING : IntersectionStatus.ENTERING;
                    Intersection intersection = new Intersection(intersect, status);
                    pointsA.add(intersection);

                    Objects.requireNonNull(intersectionsInBMap.get(borderB)).add(intersection);
                }
            }
        }

        res.put(A, pointsA);
        res.put(B, this.mountPointsInB(B, intersectionsInBMap));

        return res;
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
        resultPoints.add(start.point);

        int pos = this.nextIndex(start, pointsB, directionB);
        Intersection current = pointsB.get(pos);

        while(current != start) {
            current = getNextIntersection(current, pointsB, directionB, resultPoints);
            current = getNextIntersection(current, pointsA, directionA, resultPoints);
        }

        resultPoints.remove(resultPoints.size() -1);
        return new Polygon(resultPoints);
    }

    private Intersection getNextIntersection(Intersection current, ArrayList<Intersection> points, int direction, ArrayList<IPoint> resultPoints) {
        if (!points.contains(current)) return current;

        resultPoints.add(current.point);
        int pos = this.nextIndex(current, points, direction);
        while (points.get(pos).status == IntersectionStatus.NONE) {
            pos = this.nextIndex(current, points, direction);
            resultPoints.add(points.get(pos).point);
        }
        return points.get(pos);
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
}
