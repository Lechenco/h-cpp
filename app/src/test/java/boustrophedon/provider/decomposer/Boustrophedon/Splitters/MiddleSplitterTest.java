package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import static org.junit.Assert.*;

import com.github.javafaker.Faker;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.decomposer.Boustrophedon.Events;
import boustrophedon.provider.primitives.Point;

public class MiddleSplitterTest {
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

        ArrayList<CriticalPoint> sortedCps = CriticalPointerHelper.sort(cps);

        MiddleSplitter middleSplitter = new MiddleSplitter(cps);

        assertEquals(2, middleSplitter.getCriticalPointsBeforeIndex(sortedCps, 0).size());
        assertEquals(2, middleSplitter.getCriticalPointsBeforeIndex(sortedCps, 1).size());
        assertEquals(4, middleSplitter.getCriticalPointsBeforeIndex(sortedCps, 2).size());
        assertEquals(6, middleSplitter.getCriticalPointsBeforeIndex(sortedCps, 4).size());
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

        ArrayList<CriticalPoint> sortedCps = CriticalPointerHelper.sort(cps);

        MiddleSplitter middleSplitter = new MiddleSplitter(cps);

        assertEquals(6, middleSplitter.getRemainingPoints(sortedCps, 0).size());
        assertEquals(6, middleSplitter.getRemainingPoints(sortedCps, 1).size());
        assertEquals(4, middleSplitter.getRemainingPoints(sortedCps, 2).size());
        assertEquals(2, middleSplitter.getRemainingPoints(sortedCps, 4).size());
    }
    @Test
    public void testGetRemainingPointsReturnNull() {
        ArrayList<CriticalPoint> cps = new ArrayList<>();


        MiddleSplitter middleSplitter = new MiddleSplitter(cps);

        assertNull(middleSplitter.getRemainingPoints(cps, MiddleSplitter.INDEX_NOT_FOUNDED));
    }

    @Test
    public void testLookForMiddleIndex() {
        Faker faker = new Faker();
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(new Point(0, 0), borders),
                new CriticalPoint(new Point(1, 1), borders),
                new CriticalPoint(new Point(1, 0), borders),
                new CriticalPoint(new Point(2, 0), borders),
                new CriticalPoint(new Point(2, 1), borders),
                new CriticalPoint(new Point(0, 1), borders)
        ));

        ArrayList<CriticalPoint> sortedCps = CriticalPointerHelper.sort(cps);
        int indexMiddle = faker.number().numberBetween(0, sortedCps.size());
        sortedCps.get(indexMiddle).setEvent(Events.MIDDLE);

        MiddleSplitter middleSplitter = new MiddleSplitter(cps);
        assertEquals(indexMiddle, middleSplitter.lookForMiddleIndex(sortedCps));
    }
    @Test
    public void testLookForMiddleIndexNotFound() {
        ArrayList<CriticalPoint> cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(new Point(0, 0), borders),
                new CriticalPoint(new Point(1, 1), borders),
                new CriticalPoint(new Point(1, 0), borders),
                new CriticalPoint(new Point(2, 0), borders),
                new CriticalPoint(new Point(2, 1), borders),
                new CriticalPoint(new Point(0, 1), borders)
        ));

        ArrayList<CriticalPoint> sortedCps = CriticalPointerHelper.sort(cps);

        MiddleSplitter middleSplitter = new MiddleSplitter(cps);
        assertEquals(MiddleSplitter.INDEX_NOT_FOUNDED, middleSplitter.lookForMiddleIndex(sortedCps));
    }
}