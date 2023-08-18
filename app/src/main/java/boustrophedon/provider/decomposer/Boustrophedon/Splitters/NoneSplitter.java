package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class NoneSplitter implements ISplitter {
    private ArrayList<ICell> cells;
    private final ArrayList<CriticalPoint> criticalPoints;

    public NoneSplitter(ArrayList<CriticalPoint> criticalPoints) {
        this.criticalPoints = criticalPoints;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void split(ICriticalPoint splitPoint) {
        this.cells = new ArrayList<>();
        ICell cell = CellHelper.createCell(this.criticalPoints);
        this.cells.add(cell);
    }

    @Override
    public ArrayList<ICell> getCells() {
        return this.cells;
    }

    @Override
    public ArrayList<CriticalPoint> getRemainingPoints() {
        return null;
    }
}
