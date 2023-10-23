package boustrophedon.provider.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class AdjacencyMatrixTest {
    @Test
    public void testAddNode () {
        AdjacencyMatrix<Integer> matrix = new AdjacencyMatrix<>(
                new ArrayList<>(Arrays.asList(0, 1, 2, 3))
        );

        int size = matrix.addNode(4);

        assertEquals(4, size);
    }
}