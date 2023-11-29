package boustrophedon.provider.decomposer.Boustrophedon;

import org.junit.Before;


import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class DecomposerTest {
    Polygon triangleRectangle;
    Polygon squareRectangle;
    @Before
    public void setUp() {
        triangleRectangle = new Polygon(new Point(0, 0), new Point(5, 5), new Point(5, 0));
        squareRectangle = new Polygon(new Point(0, 0),
                new Point(5, 0),
                new Point(5, 5), new Point(0, 5)
        );
    }
}