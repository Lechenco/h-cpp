package com.dji.gsdemo.gmapsteste.adapter.map;

import android.graphics.Color;

import com.google.android.gms.maps.model.PolylineOptions;

import boustrophedon.provider.primitives.Polygon;

public class PolylineAdapter {
    public static PolylineOptions toPolylineOptions(Polygon polygon) {
        return new PolylineOptions().add(
                polygon.toLatLngArray()
        ).color(Color.RED);
    }
}
