package boustrophedon.provider.decomposer.Boustrophedon.Clippers;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.model.IClipper;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.ISubarea;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;
import boustrophedon.provider.primitives.Subarea;

public class NormalSubareaClipperTest {

    @Test
    public void testClipperWithOnlyNormalArea() {
        IPolygon normalArea = new Polygon(
                new Point(0, 0),
                new Point(0, 1),
                new Point(1, 1),
                new Point(1, 0)
        );
        ArrayList<ISubarea> subareas = new ArrayList<>(Collections.singletonList(new Subarea(normalArea)));

        IClipper clipper = new NormalSubareaClipper();
        clipper.clip(subareas);
        ArrayList<ISubarea> result = clipper.getResult();

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getPolygon().getNumberOfPoints());
    }

    @Test
    public void testClipperWithOneSplit() {
        IPolygon normalArea = new Polygon(
                new Point(0, 0),
                new Point(0, 1),
                new Point(1, 1),
                new Point(1, 0)
        );
        IPolygon specialArea = new Polygon(
                new Point(0, 0),
                new Point(0, 1),
                new Point(0.5, 1),
                new Point(0.5, 0)
        );
        ArrayList<ISubarea> subareas = new ArrayList<>(Arrays.asList(
                new Subarea(normalArea),
                new Subarea(specialArea, SubareaTypes.SPECIAL)
        ));

        IClipper clipper = new NormalSubareaClipper();
        clipper.clip(subareas);
        ArrayList<ISubarea> result = clipper.getResult();

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).getPolygon().getNumberOfPoints());
    }
}