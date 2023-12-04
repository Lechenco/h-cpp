package boustrophedon.provider.primitives;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.ISubarea;

public class Area implements IArea {

    private final ArrayList<ISubarea> subareas;

    public Area() {
        this.subareas = new ArrayList<>();
    }

    public Area(ArrayList<ISubarea> subareas) {
        this.subareas = subareas;
    }

    public Area(ISubarea... subareas) {
        this.subareas = new ArrayList<>(Arrays.asList(subareas));
    }

    @Override
    public int getNumberOfSubAreas() {
        return subareas.size();
    }

    @Override
    public void add(ISubarea sub) {
        this.subareas.add(sub);
    }

    @Override
    public ArrayList<ISubarea> getSubareas() {
        return subareas;
    }

    @Override
    public IPolygon getGeometry() {
        return this.subareas.isEmpty()
                ? null
                : this.subareas.get(0).getPolygon();
    }
}
