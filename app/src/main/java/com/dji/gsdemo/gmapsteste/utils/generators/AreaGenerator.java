package com.dji.gsdemo.gmapsteste.utils.generators;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Area;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;
import boustrophedon.provider.primitives.Subarea;

public class AreaGenerator {

    private static IPolygon createSquare(double centerX, double centerY, double size) {
        return  new Polygon(
                new Point(centerX - size, centerY),
                new Point(centerX, centerY + size),
                new Point(centerX + size, centerY),
                new Point(centerX, centerY - size)
        );
    }
    public static IArea generateSquare(double centerX, double centerY, double size) {
        IPolygon square = AreaGenerator.createSquare(centerX, centerY, size);

        return new Area(new Subarea(square));
    }
    public static IArea generateSquareSpecial(double centerX, double centerY, double size) {
        IPolygon square = AreaGenerator.createSquare(centerX, centerY, size);

        IPolygon squareSpecial = AreaGenerator.createSquare(centerX, centerY, size / 2);

        return new Area(new Subarea(square), new Subarea(squareSpecial, SubareaTypes.SPECIAL));
    }

    public static IArea generateMiddleOut(double centerX, double centerY, double size) {
        IPolygon polygon = new Polygon(
                new Point(centerX + 0 * size, centerY + 0 * size),
                new Point(centerX + 1 * size, centerY + 0 * size),
                new Point(centerX + 1.5 * size, centerY + 0.5 * size), // MIDDLE Event
                new Point(centerX + 2 * size, centerY + 0 * size),
                new Point(centerX + 3 * size, centerY + 0 * size),
                new Point(centerX + 3 * size, centerY + 3 * size),
                new Point(centerX + 0 * size, centerY + 3 * size),
                new Point(centerX + 0 * size, centerY + 2 * size),
                new Point(centerX + 0.5 * size, centerY + 1.5 * size), // OUT Event
                new Point(centerX + 0 * size, centerY + 1 * size)
        );

        return new Area(new Subarea(polygon));
    }
}
