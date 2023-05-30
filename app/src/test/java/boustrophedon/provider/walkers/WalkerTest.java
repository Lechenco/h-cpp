package boustrophedon.provider.walkers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.walkers.error.AngleOffLimitsException;
import boustrophedon.domain.walkers.model.WalkerConfig;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class WalkerTest {
    WalkerConfig config;
    Polygon triangleRectangle;
    @Before
    public void setUp() {
        config = new WalkerConfig();
        triangleRectangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(5, 0));
    }
    @Test
    public void testConstructor() {
        Walker walker = new Walker();

        assertNotNull(walker);
        assertNotNull(walker.getConfig());
        assertNotNull(walker.getPath());
    }
    @Test
    public void testConstructorWithConfig() {
        Walker walker = new Walker(config);

        assertNotNull(walker);
        assertEquals(config, walker.getConfig());
        assertNotNull(walker.getPath());
    }
    @Test
    public void testSetConfigMethod() {
        Walker walker = new Walker();

        walker.setConfig(config);

        assertEquals(config, walker.getConfig());
    }
    @Test
    public void testWalkToTheOtherSideMethodWithTriangleRectangle0Degrees() {
        Walker walker = new Walker();

        assertEquals(new Point(5, 0), walker.walkToTheOtherSide(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(1, 1), walker.walkToTheOtherSide(triangleRectangle, new Point(5, 1)));
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(5, 5)));
        // point outside triangle
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(5, -1)));
    }
    @Test
    public void testWalkToTheOtherSideMethodWithTriangleRectangle45Degrees() throws AngleOffLimitsException {
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 4));

        assertEquals(new Point(5, 5), walker.walkToTheOtherSide(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(4, 0), walker.walkToTheOtherSide(triangleRectangle, new Point(5, 1)));
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(5, 0)));
        // point outside triangle
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(5, -1)));
    }
    @Test
    public void testWalkToTheOtherSideMethodWithTriangleRectangle90Degrees() throws AngleOffLimitsException {
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 2));

        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(5, 5), walker.walkToTheOtherSide(triangleRectangle, new Point(5, 1)));
        assertEquals(new Point(2, 0), walker.walkToTheOtherSide(triangleRectangle, new Point(2, 2)));
        // point outside triangle
        assertNull(walker.walkToTheOtherSide(triangleRectangle, new Point(4, -1)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle90Degrees() throws AngleOffLimitsException {
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 2));
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to X
        walker.goal = new Point(5, 5);
        walker.directionStartToGoal = Math.PI / 4;
        double distance = walker.getConfig().getDistanceBetweenPaths();

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
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 2));
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to X
        walker.goal = new Point(5, 0);
        walker.directionStartToGoal = 0;
        double distance = walker.getConfig().getDistanceBetweenPaths();

        assertEquals(new Point(distance, 0), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(2 + distance, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));
        assertEquals(new Point(4.9 + distance, 0), walker.walkAside(triangleRectangle, new Point(4.9, 0)));

        walker.currentWall = triangleRectangle.borders.get(0); // identity
        assertEquals(new Point(distance, distance), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(1 + distance, 1 + distance), walker.walkAside(triangleRectangle, new Point(1, 1)));
        assertEquals(new Point(4.9 + distance, 4.9 + distance), walker.walkAside(triangleRectangle, new Point(4.9, 4.9)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle0Degrees() throws AngleOffLimitsException {
        Walker walker = new Walker(new WalkerConfig(0.00009, 0));
        walker.currentWall = triangleRectangle.borders.get(0); // identity
        walker.goal = new Point(5, 5);
        walker.directionStartToGoal = Math.PI / 4;
        double distance = walker.getConfig().getDistanceBetweenPaths();

        assertEquals(new Point(distance, distance), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(1 + distance, 1 + distance), walker.walkAside(triangleRectangle, new Point(1, 1)));
        assertEquals(new Point(4.9 + distance, 4.9 + distance), walker.walkAside(triangleRectangle, new Point(4.9, 4.9)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, distance), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(5, 3+distance), walker.walkAside(triangleRectangle, new Point(5, 3)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle0Degrees2() throws AngleOffLimitsException {
        Walker walker = new Walker(new WalkerConfig(0.00009, 0));
        walker.currentWall = triangleRectangle.borders.get(0); // identity
        walker.goal = new Point(0, 0);
        walker.directionStartToGoal = Math.PI / 4 + Math.PI;
        double distance = walker.getConfig().getDistanceBetweenPaths();

        assertEquals(new Point(5 - distance, 5 - distance), walker.walkAside(triangleRectangle, new Point(5, 5)));
        assertEquals(new Point(1 - distance, 1 - distance), walker.walkAside(triangleRectangle, new Point(1, 1)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, 1-distance), walker.walkAside(triangleRectangle, new Point(5, 1)));
        assertEquals(new Point(5, 5-distance), walker.walkAside(triangleRectangle, new Point(5, 5)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle45Degrees() throws AngleOffLimitsException {
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 4));
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to x
        walker.goal = new Point(5, 0);
        walker.directionStartToGoal = 0;
        double distance = walker.getConfig().getDistanceBetweenPaths();
        double distanceAngled = distance / Math.sin(Math.PI / 4);

        assertEquals(new Point(distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(0, 0)));
        assertEquals(new Point(2 + distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));
        assertEquals(new Point(4.9 + distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(4.9, 0)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(5, 3 + distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 3)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle45Degrees2() throws AngleOffLimitsException {
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 4));
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to x
        walker.goal = new Point(0, 0);
        walker.directionStartToGoal = Math.PI;
        double distance = walker.getConfig().getDistanceBetweenPaths();
        double distanceAngled = distance / Math.sin(Math.PI / 4);

        assertEquals(new Point(5 - distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(2 - distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(5, 3 + distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 3)));
    }
    @Test
    public void testWalkAsideWithTriangleRectangle45Degrees3() throws AngleOffLimitsException {
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 4));
        walker.currentWall = triangleRectangle.borders.get(2); // parallel to x
        walker.goal = new Point(5, 5);
        walker.directionStartToGoal = Math.PI / 2;
        double distance = walker.getConfig().getDistanceBetweenPaths();
        double distanceAngled = distance / Math.sin(Math.PI / 4);

        assertEquals(new Point(5 - distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(2 - distanceAngled, 0), walker.walkAside(triangleRectangle, new Point(2, 0)));

        walker.currentWall = triangleRectangle.borders.get(1); // parallel to y
        assertEquals(new Point(5, distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 0)));
        assertEquals(new Point(5, 3 + distanceAngled), walker.walkAside(triangleRectangle, new Point(5, 3)));
    }
    @Test
    public void testWalkAsideWithInverseTriangleRectangle45Degrees() throws AngleOffLimitsException {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 4));
        walker.currentWall = triangle.borders.get(2); // parallel to x
        walker.goal = new Point(-5, -5);
        walker.directionStartToGoal = - Math.PI / 4;
        double distance = walker.getConfig().getDistanceBetweenPaths();
        double distanceAngled = distance / Math.sin(Math.PI / 4);

        assertEquals(new Point(-5 + distanceAngled, 0), walker.walkAside(triangle, new Point(-5, 0)));
        assertEquals(new Point(-2 + distanceAngled, 0), walker.walkAside(triangle, new Point(-2, 0)));

        walker.currentWall = triangle.borders.get(1); // parallel to y
        assertEquals(new Point(-5, - distanceAngled), walker.walkAside(triangle, new Point(-5, 0)));
        assertEquals(new Point(-5, -3 - distanceAngled), walker.walkAside(triangle, new Point(-5, -3)));
    }
    @Test
    public void testWalkAsideWithInverseTriangleRectangle45Degrees2() throws AngleOffLimitsException {
        Polygon triangle = new Polygon(new Point(0, 0), new Point(-5, -5), new Point(-5, 0));
        Walker walker = new Walker(new WalkerConfig(0.00009, Math.PI / 4));
        walker.currentWall = triangle.borders.get(2); // parallel to x
        walker.goal = new Point(-5, -5);
        walker.directionStartToGoal = - Math.PI / 2;
        double distance = walker.getConfig().getDistanceBetweenPaths();
        double distanceAngled = distance / Math.sin(Math.PI / 4);

        assertEquals(new Point(-distanceAngled, 0), walker.walkAside(triangle, new Point(0, 0)));
        assertEquals(new Point(-2 - distanceAngled, 0), walker.walkAside(triangle, new Point(-2, 0)));

        walker.currentWall = triangle.borders.get(1); // parallel to y
        assertEquals(new Point(-5, - distanceAngled), walker.walkAside(triangle, new Point(-5, 0)));
        assertEquals(new Point(-5, -3 - distanceAngled), walker.walkAside(triangle, new Point(-5, -3)));
    }
}