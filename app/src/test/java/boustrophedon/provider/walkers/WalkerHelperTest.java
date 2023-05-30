package boustrophedon.provider.walkers;

import static org.junit.Assert.*;

import com.github.javafaker.Faker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class WalkerHelperTest {
    private static final double DOUBLE_DELTA = 0.00001;
    double NINETY_DEGREES = Math.PI / 2;
    double FORTY_FIVE_DEGREES = Math.PI / 4;
    Faker faker;
    Polygon triangleRectangle;

    @Before
    public void setUp() {
        this.faker = new Faker();
        triangleRectangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(5, 0));
    }

    @Test
    public void testGetPolygonBorders() {
        IPolygon polygon = new Polygon(
                new Point(faker.random().nextDouble(), faker.random().nextDouble()),
                new Point(faker.random().nextDouble(), faker.random().nextDouble()),
                new Point(faker.random().nextDouble(), faker.random().nextDouble())
        );

        ArrayList<IBorder> borders = WalkerHelper.getPolygonBorders(polygon);

        assertEquals(polygon.getNumberOfPoints(), borders.size());
    }

    @Test
    public void testFindWalls() {
        ArrayList<IBorder> borders = WalkerHelper.getPolygonBorders(triangleRectangle);

        ArrayList<IBorder> walls = WalkerHelper.findWalls(borders, 0);

        assertEquals(2, walls.size());
        assertEquals(borders.get(0), walls.get(0));
        assertEquals(borders.get(1), walls.get(1));
    }

    @Test
    public void testFindWalls90() {
        ArrayList<IBorder> borders = WalkerHelper.getPolygonBorders(triangleRectangle);

        ArrayList<IBorder> walls = WalkerHelper.findWalls(borders, Math.PI / 2);

        assertEquals(2, walls.size());
        assertEquals(borders.get(0), walls.get(0));
        assertEquals(borders.get(2), walls.get(1));
    }

    @Test
    public void testFindWalls180() {
        ArrayList<IBorder> borders = WalkerHelper.getPolygonBorders(triangleRectangle);

        ArrayList<IBorder> walls = WalkerHelper.findWalls(borders, Math.PI);

        assertEquals(2, walls.size());
        assertEquals(borders.get(0), walls.get(0));
        assertEquals(borders.get(1), walls.get(1));
    }


    @Test
    public void testCalcIntersectionToWallOnBorder() {
        IBorder wall = new Border(new Point(0, 0), new Point(5, 5));

        assertEquals(new Point(0, 0), WalkerHelper.calcIntersectionToWall(new Point(0, 0), wall, 0));
        assertEquals(new Point(1, 1), WalkerHelper.calcIntersectionToWall(new Point(1, 1), wall, FORTY_FIVE_DEGREES));
        assertEquals(new Point(0.0000009, 0), WalkerHelper.calcIntersectionToWall(new Point(0.0000009, 0), wall, NINETY_DEGREES));

        assertEquals(new Point(6, 6), WalkerHelper.calcIntersectionToWall(new Point(6, 6), wall, FORTY_FIVE_DEGREES));
    }
    @Test
    public void testCalcIntersectionToWall() {
        IBorder wall = new Border(new Point(0, 0), new Point(5, 5));

        assertEquals(new Point(3, 3), WalkerHelper.calcIntersectionToWall(new Point(0, 3), wall, 0));
        assertEquals(new Point(5, 5), WalkerHelper.calcIntersectionToWall(new Point(1, 5), wall, 0));
        assertEquals(new Point(7, 7), WalkerHelper.calcIntersectionToWall(new Point(0.0000009, 7), wall, 0));
    }
    @Test
    public void testCalcIntersectionToWallAngle45() {
        IBorder wall = new Border(new Point(0, 5), new Point(5, 5));

        assertEquals(5, WalkerHelper.calcIntersectionToWall(new Point(0, 0), wall, FORTY_FIVE_DEGREES).getX(),DOUBLE_DELTA );
        assertEquals(3, WalkerHelper.calcIntersectionToWall(new Point(1, 3), wall, FORTY_FIVE_DEGREES).getX(), DOUBLE_DELTA);
        assertEquals(1, WalkerHelper.calcIntersectionToWall(new Point(3, 7), wall, FORTY_FIVE_DEGREES).getX(), DOUBLE_DELTA);
    }
    @Test
    public void testCalcIntersectionToWallAngle90() {
        IBorder wall = new Border(new Point(0, 5), new Point(5, 5));

        assertEquals(0, WalkerHelper.calcIntersectionToWall(new Point(0, 0), wall, NINETY_DEGREES).getX(),DOUBLE_DELTA );
        assertEquals(1, WalkerHelper.calcIntersectionToWall(new Point(1, 3), wall, NINETY_DEGREES).getX(), DOUBLE_DELTA);
        assertEquals(3, WalkerHelper.calcIntersectionToWall(new Point(3, 7), wall, NINETY_DEGREES).getX(), DOUBLE_DELTA);
    }
    @Test
    public void testCalcIntersectionToWallWithWall90() {
        IBorder wall = new Border(new Point(5, 5), new Point(5, 0));

        assertEquals(5, WalkerHelper.calcIntersectionToWall(new Point(0, 0), wall, FORTY_FIVE_DEGREES).getY(),DOUBLE_DELTA );
        assertEquals(7, WalkerHelper.calcIntersectionToWall(new Point(1, 3), wall, FORTY_FIVE_DEGREES).getY(), DOUBLE_DELTA);
        assertEquals(1, WalkerHelper.calcIntersectionToWall(new Point(7, 3), wall, FORTY_FIVE_DEGREES).getY(), DOUBLE_DELTA);
    }
    @Test
    public void testIsPointInsidePolygon() {
        // vertice
        assertTrue(WalkerHelper.isPointInsidePolygon(new Point(0,0), triangleRectangle));
        // on Border
        assertTrue(WalkerHelper.isPointInsidePolygon(new Point(3,0), triangleRectangle));
        // inside
        assertTrue(WalkerHelper.isPointInsidePolygon(new Point(2,1), triangleRectangle));
        // outside
        assertFalse(WalkerHelper.isPointInsidePolygon(new Point(-1,0), triangleRectangle));
    }
}