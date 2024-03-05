package boustrophedon.provider.graph;

import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.graph.model.INodeChildrenObject;

public class Node<T extends INodeChildrenObject> implements INode<T> {
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

    @Override
    public boolean isAdjacent(INode<T> node) {
        return this.object.isAdjacent(node.getObject());
    }
}
