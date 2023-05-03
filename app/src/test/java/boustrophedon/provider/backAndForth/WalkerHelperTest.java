package boustrophedon.provider.backAndForth;

import static org.junit.Assert.*;

import com.github.javafaker.Faker;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class WalkerHelperTest {
    Faker faker;
    Polygon triangleRetangle;

    @Before
    public void setUp() {
        this.faker = new Faker();
        triangleRetangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(0, 5));
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
        ArrayList<IBorder> borders = WalkerHelper.getPolygonBorders(triangleRetangle);

        ArrayList<IBorder> walls = WalkerHelper.findWalls(borders, 0);

        assertEquals(2, walls.size());
        assertEquals(borders.get(0), walls.get(0));
        assertEquals(borders.get(2), walls.get(1));
    }
    @Test
    public void testFindWalls90() {
        ArrayList<IBorder> borders = WalkerHelper.getPolygonBorders(triangleRetangle);

        ArrayList<IBorder> walls = WalkerHelper.findWalls(borders, Math.PI / 2);

        assertEquals(2, walls.size());
        assertEquals(borders.get(0), walls.get(0));
        assertEquals(borders.get(1), walls.get(1));
    }
    @Test
    public void testFindWalls180() {
        ArrayList<IBorder> borders = WalkerHelper.getPolygonBorders(triangleRetangle);

        ArrayList<IBorder> walls = WalkerHelper.findWalls(borders, Math.PI);

        assertEquals(2, walls.size());
        assertEquals(borders.get(0), walls.get(0));
        assertEquals(borders.get(2), walls.get(1));
    }

    @Test
    public void testCalcIntersection() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5,0));
        IBorder wall = new Border(new Point(0, 0), new Point(5,5));

        assertEquals(new Point(0, 0), WalkerHelper.calcIntersection(new Point(0,0), refBorder, wall));
        assertEquals(new Point(0, 0), WalkerHelper.calcIntersection(new Point(2,0), refBorder, wall));
        assertEquals(new Point(1, 1), WalkerHelper.calcIntersection(new Point(2,1), refBorder, wall));
        assertEquals(new Point(2, 2), WalkerHelper.calcIntersection(new Point(1,2), refBorder, wall));
    }
    @Test
    public void testCalcIntersectionInverse() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5,0));
        IBorder wall = new Border(new Point(5, 5), new Point(0,0));

        assertEquals(new Point(0, 0), WalkerHelper.calcIntersection(new Point(0,0), refBorder, wall));
        assertEquals(new Point(0, 0), WalkerHelper.calcIntersection(new Point(2,0), refBorder, wall));
        assertEquals(new Point(1, 1), WalkerHelper.calcIntersection(new Point(2,1), refBorder, wall));
        assertEquals(new Point(2, 2), WalkerHelper.calcIntersection(new Point(1,2), refBorder, wall));
    }
    @Test
    public void testCalcIntersectionNegative() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5,0));
        IBorder wall = new Border(new Point(-5, -5), new Point(0,0));

        assertEquals(new Point(0, 0), WalkerHelper.calcIntersection(new Point(0,0), refBorder, wall));
        assertEquals(new Point(0, 0), WalkerHelper.calcIntersection(new Point(-2,0), refBorder, wall));
        assertEquals(new Point(-1, -1), WalkerHelper.calcIntersection(new Point(-2,-1), refBorder, wall));
        assertEquals(new Point(-2, -2), WalkerHelper.calcIntersection(new Point(-1,-2), refBorder, wall));
    }
    @Test
    public void testCalcIntersectionParallel() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5,0));
        IBorder wall = new Border(new Point(5, 10), new Point(2,10));

        assertEquals(new Point(0, 10), WalkerHelper.calcIntersection(new Point(0,0), refBorder, wall));
        assertEquals(new Point(5, 10), WalkerHelper.calcIntersection(new Point(5,1), refBorder, wall));
    }
}