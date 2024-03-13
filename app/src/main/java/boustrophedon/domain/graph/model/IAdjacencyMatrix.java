package boustrophedon.domain.graph.model;

import java.util.ArrayList;
import java.util.Optional;

public interface IAdjacencyMatrix<T> {
    void addAdjacency(int source, int destination);

    int addNode(T node);
    ArrayList<T> getNodes();
    Optional<T> getNode(int source, int destination);
    ArrayList<T> getAdjacency(int source);
    ArrayList<T> getAdjacency(T sourceNode);
}
