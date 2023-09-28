package com.dji.gsdemo.gmapsteste.controller.coveragePathPlanning;

import java.util.ArrayList;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.provider.decomposer.Boustrophedon.Decomposer;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;
import boustrophedon.provider.primitives.Polygon;

public class DecomposerController {
    public ArrayList<ICell> decompose(Polygon polygon) throws ExceedNumberOfAttempts {
        Decomposer decomposer = new Decomposer();
        MatrixAdjacency<Node<ICell>> matrixAdjacency = decomposer.decompose(polygon);
        return matrixAdjacency.getNodes()
                .stream()
                .map(Node::getObject)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
