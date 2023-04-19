package boustrophedon.provider;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import boustrophedon.model.IPoint;
import boustrophedon.utils.GA;

public class Point implements IPoint {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public LatLng toLatLng() {
        return new LatLng(getX(), getY());
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public IPoint walkXY(double deltaX, double deltaY) {
        return new Point(this.getX() + deltaX, this.getY() + deltaY);
    }

    @Override
    public IPoint walk(double delta, double angle) {
        double deltaX = delta * Math.cos(angle);
        double deltaY = delta * Math.sin(angle);
        return walkXY(deltaX, deltaY);
    }

    @Override
    public double calcDistance(IPoint point) {
        return GA.calcDistance(this, point);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("Point{ x=%f, y=%f }", x, y);
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Point)) return false;

        Point p = (Point) other;

        return p.getX() == this.getX() && p.getY() == this.getY();
    }
}
