package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.provider.graph.AdjacencyMatrix;
import boustrophedon.provider.graph.Node;

public interface IDecomposer<T> {
    AdjacencyMatrix<Node<ICell>> decompose(T object) throws ExceedNumberOfAttempts;
    ArrayList<ICell> getCells();
}
