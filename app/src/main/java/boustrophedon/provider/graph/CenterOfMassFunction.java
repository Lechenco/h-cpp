package boustrophedon.provider.graph;

import boustrophedon.domain.graph.IObjectiveFunction;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.helpers.graph.CenterOfMassFunctionHelper;
import boustrophedon.utils.GA;

public class CenterOfMassFunction implements IObjectiveFunction<IPolygon> {
    @Override
    public double execute(Node<IPolygon> from, Node<IPolygon> to) {
        return GA.calcDistance(
                CenterOfMassFunctionHelper.calcCenterOfMass(from.getObject()),
                CenterOfMassFunctionHelper.calcCenterOfMass(to.getObject())
        );
    }
}
