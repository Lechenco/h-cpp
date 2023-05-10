package boustrophedon.provider.primitives;

import static org.junit.Assert.*;

import com.github.javafaker.Faker;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class PolygonTest {
   @Test
    public void testConstructorAndGets(){
       Faker faker = new Faker();
       Point[] points = new Point[]{
               new Point(faker.number().randomDouble(3, 0, 1000),
                       faker.number().randomDouble(3, 0, 1000)),
               new Point(faker.number().randomDouble(3, 0, 1000),
                       faker.number().randomDouble(3, 0, 1000)),
       };

       Polygon polygon = new Polygon(points);

       Assert.assertNotNull(polygon);
       Assert.assertEquals(2, polygon.getNumberOfPoints());
       Assert.assertEquals(points[0].getX(), polygon.getPoints().get(0).getX(), 0);
       Assert.assertEquals(points[1].getX(), polygon.getPoints().get(1).getX(), 0);
       Assert.assertEquals(points[0].getY(), polygon.getPoints().get(0).getY(), 0);
       Assert.assertEquals(points[1].getY(), polygon.getPoints().get(1).getY(), 0);
   }
    @Test
    public void testConstructorThrow(){
        Point[] points = null;

        assertThrows( NullPointerException.class,  () -> new Polygon(points));
    }
    @Test
    public void testConstructorArrayAndGets(){
        Faker faker = new Faker();
        ArrayList<IPoint> points = new ArrayList<>(Arrays.asList(
                new Point(faker.number().randomDouble(3, 0, 1000),
                        faker.number().randomDouble(3, 0, 1000)),
                new Point(faker.number().randomDouble(3, 0, 1000),
                        faker.number().randomDouble(3, 0, 1000))
        ));

        Polygon polygon = new Polygon(points);

        Assert.assertNotNull(polygon);
        Assert.assertEquals(points.get(0).getX(), polygon.getPoints().get(0).getX(), 0);
        Assert.assertEquals(points.get(1).getX(), polygon.getPoints().get(1).getX(), 0);
        Assert.assertEquals(points.get(0).getY(), polygon.getPoints().get(0).getY(), 0);
        Assert.assertEquals(points.get(1).getY(), polygon.getPoints().get(1).getY(), 0);
    }
    @Test
    public void testConstructorArrayThrow() {
        ArrayList<IPoint> points = null;

        assertThrows(NullPointerException.class, () -> new Polygon(points));
    }
    @Test
    public void testLatLngMethod() {
        Faker faker = new Faker();
        ArrayList<IPoint> points = new ArrayList<>(Collections.singletonList(
                new Point(faker.number().randomDouble(3, 0, 90),
                        faker.number().randomDouble(3, 0, 90))
        ));
        IPolygon polygon = new Polygon(points);

        LatLng[] latLngs = polygon.toLatLngArray();
        Assert.assertEquals(1, latLngs.length);
        Assert.assertEquals(points.get(0).getX(), latLngs[0].latitude, 0);
        Assert.assertEquals(points.get(0).getY(), latLngs[0].longitude, 0);
    }
}