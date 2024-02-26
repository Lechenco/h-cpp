package boustrophedon.provider.decomposer.Boustrophedon.Clippers;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiFunction;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class WeilerAthertonClippingAlgorithmTest {
    Polygon clippedPolygon;
    Polygon clippingPolygon;

    @Test
    public void testClip() {
        clippedPolygon = new Polygon(
                new Point(0, 0),
                new Point(0, 5),
                new Point(5, 5),
                new Point(5, 0)
        );
        clippingPolygon = new Polygon(
                new Point(-1, -1),
                new Point(-1, 4),
                new Point(4, 4),
                new Point(4, -1)
        );

        WeilerAthertonClippingAlgorithm algorithm = new WeilerAthertonClippingAlgorithm();

        IPolygon result = algorithm.execute(clippingPolygon, clippedPolygon);

        assertEquals(1, algorithm.getDirectionA());
        assertEquals(1, algorithm.getDirectionB());
        assertEquals(WeilerAthertonClippingAlgorithm.IntersectionStatus.ENTERING, algorithm.getStartStatus());

        assertEquals(4, result.getNumberOfPoints());
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(4, 0))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(0, 0))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(0, 4))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(4, 4))));
    }

    @Test
    public void testClip2() {
        clippedPolygon = new Polygon(
                new Point(0, 0),
                new Point(0, 5),
                new Point(5, 5),
                new Point(5, 0)
        );
        clippingPolygon = new Polygon(
                new Point(-1, 0),
                new Point(-1, 4),
                new Point(4, 4),
                new Point(4, 0)
        );

        WeilerAthertonClippingAlgorithm algorithm = new WeilerAthertonClippingAlgorithm();

        IPolygon result = algorithm.execute(clippingPolygon, clippedPolygon);

        assertEquals(1, algorithm.getDirectionA());
        assertEquals(1, algorithm.getDirectionB());
        assertEquals(WeilerAthertonClippingAlgorithm.IntersectionStatus.ENTERING, algorithm.getStartStatus());

        assertEquals(4, result.getNumberOfPoints());
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(4, 0))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(0, 0))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(0, 4))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(4, 4))));
    }
    @Test
    public void testClipOnEdge() {
        clippedPolygon = new Polygon(
                new Point(0, 0),
                new Point(0, 1),
                new Point(1, 1),
                new Point(1, 0)
        );
        clippingPolygon = new Polygon(
                new Point(0, 0),
                new Point(0, 1),
                new Point(0.5, 1),
                new Point(0.5, 0)
        );

        WeilerAthertonClippingAlgorithm algorithm = new WeilerAthertonClippingAlgorithm();

        IPolygon result = algorithm.execute(clippingPolygon, clippedPolygon);

        assertEquals(1, algorithm.getDirectionA());
        assertEquals(1, algorithm.getDirectionB());
        assertEquals(WeilerAthertonClippingAlgorithm.IntersectionStatus.ENTERING, algorithm.getStartStatus());

        assertEquals(4, result.getNumberOfPoints());
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(0, 0))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(0.5, 0))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(0, 1))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(0.5, 1))));
    }
    @Test
    public void testClipTriangle() {
        clippedPolygon = new Polygon(
                new Point(0, 0),
                new Point(0, 5),
                new Point(5, 5),
                new Point(5, 0)
        );
        clippingPolygon = new Polygon(
                new Point(2, 2),
                new Point(3, -1),
                new Point(1, -1)
        );

        WeilerAthertonClippingAlgorithm algorithm = new WeilerAthertonClippingAlgorithm();

        IPolygon result = algorithm.execute(clippingPolygon, clippedPolygon);

        assertEquals(1, algorithm.getDirectionA());
        assertEquals(1, algorithm.getDirectionB());
        assertEquals(WeilerAthertonClippingAlgorithm.IntersectionStatus.ENTERING, algorithm.getStartStatus());

        assertEquals(3, result.getNumberOfPoints());
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(2, 2))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(1.3333333, 0))));
        assertTrue(result.getPoints().stream().anyMatch( p -> p.equals(new Point(2.6666666, 0))));
    }
    @Test
    public void testDifferenceTriangle() {
        clippedPolygon = new Polygon(
                new Point(0, 0),
                new Point(0, 5),
                new Point(5, 5),
                new Point(5, 0)
        );
        clippingPolygon = new Polygon(
                new Point(2, 2),
                new Point(3, -1),
                new Point(1, -1)
        );

        WeilerAthertonClippingAlgorithm algorithm = new WeilerAthertonClippingAlgorithm();

        IPolygon result = algorithm.execute(clippingPolygon, clippedPolygon, WeilerAthertonClippingAlgorithm.WeilerAthertonModes.DIFFERENCE);

        assertEquals(-1, algorithm.getDirectionA());
        assertEquals(1, algorithm.getDirectionB());
        assertEquals(WeilerAthertonClippingAlgorithm.IntersectionStatus.LEAVING, algorithm.getStartStatus());

        assertEquals(7, result.getNumberOfPoints());
        ArrayList<IPoint> expectedPoints = new ArrayList<>(Arrays.asList(
                new Point(2, 2),
                new Point(1.3333333, 0),
                new Point(2.6666666, 0),
                new Point(0, 0),
                new Point(0, 5),
                new Point(5, 5),
                new Point(5, 0)
        ));
        includesAll(expectedPoints, result.getPoints());
    }

    @Test
    public void testCalcIntersectionEdge() {
        clippedPolygon = new Polygon(
                new Point(0, 0),
                new Point(0, 1),
                new Point(1, 1),
                new Point(1, 0)
        );
        clippingPolygon = new Polygon(
                new Point(0, 0),
                new Point(0, 1),
                new Point(0.5, 1),
                new Point(0.5, 0)
        );

        WeilerAthertonClippingAlgorithm algorithm = new WeilerAthertonClippingAlgorithm();

        BiFunction<Integer, Integer, IPoint> calcIntersection = (Integer AIndex, Integer BIndex) ->
            algorithm.calcIntersection(clippedPolygon.getBorders().get(BIndex).getFirstVertice(), clippedPolygon.getBorders().get(BIndex), clippingPolygon.getBorders().get(AIndex));


        assertEquals(clippingPolygon.getBorders().get(0).getSecondVertice(), calcIntersection.apply(0, 0));
        assertEquals(clippingPolygon.getBorders().get(0).getSecondVertice(), calcIntersection.apply(0, 1));
        assertNull(calcIntersection.apply(0, 2));
        assertNull(calcIntersection.apply(0, 3));

        assertNull(calcIntersection.apply(1, 0));
        assertEquals(clippingPolygon.getBorders().get(1).getSecondVertice(), calcIntersection.apply(1, 1));
        assertEquals(clippedPolygon.getBorders().get(2).getFirstVertice(), calcIntersection.apply(1, 2));
        assertNull(calcIntersection.apply(1, 3));

        assertNull(calcIntersection.apply(2, 0));
        assertNull(calcIntersection.apply(2, 1));
        assertNull(calcIntersection.apply(2, 2));
        assertEquals(clippingPolygon.getBorders().get(2).getSecondVertice(), calcIntersection.apply(2, 3));
    }

    private void includesAll(ArrayList<IPoint> expected, ArrayList<IPoint> actual) {
        for (Object object : expected) {
            assertTrue(actual.stream().anyMatch( p -> p.equals(object)));
        }
    }
}