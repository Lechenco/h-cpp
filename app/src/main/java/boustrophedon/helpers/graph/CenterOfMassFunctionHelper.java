package boustrophedon.helpers.graph;

import java.util.Collection;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Point;

public class CenterOfMassFunctionHelper {
    static public IPoint calcCenterOfMass(IPolygon polygon) {
        return CenterOfMassFunctionHelper.calcCenterOfMass(polygon.getPoints());
    }
    static public IPoint calcCenterOfMass(Collection<IPoint> points) {
        int numberOfPoints = points.size();
        double sumOfXValues = points.stream().mapToDouble(IPoint::getX).sum();
        double sumOfYValues = points.stream().mapToDouble(IPoint::getY).sum();

        return new Point(
                sumOfXValues / numberOfPoints,
                sumOfYValues / numberOfPoints
        );
    }
}
