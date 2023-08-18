package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class OutSplitterTest {
    Polygon polygon;

    @Before
    public void setUp() {
        polygon = new Polygon(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1, 3),
                new Point(0, 3),
                new Point(0, 2),
                new Point(1, 1.5),
                new Point(0, 1)
        );
    }

    @Test
    public void testPopulateCellsCallCreateCells() throws ExceedNumberOfAttempts {
        try (MockedStatic<CellHelper> mockedStatic = Mockito.mockStatic(CellHelper.class)) {

            ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                    new CriticalPoint(polygon.getPoints().get(0), new ArrayList<>(Arrays.asList(
                            polygon.getBorders().get(0), polygon.getBorders().get(6)
                    ))),
                    new CriticalPoint(polygon.getPoints().get(1), new ArrayList<>(Arrays.asList(
                            polygon.getBorders().get(0), polygon.getBorders().get(1),
                            new Border(polygon.getPoints().get(1), polygon.getPoints().get(5))
                    ))),
                    new CriticalPoint(polygon.getPoints().get(2), new ArrayList<>(Arrays.asList(
                            polygon.getBorders().get(1), polygon.getBorders().get(2),
                            new Border(polygon.getPoints().get(2), polygon.getPoints().get(5))
                    ))),
                    new CriticalPoint(polygon.getPoints().get(3), new ArrayList<>(Arrays.asList(
                            polygon.getBorders().get(2), polygon.getBorders().get(3)
                    ))),
                    new CriticalPoint(polygon.getPoints().get(4), new ArrayList<>(Arrays.asList(
                            polygon.getBorders().get(3), polygon.getBorders().get(4)
                    ))),
                    new CriticalPoint(polygon.getPoints().get(5), new ArrayList<>(Arrays.asList(
                            polygon.getBorders().get(4), polygon.getBorders().get(5),
                            new Border(polygon.getPoints().get(2), polygon.getPoints().get(5)),
                            new Border(polygon.getPoints().get(1), polygon.getPoints().get(5))
                    ))),
                    new CriticalPoint(polygon.getPoints().get(6), new ArrayList<>(Arrays.asList(
                            polygon.getBorders().get(5), polygon.getBorders().get(6)
                    )))
            ));
            OutSplitter splitter = new OutSplitter(cps);
            splitter.cells = new ArrayList<>();
            splitter.populateCells(cps, cps.get(5), cps.get(6));
            splitter.populateCells(cps, cps.get(5), cps.get(4));

            mockedStatic.verify(
                    () -> CellHelper.createCell(Mockito.any()),
                    Mockito.times(2)
            );

            assertEquals(2, splitter.cells.size());
        }
    }

    @Test
    public void testPopulateCells() throws ExceedNumberOfAttempts {
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(polygon.getPoints().get(0), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(0), polygon.getBorders().get(6)
                ))),
                new CriticalPoint(polygon.getPoints().get(1), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(0), polygon.getBorders().get(1),
                        new Border(polygon.getPoints().get(1), polygon.getPoints().get(5))
                ))),
                new CriticalPoint(polygon.getPoints().get(2), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(1), polygon.getBorders().get(2),
                        new Border(polygon.getPoints().get(2), polygon.getPoints().get(5))
                ))),
                new CriticalPoint(polygon.getPoints().get(3), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(2), polygon.getBorders().get(3)
                ))),
                new CriticalPoint(polygon.getPoints().get(4), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(3), polygon.getBorders().get(4)
                ))),
                new CriticalPoint(polygon.getPoints().get(5), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(4), polygon.getBorders().get(5),
                        new Border(polygon.getPoints().get(2), polygon.getPoints().get(5)),
                        new Border(polygon.getPoints().get(1), polygon.getPoints().get(5))
                ))),
                new CriticalPoint(polygon.getPoints().get(6), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(5), polygon.getBorders().get(6)
                )))
        ));
        OutSplitter splitter = new OutSplitter(cps);
        splitter.cells = new ArrayList<>();
        splitter.populateCells(cps, cps.get(5), cps.get(6));
        splitter.populateCells(cps, cps.get(5), cps.get(4));

        assertEquals(2, splitter.cells.size());
        assertEquals(4, splitter.cells.get(0).getPolygon().getNumberOfPoints());
        assertEquals(4, splitter.cells.get(0).getPolygon().getNumberOfPoints());
    }

    @Test
    public void testPopulateCellsThrowsException() {

        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(polygon.getPoints().get(0), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(0), polygon.getBorders().get(2)
                ))),
                new CriticalPoint(polygon.getPoints().get(1), new ArrayList<>(Arrays.asList(
                        polygon.getBorders().get(0), polygon.getBorders().get(1)
                )))
        ));
        OutSplitter splitter = new OutSplitter(cps);
        splitter.cells = new ArrayList<>();

        assertThrows(ExceedNumberOfAttempts.class, () -> splitter.populateCells(cps, cps.get(0), cps.get(1)));
    }

}