package boustrophedon.controllers.decomposer.Boustrophedon.Splitters;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.domain.primitives.model.IBorder;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.MiddleSplitter;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.NoneSplitter;
import boustrophedon.provider.decomposer.Boustrophedon.Splitters.OutSplitter;
import boustrophedon.provider.primitives.Point;

public class SplitterControllerTest {
    ArrayList<CriticalPoint> cps;
    ArrayList<IBorder> borders = new ArrayList<>();
//    MockedConstruction<NoneSplitter> noneSplitter;
//    MockedConstruction<OutSplitter> outSplitter;
//    MockedConstruction<MiddleSplitter> middleSplitter;

    @Before
    public void setUp() {
        cps = new ArrayList<>(Arrays.asList(
                new CriticalPoint(new Point(0, 0), borders),
                new CriticalPoint(new Point(1, 1), borders),
                new CriticalPoint(new Point(1, 0), borders),
                new CriticalPoint(new Point(2, 0), borders),
                new CriticalPoint(new Point(2, 1), borders),
                new CriticalPoint(new Point(0, 1), borders)
        ));
//         noneSplitter = Mockito.mockConstruction(NoneSplitter.class);
//         outSplitter = Mockito.mockConstruction(OutSplitter.class);
//         middleSplitter = Mockito.mockConstruction(MiddleSplitter.class);
    }

    @Test
    public void testCreateSplitterNull() {
        MockedConstruction<NoneSplitter> noneSplitter = Mockito.mockConstruction(NoneSplitter.class);
        MockedConstruction<OutSplitter> outSplitter = Mockito.mockConstruction(OutSplitter.class);
        MockedConstruction<MiddleSplitter> middleSplitter = Mockito.mockConstruction(MiddleSplitter.class);

        SplitterController controller = new SplitterController(cps);
        ISplitter res = controller.createSplitter(null);

        assertTrue(res instanceof NoneSplitter);
        assertEquals(1, noneSplitter.constructed().size());
        assertEquals(0, outSplitter.constructed().size());
        assertEquals(0, middleSplitter.constructed().size());

        noneSplitter.close();
        outSplitter.close();
        middleSplitter.close();
    }
    @Test
    public void testCreateSplitterMiddle() {
        MockedConstruction<NoneSplitter> noneSplitter = Mockito.mockConstruction(NoneSplitter.class);
        MockedConstruction<OutSplitter> outSplitter = Mockito.mockConstruction(OutSplitter.class);
        MockedConstruction<MiddleSplitter> middleSplitter = Mockito.mockConstruction(MiddleSplitter.class);

        SplitterController controller = new SplitterController(cps);
        this.cps.get(0).setEvent(Events.MIDDLE);
        ISplitter res = controller.createSplitter(this.cps.get(0));

        assertTrue(res instanceof MiddleSplitter);
        assertEquals(0, noneSplitter.constructed().size());
        assertEquals(0, outSplitter.constructed().size());
        assertEquals(1, middleSplitter.constructed().size());
        noneSplitter.close();
        outSplitter.close();
        middleSplitter.close();
    }
    @Test
    public void testCreateSplitterIn() {
        MockedConstruction<NoneSplitter> noneSplitter = Mockito.mockConstruction(NoneSplitter.class);
        MockedConstruction<OutSplitter> outSplitter = Mockito.mockConstruction(OutSplitter.class);
        MockedConstruction<MiddleSplitter> middleSplitter = Mockito.mockConstruction(MiddleSplitter.class);

        SplitterController controller = new SplitterController(cps);
        this.cps.get(0).setEvent(Events.IN);
        ISplitter res = controller.createSplitter(this.cps.get(0));

        assertTrue(res instanceof MiddleSplitter);
        assertEquals(0, noneSplitter.constructed().size());
        assertEquals(0, outSplitter.constructed().size());
        assertEquals(1, middleSplitter.constructed().size());
        noneSplitter.close();
        outSplitter.close();
        middleSplitter.close();
    }
    @Test
    public void testCreateSplitterOut() {
        MockedConstruction<NoneSplitter> noneSplitter = Mockito.mockConstruction(NoneSplitter.class);
        MockedConstruction<OutSplitter> outSplitter = Mockito.mockConstruction(OutSplitter.class);
        MockedConstruction<MiddleSplitter> middleSplitter = Mockito.mockConstruction(MiddleSplitter.class);

        SplitterController controller = new SplitterController(cps);
        this.cps.get(0).setEvent(Events.OUT);
        ISplitter res = controller.createSplitter(this.cps.get(0));

        assertTrue(res instanceof OutSplitter);
        assertEquals(0, noneSplitter.constructed().size());
        assertEquals(1, outSplitter.constructed().size());
        assertEquals(0, middleSplitter.constructed().size());
        noneSplitter.close();
        outSplitter.close();
        middleSplitter.close();
    }

    @Test
    public void testFindNextEvent() {
        SplitterController controller = new SplitterController(cps);
        this.cps.get(0).setEvent(Events.OUT);
        this.cps.get(5).setEvent(Events.IN);
        ArrayList<CriticalPoint> sortedCP = CriticalPointerHelper.sort(this.cps);

        assertEquals(this.cps.get(0), controller.findNextEvent(sortedCP));
        assertEquals(this.cps.get(5), controller.findNextEvent(sortedCP));
        assertNull(controller.findNextEvent(sortedCP));
    }
    @Test
    public void testFindNextEventWithoutEvent() {
        SplitterController controller = new SplitterController(cps);
        ArrayList<CriticalPoint> sortedCP = CriticalPointerHelper.sort(this.cps);

        assertNull(controller.findNextEvent(sortedCP));
    }
}