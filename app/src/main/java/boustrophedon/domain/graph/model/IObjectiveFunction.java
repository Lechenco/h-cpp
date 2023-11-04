package boustrophedon.domain.graph.model;

public interface IObjectiveFunction<T> {
    double execute(T from, T to);
}
