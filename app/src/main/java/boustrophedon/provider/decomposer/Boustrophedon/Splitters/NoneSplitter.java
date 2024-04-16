package boustrophedon.provider.decomposer.Boustrophedon.Splitters;

import java.util.ArrayList;
import java.util.Optional;

import boustrophedon.domain.decomposer.enums.Events;
import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.ICriticalPoint;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;

public class NoneSplitter extends OutSplitter {

    public NoneSplitter(ArrayList<ICriticalPoint> criticalPoints) {
        super(criticalPoints);
    }

    @Override
    public void split(ICriticalPoint splitPoint, SubareaTypes subareaType) throws ExceedNumberOfAttempts {
        this.cells = new ArrayList<>();

        Optional<ICriticalPoint> inPointOptional = this
                .criticalPoints.stream().filter(cp -> cp.getEvent() == Events.IN)
                .findFirst();
        if (inPointOptional.isPresent()) {
            this.populateCells(this.criticalPoints, inPointOptional.get());
        } else {
            ICell cell = CellHelper.createCell(this.criticalPoints);
            this.cells.add(cell);
        }
        this.cells.forEach(cell -> cell.getSubarea().setSubareaType(subareaType));
    }

    @Override
    public ArrayList<ICriticalPoint> getRemainingPoints() {
        return null;
    }
}
