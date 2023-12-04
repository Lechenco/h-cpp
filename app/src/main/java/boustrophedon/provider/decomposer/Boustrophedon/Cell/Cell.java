package boustrophedon.provider.decomposer.Boustrophedon.Cell;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.ISubarea;
import boustrophedon.provider.primitives.Subarea;

public class Cell implements ICell {
    private boolean visited = false;

    private final ISubarea subarea;

    public Cell(IPolygon polygon) {
        this.subarea = new Subarea(polygon);
    }

    public Cell(ISubarea subarea) {
        this.subarea = subarea;
    }

    @Override
    public void visit() {
        this.visited = true;
    }

    @Override
    public boolean getVisited() {
        return this.visited;
    }

    @Override
    public IPolygon getPolygon() {
        return this.subarea.getPolygon();
    }

    @Override
    public ISubarea getSubarea() {
        return subarea;
    }
}
