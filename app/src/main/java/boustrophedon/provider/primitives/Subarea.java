package boustrophedon.provider.primitives;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.ISubarea;

public class Subarea implements ISubarea {
    private IPolygon polygon;

    private SubareaTypes type = SubareaTypes.NORMAL;

    public Subarea(IPolygon polygon) {
        this.polygon = polygon;
    }

    public Subarea(IPolygon polygon, SubareaTypes type) {
        this.polygon = polygon;
        this.type = type;
    }

    @Override
    public SubareaTypes getSubareaType() {
        return type;
    }

    @Override
    public void setSubareaType(SubareaTypes type) {
        this.type = type;
    }

    @Override
    public IPolygon getPolygon() {
        return polygon;
    }

    @Override
    public void setPolygon(IPolygon polygon) {
        this.polygon = polygon;
    }
}
