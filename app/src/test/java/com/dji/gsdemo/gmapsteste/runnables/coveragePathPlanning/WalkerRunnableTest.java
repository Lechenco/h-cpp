package com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;

import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.domain.walkers.error.AngleOffLimitsException;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.Cell;
import boustrophedon.provider.decomposer.Boustrophedon.Decomposer;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;
import boustrophedon.provider.primitives.Polyline;
import boustrophedon.provider.walkers.Walker;

public class WalkerRunnableTest {
    class Callback implements RunnableCallback<IPolyline> {
        @Override
        public void onComplete(IPolyline result) {

        }

        @Override
        public void onError(Exception e) {

        }
    }

    @Test
    public void testConstructor() {
        ICell cellMock = Mockito.mock(Cell.class);
        Handler handlerMock = Mockito.mock(Handler.class);
        RunnableCallback<IPolyline> callback = new Callback();
        WalkerRunnable runnable = new WalkerRunnable(cellMock, handlerMock, callback);

        assertEquals(cellMock, runnable.getInput());
        assertEquals(handlerMock, runnable.getHandler());
        assertEquals(callback, runnable.getCallback());
    }

    @Test
    public void testRun() {
        ICell cellMock = Mockito.mock(Cell.class);
        IPolygon polygon = Mockito.mock(Polygon.class);
        Handler handlerMock = Mockito.mock(Handler.class);
        RunnableCallback<IPolyline> callback = Mockito.mock(Callback.class);

        Mockito.when(handlerMock.post(Mockito.any())).then(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        });
        Mockito.when(cellMock.getPolygon()).thenReturn(polygon);
        Mockito.when(
                polygon.getPoints()).thenReturn(
                        new ArrayList<>(Collections.singletonList(Mockito.mock(Point.class))
                )
        );

        IPolyline polyline = Mockito.mock(Polyline.class);
        try (MockedConstruction<Walker> walkerMockedConstruction = Mockito.mockConstruction(Walker.class, (mock, context) -> {
            when(mock.generatePath(Mockito.any(), Mockito.any())).thenReturn(polyline);
        })) {
            WalkerRunnable runnable = new WalkerRunnable(cellMock, handlerMock, callback);
            runnable.run();

            assertEquals(1, walkerMockedConstruction.constructed().size());
            Mockito.verify(handlerMock, Mockito.times(1)).post(Mockito.any());
            Mockito.verify(callback, Mockito.times(1)).onComplete(polyline);
        }
    }
}