package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import java.util.ArrayList;
import java.util.Stack;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class MiddleSplitter extends Splitter {
    static int NUMBER_OF_ATTEMPTS = 5;
    private final Stack<ICriticalPoint> walked = new Stack<>();
    private final Stack<ICriticalPoint> deadEnd = new Stack<>();
    public MiddleSplitter(ArrayList<ICriticalPoint> criticalPoints) {
        super(criticalPoints);
    }

    @Override
    void populateCells(ArrayList<ICriticalPoint> cellPoints, ICriticalPoint splitPoint) throws ExceedNumberOfAttempts {
        walked.clear();
        deadEnd.clear();
        walked.push(splitPoint);
        boolean looped = false;
        int walkSizeLastTime = 0;
        int attempts = 0;
        while (!looped && attempts < NUMBER_OF_ATTEMPTS) {
            this.walk(cellPoints);
            looped = connectsWithEdges(splitPoint) && walked.size() > 2;

            if (!looped && walked.size() == walkSizeLastTime) {
                deadEnd.push(walked.size() > 1 ? walked.pop() : walked.peek());
                attempts++;
            }
            walkSizeLastTime = walked.size();
        }

        if (attempts == NUMBER_OF_ATTEMPTS)
            throw new ExceedNumberOfAttempts();

        this.cells.add(CellHelper.createCell(new ArrayList<>(walked)));
    }

    private void walk(ArrayList<ICriticalPoint> cellPoints) {
        for (ICriticalPoint cp : cellPoints) {
            if (
                    !walked.contains(cp) &&
                    !deadEnd.contains(cp) &&
                    connectsWithEdges(cp)
            ) {
                walked.add(cp);
            }
        }
    }
    private boolean connectsWithEdges(ICriticalPoint cp) {
        return cp.getEdgesPoints().contains(walked.peek().getVertices());
    }
}
