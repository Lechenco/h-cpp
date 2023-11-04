package boustrophedon.provider.graph;

import boustrophedon.domain.graph.model.IObjectiveFunction;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.graph.CenterOfMassFunctionHelper;
import boustrophedon.utils.GA;

public class CenterOfMassFunction implements IObjectiveFunction<IPolygon> {
    @Override
    public double execute(IPolygon from, IPolygon to) {
        return GA.calcDistance(
                CenterOfMassFunctionHelper.calcCenterOfMass(from),
                CenterOfMassFunctionHelper.calcCenterOfMass(to)
        );
    }
}
