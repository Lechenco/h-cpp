package boustrophedon.provider.walkers;

import static org.junit.Assert.*;

import static boustrophedon.constants.AngleConstants.FORTY_FIVE_DEGREES;
import static boustrophedon.constants.AngleConstants.HUNDRED_AND_EIGHTY_DEGREES;
import static boustrophedon.constants.AngleConstants.HUNDRED_AND_TWENTY_DEGREES;
import static boustrophedon.constants.AngleConstants.NINETY_DEGREES;
import static boustrophedon.constants.AngleConstants.ZERO_DEGREES;

import org.junit.Before;
import org.junit.Test;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.walkers.error.AngleOffLimitsException;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class WalkerTest {
    static final double DISTANCE_VALUE = 0.00009;
    Polygon triangleRectangle;
    @Before
    public void setUp() {
        triangleRectangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(5, 0));
    }
    @Test
    public void testConstructor() {
        Walker walker = new Walker.WalkerBuilder().build();

        assertNotNull(walker);
        assertNotNull(walker.getPath());
    }
    @Test
    public void testWalkToTheOtherSideMethodWithTriangleRectangle0Degrees() {
        Walker walker = new Walker.WalkerBuilder().build();

        assertEquals(new Point(5, 0), walker.walkToTheOtherSide(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(1, 1), walker.walkToTheOtherSide(triangleRectangle, new Point(5, 1)));
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(5, 5)));
        // point outside triangle
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(5, -1)));
    }
    @Test
    public void testWalkToTheOtherSideMethodWithTriangleRectangle45Degrees() throws AngleOffLimitsException {
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(DISTANCE_VALUE).atDirection(FORTY_FIVE_DEGREES).build();

        assertEquals(new Point(5, 5), walker.walkToTheOtherSide(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(4, 0), walker.walkToTheOtherSide(triangleRectangle, new Point(5, 1)));
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(5, 0)));
        // point outside triangle
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(5, -1)));
    }
    @Test
    public void testWalkToTheOtherSideMethodWithTriangleRectangle90Degrees() throws AngleOffLimitsException {
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(DISTANCE_VALUE).atDirection(NINETY_DEGREES).build();

        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(5, 5), walker.walkToTheOtherSide(triangleRectangle, new Point(5, 1)));
        assertEquals(new Point(2, 0), walker.walkToTheOtherSide(triangleRectangle, new Point(2, 2)));
        // point outside triangle
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(4, -1)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle90Degrees() throws AngleOffLimitsException {
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(DISTANCE_VALUE).atDirection(NINETY_DEGREES).build();
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to X
        walker.goal = new Point(5, 5);
        walker.directionStartToGoal = FORTY_FIVE_DEGREES;
        double distance = DISTANCE_VALUE;

        assertEquals(new Point(distance, 0), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(2 + distance, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));
        assertEquals(new Point(4.9 + distance, 0), walker.walkAside(triangleRectangle, new Point(4.9, 0)));

        walker.currentWall = triangleRectangle.borders.get(0); // identity
        assertEquals(new Point(distance, distance), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(1 + distance, 1 + distance), walker.walkAside(triangleRectangle, new Point(1, 1)));
        assertEquals(new Point(4.9 + distance, 4.9 + distance), walker.walkAside(triangleRectangle, new Point(4.9, 4.9)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle90Degrees2() throws AngleOffLimitsException {
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(DISTANCE_VALUE).atDirection(NINETY_DEGREES).build();
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to X
        walker.goal = new Point(5, 0);
        walker.directionStartToGoal = ZERO_DEGREES;
        double distance = DISTANCE_VALUE;

        assertEquals(new Point(distance, 0), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(2 + distance, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));
        assertEquals(new Point(4.9 + distance, 0), walker.walkAside(triangleRectangle, new Point(4.9, 0)));

        walker.currentWall = triangleRectangle.borders.get(0); // identity
        assertEquals(new Point(distance, distance), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(1 + distance, 1 + distance), walker.walkAside(triangleRectangle, new Point(1, 1)));
        assertEquals(new Point(4.9 + distance, 4.9 + distance), walker.walkAside(triangleRectangle, new Point(4.9, 4.9)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle0Degrees() {
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(DISTANCE_VALUE).build();
        walker.currentWall = triangleRectangle.borders.get(0); // identity
        walker.goal = new Point(5, 5);
        walker.directionStartToGoal = FORTY_FIVE_DEGREES;
        double distance = DISTANCE_VALUE;

        assertEquals(new Point(distance, distance), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(1 + distance, 1 + distance), walker.walkAside(triangleRectangle, new Point(1, 1)));
        assertEquals(new Point(4.9 + distance, 4.9 + distance), walker.walkAside(triangleRectangle, new Point(4.9, 4.9)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, distance), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(5, 3+distance), walker.walkAside(triangleRectangle, new Point(5, 3)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle0Degrees2() {
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(DISTANCE_VALUE).build();
        walker.currentWall = triangleRectangle.borders.get(0); // identity
        walker.goal = new Point(0, 0);
        walker.directionStartToGoal = FORTY_FIVE_DEGREES + HUNDRED_AND_EIGHTY_DEGREES;
        double distance = DISTANCE_VALUE;

        assertEquals(new Point(5 - distance, 5 - distance), walker.walkAside(triangleRectangle, new Point(5, 5)));
        assertEquals(new Point(1 - distance, 1 - distance), walker.walkAside(triangleRectangle, new Point(1, 1)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, 1-distance), walker.walkAside(triangleRectangle, new Point(5, 1)));
        assertEquals(new Point(5, 5-distance), walker.walkAside(triangleRectangle, new Point(5, 5)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle45Degrees() throws AngleOffLimitsException {
        double distance = DISTANCE_VALUE;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).atDirection(FORTY_FIVE_DEGREES).build();
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to x
        walker.start = new Point(0, 0);
        walker.goal = new Point(5, 0);
        walker.directionStartToGoal = 0;
        double distanceAngled = distance / Math.sin(FORTY_FIVE_DEGREES);

        assertEquals(new Point(distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(2 + distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));
        assertEquals(new Point(4.9 + distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(4.9, 0)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, 5 - distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 5)));
        assertEquals(new Point(5, 3 - distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 3)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle45Degrees2() throws AngleOffLimitsException {
        double distance = DISTANCE_VALUE;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).atDirection(FORTY_FIVE_DEGREES).build();
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to x
        walker.goal = new Point(0, 0);
        walker.directionStartToGoal = HUNDRED_AND_EIGHTY_DEGREES;
        double distanceAngled = distance / Math.sin(FORTY_FIVE_DEGREES);

        assertEquals(new Point(5 - distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(2 - distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(5, 3 + distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 3)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle45Degrees3() throws AngleOffLimitsException {
        double distance = DISTANCE_VALUE;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).atDirection(FORTY_FIVE_DEGREES).build();
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to x
        walker.goal = new Point(5, 5);
        walker.directionStartToGoal = NINETY_DEGREES;
        double distanceAngled = distance / Math.sin(FORTY_FIVE_DEGREES);

        assertEquals(new Point(5 - distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(2 - distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(5, 3 + distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 3)));
    }
    @Test
    public void testWalkAsideWithInverseTriangleRectangle45Degrees() throws AngleOffLimitsException {
        double distance = DISTANCE_VALUE;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).atDirection(FORTY_FIVE_DEGREES).build();
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        walker.currentWall = triangle.borders.get(2); // parallel to x
        walker.goal = new Point(-5, -5);
        walker.directionStartToGoal = - FORTY_FIVE_DEGREES;
        double distanceAngled = distance / Math.sin(FORTY_FIVE_DEGREES);

        assertEquals(new Point(-5 + distanceAngled, 0), walker.walkAside(triangle, new Point(-5, 0)));
        assertEquals(new Point(-2 + distanceAngled, 0), walker.walkAside(triangle, new Point(-2, 0)));

        walker.currentWall = triangle.borders.get(1); // parallel to y
        assertEquals(new Point(-5, - distanceAngled), walker.walkAside(triangle, new Point(-5, 0)));
        assertEquals(new Point(-5, -3 - distanceAngled), walker.walkAside(triangle, new Point(-5, -3)));
    }
    @Test
    public void testWalkAsideWithInverseTriangleRectangle45Degrees2() throws AngleOffLimitsException {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        double distance = DISTANCE_VALUE;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).atDirection(FORTY_FIVE_DEGREES).build();
        walker.currentWall = triangle.borders.get(2); // parallel to x
        walker.goal = new Point(-5, -5);
        walker.directionStartToGoal = - NINETY_DEGREES;
        double distanceAngled = distance / Math.sin(FORTY_FIVE_DEGREES);

        assertEquals(new Point(-5 + distanceAngled, 0), walker.walkAside(triangle, new Point(-5, 0)));
        assertEquals(new Point(-2 + distanceAngled, 0), walker.walkAside(triangle, new Point(-2, 0)));

        walker.currentWall = triangle.borders.get(1); // parallel to y
        assertEquals(new Point(-5, - distanceAngled), walker.walkAside(triangle, new Point(-5, 0)));
        assertEquals(new Point(-5, -3 - distanceAngled), walker.walkAside(triangle, new Point(-5, -3)));
    } @Test
    public void testWalkAsideWithInverseTriangleRectangle45Degrees3() {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(-5, 5));
        double distance = DISTANCE_VALUE;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).build();
        walker.currentWall = triangle.borders.get(2);
        walker.goal = new Point(-5, 5);
        walker.directionStartToGoal = HUNDRED_AND_TWENTY_DEGREES;

        assertEquals(new Point(-distance, distance), walker.walkAside(triangle, new Point(0, 0)));
        assertEquals(new Point(-2 - distance, 2 + distance), walker.walkAside(triangle, new Point(-2, 2)));

        walker.currentWall = triangle.borders.get(0); // parallel to y
        assertEquals(new Point(distance, distance), walker.walkAside(triangle, new Point(0, 0)));
        assertEquals(new Point(3 + distance, 3 + distance), walker.walkAside(triangle, new Point(3, 3)));
    }
    @Test
    public void testWalkAsideAndValidate() {
        Polygon polygon = new Polygon(
                new Point(0, 0), new Point(5, 0),
                new Point(5, 3), new Point(1, 3),
                new Point(0, 1.5)
        );
        double distance = 1;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).build();
        walker.setPolygon(polygon);
        walker.currentWall = polygon.borders.get(4);
        walker.goal = new Point(5, 3);
        walker.directionStartToGoal = NINETY_DEGREES;

        assertEquals(new Point(1.0 / 3, 2), walker.walkAsideAndValidate(new Point(0, 1)));
    }
    @Test
    public void testSetPolygon() {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        Walker walker = new Walker.WalkerBuilder().build();

        walker.setPolygon(triangle);

        assertEquals(triangle, walker.polygon);
        assertEquals(triangle.borders.size(), walker.polygonBorders.size());
    }

    @Test
    public void testCalcStartpoint() {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        Walker walker = new Walker.WalkerBuilder().build();

        walker.setPolygon(triangle);

        assertEquals(new Point(0, 0), walker.calcStartPoint(new Point(-1, -1)));
    }

    @Test
    public void testCalcGoal() {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(DISTANCE_VALUE).build();

        walker.setPolygon(triangle);

        walker.calcGoal(new Point(0, 0));
        assertEquals(new Point(-5, -5), walker.goal);
        assertEquals(-HUNDRED_AND_TWENTY_DEGREES, walker.directionStartToGoal, 0.00001);

        walker.calcGoal(new Point(-5, -5));
        assertEquals(new Point(0, 0), walker.goal);
        assertEquals(FORTY_FIVE_DEGREES, walker.directionStartToGoal, 0.00001);
    }

    @Test
    public void testGeneratePath()  {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        double distance = 1;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).build();

        walker.setPolygon(triangle);
        walker.walk(new Point(0, 0));

        assertEquals(11, walker.getPath().getNumberOfPoints());
    }
    @Test
    public void testGeneratePath2()  {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        double distance = 1;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).build();

        walker.setPolygon(triangle);
        walker.walk(new Point(-5, -5));

        assertEquals(11, walker.getPath().getNumberOfPoints());
    }
    @Test
    public void testGeneratePathSquare()  {
        Polygon square = new Polygon(new Point(0, 0),
                new Point(5, 0),
                new Point(5, 5), new Point(0, 5)
        );
        double distance = 1;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).build();

        walker.setPolygon(square);
        walker.walk(new Point(0, 0));

        assertEquals(12, walker.getPath().getNumberOfPoints());
    }
    @Test
    public void testGeneratePathSquare2() throws AngleOffLimitsException {
        Polygon square = new Polygon(new Point(0, 0),
                new Point(2, 2),
                new Point(0, 4), new Point(-2, 2)
        );
        double distance = 1;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).atDirection(FORTY_FIVE_DEGREES).build();

        walker.setPolygon(square);
        walker.walk(new Point(0, 0));

        assertEquals(7, walker.getPath().getNumberOfPoints());
    }
    @Test
    public void testGeneratePathTrapezoid()  {
        Polygon square = new Polygon(new Point(-1, 0),
                new Point(5, 0),
                new Point(5, 5), new Point(0, 5)
        );
        double distance = 1;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).build();

        walker.setPolygon(square);
        walker.walk(new Point(-1, 0));

        assertEquals(12, walker.getPath().getNumberOfPoints());
    }
    @Test
    public void testGeneratePathTrapezoid2() throws AngleOffLimitsException {
        Polygon square = new Polygon(new Point(-1, 0),
                new Point(5, 0),
                new Point(5, 5), new Point(0, 5)
        );
        double distance = 1;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).atDirection(NINETY_DEGREES).build();

        walker.setPolygon(square);
        walker.walk(new Point(-1, 0));

        assertEquals(13, walker.getPath().getNumberOfPoints());
    }

    @Test
    public void testGeneratePathTrapezoid3() {
        Polygon polygon = new Polygon(
                new Point(0, 0), new Point(5, 0),
                new Point(5, 3), new Point(1, 3),
                new Point(0, 1.5)
        );
        double distance = 1;
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(distance).build();

        walker.setPolygon(polygon);
        walker.walk(new Point(0, 0));

        assertEquals(8, walker.getPath().getNumberOfPoints());
    }

    @Test
    public void testGeneratePathPolygon() throws AngleOffLimitsException {
        Polygon polygon = new Polygon(
                new Point(-23.204948 ,-50.643659 ),
                new Point(-23.207448,-50.646159 ),
                new Point(-23.209948,-50.646159 ),
                new Point(-23.209948,-50.638659 ),
                new Point(-23.209948,-50.631159 ),
                new Point(-23.204948,-50.631159)
        );

        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(6.0E-4).atDirection(NINETY_DEGREES).build();

        walker.setPolygon(polygon);
        walker.walk(new Point(-23.210048, -50.638759 ));

        assertEquals(18, walker.getPath().getNumberOfPoints());
    }
    @Test
    public void testGeneratePathPolygon2() throws AngleOffLimitsException {
        Polygon polygon = new Polygon(
                new Point(-23.204948,-50.643659),
                new Point(-23.207448,-50.646159),
                new Point(-23.209948,-50.646159 ),
                new Point(-23.209948,-50.638659),
                new Point(-23.209948,-50.631159 ),
                new Point(-23.204948,-50.631159 )
        );
        Walker walker = new Walker.WalkerBuilder().withDistanceBetweenPaths(6.0E-4).atDirection(NINETY_DEGREES).build();
        IPoint p = new Point(-23.207548, -50.646159);
        walker.setPolygon(polygon);

        walker.currentWall = polygon.borders.get(1);
        walker.goal = new Point(5, 3);
        walker.directionStartToGoal = -0.7853981633978036;

        assertEquals(new Point(-23.206948, -50.645659 ), walker.walkAsideAndValidate(p));
    }
}