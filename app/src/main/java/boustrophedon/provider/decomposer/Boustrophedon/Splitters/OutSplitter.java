package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import java.util.ArrayList;
import java.util.Stack;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class OutSplitter extends Splitter {
    static int NUMBER_OF_ATTEMPTS = 5;
    private final Stack<ICriticalPoint> walked = new Stack<>();
    private final Stack<ICriticalPoint> deadEnd = new Stack<>();
    public OutSplitter(ArrayList<ICriticalPoint> criticalPoints) {
        super(criticalPoints);
    }

    @Override
    void populateCells(ArrayList<ICriticalPoint> cellPoints, ICriticalPoint splitPoint) throws ExceedNumberOfAttempts {
        for (ICriticalPoint intersection : splitPoint.getIntersectionsInNormal()) {
            populateCells(cellPoints, splitPoint, intersection);
        }
    }

    protected void populateCells(ArrayList<ICriticalPoint> cellPoints, ICriticalPoint splitPoint, ICriticalPoint entryPoint) throws ExceedNumberOfAttempts {
            walked.clear();
            deadEnd.clear();
            walked.push(splitPoint);
            walked.push(entryPoint);
            this.walkUntilLoop(cellPoints, splitPoint);

            this.cells.add(CellHelper.createCell(new ArrayList<>(walked)));
    }

    private void walkUntilLoop(ArrayList<ICriticalPoint> cellPoints, ICriticalPoint goal) throws ExceedNumberOfAttempts {
        boolean looped = false;
        int walkSizeLastTime = 0;
        int attempts = 0;
        while (!looped && attempts < NUMBER_OF_ATTEMPTS) {
            this.walk(cellPoints, goal);
            looped = connectsWithEdges(goal) && walked.size() > 2;

            if (!looped && walked.size() == walkSizeLastTime) {
                deadEnd.push(walked.size() > 1 ? walked.pop() : walked.peek());
                attempts++;
            }
            walkSizeLastTime = walked.size();
        }

        if (attempts == NUMBER_OF_ATTEMPTS)
            throw new ExceedNumberOfAttempts();
    }

    private void walk(ArrayList<ICriticalPoint> cellPoints, ICriticalPoint goal) {
        for (ICriticalPoint cp : cellPoints) {
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
    private boolean connectsWithEdges(ICriticalPoint cp) {
        return cp.getEdgesPoints().contains(walked.peek().getVertices());
    }
}
