package boustrophedon.provider.decomposer.Boustrophedon;

import org.junit.Test;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class DecomposerIntegrationTest {
    IPolygon polygon = new Polygon(
            new Point(0, 0),
            new Point(1, 0),
            new Point(1.5, 0.5),
            new Point(2, 0),
            new Point(3, 0),
            new Point(3, 1),
            new Point(0, 1)
    );
    @Test
    public void testDecompose() throws ExceedNumberOfAttempts {
        PolygonDecomposer decomposer = new PolygonDecomposer();

        decomposer.decompose(polygon);
    }
}