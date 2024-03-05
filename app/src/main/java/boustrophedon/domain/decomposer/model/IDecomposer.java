package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;

public interface IDecomposer<T> {
    IAdjacencyMatrix<INode<ICell>> decompose(T object) throws ExceedNumberOfAttempts;
    ArrayList<ICell> getCells();
}
