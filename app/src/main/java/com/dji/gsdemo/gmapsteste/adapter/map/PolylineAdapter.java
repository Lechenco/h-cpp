package com.dji.gsdemo.gmapsteste.adapter.map;

import android.graphics.Color;

import com.google.android.gms.maps.model.PolylineOptions;

import boustrophedon.domain.primitives.model.IPolyline;

public class PolylineAdapter {
    public static PolylineOptions toPolylineOptions(IPolyline polyline) {
        return new PolylineOptions().add(
                polyline.toLatLngArray()
        ).color(Color.RED);
    }
}
