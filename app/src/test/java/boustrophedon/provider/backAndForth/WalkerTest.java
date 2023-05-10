package boustrophedon.provider.backAndForth;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import boustrophedon.domain.backAndForth.model.WalkerConfig;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class WalkerTest {
    WalkerConfig config;
    Polygon triangleRetangle;
    @Before
    public void setUp() {
        config = new WalkerConfig();
        triangleRetangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(0, 5));
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
    public void testWalkToTheOtherSideMethodWithTriangleRectangle() {
        Walker walker = new Walker();
        Point currentPoint = new Point(0, 0);

        IPoint point = walker.walkToTheOtherSide(triangleRetangle, currentPoint);

        assertEquals(new Point(0, 5), point);
    }
}