package boustrophedon.factories.decomposer.Boustrophedon.CriticalPoint;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class CriticalPointFactoryTest {
    @Test
    public void testAddIntersection() {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 1),
                new Point(2.5, 1.5), // IN Event
                new Point(3, 2),
                new Point(3, 3),
                new Point(0, 3)
        );
        ArrayList<IPoint> points = polygon.getPoints();
        ArrayList<IBorder> borders = polygon.getBorders();
        ArrayList<ICriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(points.get(0), new ArrayList<>(Arrays.asList(borders.get(6), borders.get(0)))),
                new CriticalPoint(points.get(1), new ArrayList<>(Arrays.asList(borders.get(0), borders.get(1)))),
                new CriticalPoint(points.get(2), new ArrayList<>(Arrays.asList(borders.get(1), borders.get(2)))),
                new CriticalPoint(points.get(3), new ArrayList<>(Arrays.asList(borders.get(2), borders.get(3)))),
                new CriticalPoint(points.get(4), new ArrayList<>(Arrays.asList(borders.get(3), borders.get(4)))),
                new CriticalPoint(points.get(5), new ArrayList<>(Arrays.asList(borders.get(4), borders.get(5)))),
                new CriticalPoint(points.get(6), new ArrayList<>(Arrays.asList(borders.get(5), borders.get(6))))
        ));

        assertEquals(7, cps.size());

        CriticalPoint intersection = new CriticalPoint(new Point(2.5, 3));
        CriticalPointFactory.addIntersections(cps, new ArrayList<>(Collections.singletonList(intersection)));

        assertEquals(8, cps.size());
        assertEquals(intersection, cps.get(6));
    }
    @Test
    public void testUpdateBorders() {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 1),
                new Point(2.5, 1.5), // IN Event
                new Point(3, 2),
                new Point(3, 3),
                new Point(0, 3)
        );
        ArrayList<IPoint> points = polygon.getPoints();
        ArrayList<IBorder> borders = polygon.getBorders();
        ArrayList<ICriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(points.get(0), new ArrayList<>(Arrays.asList(borders.get(6), borders.get(0)))),
                new CriticalPoint(new Point(2.5, 0)),
                new CriticalPoint(points.get(1), new ArrayList<>(Arrays.asList(borders.get(0), borders.get(1)))),
                new CriticalPoint(points.get(2), new ArrayList<>(Arrays.asList(borders.get(1), borders.get(2)))),
                new CriticalPoint(points.get(3), new ArrayList<>(Arrays.asList(borders.get(2), borders.get(3)))),
                new CriticalPoint(points.get(4), new ArrayList<>(Arrays.asList(borders.get(3), borders.get(4)))),
                new CriticalPoint(points.get(5), new ArrayList<>(Arrays.asList(borders.get(4), borders.get(5)))),
                new CriticalPoint(new Point(2.5, 3)),
                new CriticalPoint(points.get(6), new ArrayList<>(Arrays.asList(borders.get(5), borders.get(6))))
        ));

        assertEquals(0, cps.get(1).getEdges().size());
        assertEquals(0, cps.get(7).getEdges().size());

        CriticalPointFactory.updateBorders(cps, 7);

        assertEquals(2, cps.get(6).getEdges().size());
        assertEquals(2, cps.get(8).getEdges().size());
        assertEquals(2, cps.get(7).getEdges().size());
        assertEquals(points.get(5), cps.get(7).getEdgesPoints().get(0));
        assertEquals(points.get(6), cps.get(7).getEdgesPoints().get(1));
    }

    @Test
    public void testExecuteWithoutEvents() {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 3),
                new Point(0, 3)
        );

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);

        assertEquals(4, cps.size());
    }
    @Test
    public void testExecuteINEvent() {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 1),
                new Point(2.5, 1.5), // IN Event
                new Point(3, 2),
                new Point(3, 3),
                new Point(0, 3)
        );

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);

        assertEquals(9, cps.size());
    }
}