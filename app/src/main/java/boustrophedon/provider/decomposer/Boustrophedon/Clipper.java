package boustrophedon.provider.decomposer.Boustrophedon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import boustrophedon.controllers.decomposer.Boustrophedon.Splitters.SplitterController;
import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IClipper;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.ISubarea;
import boustrophedon.factories.decomposer.Boustrophedon.CriticalPoint.CriticalPointFactory;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class Clipper implements IClipper {
    @Override
    public void clip(IArea area) {
        clip(area.getSubareas());
    }

    @Override
    public ArrayList<ICell> getCells() {
        return null;
    }
    private void clip(Collection<ISubarea> subareas) {
        Collection<ISubarea> newSubareas= new ArrayList<>();

        ISubarea normalArea = getNormalArea(subareas);

        if (normalArea == null) return;

        ArrayList<ICriticalPoint> clippingPoints = new ArrayList<>();
        for (ISubarea subarea : subareas) {
            if (subarea.getSubareaType() != SubareaTypes.NORMAL) {
                clippingPoints.addAll(generateClippingPoints(normalArea, subarea));
            }
        }
        clippingPoints = this.removeDuplicatedClippingPoints(clippingPoints);


        newSubareas = splitNormalArea(normalArea, clippingPoints);
        // cut subarea from normal area
    }

    private ArrayList<ICriticalPoint> generateClippingPoints(ISubarea normalArea, ISubarea clippingArea) {
        ArrayList<ICriticalPoint> clippingPoints = new ArrayList<>();
        for (IPoint point : clippingArea.getPolygon().getPoints()) {
            ICriticalPoint cp = new CriticalPoint(point);
            cp.setEvent(Events.CLIP);
            cp.detectPointEvent(normalArea.getPolygon());
            clippingPoints.add(cp);
        }

        return clippingPoints;
    }


    private ArrayList<ISubarea> splitNormalArea(ISubarea normalArea, ArrayList<ICriticalPoint> clippingPoints) {
        ArrayList<ISubarea> newSubareas= new ArrayList<>();
        ArrayList<ICriticalPoint> normalPolygonWithIntersections = CriticalPointFactory.execute(normalArea.getPolygon(), clippingPoints);
        //SplitterController splitterController = new SplitterController(normalPolygonWithIntersections);

        return newSubareas;
    }

    private ArrayList<ICriticalPoint> removeDuplicatedClippingPoints(ArrayList<ICriticalPoint> clippingPoints) {
        Set<IPoint> intersectionSet = new HashSet<>();

        ArrayList<ICriticalPoint> newClippingPoints = new ArrayList<>();

        for(ICriticalPoint cp : clippingPoints) {
            if (intersectionSet.stream().noneMatch(inter -> {
                ArrayList<IPoint> points = cp.getEdgesPoints();
                return points.stream().anyMatch(p -> p.equals(inter));
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
}
