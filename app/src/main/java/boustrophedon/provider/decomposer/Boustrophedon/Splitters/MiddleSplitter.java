package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;

import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class MiddleSplitter extends Splitter {

    public MiddleSplitter(ArrayList<CriticalPoint> criticalPoints) {
        super(criticalPoints);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void populateCells(ArrayList<CriticalPoint> cellPoints, CriticalPoint splitPoint) {
        IPoint current = splitPoint.getVertices();
        ArrayList<CriticalPoint> walkedPoints = new ArrayList<>(Collections.singletonList(splitPoint));
        for (CriticalPoint cp : cellPoints) {
            if (cp.getEdgesPoints().contains(current) && !walkedPoints.contains(cp)) {
                walkedPoints.add(cp);
                current = cp.getVertices();
            }
        }

        this.cells.add(CellHelper.createCell(walkedPoints));
    }
}
