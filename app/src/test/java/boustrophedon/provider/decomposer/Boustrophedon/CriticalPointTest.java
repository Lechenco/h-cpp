package boustrophedon.provider.decomposer.Boustrophedon;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class CriticalPointTest {
    Polygon triangleRectangle;
    Polygon squareRectangle;
    @Before
    public void setUp() {
        triangleRectangle = new Polygon(new Point(0, 0),
                new Point(5, 5), new Point(5, 0));
        squareRectangle = new Polygon(new Point(0, 0),
                new Point(5, 0),
                new Point(5, 5), new Point(0, 5)
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

        assertEquals(Events.NONE, criticalPoint.getEvent());
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
}