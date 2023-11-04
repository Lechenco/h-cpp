package boustrophedon.domain.graph.model;

import java.util.ArrayList;
import java.util.Collection;

import boustrophedon.domain.graph.error.ElementNotFoundedException;

public interface IObjectiveMatrix<T> {
    void setObjectiveFunction(IObjectiveFunction<T> objectiveFunction);
    void calcObjective();
    ArrayList<T> getNodes();
    double getObjective(int i, int j);
    double getObjective(T i, T j) throws ElementNotFoundedException;
    int getBestObjectiveIndex(T i) throws ElementNotFoundedException;
    int getBestObjectiveIndex(int i) throws ElementNotFoundedException;
    int getBestObjectiveIndexExcept(int i, Collection<Integer> ignoreIndexes) throws ElementNotFoundedException;
}
