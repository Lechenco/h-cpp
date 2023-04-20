package boustrophedon.provider;


import com.github.javafaker.Faker;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import boustrophedon.model.IPoint;

public class PointTest {
    private static final double DOUBLE_DELTA = 0.00001;
    private static final double HUNDRED_AND_EIGHTY_DEGREES = Math.PI;
    private static final double NINETY_DEGREES = Math.PI / 2;
    private static final double FORTY_FIVE_DEGREES = Math.PI / 4;
    @Test
    public void testConstructorAndGets() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point = new Point(x, y);

        Assert.assertNotNull(point);
        Assert.assertEquals(point.getX(), x, DOUBLE_DELTA);
        Assert.assertEquals(point.getY(), y, DOUBLE_DELTA);
    }

    @Test
    public void testToLatLngMethod() {
        Faker faker = new Faker();
        double x = faker.number().randomDouble(9, 0, 90);
        double y = faker.number().randomDouble(9, 0, 90);

        IPoint point = new Point(x, y);
        LatLng latLng = point.toLatLng();

        Assert.assertEquals(x, latLng.latitude, DOUBLE_DELTA);
        Assert.assertEquals(y, latLng.longitude, DOUBLE_DELTA);
    }

    @Test
    public void testWalkXYMethodNoDistance() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walkXY(0, 0);

        Assert.assertEquals(point, pointWalked);
    }

    @Test
    public void testWalkXYMethodMoveOnlyX() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walkXY(0.1, 0);

        Assert.assertNotEquals(point, pointWalked);
        Assert.assertEquals(x + 0.1, pointWalked.getX(), DOUBLE_DELTA);
    }

    @Test
    public void testWalkXYMethodMoveOnlyY() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walkXY(0, 0.1);

        Assert.assertNotEquals(point, pointWalked);
        Assert.assertEquals(y + 0.1, pointWalked.getY(), DOUBLE_DELTA);
    }

    @Test
    public void testWalkMethodWithNoDistance() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walk(0, NINETY_DEGREES);

        Assert.assertEquals(point, pointWalked);
    }

    @Test
    public void testWalkMethodWithNoAngle() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walk(0.1, 0);

        Assert.assertEquals(x + 0.1, pointWalked.getX(), DOUBLE_DELTA);
    }

    @Test
    public void testWalkMethodWith90Degrees() {
        Faker faker = new Faker();
        double x = faker.number().randomDouble(9, 0, 1000);
        double y = faker.number().randomDouble(9, 0, 1000);

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walk(0.1, NINETY_DEGREES);

        Assert.assertEquals(y + 0.1, pointWalked.getY(), DOUBLE_DELTA);
    }
    @Test
    public void testWalkMethodWithMinus90Degrees() {
        Faker faker = new Faker();
        double x = faker.number().randomDouble(9, 0, 1000);
        double y = faker.number().randomDouble(9, 0, 1000);

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walk(0.1, - NINETY_DEGREES);

        Assert.assertEquals(y - 0.1, pointWalked.getY(), DOUBLE_DELTA);
    }
    @Test
    public void testWalkMethodWith45Degrees() {
        Faker faker = new Faker();
        double x = faker.number().randomDouble(9, 0, 1000);
        double y = faker.number().randomDouble(9, 0, 1000);

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walk(0.1, FORTY_FIVE_DEGREES);

        Assert.assertEquals(y + 0.1 * Math.sin(FORTY_FIVE_DEGREES), pointWalked.getY(), DOUBLE_DELTA);
        Assert.assertEquals(x + 0.1 * Math.cos(FORTY_FIVE_DEGREES), pointWalked.getX(), DOUBLE_DELTA);
    }
    @Test
    public void testWalkMethodWithMinus45Degrees() {
        Faker faker = new Faker();
        double x = faker.number().randomDouble(9, 0, 1000);
        double y = faker.number().randomDouble(9, 0, 1000);

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walk(0.1, - FORTY_FIVE_DEGREES);

        Assert.assertEquals(y - 0.1 * Math.sin(FORTY_FIVE_DEGREES), pointWalked.getY(), DOUBLE_DELTA);
        Assert.assertEquals(x + 0.1 * Math.cos(FORTY_FIVE_DEGREES), pointWalked.getX(), DOUBLE_DELTA);
    }
    @Test
    public void testWalkMethodWith180Degrees() {
        Faker faker = new Faker();
        double x = faker.number().randomDouble(9, 0, 1000);
        double y = faker.number().randomDouble(9, 0, 1000);

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walk(0.1, HUNDRED_AND_EIGHTY_DEGREES);

        Assert.assertEquals(x - 0.1, pointWalked.getX(), DOUBLE_DELTA);
    }
    @Test
    public void testWalkMethodWithMinus180Degrees() {
        Faker faker = new Faker();
        double x = faker.number().randomDouble(9, 0, 1000);
        double y = faker.number().randomDouble(9, 0, 1000);

        IPoint point = new Point(x, y);
        IPoint pointWalked = point.walk(0.1, - HUNDRED_AND_EIGHTY_DEGREES);

        Assert.assertEquals(x - 0.1, pointWalked.getX(), DOUBLE_DELTA);
    }

    @Test
    public void testCalcDistanceMethodNoDistance() {
        Faker faker = new Faker();
        double x = faker.number().randomDouble(9, 0, 1000);
        double y = faker.number().randomDouble(9, 0, 1000);

        IPoint point = new Point(x, y);
        double distance = point.calcDistance(point);

        Assert.assertEquals(0, distance, 0);
    }

    @Test
    public void testToString() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point = new Point(x, y);

        Assert.assertEquals(point.toString(), String.format("Point{ x=%f, y=%f }", x, y));
    }

    @Test
    public void testEqualsTrue() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point1 = new Point(x, y);
        IPoint point2 = new Point(x, y);

        Assert.assertEquals(point1, point2);
    }

    @Test
    public void testEqualsFalse() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point1 = new Point(x, y);
        IPoint point2 = new Point(y, x);

        Assert.assertNotEquals(point1, point2);
    }
    @Test
    public void testEqualsFalseDelta() {
        Faker faker = new Faker();
        double x = faker.number().randomNumber();
        double y = faker.number().randomNumber();

        IPoint point1 = new Point(x, y);
        IPoint point2 = new Point(x, y + DOUBLE_DELTA);

        Assert.assertNotEquals(point1, point2);
    }
}