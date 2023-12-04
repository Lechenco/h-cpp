package boustrophedon.provider.graph;

import java.util.ArrayList;
import java.util.Optional;

import boustrophedon.domain.graph.model.IAdjacencyMatrix;

public class AdjacencyMatrix<T> implements IAdjacencyMatrix<T> {
    private int [][] adjacencyMatrix;
    private int length;
    private final ArrayList<T> nodes;

    public AdjacencyMatrix() {
        this.nodes = new ArrayList<>();
        this.length = 0;
        this.adjacencyMatrix = new int[length][length];
    }

    public AdjacencyMatrix(ArrayList<T> nodes) {
        this.nodes = nodes;
        this.length = this.nodes.size();
        this.adjacencyMatrix = new int[length][length];
    }

    @Override
    public void addAdjacency(int source, int destination) {
        this.adjacencyMatrix[source][destination] = 1;
        this.adjacencyMatrix[destination][source] = 1;
    }

    @Override
    public void concat(IAdjacencyMatrix<T> matrix) {

    }

    @Override
    public int addNode(T node) {
        this.length += 1;
        this.nodes.add(node);
        int[][] oldMatrix = this.adjacencyMatrix;
        this.adjacencyMatrix = new int[this.length][this.length];
        for (int i = 0; i < this.length -1; i++) {
            int[] oldRow = oldMatrix[i];
            System.arraycopy(oldRow, 0, this.adjacencyMatrix[i], 0, oldRow.length);
        }

        return length -1;
    }

    @Override
    public Optional<T> getNode(int source, int destination) {
        if (source >= this.length
                || destination >= this.length
                || this.adjacencyMatrix[source][destination] == 0
        )
            return Optional.empty();

        return Optional.of(this.nodes.get(this.adjacencyMatrix[source][destination])) ;
    }

    @Override
    public ArrayList<T> getAdjacency(int source) {
        int[] destinations = this.adjacencyMatrix[source];
        ArrayList<T> adjacencyNodes = new ArrayList<>();

        for (int i = 0; i < this.length; i++) {
            if (destinations[i] == 1)
                adjacencyNodes.add(this.nodes.get(i));
        }

        return adjacencyNodes;
    }

    @Override
    public ArrayList<T> getAdjacency(T sourceNode) {
        for (int i = 0; i < this.length; i++) {
            if (sourceNode == this.nodes.get(i))
                return getAdjacency(i);
        }
        return null;
    }

    public ArrayList<T> getNodes() {
        return nodes;
    }
}
