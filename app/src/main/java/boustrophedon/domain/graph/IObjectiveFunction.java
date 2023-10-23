package boustrophedon.domain.graph;

import boustrophedon.provider.graph.Node;

public interface IObjectiveFunction<T> {
    double execute(Node<T> from, Node<T> to);
}
