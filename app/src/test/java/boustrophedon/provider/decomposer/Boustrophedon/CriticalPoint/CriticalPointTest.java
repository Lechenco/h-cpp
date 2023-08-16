package boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Events;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class CriticalPointTest {
    Polygon triangleRectangle;
    Polygon squareRectangle;

    CriticalPoint cp;
    @Before
    public void setUp() {
        triangleRectangle = new Polygon(new Point(0, 0),
                new Point(5, 5), new Point(5, 0));
        squareRectangle = new Polygon(new Point(0, 0),
                new Point(5, 0),
                new Point(5, 5), new Point(0, 5)
        );

        cp = new CriticalPoint(new Point(0,0),
                new ArrayList<>(
                    Arrays.asList(
                        triangleRectangle.getBorders().get(0),
                        triangleRectangle.getBorders().get(2)
                    )
                )
        );
    }

    @Test
    public void testDetectEvent() {
        CriticalPoint criticalPoint = new CriticalPoint(squareRectangle.points.get(0),
                new ArrayList<>(Arrays.asList(
                        squareRectangle.borders.get(0),
                        squareRectangle.borders.get(3))
                )
        );

        criticalPoint.detectPointEvent(squareRectangle);

        Assert.assertEquals(Events.NONE, criticalPoint.getEvent());
    }

    @Test
    public void testDetectMiddleEvent() {
        Polygon polygon = new Polygon(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1.5, 1),
                new Point(2, 0),
                new Point(3, 0),
                new Point(3, 4),
                new Point(0, 4)
        );
        CriticalPoint criticalPoint = new CriticalPoint(polygon.points.get(2),
                new ArrayList<>(Arrays.asList(
                        polygon.borders.get(1),
                        polygon.borders.get(2))
                )
        );

        criticalPoint.detectPointEvent(polygon);
        assertEquals(Events.MIDDLE, criticalPoint.getEvent());

        CriticalPoint criticalPoint1 = new CriticalPoint(polygon.points.get(1),
                new ArrayList<>(Arrays.asList(
                        polygon.borders.get(0),
                        polygon.borders.get(1))
                )
        );

        criticalPoint1.detectPointEvent(polygon);
        assertEquals(Events.NONE, criticalPoint1.getEvent());
    }

    @Test
    public void testDetectInEvent() {
        Polygon polygon = new Polygon(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 1),
                new Point(1.5, 2),
                new Point(3, 3),
                new Point(3, 4),
                new Point(0, 4)
        );
        CriticalPoint criticalPoint = new CriticalPoint(polygon.points.get(3),
                new ArrayList<>(Arrays.asList(
                        polygon.borders.get(2),
                        polygon.borders.get(3))
                )
        );

        criticalPoint.detectPointEvent(polygon);
        assertEquals(Events.IN, criticalPoint.getEvent());

        CriticalPoint criticalPoint1 = new CriticalPoint(polygon.points.get(2),
                new ArrayList<>(Arrays.asList(
                        polygon.borders.get(1),
                        polygon.borders.get(2))
                )
        );

        criticalPoint1.detectPointEvent(polygon);
        assertEquals(Events.NONE, criticalPoint1.getEvent());
    }

    @Test
    public void testDetectOutEvent() {
        Polygon polygon = new Polygon(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 4),
                new Point(0, 4),
                new Point(0, 3),
                new Point(1.5, 2),
                new Point(0, 1)
        );
        CriticalPoint criticalPoint = new CriticalPoint(polygon.points.get(5),
                new ArrayList<>(Arrays.asList(
                        polygon.borders.get(4),
                        polygon.borders.get(5))
                )
        );

        criticalPoint.detectPointEvent(polygon);
        assertEquals(Events.OUT, criticalPoint.getEvent());

        CriticalPoint criticalPoint1 = new CriticalPoint(polygon.points.get(2),
                new ArrayList<>(Arrays.asList(
                        polygon.borders.get(1),
                        polygon.borders.get(2))
                )
        );

        criticalPoint1.detectPointEvent(polygon);
        assertEquals(Events.NONE, criticalPoint1.getEvent());
    }

    @Test
    public void testAddIntersectionInNormalPoints() {
        Point p = new Point(1, 1);
        cp.addIntersectionsInNormalPoints(triangleRectangle.getBorders().get(1), p);

        ArrayList<CriticalPoint> intersections = cp.getIntersectionsInNormal();

        assertEquals(1, intersections.size());
        assertEquals(p, intersections.get(0).getVertices());
    }

    @Test
    public void testCalcIntersectionsInAngle() {
        ArrayList<IPoint> points = cp.calcIntersectionsInAngle(triangleRectangle, 0);

        assertEquals(1, points.size());
        assertEquals(new Point(5,0), points.get(0));
    }
    @Test
    public void testCalcIntersectionsInAngle90() {
        ArrayList<IPoint> points = cp.calcIntersectionsInAngle(triangleRectangle, Math.PI /2);

        assertEquals(0, points.size());
    }
    @Test
    public void testCalcIntersectionsInAngleNotAddDuplicates() {
        //Concave Polygon Middle Event
        Polygon polygon = new Polygon(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1.5, 1),
                new Point(2, 0),
                new Point(3, 0),
                new Point(3, 4),
                new Point(0, 4)
        );
        ArrayList<IPoint> points = cp.calcIntersectionsInAngle(polygon, 0);

        assertEquals(3, points.size());
        assertEquals(new Point(1,0), points.get(0));
        assertEquals(new Point(2,0), points.get(1));
        assertEquals(new Point(3,0), points.get(2));
    }

    @Test
    public void testGetEdgesPoints() {
        ArrayList<IPoint> points = cp.getEdgesPoints();

        assertEquals(2, points.size());
        assertEquals(new Point(5, 5), points.get(0));
        assertEquals(new Point(5, 0), points.get(1));
    }

    @Test
    public void testIsAConvexPoint() {
        assertTrue(cp.isAConvexPoint(0, 0));
        assertTrue(cp.isAConvexPoint(1, 1));
        assertFalse(cp.isAConvexPoint(1, 2));
        assertFalse(cp.isAConvexPoint(2, 1));
        assertFalse(cp.isAConvexPoint(2, 2));
        assertTrue(cp.isAConvexPoint(1, 3));
        assertTrue(cp.isAConvexPoint(3, 1));
    }
}