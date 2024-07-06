package boustrophedon.integrationTest;

import static boustrophedon.constants.AngleConstants.NINETY_DEGREES;

import com.dji.gsdemo.gmapsteste.utils.samples.JSONReader;
import com.dji.gsdemo.gmapsteste.utils.samples.SampleFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.decomposer.model.IDecomposer;
import boustrophedon.domain.graph.error.ElementNotFoundedException;
import boustrophedon.domain.graph.model.IAdjacencyMatrix;
import boustrophedon.domain.graph.model.INode;
import boustrophedon.domain.graph.model.IObjectiveFunction;
import boustrophedon.domain.graph.model.IObjectiveMatrix;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.domain.walkers.error.AngleOffLimitsException;
import boustrophedon.helpers.walkers.WalkerHelper;
import boustrophedon.provider.decomposer.Boustrophedon.AreaDecomposer;
import boustrophedon.provider.decomposer.Boustrophedon.Cell.CellHelper;
import boustrophedon.provider.graph.CenterOfMassFunction;
import boustrophedon.provider.graph.ObjectiveMatrix;
import boustrophedon.provider.primitives.Polyline;
import boustrophedon.provider.walkers.Walker;
import boustrophedon.utils.AngleUtils;

public class BaseIntegrationTest {
    public static double DEFAULT_DISTANCE_BETWEEN_PATHS = 0.000105472;
    public static double DEFAULT_DISTANCE_BETWEEN_PATHS_SPECIAL = 0.000052736;// 0.000026368;
    public static double DEFAULT_DIRECTION = NINETY_DEGREES;

    public ResultMetrics runNTimes(String filename, int times, boolean asSpecial) throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        ArrayList<ResultMetrics> metricArray = new ArrayList<>();

        run(filename, asSpecial);

        for (int i = 0; i < times; i++) {
            metricArray.add(run(filename, asSpecial));
            System.out.printf("[%d/%d]: %s%n", i, times, metricArray.get(metricArray.size() -1));
        }

        return calcAverageMetric(metricArray);
    }

    private ResultMetrics calcAverageMetric(ArrayList<ResultMetrics> metricsArray) {
        ResultMetrics metrics = new ResultMetrics();

        ArrayList<Double> totals = metricsArray.stream().map(m -> m.totalDuration).collect(Collectors.toCollection(ArrayList::new));
        metrics.totalDuration = calcAverage(totals);
        metrics.totalDurationStd = calcStandardDeviation(metrics.totalDuration, totals);

        ArrayList<Double> decomposes = metricsArray.stream().map(m -> m.decomposeDuration).collect(Collectors.toCollection(ArrayList::new));
        metrics.decomposeDuration = calcAverage(decomposes);
        metrics.decomposeDurationStd = calcStandardDeviation(metrics.decomposeDuration, decomposes);

        ArrayList<Double> objectives = metricsArray.stream().map(m -> m.objectiveDuration).collect(Collectors.toCollection(ArrayList::new));
        metrics.objectiveDuration = calcAverage(objectives);
        metrics.objectiveDurationStd = calcStandardDeviation(metrics.objectiveDuration, objectives);

        ArrayList<Double> cellOrders = metricsArray.stream().map(m -> m.cellOrderDuration).collect(Collectors.toCollection(ArrayList::new));
        metrics.cellOrderDuration = calcAverage(cellOrders);
        metrics.cellOrderDurationStd = calcStandardDeviation(metrics.cellOrderDuration, cellOrders);

        ArrayList<Double> walkAlls = metricsArray.stream().map(m -> m.walkAllDuration).collect(Collectors.toCollection(ArrayList::new));
        metrics.walkAllDuration = calcAverage(walkAlls);
        metrics.walkAllDurationStd = calcStandardDeviation(metrics.walkAllDuration, walkAlls);

        metrics.pathLength = metricsArray.get(0).pathLength;
        return metrics;
    }

    private double calcAverage(ArrayList<Double> arrayList) {
        double sum = arrayList.stream().reduce(0.0, Double::sum);

        return sum / arrayList.size();
    }

    private double calcStandardDeviation(double average, ArrayList<Double> arrayList) {
        double aux = arrayList.stream().reduce(0.0, (a, b) -> a + Math.pow(b - average, 2));
        return Math.sqrt(aux);
    }

    public ResultMetrics run(String filename, boolean asSpecial) throws ElementNotFoundedException, AngleOffLimitsException, ExceedNumberOfAttempts {
        SampleFile sample = this.loadSample(filename);
        IArea area = sample.generateArea();
        IPoint startedPoint = sample.generateStartPosition();

       return run(area, startedPoint, asSpecial);
    }

    private ResultMetrics run (IArea area, IPoint startedPoint, boolean asSpecial) throws ExceedNumberOfAttempts, ElementNotFoundedException, AngleOffLimitsException {
        ResultMetrics metrics = new ResultMetrics();
        // Decomposer
        ResultMetrics.startTimer();
        IAdjacencyMatrix<INode<ICell>> adjacencyMatrix = decompose(area);
        metrics.decomposeDuration = ResultMetrics.endTimer();

        // Calc Objective
        ResultMetrics.startTimer();
        IObjectiveMatrix<IPolygon> objectiveMatrix = calcObjective(adjacencyMatrix);
        metrics.objectiveDuration = ResultMetrics.endTimer();

        // Calc Best Cell Order
        ResultMetrics.startTimer();
        Collection<Integer> cellsOrder = calcCellsOrder(adjacencyMatrix, objectiveMatrix, startedPoint);
        metrics.cellOrderDuration = ResultMetrics.endTimer();

        // WalkAll
        ResultMetrics.startTimer();
        IPolyline path = walkAll(adjacencyMatrix, cellsOrder, startedPoint, asSpecial);
        metrics.walkAllDuration = ResultMetrics.endTimer();

        metrics.pathLength =  getLength(path);
        metrics.calcTotalDuration();

        return metrics;
    }


    private IAdjacencyMatrix<INode<ICell>> decompose(IArea area) throws ExceedNumberOfAttempts {
        IDecomposer<IArea> decomposer = new AreaDecomposer();
        return decomposer.decompose(area);
    }

    private IObjectiveMatrix<IPolygon> calcObjective(IAdjacencyMatrix<INode<ICell>> adjacencyMatrix) {
        ArrayList<IPolygon> polygons = adjacencyMatrix
                .getNodes().stream().map(node -> node.getObject().getPolygon())
                .collect(Collectors.toCollection(ArrayList::new));
        IObjectiveFunction<IPolygon> function = new CenterOfMassFunction();
        IObjectiveMatrix<IPolygon> objectiveMatrix = new ObjectiveMatrix<>(polygons, function);

        objectiveMatrix.calcObjective();

        return  objectiveMatrix;
    }

    private Collection<Integer> calcCellsOrder(
            IAdjacencyMatrix<INode<ICell>> adjacencyMatrix,
           IObjectiveMatrix<IPolygon> objectiveMatrix,
            IPoint startedPoint
    ) throws ElementNotFoundedException {
        ArrayList<ICell> cells = adjacencyMatrix.getNodes().stream()
                .map(INode::getObject).collect(Collectors.toCollection(ArrayList::new));
        ICell starterCell = CellHelper.getClosestCellToPoint(cells, startedPoint);
        Collection<Integer> cellsOrder = new ArrayList<>();
        cellsOrder.add(cells.indexOf(starterCell));

        int currentIndex = cells.indexOf(starterCell);
        while(cellsOrder.size() < objectiveMatrix.getNodes().size()) {
            currentIndex = objectiveMatrix.getBestObjectiveIndexExcept(currentIndex, cellsOrder);
            cellsOrder.add(currentIndex);
        }
        return  cellsOrder;
    }

    private IPolyline walkAll(
            IAdjacencyMatrix<INode<ICell>> adjacencyMatrix,
            Collection<Integer> cellsOrder,
            IPoint startedPoint,
            boolean asSpecial
    ) throws AngleOffLimitsException {
        ArrayList<ICell> cells = adjacencyMatrix.getNodes().stream()
                .map(INode::getObject).collect(Collectors.toCollection(ArrayList::new));
        IPolyline path = new Polyline();
        if (startedPoint != null) path.add(startedPoint);

        for (int index: cellsOrder) {
            ICell cell = cells.get(index);
            IPoint startPoint = path.getNumberOfPoints() > 0
                    ? WalkerHelper.getClosestMaximizedAnglePoint(cell.getPolygon(), path.getLastPoint(), AngleUtils.add90Degrees(DEFAULT_DIRECTION))
                    : null;
            IPolyline polyline = this.walk(cell.getPolygon(), startPoint, cell.getSubarea().getSubareaType(), asSpecial);

            path.add(polyline.getPoints());
        }

        return path;
    }

    private IPolyline walk(IPolygon polygon, IPoint initialPoint, SubareaTypes subareaType, boolean asSpecial) throws AngleOffLimitsException {
        Walker walker = new Walker.WalkerBuilder()
                .withDistanceBetweenPaths(asSpecial || subareaType == SubareaTypes.SPECIAL ? DEFAULT_DISTANCE_BETWEEN_PATHS_SPECIAL : DEFAULT_DISTANCE_BETWEEN_PATHS)
                .atDirection(DEFAULT_DIRECTION).build();

        return walker.walk(
                polygon,
                initialPoint != null ? initialPoint : polygon.getPoints().get(0)
        );
    }

    private double getLength(IPolyline path) {
        if (path == null) {
            return 0;
        }

        return SphericalUtil.computeLength(Arrays.asList(path.toLatLngArray()));
    }

    private SampleFile loadSample(String sampleName) {
        String jsonFileString = JSONReader.getJsonFromAssets( "examples/" + sampleName + ".json");

        Gson gson = new Gson();
        Type sampleType = new TypeToken<SampleFile>() { }.getType();

        return gson.fromJson(jsonFileString, sampleType);
    }
}
