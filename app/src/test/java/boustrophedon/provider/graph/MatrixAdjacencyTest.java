package boustrophedon.provider.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class MatrixAdjacencyTest {
    @Test
    public void testAddNode () {
        MatrixAdjacency<Integer> matrix = new MatrixAdjacency<>(
                new ArrayList<>(Arrays.asList(0, 1, 2, 3))
        );

        int size = matrix.addNode(4);

        assertEquals(4, size);
    }
}