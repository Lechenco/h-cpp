package boustrophedon.provider.graph;

import boustrophedon.domain.graph.model.INode;

public class Node<T> implements INode<T> {
    private T object;
    private int index;

    public Node(T object) {
        this.object = object;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public T getObject() {
        return this.object;
    }

    @Override
    public void setObject(T object) {
        this.object = object;
    }
}
