package boustrophedon.provider.primitives;

import static org.junit.Assert.*;

import com.github.javafaker.Faker;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.utils.GA;

public class BorderTest {
    private static final double DOUBLE_DELTA = 0.00001;

    @Test
    public void testConstructorAndGets() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );

        Border border = new Border(p1, p2);

        assertNotNull(border);
        assertEquals(p1, border.getFirstVertice());
        assertEquals(p2, border.getSecondVertice());
    }

    @Test
    public void testGetLengthMethod() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        double mockLength = faker.random().nextDouble();
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            mockedStatic.when(() -> GA.calcDistance(p1, p2)).thenReturn(mockLength);

            Border border = new Border(p1, p2);
            assertEquals(mockLength, border.getLength(), DOUBLE_DELTA);
        }
    }

    @Test
    public void testGetAngleMethod() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        double mockLength = faker.random().nextDouble();
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            mockedStatic.when(() -> GA.calcAngle(p1, p2)).thenReturn(mockLength);

            Border border = new Border(p1, p2);
            assertEquals(mockLength, border.getAngle(), DOUBLE_DELTA);
        }
    }

    @Test
    public void testGetPositiveAngleMethod() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        double mockAngle = faker.random().nextDouble();
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            mockedStatic.when(() -> GA.calcAngle(p1, p2)).thenReturn(mockAngle);

            Border border = new Border(p1, p2);
            assertEquals(mockAngle, border.getPositiveAngle(), DOUBLE_DELTA);
        }
    }

    @Test
    public void testGetPositiveAngleMethodWithNegativeAngle() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        double mockAngle = -Math.PI / 4;
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            mockedStatic.when(() -> GA.calcAngle(p1, p2)).thenReturn(mockAngle);

            Border border = new Border(p1, p2);
            assertEquals(3 * Math.PI / 4, border.getPositiveAngle(), DOUBLE_DELTA);
        }
    }

    @Test
    public void testIsParallelToYMethodFalse() {
        Faker faker = new Faker();

        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );

        Border border = new Border(p1, p2);

        assertFalse(border.isParallelToY());
    }

    @Test
    public void testIsParallelToYMethodTrue() {
        Faker faker = new Faker();
        double mockX = faker.number().randomDouble(7, 0, 90);
        IPoint p1 = new Point(
                mockX,
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                mockX,
                faker.number().randomDouble(7, 0, 90)
        );

        Border border = new Border(p1, p2);

        assertTrue(border.isParallelToY());
    }

    @Test
    public void testGetCoefficientsMethod() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        double[] mockAngles = new double[]{faker.random().nextDouble(), faker.random().nextDouble()};
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            mockedStatic.when(() -> GA.getCoefficients(p1, p2)).thenReturn(mockAngles);

            Border border = new Border(p1, p2);
            assertEquals(mockAngles, border.getCoefficients());
        }
    }

    @Test
    public void testGetCoefficientsMethodTrow() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            mockedStatic.when(() -> GA.getCoefficients(p1, p2)).thenThrow(new Exception("Mock Throw"));

            Border border = new Border(p1, p2);
            assertThrows("Mock Throw",
                    RuntimeException.class,
                    border::getCoefficients);
        }
    }

    @Test
    public void testGetParallelLineCoefficientsMethod() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p3 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        double[] mockCoefficients = new double[]{faker.random().nextDouble(), faker.random().nextDouble()};
        double[] mockParallelCoefficients = new double[]{faker.random().nextDouble(), faker.random().nextDouble()};
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            mockedStatic.when(() -> GA.getCoefficients(p1, p2)).thenReturn(mockCoefficients);
            mockedStatic.when(() -> GA.calcParallelLineCoefficients(mockCoefficients[0], p3)).thenReturn(mockParallelCoefficients);

            Border border = new Border(p1, p2);
            assertEquals(mockParallelCoefficients, border.getParallelLineCoefficients(p3));
        }
    }

    @Test
    public void testGetAngleDiffMethod() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        double mockAngle = faker.random().nextDouble();
        double mockAngle2 = faker.random().nextDouble();
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            mockedStatic.when(() -> GA.calcAngle(p1, p2)).thenReturn(mockAngle);

            Border border = new Border(p1, p2);
            assertEquals(mockAngle2 - mockAngle, border.getAngleDiff(mockAngle2), DOUBLE_DELTA);
        }
    }

    @Test
    public void testGetDistanceToPointMethod() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p3 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        double mockDistance = faker.random().nextDouble();
        try (MockedStatic<GA> mockedStatic = Mockito.mockStatic(GA.class)) {
            Border border = new Border(p1, p2);

            mockedStatic.when(() -> GA.calcDistance(border, p3)).thenReturn(mockDistance);


            assertEquals(mockDistance, border.getDistanceToPoint(p3), DOUBLE_DELTA);
        }
    }

    @Test
    public void testEqualsMethod() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );

        Border border1 = new Border(p1, p2);
        Border border2 = new Border(p1, p2);

        assertEquals(border1, border2);
    }

    @Test
    public void testEqualsMethodFalse() {
        Faker faker = new Faker();
        IPoint p1 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p2 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );
        IPoint p3 = new Point(
                faker.number().randomDouble(7, 0, 90),
                faker.number().randomDouble(7, 0, 90)
        );

        Border border1 = new Border(p1, p2);
        Border border2 = new Border(p1, p3);

        assertNotEquals(border1, border2);
        assertNotEquals(border1, new Object());
    }

    @Test
    public void testIsOnBorderMethod() {
        IPoint p1 = new Point(0, 0);
        IPoint p2 = new Point(5, 5);
        IPoint p3 = new Point(2, 3);

        Border border = new Border(p1, p2);

        assertFalse(border.isOnBorder(p3));
    }

    @Test
    public void testIsOnBorderMethodBeforeBorder() {
        IPoint p1 = new Point(0, 0);
        IPoint p2 = new Point(5, 5);
        IPoint p3 = new Point(-DOUBLE_DELTA, -DOUBLE_DELTA);

        Border border = new Border(p1, p2);

        assertFalse(border.isOnBorder(p3));
    }

    @Test
    public void testIsOnBorderMethodAfterBorder() {
        IPoint p1 = new Point(0, 0);
        IPoint p2 = new Point(5, 5);
        IPoint p3 = new Point(5 + DOUBLE_DELTA, 5 + DOUBLE_DELTA);

        Border border = new Border(p1, p2);

        assertFalse(border.isOnBorder(p3));
    }

    @Test
    public void testIsOnBorderMethodAboveBorder() {
        IPoint p1 = new Point(0, 0);
        IPoint p2 = new Point(5, 5);
        IPoint p3 = new Point(3, 3.00001);
        IPoint p4 = new Point(2.9999, 3);

        Border border = new Border(p1, p2);

        assertFalse(border.isOnBorder(p3));
        assertFalse(border.isOnBorder(p4));
    }

    @Test
    public void testIsOnBorderMethodBelowBorder() {
        IPoint p1 = new Point(0, 0);
        IPoint p2 = new Point(5, 5);
        IPoint p3 = new Point(3.0001, 3);
        IPoint p4 = new Point(3, 2.9999);

        Border border = new Border(p1, p2);

        assertFalse(border.isOnBorder(p3));
        assertFalse(border.isOnBorder(p4));
    }

    @Test
    public void testIsOnBorderMethodTrue() {
        IPoint p1 = new Point(0, 0);
        IPoint p2 = new Point(5, 5);

        Border border = new Border(p1, p2);

        assertTrue(border.isOnBorder(new Point(0, 0)));
        assertTrue(border.isOnBorder(new Point(2, 2)));
        assertTrue(border.isOnBorder(new Point(5, 5)));

        Border border2 = new Border(p2, p1);

        assertTrue(border2.isOnBorder(new Point(0, 0)));
        assertTrue(border2.isOnBorder(new Point(2, 2)));
        assertTrue(border2.isOnBorder(new Point(5, 5)));
    }

    @Test
    public void testIsOnBorderMethodInverse() {
        IPoint p1 = new Point(0, 0);
        IPoint p2 = new Point(5, -5);

        Border border = new Border(p1, p2);

        assertTrue(border.isOnBorder(new Point(0, 0)));
        assertTrue(border.isOnBorder(new Point(2, -2)));
        assertTrue(border.isOnBorder(new Point(5, -5)));

        Border border2 = new Border(p2, p1);

        assertTrue(border2.isOnBorder(new Point(0, 0)));
        assertTrue(border2.isOnBorder(new Point(2, -2)));
        assertTrue(border2.isOnBorder(new Point(5, -5)));
    }

    @Test
    public void testIsOnBorderMethodParallelToY() {
        IPoint p1 = new Point(0, 0);
        IPoint p2 = new Point(0, 5);

        Border border = new Border(p1, p2);

        assertFalse(border.isOnBorder(new Point(1, 0)));
        assertTrue(border.isOnBorder(new Point(0, 1)));
    }
}