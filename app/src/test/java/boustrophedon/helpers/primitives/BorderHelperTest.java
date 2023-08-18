package boustrophedon.helpers.primitives;

import static org.junit.Assert.*;

import com.github.javafaker.Faker;

import org.junit.Before;
import org.junit.Test;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;

public class BorderHelperTest {
    Faker faker;

    @Before
    public void setUp() {
        this.faker = new Faker();
    }
    @Test
    public void testCalcIntersection() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5, 0));
        IBorder wall = new Border(new Point(0, 0), new Point(5, 5));

        assertEquals(new Point(0, 0), BorderHelper.calcIntersection(new Point(0, 0), refBorder, wall));
        assertEquals(new Point(0, 0), BorderHelper.calcIntersection(new Point(2, 0), refBorder, wall));
        assertEquals(new Point(1, 1), BorderHelper.calcIntersection(new Point(2, 1), refBorder, wall));
        assertEquals(new Point(2, 2), BorderHelper.calcIntersection(new Point(1, 2), refBorder, wall));
    }

    @Test
    public void testCalcIntersectionInverse() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5, 0));
        IBorder wall = new Border(new Point(5, 5), new Point(0, 0));

        assertEquals(new Point(0, 0), BorderHelper.calcIntersection(new Point(0, 0), refBorder, wall));
        assertEquals(new Point(0, 0), BorderHelper.calcIntersection(new Point(2, 0), refBorder, wall));
        assertEquals(new Point(1, 1), BorderHelper.calcIntersection(new Point(2, 1), refBorder, wall));
        assertEquals(new Point(2, 2), BorderHelper.calcIntersection(new Point(1, 2), refBorder, wall));
    }

    @Test
    public void testCalcIntersectionNegative() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5, 0));
        IBorder wall = new Border(new Point(-5, -5), new Point(0, 0));

        assertEquals(new Point(0, 0), BorderHelper.calcIntersection(new Point(0, 0), refBorder, wall));
        assertEquals(new Point(0, 0), BorderHelper.calcIntersection(new Point(-2, 0), refBorder, wall));
        assertEquals(new Point(-1, -1), BorderHelper.calcIntersection(new Point(-2, -1), refBorder, wall));
        assertEquals(new Point(-2, -2), BorderHelper.calcIntersection(new Point(-1, -2), refBorder, wall));
    }

    @Test
    public void testCalcIntersectionParallel() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5, 0));
        IBorder wall = new Border(new Point(5, 10), new Point(2, 10));

        assertEquals(new Point(0, 10), BorderHelper.calcIntersection(new Point(0, 0), refBorder, wall));
        assertEquals(new Point(5, 10), BorderHelper.calcIntersection(new Point(5, 1), refBorder, wall));
    }

    @Test
    public void testCalcIntersectionWithRefParallelToY() {
        IBorder wall = new Border(new Point(5, 5), new Point(0, 0));

        assertEquals(new Point(0, 0), BorderHelper.calcIntersectionWithRefParallelToY(new Point(0, 0), wall));
        assertEquals(new Point(0, 0), BorderHelper.calcIntersectionWithRefParallelToY(new Point(0, 5), wall));
        assertEquals(new Point(5, 5), BorderHelper.calcIntersectionWithRefParallelToY(new Point(5, 10), wall));
        assertEquals(new Point(2, 2), BorderHelper.calcIntersectionWithRefParallelToY(new Point(2, -3), wall));
    }

    @Test
    public void testCalcIntersectionWithRefParallelToYParallel() {
        IBorder wall = new Border(new Point(0, 5), new Point(0, 0));

        assertEquals(new Point(0, 0), BorderHelper.calcIntersectionWithRefParallelToY(new Point(0, 0), wall));
        assertEquals(new Point(0, 5), BorderHelper.calcIntersectionWithRefParallelToY(new Point(5, 5), wall));
        assertEquals(new Point(0, 10), BorderHelper.calcIntersectionWithRefParallelToY(new Point(5, 10), wall));
        assertEquals(new Point(0, -3), BorderHelper.calcIntersectionWithRefParallelToY(new Point(2, -3), wall));
    }

    @Test
    public void testCalcIntersectionWithWallParallelToY() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(5, 0));
        IBorder wall = new Border(new Point(1.5, 5), new Point(1.5, 0));

        assertEquals(new Point(1.5, 0), BorderHelper.calcIntersectionWithWallParallelToY(new Point(0, 0), wall, refBorder));
        double y = faker.random().nextDouble();
        assertEquals(new Point(1.5, y), BorderHelper.calcIntersectionWithWallParallelToY(new Point(0, y), wall, refBorder));
    }
    @Test
    public void testCalcIntersectionWithWallParallelToYParallel() {
        IBorder refBorder = new Border(new Point(0, 0), new Point(0, 5));
        IBorder wall = new Border(new Point(1.5, 5), new Point(1.5, 0));

        double y = faker.random().nextDouble();
        assertEquals(new Point(1.5, y), BorderHelper.calcIntersectionWithWallParallelToY(new Point(0, y), wall, refBorder));
    }
}