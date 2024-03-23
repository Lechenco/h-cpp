package boustrophedon.domain.decomposer.model;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;

public interface ISplitter {
    void split(ICriticalPoint splitPoint, SubareaTypes subareaType) throws ExceedNumberOfAttempts;

    ArrayList<ICell> getCells();

    ArrayList<ICriticalPoint> getRemainingPoints();
}
