package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.primitives.model.ISubarea;

public interface IClipper {
    void clip(ArrayList<ISubarea> subareas);
    ArrayList<ISubarea> getResult();
}
