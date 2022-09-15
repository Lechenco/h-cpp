package boustrophedon.model;
import android.icu.text.Edits;

import java.util.ArrayList;
import java.util.Iterator;

public interface IPolygonDecomposer {
    void decompose(IPolygon polygon);
    ArrayList<IPolygon> getSubPolygon(IPoint position);

    ArrayList<IPoint> getCriticalInflectionPoints(IPolygon polygon);
    double getInteriorAngleOfVertex(IPoint vertex);
    void sortVertices(ArrayList<IPoint> vertices);
    void createCells(ArrayList<IPoint> criticalVertices, IPolygon polygon);
    boolean findAbovePoint(IPoint above, IPoint criticalPoint, ArrayList<IPoint> points, IPolygon polygon);
    boolean findBelowPoint(IPoint below, IPoint criticalPoint, ArrayList<IPoint> points, IPolygon polygon);
    void sliceNewCell(IPoint upper, IPoint lower);
    Iterator<ICell> findWorkingCell(IPoint upper, IPoint lower);
    Iterator<ICell> getClosestCell(IPoint position);
    ArrayList<ICell> visitCells(Iterator<ICell> startingCell);
    ArrayList<ICell> generateCellTree(Iterator<ICell> root);
    ArrayList<ArrayList<ICell>> determineCellsToMerge(ArrayList<ICell> cells);
    ICell mergeCells(ICell c1, ICell c2);
    ArrayList<IPolygon> toPolygons(ArrayList<ICell> cells);
}
