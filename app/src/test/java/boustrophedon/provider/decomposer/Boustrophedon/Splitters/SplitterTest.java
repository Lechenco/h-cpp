package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.primitives.Border;
import boustrophedon.provider.primitives.Point;

public class SplitterTest {
    ArrayList<IBorder> borders = new ArrayList<>();
    @Test
    public void testGetCriticalPointsBeforeIndex() {
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(new Point(0, 0), borders),
                new CriticalPoint(new Point(1, 1), borders),
                new CriticalPoint(new Point(1, 0), borders),
                new CriticalPoint(new Point(2, 0), borders),
                new CriticalPoint(new Point(2, 1), borders),
                new CriticalPoint(new Point(0, 1), borders)
        ));

        Splitter splitter = new Splitter(cps){
            @Override
            void populateCells(ArrayList<CriticalPoint> cellPoints, CriticalPoint splitPoint) {

            }
        };

        assertEquals(2, splitter.calcCellPoints(cps.get(0)).size());
        assertEquals(4, splitter.calcCellPoints(cps.get(1)).size());
        assertEquals(4, splitter.calcCellPoints(cps.get(2)).size());
        assertEquals(6, splitter.calcCellPoints(cps.get(4)).size());
    }

    @Test
    public void testGetRemainingPoints() {
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(new Point(0, 0), borders),
                new CriticalPoint(new Point(1, 1), borders),
                new CriticalPoint(new Point(1, 0), borders),
                new CriticalPoint(new Point(2, 0), borders),
                new CriticalPoint(new Point(2, 1), borders),
                new CriticalPoint(new Point(0, 1), borders)
        ));

        Splitter splitter = new Splitter(cps){
            @Override
            void populateCells(ArrayList<CriticalPoint> cellPoints, CriticalPoint splitPoint) {

            }
        };

        assertEquals(6, splitter.calcRemainingPoints(cps.get(0)).size());
        assertEquals(4, splitter.calcRemainingPoints(cps.get(1)).size());
        assertEquals(4, splitter.calcRemainingPoints(cps.get(2)).size());
        assertEquals(2, splitter.calcRemainingPoints(cps.get(4)).size());
    }

    @Test
    public void testReplaceEdges() {
        CriticalPoint cp1 = new CriticalPoint(new Point(0, 0), new ArrayList<>(Arrays.asList(
                new Border(new Point(0, 0), new Point(2, 0)),
                new Border(new Point(0, 0), new Point(2, 2))
        )));
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                cp1,
                new CriticalPoint(new Point(1, 1), borders),
                new CriticalPoint(new Point(2, 2), borders)
        ));

        Splitter splitter = new Splitter(cps){
            @Override
            void populateCells(ArrayList<CriticalPoint> cellPoints, CriticalPoint splitPoint) {

            }
        };

        splitter.remainingPoints = cps;

        splitter.addSplitEdge(cp1, cps.get(1));

        assertEquals(3, cp1.getEdges().size());
        assertEquals(new Point(2, 2), cp1.getEdgesPoints().get(1));
        assertEquals(new Point(1, 1), cp1.getEdgesPoints().get(2));
    }
}