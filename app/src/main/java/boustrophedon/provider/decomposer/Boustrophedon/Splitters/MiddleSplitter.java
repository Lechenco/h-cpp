package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import java.util.ArrayList;
import java.util.Stack;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class MiddleSplitter extends Splitter {
    static int NUMBER_OF_ATTEMPTS = 5;
    private final Stack<CriticalPoint> walked = new Stack<>();
    private final Stack<CriticalPoint> deadEnd = new Stack<>();
    public MiddleSplitter(ArrayList<CriticalPoint> criticalPoints) {
        super(criticalPoints);
    }

    @Override
    void populateCells(ArrayList<CriticalPoint> cellPoints, CriticalPoint splitPoint) throws ExceedNumberOfAttempts {
        walked.clear();
        deadEnd.clear();
        walked.push(splitPoint);
        boolean looped = false;
        int walkSizeLastTime = 0;
        int attempts = 0;
        while (!looped && attempts < NUMBER_OF_ATTEMPTS) {
            this.walk(cellPoints);
            looped = connectsWithEdges(splitPoint) && walked.size() > 2;

            if (!looped && walked.size() == walkSizeLastTime && walked.size() > 1) {
                deadEnd.push(walked.pop());
                attempts++;
            }
            walkSizeLastTime = walked.size();
        }

        if (attempts == NUMBER_OF_ATTEMPTS)
            throw new ExceedNumberOfAttempts();

        this.cells.add(CellHelper.createCell(new ArrayList<>(walked)));
    }

    private void walk(ArrayList<CriticalPoint> cellPoints) {
        for (CriticalPoint cp : cellPoints) {
            if (
                    !walked.contains(cp) &&
                    !deadEnd.contains(cp) &&
                    connectsWithEdges(cp)
            ) {
                walked.add(cp);
            }
        }
    }
    private boolean connectsWithEdges(CriticalPoint cp) {
        return cp.getEdgesPoints().contains(walked.peek().getVertices());
    }
}
