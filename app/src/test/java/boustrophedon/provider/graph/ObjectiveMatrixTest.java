package boustrophedon.provider.graph;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import boustrophedon.domain.graph.error.ElementNotFoundedException;
import boustrophedon.domain.graph.model.IObjectiveFunction;

public class ObjectiveMatrixTest {

    static class ObjectiveFunction implements IObjectiveFunction<Integer> {
        @Override
        public double execute(Integer from, Integer to) {
            return from + to;
        }
    }
    @Test
    public void testConstructor() {
        ArrayList<Integer> nodes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        ObjectiveMatrix<Integer> objectiveMatrix = new ObjectiveMatrix<>(nodes);

        assertEquals(5, objectiveMatrix.getNodes().size());
        assertEquals(5, objectiveMatrix.objectiveMatrix.length);
    }
    @Test
    public void testConstructorWithFunction() {
        ArrayList<Integer> nodes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IObjectiveFunction<Integer> mock = Mockito.mock(ObjectiveFunction.class);

        ObjectiveMatrix<Integer> objectiveMatrix = new ObjectiveMatrix<>(nodes, mock);

        assertEquals(5, objectiveMatrix.getNodes().size());
        assertEquals(5, objectiveMatrix.objectiveMatrix.length);
        assertEquals(mock, objectiveMatrix.objectiveFunction);
    }

    @Test
    public void testSetObjectiveFunction() {
        ArrayList<Integer> nodes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IObjectiveFunction<Integer> mock = Mockito.mock(ObjectiveFunction.class);

        ObjectiveMatrix<Integer> objectiveMatrix = new ObjectiveMatrix<>(nodes);

        assertNull(objectiveMatrix.objectiveFunction);
        objectiveMatrix.setObjectiveFunction(mock);

        assertEquals(mock, objectiveMatrix.objectiveFunction);
    }

    @Test
    public void testCalcObjective() {
        ArrayList<Integer> nodes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IObjectiveFunction<Integer> mock = Mockito.mock(ObjectiveFunction.class);

        ObjectiveMatrix<Integer> objectiveMatrix = new ObjectiveMatrix<>(nodes, mock);

        objectiveMatrix.calcObjective();

        Mockito.verify(mock, Mockito.times(20)).execute(Mockito.anyInt(), Mockito.anyInt());
        assertEquals(mock, objectiveMatrix.objectiveFunction);
    }
    @Test
    public void testGetBestObjective() throws ElementNotFoundedException {
        ArrayList<Integer> nodes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IObjectiveFunction<Integer> function = new ObjectiveFunction();
        ObjectiveMatrix<Integer> objectiveMatrix = new ObjectiveMatrix<>(nodes, function);
        objectiveMatrix.calcObjective();



        assertEquals(1, objectiveMatrix.getBestObjectiveIndex(0));
        assertEquals(0, objectiveMatrix.getBestObjectiveIndex(1));
    }
    @Test
    public void testGetBestObjective_Throws() throws ElementNotFoundedException {
        ArrayList<Integer> nodes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IObjectiveFunction<Integer> function = new ObjectiveFunction();
        ObjectiveMatrix<Integer> objectiveMatrix = new ObjectiveMatrix<>(nodes, function);
        objectiveMatrix.calcObjective();



        assertThrows(ElementNotFoundedException.class, () -> objectiveMatrix.getBestObjectiveIndex(6));
    }

    @Test
    public void testGetBestObjectiveExcept() throws ElementNotFoundedException {
        ArrayList<Integer> nodes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IObjectiveFunction<Integer> function = new ObjectiveFunction();
        ObjectiveMatrix<Integer> objectiveMatrix = new ObjectiveMatrix<>(nodes, function);
        objectiveMatrix.calcObjective();

        assertEquals(3, objectiveMatrix.getBestObjectiveIndexExcept(0, Arrays.asList(1, 2)));
        assertEquals(2, objectiveMatrix.getBestObjectiveIndexExcept(1, Collections.singletonList(0)));
    }
    @Test
    public void testGetBestObjectiveExcept_Throws() {
        ArrayList<Integer> nodes = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        IObjectiveFunction<Integer> function = new ObjectiveFunction();
        ObjectiveMatrix<Integer> objectiveMatrix = new ObjectiveMatrix<>(nodes, function);
        objectiveMatrix.calcObjective();

        assertThrows(ElementNotFoundedException.class, () -> objectiveMatrix.getBestObjectiveIndexExcept(6, Arrays.asList(0, 1, 2, 3, 4)));
    }
}