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
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

@RunWith(MockitoJUnitRunner.class)
public class MatrixControllerTest {
    @Test
    public void testAddCellsToMatrixEmpty() {
        AdjacencyMatrix<Node<ICell>> adjacencyMatrix = Mockito.spy(new AdjacencyMatrix());
        Map<IPoint, Integer> map = Mockito.spy(new HashMap());

        MatrixController controller = new MatrixController(map, adjacencyMatrix);

        IPoint splitPoint = new Point(1, 1);
        ICell cell = new Cell(
                new Polygon(
                        splitPoint,
                        new Point(0, 0),
                        new Point(1, 0))
        );
        controller.addCellsToMatrix(cell, splitPoint);
        Mockito.verify(adjacencyMatrix).addNode(Mockito.any());
        Mockito.verify(map).put(splitPoint, 0);
    }
    @Test
    public void testAddCellsToMatrixNullPoint() {
        AdjacencyMatrix<Node<ICell>> adjacencyMatrix = Mockito.spy(new AdjacencyMatrix());
        Map<IPoint, Integer> map = Mockito.spy(new HashMap());

        MatrixController controller = new MatrixController(map, adjacencyMatrix);

        IPoint splitPoint = new Point(1, 1);
        ICell cell = new Cell(
                new Polygon(
                        splitPoint,
                        new Point(0, 0),
                        new Point(1, 0))
        );
        controller.addCellsToMatrix(cell, null);
        Mockito.verify(adjacencyMatrix).addNode(Mockito.any());
        Mockito.verify(map, Mockito.times(0)).put(splitPoint, 0);
    }
    @Test
    public void testAddCellsToMatrixAddAdjacency() {
        AdjacencyMatrix<Node<ICell>> adjacencyMatrix = Mockito.spy(new AdjacencyMatrix());
        Map<IPoint, Integer> map = Mockito.spy(new HashMap());

        MatrixController controller = new MatrixController(map, adjacencyMatrix);

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

        Mockito.verify(adjacencyMatrix, Mockito.times(2)).addNode(Mockito.any());
        Mockito.verify(map, Mockito.times(1)).put(splitPoint, 0);

        assertEquals(1, controller.getMatrixAdjacency().getAdjacency(0).size());
        assertEquals(cell2, controller.getMatrixAdjacency().getAdjacency(0).get(0).getObject());
    }
}