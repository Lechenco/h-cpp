package com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import android.os.Handler;

import com.dji.gsdemo.gmapsteste.app.RunnableCallback;

import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;


import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPolygon;

import boustrophedon.provider.decomposer.Boustrophedon.Decomposer;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;
import boustrophedon.provider.primitives.Polygon;

public class DecomposerRunnableTest {

    class Callback implements RunnableCallback<AdjacencyMatrix<Node<ICell>>> {
        @Override
        public void onComplete(AdjacencyMatrix<Node<ICell>> result) {

        }

        @Override
        public void onError(Exception e) {

        }
    }
    @Test
    public void testConstructor() {
        IPolygon polygonMock = Mockito.mock(Polygon.class);
        Handler handlerMock = Mockito.mock(Handler.class);
        RunnableCallback<AdjacencyMatrix<Node<ICell>>> callback = new Callback();
        DecomposerRunnable runnable = new DecomposerRunnable(polygonMock, handlerMock, callback);

        assertEquals(polygonMock, runnable.getInput());
        assertEquals(handlerMock, runnable.getHandler());
        assertEquals(callback, runnable.getCallback());
    }
    @Test
    public void testRun() {
        IPolygon polygonMock = Mockito.mock(Polygon.class);
        Handler handlerMock = Mockito.mock(Handler.class);
        RunnableCallback<AdjacencyMatrix<Node<ICell>>> callback = Mockito.mock(Callback.class);

        AdjacencyMatrix<Node<ICell>> matrix = (AdjacencyMatrix<Node<ICell>>) Mockito.mock(AdjacencyMatrix.class);
        try(MockedConstruction<Decomposer> decomposerMocked = Mockito.mockConstruction(Decomposer.class,(mock, context)-> {
            when(mock.decompose(Mockito.any())).thenReturn(matrix);
        })){
            Mockito.when(handlerMock.post(Mockito.any())).then(invocation -> {
                ((Runnable) invocation.getArgument(0)).run();
                return null;
            });
            DecomposerRunnable runnable = new DecomposerRunnable(polygonMock, handlerMock, callback);
            runnable.run();

            assertEquals(1, decomposerMocked.constructed().size());
            Mockito.verify(handlerMock, Mockito.times(1)).post(Mockito.any());
            Mockito.verify(callback, Mockito.times(1)).onComplete(matrix);
        }
    }
    @Test
    public void testRun_onError() {
        IPolygon polygonMock = Mockito.mock(Polygon.class);
        Handler handlerMock = Mockito.mock(Handler.class);
        RunnableCallback<AdjacencyMatrix<Node<ICell>>> callback = Mockito.mock(Callback.class);

        try(MockedConstruction<Decomposer> decomposerMocked = Mockito.mockConstruction(Decomposer.class,(mock, context)-> {
            when(mock.decompose(Mockito.any())).thenThrow(ExceedNumberOfAttempts.class);
        })){
            Mockito.when(handlerMock.post(Mockito.any())).then(invocation -> {
                ((Runnable) invocation.getArgument(0)).run();
                return null;
            });
            DecomposerRunnable runnable = new DecomposerRunnable(polygonMock, handlerMock, callback);
            runnable.run();

            assertEquals(1, decomposerMocked.constructed().size());
            Mockito.verify(handlerMock, Mockito.times(1)).post(Mockito.any());
            Mockito.verify(callback, Mockito.times(1)).onError(Mockito.any());
        }
    }
}