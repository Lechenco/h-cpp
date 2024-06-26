package boustrophedon.controllers.decomposer.Boustrophedon.Splitters;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.factories.decomposer.Boustrophedon.CriticalPoint.CriticalPointFactory;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class SplitterControllerIntegrationTest {
    @Test
    public void executeNoEventsDiamond() throws ExceedNumberOfAttempts {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(1.5, -1.5),
                new Point(3, 3),
                new Point(1.5, 4.5)
        );

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(cps, SubareaTypes.NORMAL);
        IAdjacencyMatrix<INode<ICell>> matrix = splitterController.execute();

        assertEquals(1, matrix.getNodes().size());
    }
    @Test
    public void executeNoEventsSquare() throws ExceedNumberOfAttempts {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(3, 0),
                new Point(3, 1),
                new Point(0, 1)
        );

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(cps, SubareaTypes.NORMAL);
        IAdjacencyMatrix<INode<ICell>> matrix = splitterController.execute();

        assertEquals(1, matrix.getNodes().size());
    }
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

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(cps, SubareaTypes.NORMAL);
        IAdjacencyMatrix<INode<ICell>> matrix = splitterController.execute();

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

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(cps, SubareaTypes.NORMAL);

        IAdjacencyMatrix<INode<ICell>> matrix = splitterController.execute();
        assertEquals(3, matrix.getNodes().size());
    }
    @Test
    public void executeInEvent2() throws ExceedNumberOfAttempts {
        IPolygon polygon = new Polygon(
                new Point(0, 0),
                new Point(2.6, 0),
                new Point(2.7, 0.8), // MIDDLE Event
                new Point(2.8, 0),
                new Point(3, 0),
                new Point(3, 1),
                new Point(2.5, 1.5), // IN Event
                new Point(3, 2),
                new Point(3, 3),
                new Point(0, 3)
        );

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(cps, SubareaTypes.NORMAL);

        IAdjacencyMatrix<INode<ICell>> matrix = splitterController.execute();
        assertEquals(4, matrix.getNodes().size());
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

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(cps, SubareaTypes.NORMAL);

        IAdjacencyMatrix<INode<ICell>> matrix = splitterController.execute();
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

        ArrayList<ICriticalPoint> cps = CriticalPointFactory.execute(polygon);
        SplitterController splitterController = new SplitterController(cps, SubareaTypes.NORMAL);

        IAdjacencyMatrix<INode<ICell>> matrix = splitterController.execute();
        assertEquals(4, matrix.getNodes().size());
    }
}