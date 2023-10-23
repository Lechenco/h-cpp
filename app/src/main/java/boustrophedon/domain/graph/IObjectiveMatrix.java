package boustrophedon.domain.graph;

public interface IObjectiveMatrix<T> {
    void setObjectiveFunction(IObjectiveFunction<T> objectiveFunction);
    void calcObjective();
}
