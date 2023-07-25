package boustrophedon.provider.decomposer.Boustrophedon;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPolygon;

public class Cell implements ICell {
    private boolean visited = false;

    private IPolygon polygon;

    public Cell(IPolygon polygon) {
        this.polygon = polygon;
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
        return this.polygon;
    }
}
