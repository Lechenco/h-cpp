package boustrophedon.helpers.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class CenterOfMassFunctionHelperTest {
    @Test
    public void testCalcCenterOfMassPoints() {
        Collection<IPoint> points = Arrays.asList(
                new Point(-1, 0),
                new Point(3, 0)
        );

        assertEquals(new Point(1, 0), CenterOfMassFunctionHelper.calcCenterOfMass(points));
    }
    @Test
    public void testCalcCenterOfMassPoints2() {
        Collection<IPoint> points = Arrays.asList(
                new Point(-1, 0),
                new Point(0, 3),
                new Point(3, 0)
        );

        assertEquals(new Point(2.0 / 3, 1), CenterOfMassFunctionHelper.calcCenterOfMass(points));
    }
    @Test
    public void testCalcCenterOfMassPolygon() {
        Collection<IPoint> points = Arrays.asList(
                new Point(-1, 0),
                new Point(0, 3),
                new Point(3, 0)
        );
        IPolygon polygon = new Polygon(new ArrayList<>(points));

        assertEquals(new Point(2.0 / 3, 1), CenterOfMassFunctionHelper.calcCenterOfMass(polygon));
    }
}