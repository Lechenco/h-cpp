package com.dji.gsdemo.gmapsteste.adapter.map;

import android.graphics.Color;

import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Arrays;

import boustrophedon.provider.primitives.Polygon;

public class PolygonAdapter {
    public static PolygonOptions toPolygonOptions(Polygon polygon) {
        return new PolygonOptions().add(
                polygon.toLatLngArray()
        ).fillColor(Color.GREEN);
    }

    public static PolygonOptions addHole(PolygonOptions options, Polygon hole) {
        return options.addHole(Arrays.asList(hole.toLatLngArray()));
    }
}
