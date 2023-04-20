package boustrophedon.provider;

import com.github.javafaker.Faker;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.model.IPoint;
import boustrophedon.model.IPolyline;

public class PolylineTest {
    @Test
    public void testConstructorAndGets() {
        Faker faker = new Faker();
        Point[] points = new Point[]{
                new Point(faker.number().randomDouble(3, 0, 1000),
                        faker.number().randomDouble(3, 0, 1000)),
                new Point(faker.number().randomDouble(3, 0, 1000),
                        faker.number().randomDouble(3, 0, 1000))
        };

        IPolyline polyline = new Polyline(points);

        Assert.assertNotNull(polyline);
        Assert.assertEquals(points[0].getX(), polyline.getPoints().get(0).getX(), 0);
        Assert.assertEquals(points[1].getX(), polyline.getPoints().get(1).getX(), 0);
        Assert.assertEquals(points[0].getY(), polyline.getPoints().get(0).getY(), 0);
        Assert.assertEquals(points[1].getY(), polyline.getPoints().get(1).getY(), 0);
    }
    @Test
    public void testConstructorAndGetsWithArrayList() {
        Faker faker = new Faker();
       ArrayList<IPoint> points = new ArrayList<>(Arrays.asList(
               new Point(faker.number().randomDouble(3, 0, 1000),
                       faker.number().randomDouble(3, 0, 1000)),
               new Point(faker.number().randomDouble(3, 0, 1000),
                       faker.number().randomDouble(3, 0, 1000))
       ));

        IPolyline polyline = new Polyline(points);

        Assert.assertNotNull(polyline);
        Assert.assertEquals(points.get(0).getX(), polyline.getPoints().get(0).getX(), 0);
        Assert.assertEquals(points.get(1).getX(), polyline.getPoints().get(1).getX(), 0);
        Assert.assertEquals(points.get(0).getY(), polyline.getPoints().get(0).getY(), 0);
        Assert.assertEquals(points.get(1).getY(), polyline.getPoints().get(1).getY(), 0);
    }
    @Test
    public void testConstructorShouldThrowException() {
        Point[] points = null;
        Assert.assertThrows(NullPointerException.class, () -> new Polyline(points));
    }
    @Test
    public void testConstructorShouldThrowExceptionWithArrayList() {
        ArrayList<IPoint> points = null;
        Assert.assertThrows(NullPointerException.class, () -> new Polyline(points));
    }
    @Test
    public void testGetNumberOfPointsMethod() {
        Faker faker = new Faker();
        Point[] points = new Point[]{
                new Point(faker.number().randomDouble(3, 0, 1000),
                        faker.number().randomDouble(3, 0, 1000)),
                new Point(faker.number().randomDouble(3, 0, 1000),
                        faker.number().randomDouble(3, 0, 1000))
        };

        IPolyline polyline = new Polyline(points);

        Assert.assertEquals(2, polyline.getNumberOfPoints());
    }
    @Test
    public void testGetNumberOfPointsMethodWithNoPoints() {
        IPolyline polyline = new Polyline(new ArrayList<>());

        Assert.assertEquals(0, polyline.getNumberOfPoints());
    }
    @Test
    public void testAddMethod() {
        Faker faker = new Faker();
        IPoint point = new Point(faker.number().randomDouble(3, 0, 1000),
                faker.number().randomDouble(3, 0, 1000));
        IPolyline polyline = new Polyline(new ArrayList<>());

        polyline.add(point);
        Assert.assertEquals(1, polyline.getNumberOfPoints());
        Assert.assertEquals(point.getX(), polyline.getPoints().get(0).getX(), 0);
        Assert.assertEquals(point.getY(), polyline.getPoints().get(0).getY(), 0);
    }
    @Test
    public void testAddMethodWithNull() {
        IPoint point = null;
        IPolyline polyline = new Polyline(new ArrayList<>());

        Assert.assertThrows("No point", NullPointerException.class, () -> polyline.add(point));
    }
    @Test
    public void testAddMethodWithPoints() {
        Faker faker = new Faker();
        Point[] points = new Point[]{
                new Point(faker.number().randomDouble(3, 0, 1000),
                        faker.number().randomDouble(3, 0, 1000)),
        };
        IPolyline polyline = new Polyline(new ArrayList<>());

        polyline.add(points);
        Assert.assertEquals(1, polyline.getNumberOfPoints());
        Assert.assertEquals(points[0].getX(), polyline.getPoints().get(0).getX(), 0);
        Assert.assertEquals(points[0].getY(), polyline.getPoints().get(0).getY(), 0);
    }
    @Test
    public void testAddMethodWithPointsNull() {
        Point[] points = null;
        IPolyline polyline = new Polyline(new ArrayList<>());

        Assert.assertThrows("No point", NullPointerException.class, () -> polyline.add(points));
    }
    @Test
    public void testAddMethodWithCollection() {
        Faker faker = new Faker();
        ArrayList<IPoint> points = new ArrayList<>(Arrays.asList(
                new Point(faker.number().randomDouble(3, 0, 1000),
                        faker.number().randomDouble(3, 0, 1000))
        ));
        IPolyline polyline = new Polyline(new ArrayList<>());
        polyline.add(points);
        Assert.assertEquals(1, polyline.getNumberOfPoints());
        Assert.assertEquals(points.get(0).getX(), polyline.getPoints().get(0).getX(), 0);
        Assert.assertEquals(points.get(0).getY(), polyline.getPoints().get(0).getY(), 0);
    }
    @Test
    public void testAddMethodWithCollectionNull() {
        ArrayList<IPoint> points = null;
        IPolyline polyline = new Polyline(new ArrayList<>());

        Assert.assertThrows("No point", NullPointerException.class, () -> polyline.add(points));
    }
    @Test
    public void testLatLngMethod() {
        Faker faker = new Faker();
        ArrayList<IPoint> points = new ArrayList<>(Arrays.asList(
                new Point(faker.number().randomDouble(3, 0, 90),
                        faker.number().randomDouble(3, 0, 90))
        ));
        IPolyline polyline = new Polyline(points);

        LatLng[] latLngs = polyline.toLatLngArray();
        Assert.assertEquals(1, latLngs.length);
        Assert.assertEquals(points.get(0).getX(), latLngs[0].latitude, 0);
        Assert.assertEquals(points.get(0).getY(), latLngs[0].longitude, 0);
    }
}