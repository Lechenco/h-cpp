package boustrophedon.domain.primitives.model;

import boustrophedon.domain.decomposer.enums.SubareaTypes;

public interface ISubarea {
    SubareaTypes getSubareaType();
    void setSubareaType(SubareaTypes type);

    IPolygon getPolygon();
    void setPolygon(IPolygon polygon);
}
