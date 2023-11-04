package boustrophedon.domain.graph.model;

public interface INode<T> {
    void setIndex(int index);
    int getIndex();
    T getObject();
    void setObject(T object);
}
