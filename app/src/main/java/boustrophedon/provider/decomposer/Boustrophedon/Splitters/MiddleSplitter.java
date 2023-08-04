package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.function.Predicate;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ISplitter;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPointerHelper;
import boustrophedon.provider.decomposer.Boustrophedon.Events;

public class MiddleSplitter implements ISplitter {
    private final ArrayList<CriticalPoint> criticalPoints;
    ArrayList<ICell> cells;
    public MiddleSplitter(ArrayList<CriticalPoint> criticalPoints) {
        this.criticalPoints = criticalPoints;
    }
    static int INDEX_NOT_FOUNDED = -1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public ArrayList<ICell> split() {
        this.cells = new ArrayList<>();

        ArrayList<CriticalPoint> remainingCPSorted = CriticalPointerHelper.sort(this.criticalPoints);

        while(remainingCPSorted != null && remainingCPSorted.size() > 0) {
            int middleEventIndex = this.lookForMiddleIndex(remainingCPSorted);
            addCellUntilIndex(remainingCPSorted, middleEventIndex);
            remainingCPSorted = getRemainingPoints(remainingCPSorted, middleEventIndex);
        }

        return cells;
    }

    protected int lookForMiddleIndex(ArrayList<CriticalPoint> sortedCP) {
        for (int i = 0; i < sortedCP.size(); i++) {
            CriticalPoint cp = sortedCP.get(i);

            if (cp.isSplit())
                continue;

            cp.setSplit(true);

            if (cp.getEvent() == Events.MIDDLE) return i;
        }

        return INDEX_NOT_FOUNDED;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addCellUntilIndex(ArrayList<CriticalPoint> sortedPoints, int cpIndex) {
        ArrayList<CriticalPoint> newCellCPs = cpIndex != INDEX_NOT_FOUNDED ?
                getCriticalPointsBeforeIndex(sortedPoints, cpIndex):
                CriticalPointerHelper.unsorted(sortedPoints, this.criticalPoints);

        this.cells.add(CellHelper.createCell(newCellCPs));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected ArrayList<CriticalPoint> getCriticalPointsBeforeIndex(ArrayList<CriticalPoint> sortedPoints, int cpIndex) {
        IPoint splitPoint = sortedPoints.get(cpIndex).getVertices();

        Predicate<CriticalPoint> beforeX = criticalPoint -> criticalPoint.getVertices().getX() <= splitPoint.getX();
        ArrayList<CriticalPoint> middleCellCP = CriticalPointerHelper.filter(sortedPoints, beforeX);

        return CriticalPointerHelper.unsorted(middleCellCP, this.criticalPoints);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected ArrayList<CriticalPoint> getRemainingPoints(ArrayList<CriticalPoint> sortedPoints, int cpIndex) {
        if (cpIndex == INDEX_NOT_FOUNDED) return  null;

        IPoint splitPoint = sortedPoints.get(cpIndex).getVertices();
        Predicate<CriticalPoint> afterX = criticalPoint -> criticalPoint.getVertices().getX() >= splitPoint.getX();
        return CriticalPointerHelper.filter(sortedPoints, afterX);
    }
}
