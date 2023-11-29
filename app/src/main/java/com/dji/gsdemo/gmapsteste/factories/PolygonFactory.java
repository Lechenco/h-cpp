package com.dji.gsdemo.gmapsteste.factories;

import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class PolygonFactory {
    public static IPolygon generateSquare(double centerX, double centerY, double size) {
        return new Polygon(
                new Point(centerX - 1 * size, centerY),
                new Point(centerX, centerY + 1 * size),
                new Point(centerX + 1 * size, centerY),
                new Point(centerX, centerY - 1 * size)
        );
    }
    public static IPolygon generateMiddleOut(double centerX, double centerY, double size) {
        return new Polygon(
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
    }
}
