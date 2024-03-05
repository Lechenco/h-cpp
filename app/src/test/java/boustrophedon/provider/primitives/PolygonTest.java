package boustrophedon.provider.primitives;

import static org.junit.Assert.*;

import static boustrophedon.constants.AngleConstants.FORTY_FIVE_DEGREES;
import static boustrophedon.constants.AngleConstants.HUNDRED_AND_EIGHTY_DEGREES;
import static boustrophedon.constants.AngleConstants.NINETY_DEGREES;
import static boustrophedon.constants.AngleConstants.THIRTY_DEGREES;

import com.github.javafaker.Faker;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;

public class PolygonTest {
    @Test
    public void testConstructorAndGets() {
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
    public void testConstructorThrow() {
        Point[] points = null;

        //noinspection ConstantValue
        assertThrows(NullPointerException.class, () -> new Polygon(points));
    }

    @Test
    public void testConstructorArrayAndGets() {
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

        //noinspection ConstantValue
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

    @Test
    public void testGetOutsiderPointInDirection() {
        IPolygon triangleRectangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(5, 0));
        Assert.assertEquals(new Point(0, 0),
                triangleRectangle.getOutsiderPointInDirection(new Point(0, 0), NINETY_DEGREES));
        Assert.assertEquals(new Point(5, 5),
                triangleRectangle.getOutsiderPointInDirection(new Point(0, 0), FORTY_FIVE_DEGREES));
    }

    @Test
    public void testGetClosestVertices() {
        ArrayList<IPoint> points = new ArrayList<>(
                Arrays.asList(
                        new Point(0, 0), new Point(5, 5), new Point(5, 0)
                )
        );
        IPolygon triangleRectangle = new Polygon(points);

        Assert.assertEquals(points.get(0), triangleRectangle.getClosestVertices(new Point(-1, 0)));
        Assert.assertEquals(points.get(2), triangleRectangle.getClosestVertices(new Point(4, -1)));
    }
    @Test
    public void testGetFarthestVertices() {
        ArrayList<IPoint> points = new ArrayList<>(
                Arrays.asList(
                        new Point(0, 0), new Point(5, 5), new Point(5, 0)
                )
        );
        IPolygon triangleRectangle = new Polygon(points);

        Assert.assertEquals(points.get(1), triangleRectangle.getFarthestVertices(new Point(-1, 0)));
        Assert.assertEquals(points.get(1), triangleRectangle.getFarthestVertices(new Point(4, -1)));
    }
    @Test
    public void testGetFarthestVerticesWithDirection() {
        ArrayList<IPoint> points = new ArrayList<>(
                Arrays.asList(
                        new Point(0, 0), new Point(5, 5), new Point(5, 0)
                )
        );
        IPolygon triangleRectangle = new Polygon(points);

        Assert.assertEquals(points.get(1), triangleRectangle.getFarthestVertices(new Point(-1, 0)));
        Assert.assertEquals(points.get(0), triangleRectangle.getFarthestVertices(new Point(4, -1), HUNDRED_AND_EIGHTY_DEGREES));
        Assert.assertEquals(points.get(1), triangleRectangle.getFarthestVertices(new Point(0, 0), THIRTY_DEGREES));
    }
    @Test
    public void testIsAdjacentTo() {
        IPolygon polygon1 = new Polygon(
                new Point(0,0), new Point(1,0),
                new Point(1,1)
        );
        IPolygon polygon2 = new Polygon(
                new Point(0,0), new Point(0,1),
                new Point(1,1)
        );

        assertTrue(polygon1.isAdjacentTo(polygon2));
    }
    @Test
    public void testIsAdjacentTo2() {
        IPolygon polygon1 = new Polygon(
                new Point(0,0), new Point(-1,0),
                new Point(-1,-1)
        );
        IPolygon polygon2 = new Polygon(
                new Point(0,0), new Point(0,1),
                new Point(1,1)
        );

        assertTrue(polygon1.isAdjacentTo(polygon2));
    }

    @Test
    public void testIsAdjacentTo3() {
        IPolygon polygon1 = new Polygon(
                new Point(0,0.5), new Point(-1,0),
                new Point(-1,-1)
        );
        IPolygon polygon2 = new Polygon(
                new Point(0,0), new Point(0,1),
                new Point(1,1)
        );

        assertFalse(polygon1.isAdjacentTo(polygon2));
    }
}