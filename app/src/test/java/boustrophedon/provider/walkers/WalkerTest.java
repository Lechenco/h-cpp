package boustrophedon.provider.walkers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
}