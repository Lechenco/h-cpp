package boustrophedon.domain.graph.model;

public interface INode<T extends INodeChildrenObject> {
    void setIndex(int index);
    int getIndex();
    T getObject();
    void setObject(T object);
    boolean isAdjacent(INode<T> node);
}
