package boustrophedon.provider.decomposer.Boustrophedon;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class DecomposerTest {
    Polygon triangleRectangle;
    Polygon squareRectangle;
    @Before
    public void setUp() {
        triangleRectangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(5, 0));
        squareRectangle = new Polygon(new Point(0, 0),
                new Point(5, 0),
                new Point(5, 5), new Point(0, 5)
        );
    }

    @Test
    public void testLoadCriticalPoints() {
        Decomposer decomposer = new Decomposer();

        ArrayList<CriticalPoint> criticalPoints = decomposer.getCriticalPoints(triangleRectangle);

        assertEquals(3, criticalPoints.size());
        assertEquals(2, criticalPoints.get(0).getEdges().size());
        assertEquals(triangleRectangle.borders.get(0), criticalPoints.get(0).getEdges().get(0));
        assertEquals(triangleRectangle.borders.get(2), criticalPoints.get(0).getEdges().get(1));
        assertEquals(2, criticalPoints.get(1).getEdges().size());
        assertEquals(triangleRectangle.borders.get(0), criticalPoints.get(1).getEdges().get(0));
        assertEquals(triangleRectangle.borders.get(1), criticalPoints.get(1).getEdges().get(1));
        assertEquals(2, criticalPoints.get(2).getEdges().size());
        assertEquals(triangleRectangle.borders.get(1), criticalPoints.get(2).getEdges().get(0));
        assertEquals(triangleRectangle.borders.get(2), criticalPoints.get(2).getEdges().get(1));
    }
}