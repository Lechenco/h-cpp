package boustrophedon.controllers.decomposer.Boustrophedon.Splitters;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.Cell;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class SplitterControllerIntegrationTest {
    @Test
    public void executeMiddleEvent() throws ExceedNumberOfAttempts {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1.5, 0.5),
                new Point(2, 0),
                new Point(3, 0),
                new Point(3, 1),
                new Point(0, 1)
        );
        ArrayList<IPoint> points = polygon.getPoints();
        ArrayList<IBorder> borders = polygon.getBorders();
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
           new CriticalPoint(points.get(0), new ArrayList<>(Arrays.asList(borders.get(6), borders.get(0)))),
                new CriticalPoint(points.get(1), new ArrayList<>(Arrays.asList(borders.get(0), borders.get(1)))),
                new CriticalPoint(points.get(2), new ArrayList<>(Arrays.asList(borders.get(1), borders.get(2)))),
                new CriticalPoint(points.get(3), new ArrayList<>(Arrays.asList(borders.get(2), borders.get(3)))),
                new CriticalPoint(points.get(4), new ArrayList<>(Arrays.asList(borders.get(3), borders.get(4)))),
                new CriticalPoint(points.get(5), new ArrayList<>(Arrays.asList(borders.get(4), borders.get(5)))),
                new CriticalPoint(points.get(6), new ArrayList<>(Arrays.asList(borders.get(5), borders.get(6))))
        ));

        cps.forEach(cp -> cp.detectPointEvent(polygon));
        CriticalPointerHelper.addIntersections(cps.get(2).getIntersectionsInNormal().get(0), cps);

        SplitterController splitterController = new SplitterController(cps);
        MatrixAdjacency<Node<ICell>> matrix = splitterController.execute();

        assertEquals(2, matrix.getNodes().size());
    }
    @Test
    public void executeInEvent() throws ExceedNumberOfAttempts {
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
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(points.get(0), new ArrayList<>(Arrays.asList(borders.get(6), borders.get(0)))),
                new CriticalPoint(points.get(1), new ArrayList<>(Arrays.asList(borders.get(0), borders.get(1)))),
                new CriticalPoint(points.get(2), new ArrayList<>(Arrays.asList(borders.get(1), borders.get(2)))),
                new CriticalPoint(points.get(3), new ArrayList<>(Arrays.asList(borders.get(2), borders.get(3)))),
                new CriticalPoint(points.get(4), new ArrayList<>(Arrays.asList(borders.get(3), borders.get(4)))),
                new CriticalPoint(points.get(5), new ArrayList<>(Arrays.asList(borders.get(4), borders.get(5)))),
                new CriticalPoint(points.get(6), new ArrayList<>(Arrays.asList(borders.get(5), borders.get(6))))
        ));

        cps.forEach(cp -> cp.detectPointEvent(polygon));
        CriticalPointerHelper.addIntersections(cps.get(3).getIntersectionsInNormal().get(0), cps);
        CriticalPointerHelper.addIntersections(cps.get(4).getIntersectionsInNormal().get(1), cps);

        SplitterController splitterController = new SplitterController(cps);

        MatrixAdjacency<Node<ICell>> matrix = splitterController.execute();
        assertEquals(3, matrix.getNodes().size());
    }
    @Test
    public void executeOUTEvent() throws ExceedNumberOfAttempts {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 3),
                new Point(0, 3),
                new Point(0, 2),
                new Point(0.5, 1.5), // OUT Event
                new Point(0, 1)
        );
        ArrayList<IPoint> points = polygon.getPoints();
        ArrayList<IBorder> borders = polygon.getBorders();
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(points.get(0), new ArrayList<>(Arrays.asList(borders.get(6), borders.get(0)))),
                new CriticalPoint(points.get(1), new ArrayList<>(Arrays.asList(borders.get(0), borders.get(1)))),
                new CriticalPoint(points.get(2), new ArrayList<>(Arrays.asList(borders.get(1), borders.get(2)))),
                new CriticalPoint(points.get(3), new ArrayList<>(Arrays.asList(borders.get(2), borders.get(3)))),
                new CriticalPoint(points.get(4), new ArrayList<>(Arrays.asList(borders.get(3), borders.get(4)))),
                new CriticalPoint(points.get(5), new ArrayList<>(Arrays.asList(borders.get(4), borders.get(5)))),
                new CriticalPoint(points.get(6), new ArrayList<>(Arrays.asList(borders.get(5), borders.get(6))))
        ));

        cps.forEach(cp -> cp.detectPointEvent(polygon));
        CriticalPointerHelper.addIntersections(cps.get(5).getIntersectionsInNormal().get(0), cps);
        CriticalPointerHelper.addIntersections(cps.get(6).getIntersectionsInNormal().get(1), cps);

        SplitterController splitterController = new SplitterController(cps);

        MatrixAdjacency<Node<ICell>> matrix = splitterController.execute();
        assertEquals(3, matrix.getNodes().size());
    }

    @Test
    public void executeMIDDLEAndOUTEvent() throws ExceedNumberOfAttempts {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1.5, 0.5), // MIDDLE Event
                new Point(2, 0),
                new Point(3, 0),
                new Point(3, 3),
                new Point(0, 3),
                new Point(0, 2),
                new Point(0.5, 1.5), // OUT Event
                new Point(0, 1)
        );
        ArrayList<IPoint> points = polygon.getPoints();
        ArrayList<IBorder> borders = polygon.getBorders();
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(points.get(0), new ArrayList<>(Arrays.asList(borders.get(9), borders.get(0)))),
                new CriticalPoint(points.get(1), new ArrayList<>(Arrays.asList(borders.get(0), borders.get(1)))),
                new CriticalPoint(points.get(2), new ArrayList<>(Arrays.asList(borders.get(1), borders.get(2)))),
                new CriticalPoint(points.get(3), new ArrayList<>(Arrays.asList(borders.get(2), borders.get(3)))),
                new CriticalPoint(points.get(4), new ArrayList<>(Arrays.asList(borders.get(3), borders.get(4)))),
                new CriticalPoint(points.get(5), new ArrayList<>(Arrays.asList(borders.get(4), borders.get(5)))),
                new CriticalPoint(points.get(6), new ArrayList<>(Arrays.asList(borders.get(5), borders.get(6)))),
                new CriticalPoint(points.get(7), new ArrayList<>(Arrays.asList(borders.get(6), borders.get(7)))),
                new CriticalPoint(points.get(8), new ArrayList<>(Arrays.asList(borders.get(7), borders.get(8)))),
                new CriticalPoint(points.get(9), new ArrayList<>(Arrays.asList(borders.get(8), borders.get(9))))
        ));

        cps.forEach(cp -> cp.detectPointEvent(polygon));
        CriticalPointerHelper.addIntersections(cps.get(2).getIntersectionsInNormal().get(0), cps);
        CriticalPointerHelper.addIntersections(cps.get(9).getIntersectionsInNormal().get(0), cps);
        CriticalPointerHelper.addIntersections(cps.get(10).getIntersectionsInNormal().get(1), cps);

        SplitterController splitterController = new SplitterController(cps);

        MatrixAdjacency<Node<ICell>> matrix = splitterController.execute();
        assertEquals(4, matrix.getNodes().size());
    }
}