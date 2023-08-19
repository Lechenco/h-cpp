package boustrophedon.controllers.graph;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.Cell;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

@RunWith(MockitoJUnitRunner.class)
public class MatrixControllerTest {
    @Test
    public void testAddCellsToMatrixEmpty() {
        MatrixAdjacency<Node<ICell>> matrixAdjacency = Mockito.spy(new MatrixAdjacency());
        Map<IPoint, Integer> map = Mockito.spy(new HashMap());

        MatrixController controller = new MatrixController(map, matrixAdjacency);

        IPoint splitPoint = new Point(1, 1);
        ICell cell = new Cell(
                new Polygon(
                        splitPoint,
                        new Point(0, 0),
                        new Point(1, 0))
        );
        controller.addCellsToMatrix(cell, splitPoint);
        Mockito.verify(matrixAdjacency).addNode(Mockito.any());
        Mockito.verify(map).put(splitPoint, 0);
    }
    @Test
    public void testAddCellsToMatrixNullPoint() {
        MatrixAdjacency<Node<ICell>> matrixAdjacency = Mockito.spy(new MatrixAdjacency());
        Map<IPoint, Integer> map = Mockito.spy(new HashMap());

        MatrixController controller = new MatrixController(map, matrixAdjacency);

        IPoint splitPoint = new Point(1, 1);
        ICell cell = new Cell(
                new Polygon(
                        splitPoint,
                        new Point(0, 0),
                        new Point(1, 0))
        );
        controller.addCellsToMatrix(cell, null);
        Mockito.verify(matrixAdjacency).addNode(Mockito.any());
        Mockito.verify(map, Mockito.times(0)).put(splitPoint, 0);
    }
    @Test
    public void testAddCellsToMatrixAddAdjacency() {
        MatrixAdjacency<Node<ICell>> matrixAdjacency = Mockito.spy(new MatrixAdjacency());
        Map<IPoint, Integer> map = Mockito.spy(new HashMap());

        MatrixController controller = new MatrixController(map, matrixAdjacency);

        IPoint splitPoint = new Point(1, 1);
        ICell cell = new Cell(
                new Polygon(
                        splitPoint,
                        new Point(0, 0),
                        new Point(1, 0))
        );
        ICell cell2 = new Cell(
                new Polygon(
                        splitPoint,
                        new Point(2, 2),
                        new Point(2, 0))
        );
        controller.addCellsToMatrix(cell, splitPoint);
        controller.addCellsToMatrix(cell2, null);

        Mockito.verify(matrixAdjacency, Mockito.times(2)).addNode(Mockito.any());
        Mockito.verify(map, Mockito.times(1)).put(splitPoint, 0);

        assertEquals(1, controller.getMatrixAdjacency().getAdjacency(0).size());
        assertEquals(cell2, controller.getMatrixAdjacency().getAdjacency(0).get(0).getObject());
    }
}