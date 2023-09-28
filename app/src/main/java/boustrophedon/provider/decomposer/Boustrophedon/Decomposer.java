package boustrophedon.provider.decomposer.Boustrophedon;

import java.util.ArrayList;

import boustrophedon.controllers.decomposer.Boustrophedon.Splitters.SplitterController;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.DecomposerConfig;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IPolygonDecomposer;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.factories.decomposer.Boustrophedon.CriticalPoint.CriticalPointFactory;
import boustrophedon.provider.decomposer.Boustrophedon.CriticalPoint.CriticalPoint;
import boustrophedon.provider.graph.MatrixAdjacency;
import boustrophedon.provider.graph.Node;

public class Decomposer implements IPolygonDecomposer {
    private DecomposerConfig config;
    private ArrayList<CriticalPoint> criticalPoints;

    private MatrixAdjacency<Node<ICell>> matrixAdjacency;

    public Decomposer() {
        this.setConfig(new DecomposerConfig());
    }

    @Override
    public void setConfig(DecomposerConfig config) {
        this.config = config;
    }

    @Override
    public MatrixAdjacency<Node<ICell>> decompose(IPolygon polygon) throws ExceedNumberOfAttempts {
        this.criticalPoints = CriticalPointFactory.execute(polygon);

        SplitterController splitterController = new SplitterController(this.criticalPoints);
        this.matrixAdjacency = splitterController.execute();
        return this.matrixAdjacency;
    }

    @Override
    public MatrixAdjacency<Node<ICell>> getMatrixAdjacency() {
        return this.matrixAdjacency;
    }

    @Override
    public ArrayList<ICell> getCells() {
        return null;
    }
}
