package com.dji.gsdemo.gmapsteste.controller.coveragePathPlanning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.domain.walkers.error.AngleOffLimitsException;
import boustrophedon.domain.walkers.model.WalkerConfig;
import boustrophedon.provider.walkers.Walker;

public class WalkerController {
    public static IPolyline walk(IPolygon polygon) throws AngleOffLimitsException {
        Walker walker = new Walker(
            new WalkerConfig(0.0006, Math.PI / 2)
        );
        return walker.generatePath(
            polygon,
            polygon.getPoints().get(0)
        );
    }

    public static IPolyline walk(ICell cell) throws AngleOffLimitsException {
        return WalkerController.walk(cell.getPolygon());
    }

    public static ArrayList<IPolyline> walkAll(Collection<ICell> cells)  {
        return cells
                .stream()
                .map(c -> {
                    try {
                        return WalkerController.walk(c);
                    } catch (AngleOffLimitsException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
