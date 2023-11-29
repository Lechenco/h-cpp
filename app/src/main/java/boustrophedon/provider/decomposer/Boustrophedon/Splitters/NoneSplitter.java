package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import java.util.ArrayList;
import java.util.Optional;

import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;

public class NoneSplitter extends OutSplitter {

    public NoneSplitter(ArrayList<CriticalPoint> criticalPoints) {
        super(criticalPoints);
    }

    @Override
    public void split(ICriticalPoint splitPoint) throws ExceedNumberOfAttempts {
        this.cells = new ArrayList<>();

        Optional<CriticalPoint> inPointOptional = this
                .criticalPoints.stream().filter(cp -> cp.getEvent() == Events.IN)
                .findFirst();
        if (inPointOptional.isPresent()) {
            this.populateCells(this.criticalPoints, inPointOptional.get());
        } else {
            ICell cell = CellHelper.createCell(this.criticalPoints);
            this.cells.add(cell);
        }
    }

    @Override
    public ArrayList<CriticalPoint> getRemainingPoints() {
        return null;
    }
}
