package boustrophedon.domain.graph.model;

import boustrophedon.domain.graph.error.ElementNotFoundedException;

public interface IObjectiveMatrix<T> {
    void setObjectiveFunction(IObjectiveFunction<T> objectiveFunction);
    void calcObjective();
    double getObjective(int i, int j);
    double getObjective(T i, T j) throws ElementNotFoundedException;
    int getBestObjectiveIndex(T i) throws ElementNotFoundedException;
}
