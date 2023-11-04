package boustrophedon.provider.graph;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.OptionalDouble;

import boustrophedon.domain.graph.error.ElementNotFoundedException;
import boustrophedon.domain.graph.model.IObjectiveFunction;
import boustrophedon.domain.graph.model.IObjectiveMatrix;

public class ObjectiveMatrix<T> implements IObjectiveMatrix<T> {
    private double[][] objectiveMatrix;
    private int length;
    private ArrayList<T> nodes;

    private IObjectiveFunction<T> objectiveFunction;

    public ObjectiveMatrix(ArrayList<T> nodes) {
        this.initiatePrivateFields(nodes);
    }

    public ObjectiveMatrix(ArrayList<T> nodes, IObjectiveFunction<T> objectiveFunction) {
        this.initiatePrivateFields(nodes);
        this.objectiveFunction = objectiveFunction;
    }

    @Override
    public void setObjectiveFunction(IObjectiveFunction<T> objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    @Override
    public void calcObjective() {
        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.length; j++) {
                double objectiveValue = i == j ?
                    Double.MAX_VALUE :
                    this.objectiveFunction.execute(
                        this.nodes.get(i),
                        this.nodes.get(j)
                    );
                this.objectiveMatrix[i][j] = objectiveValue;
            }
        }
    }

    @Override
    public ArrayList<T> getNodes() {
        return this.nodes;
    }

    @Override
    public double getObjective(int i, int j) {
        return this.objectiveMatrix[i][j];
    }

    @Override
    public double getObjective(T i, T j) throws ElementNotFoundedException {
        int rowIndex = this.nodes.indexOf(i);
        int columnIndex = this.nodes.indexOf(j);

        if (rowIndex == -1)
            throw new ElementNotFoundedException(rowIndex);
        if (columnIndex == -1)
            throw new ElementNotFoundedException(columnIndex);

        return this.objectiveMatrix[rowIndex][columnIndex];
    }

    @Override
    public int getBestObjectiveIndex(T i) throws ElementNotFoundedException {
        int rowIndex = this.nodes.indexOf(i);

        return getBestObjectiveIndex(rowIndex);
    }
    @Override
    public int getBestObjectiveIndex(int  rowIndex) throws ElementNotFoundedException {
        if (rowIndex == -1 || rowIndex >= this.nodes.size())
            throw new ElementNotFoundedException(rowIndex);
        OptionalDouble bestObjectiveOptional = Arrays.stream(this.objectiveMatrix[rowIndex]).min();

        if (bestObjectiveOptional.isPresent())
            return ArrayUtils.indexOf(this.objectiveMatrix[rowIndex], bestObjectiveOptional.getAsDouble());

        throw new ElementNotFoundedException();
    }

    @Override
    public int getBestObjectiveIndexExcept(int rowIndex, Collection<Integer> ignoreIndexes) throws ElementNotFoundedException {
        if (rowIndex == -1 || rowIndex >= this.nodes.size())
            throw new ElementNotFoundedException(rowIndex);

        ArrayList<Double> objectives = new ArrayList<>();
        for (int i = 0; i < this.nodes.size(); i++) {
            double v = ignoreIndexes.contains(i) ? Double.MAX_VALUE : this.objectiveMatrix[rowIndex][i];
            objectives.add(v);
        }

        Optional<Double> bestObjectiveOptional = objectives.stream().min(Comparator.naturalOrder());

        if (bestObjectiveOptional.isPresent())
            return ArrayUtils.indexOf(this.objectiveMatrix[rowIndex], bestObjectiveOptional.get());

        throw new ElementNotFoundedException();
    }

    private void initiatePrivateFields(ArrayList<T> nodes) {
        this.nodes = nodes;
        this.length = 0;
        this.objectiveMatrix = new double[length][length];
    }
}
