package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class MiddleSplitterTest {
    Polygon triangleRectangle;

    @Before
    public void setUp() {
        triangleRectangle = new Polygon(new Point(0, 0),
                new Point(5, 5), new Point(5, 0));
    }

    @Test
    public void testPopulateCellsCallCreateCells() throws ExceedNumberOfAttempts {
        try (MockedStatic<CellHelper> mockedStatic = Mockito.mockStatic(CellHelper.class)) {

            ArrayList<ICriticalPoint> cps = new ArrayList<>(Arrays.asList(
                    new CriticalPoint(triangleRectangle.getPoints().get(0), new ArrayList<>(Arrays.asList(
                            triangleRectangle.getBorders().get(0), triangleRectangle.getBorders().get(2)
                    ))),
                    new CriticalPoint(triangleRectangle.getPoints().get(1), new ArrayList<>(Arrays.asList(
                            triangleRectangle.getBorders().get(0), triangleRectangle.getBorders().get(1)
                    ))),
                    new CriticalPoint(triangleRectangle.getPoints().get(2), new ArrayList<>(Arrays.asList(
                            triangleRectangle.getBorders().get(1), triangleRectangle.getBorders().get(2)
                    )))
            ));
            MiddleSplitter splitter = new MiddleSplitter(cps);
            splitter.cells = new ArrayList<>();
            splitter.populateCells(cps, cps.get(0));

            mockedStatic.verify(
                    () -> CellHelper.createCell(Mockito.any()),
                    Mockito.times(1)
            );

            assertEquals(1, splitter.cells.size());
        }
    }

    @Test
    public void testPopulateCells() throws ExceedNumberOfAttempts {

        ArrayList<ICriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(triangleRectangle.getPoints().get(0), new ArrayList<>(Arrays.asList(
                        triangleRectangle.getBorders().get(0), triangleRectangle.getBorders().get(2)
                ))),
                new CriticalPoint(triangleRectangle.getPoints().get(1), new ArrayList<>(Arrays.asList(
                        triangleRectangle.getBorders().get(0), triangleRectangle.getBorders().get(1)
                ))),
                new CriticalPoint(triangleRectangle.getPoints().get(2), new ArrayList<>(Arrays.asList(
                        triangleRectangle.getBorders().get(1), triangleRectangle.getBorders().get(2)
                )))
        ));
        MiddleSplitter splitter = new MiddleSplitter(cps);
        splitter.cells = new ArrayList<>();
        splitter.populateCells(cps, cps.get(0));

        assertEquals(1, splitter.cells.size());
        assertEquals(3, splitter.cells.get(0).getPolygon().getNumberOfPoints());
    }
    @Test
    public void testPopulateCellsThrowsException() {

        ArrayList<ICriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(triangleRectangle.getPoints().get(0), new ArrayList<>(Arrays.asList(
                        triangleRectangle.getBorders().get(0), triangleRectangle.getBorders().get(2)
                ))),
                new CriticalPoint(triangleRectangle.getPoints().get(1), new ArrayList<>(Arrays.asList(
                        triangleRectangle.getBorders().get(0), triangleRectangle.getBorders().get(1)
                )))
        ));
        MiddleSplitter splitter = new MiddleSplitter(cps);
        splitter.cells = new ArrayList<>();

        assertThrows(ExceedNumberOfAttempts.class, () -> splitter.populateCells(cps, cps.get(0)));
    }
    @Test
    public void testPopulateCellsWithPointWithoutConnection() throws ExceedNumberOfAttempts {
        ArrayList<ICriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(triangleRectangle.getPoints().get(0), new ArrayList<>(Arrays.asList(
                        new Border(new Point(-1, 0), new Point(0, 0)),
                        triangleRectangle.getBorders().get(0), triangleRectangle.getBorders().get(2)
                ))),
                new CriticalPoint(new Point(-1, 0), new ArrayList<>(
                        Arrays.asList(
                                new Border(new Point(-1, 0), new Point(0, 0)),
                                new Border(new Point(-1, 0), new Point(-1, -1))
                        )
                )),
                new CriticalPoint(triangleRectangle.getPoints().get(1), new ArrayList<>(Arrays.asList(
                        triangleRectangle.getBorders().get(0), triangleRectangle.getBorders().get(1)
                ))),
                new CriticalPoint(triangleRectangle.getPoints().get(2), new ArrayList<>(Arrays.asList(
                        triangleRectangle.getBorders().get(1), triangleRectangle.getBorders().get(2)
                )))
        ));
        MiddleSplitter splitter = new MiddleSplitter(cps);
        splitter.cells = new ArrayList<>();
        splitter.populateCells(cps, cps.get(2));

        assertEquals(1, splitter.cells.size());
        assertEquals(3, splitter.cells.get(0).getPolygon().getNumberOfPoints());
    }
}