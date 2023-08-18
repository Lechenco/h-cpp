package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Stack;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class OutSplitter extends Splitter {
    static int NUMBER_OF_ATTEMPTS = 5;
    private final Stack<CriticalPoint> walked = new Stack<>();
    private final Stack<CriticalPoint> deadEnd = new Stack<>();
    public OutSplitter(ArrayList<CriticalPoint> criticalPoints) {
        super(criticalPoints);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void populateCells(ArrayList<CriticalPoint> cellPoints, CriticalPoint splitPoint) throws ExceedNumberOfAttempts {
        for (CriticalPoint intersection : splitPoint.getIntersectionsInNormal()) {
            populateCells(cellPoints, splitPoint, intersection);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void populateCells(ArrayList<CriticalPoint> cellPoints, CriticalPoint splitPoint, CriticalPoint entryPoint) throws ExceedNumberOfAttempts {
            walked.clear();
            deadEnd.clear();
            walked.push(splitPoint);
            walked.push(entryPoint);
            this.walkUntilLoop(cellPoints, splitPoint);

            this.cells.add(CellHelper.createCell(new ArrayList<>(walked)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void walkUntilLoop(ArrayList<CriticalPoint> cellPoints, CriticalPoint goal) throws ExceedNumberOfAttempts {
        boolean looped = false;
        int attempts = 0;
        while (!looped && attempts < NUMBER_OF_ATTEMPTS) {
            this.walk(cellPoints, goal);
            looped = connectsWithEdges(goal) && walked.size() > 2;

            if (!looped && walked.size() < 2) {
                deadEnd.push(walked.pop());
            }
            attempts++;
        }

        if (attempts == NUMBER_OF_ATTEMPTS)
            throw new ExceedNumberOfAttempts();
    }

    private void walk(ArrayList<CriticalPoint> cellPoints, CriticalPoint goal) {
        for (CriticalPoint cp : cellPoints) {
            if (
                    !walked.contains(cp) &&
                            !deadEnd.contains(cp) &&
                            connectsWithEdges(cp)
            ) {
                walked.add(cp);
            }
            if (walked.size() > 2 && connectsWithEdges(goal)) return;
        }
    }
    private boolean connectsWithEdges(CriticalPoint cp) {
        return cp.getEdgesPoints().contains(walked.peek().getVertices());
    }
}
