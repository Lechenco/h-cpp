package boustrophedon.provider.decomposer.Boustrophedon.Clippers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import boustrophedon.controllers.decomposer.Boustrophedon.Splitters.SplitterController;
import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IClipper;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.ISubarea;
import boustrophedon.factories.decomposer.Boustrophedon.CriticalPoint.CriticalPointFactory;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.primitives.Border;

public class NormalSubareaClipper implements IClipper {
    ArrayList<ISubarea> res;

    public NormalSubareaClipper() {
        this.res = new ArrayList<>();
    }

    @Override
    public ArrayList<ISubarea> getResult() {
        return res;
    }
    @Override
    public void clip(ArrayList<ISubarea> subareas) {
        ISubarea normalArea = getNormalArea(subareas);
        if (normalArea == null) return;

        ArrayList<ISubarea> extraAreas = this.getExtraNormalAreas(subareas);
        res = this.splitNormalAreaOnClippingPoints(normalArea, extraAreas);
        this.clipNormalAreaWithClippingAlgorithm(extraAreas);

        res.addAll(extraAreas);
    }

    private void clipNormalAreaWithClippingAlgorithm(ArrayList<ISubarea> extraAreas) {
        WeilerAthertonClippingAlgorithm weilerAthertonClippingAlgorithm = new WeilerAthertonClippingAlgorithm();
        for (ISubarea subArea : res) {
            for (ISubarea clippingArea : extraAreas) {
                IPolygon clippedDifference = weilerAthertonClippingAlgorithm.execute(
                        clippingArea.getPolygon(),
                        subArea.getPolygon(),
                        WeilerAthertonClippingAlgorithm.WeilerAthertonModes.DIFFERENCE);
                if (clippedDifference != null && !clippingArea.getPolygon().containsAll(clippedDifference.getPoints()))
                    subArea.setPolygon(clippedDifference);
                else if (clippedDifference == null && clippingArea.getPolygon().containsAll(subArea.getPolygon().getPoints()))
                    res.remove(subArea);

            }
        }
    }
    private boolean polygonContainsPolygon(IPolygon p1, IPolygon p2) {
        return p1.containsAll(p2.getPoints());
    }

    private ArrayList<ISubarea> splitNormalAreaOnClippingPoints(ISubarea normalArea, ArrayList<ISubarea> extraAreas) {
        ArrayList<ICriticalPoint> clippingPoints = new ArrayList<>();
        for (ISubarea subarea : extraAreas) {
            clippingPoints.addAll(generateClippingPoints(normalArea, subarea));
        }
        clippingPoints = this.removeDuplicatedClippingPoints(clippingPoints);

        return splitNormalArea(normalArea, clippingPoints);
    }

    private ArrayList<ICriticalPoint> generateClippingPoints(ISubarea normalArea, ISubarea clippingArea) {
        ArrayList<ICriticalPoint> clippingPoints = new ArrayList<>();
        for (IPoint point : clippingArea.getPolygon().getPoints()) {
            ICriticalPoint cp = new CriticalPoint(point);
            cp.setEvent(Events.CLIP);
            cp.detectPointEvent(normalArea.getPolygon());
            clippingPoints.add(cp);
            cp.getIntersectionsInNormal().forEach(i -> {
                cp.getEdges().add(new Border(i.getVertices(), cp.getVertices()));
            });
        }

        return clippingPoints;
    }


    private ArrayList<ISubarea> splitNormalArea(ISubarea normalArea, ArrayList<ICriticalPoint> clippingPoints) {
        ArrayList<ISubarea> newNormalSubareas;
        ArrayList<ICriticalPoint> normalPolygonWithIntersections = CriticalPointFactory.execute(normalArea.getPolygon(), clippingPoints);
        SplitterController splitterController = new SplitterController(normalPolygonWithIntersections);
        try {
            IAdjacencyMatrix<INode<ICell>> adjacencyMatrix = splitterController.execute();
            newNormalSubareas = adjacencyMatrix.getNodes()
                    .stream().map(n -> n.getObject().getSubarea())
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (ExceedNumberOfAttempts e) {
            newNormalSubareas = new ArrayList<>(Collections.singleton(normalArea));
        }
        return newNormalSubareas;
    }

    private ArrayList<ICriticalPoint> removeDuplicatedClippingPoints(ArrayList<ICriticalPoint> clippingPoints) {
        Set<IPoint> intersectionSet = new HashSet<>();

        ArrayList<ICriticalPoint> newClippingPoints = new ArrayList<>();

        for(ICriticalPoint cp : clippingPoints) {
            if (intersectionSet.stream().noneMatch(inter -> {
                ArrayList<IPoint> points = cp.getEdgesPoints();
                return points.stream().anyMatch(p -> p.equals(inter) || p.getX() == inter.getX()); // TODO: add angle to validation
            })) {
                intersectionSet.addAll(cp.getEdgesPoints());
                newClippingPoints.add(cp);
            }
        }
        return newClippingPoints;
    }

    private ISubarea getNormalArea(Collection<ISubarea> subareas) {
        return subareas.stream()
                .filter( s -> s.getSubareaType() == SubareaTypes.NORMAL)
                .findFirst().orElse(null);
    }
    private ArrayList<ISubarea> getExtraNormalAreas(Collection<ISubarea> subareas) {
        return subareas.stream()
                .filter( s -> s.getSubareaType() != SubareaTypes.NORMAL)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
