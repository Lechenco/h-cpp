package boustrophedon.domain.graph;

public interface INode<T> {
    void setIndex(int index);
    int getIndex();
    T getObject();
    void setObject(T object);
}
