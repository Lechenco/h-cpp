package com.dji.gsdemo.gmapsteste.adapter.map;

import android.graphics.Color;

import com.google.android.gms.maps.model.PolygonOptions;

import java.util.Arrays;

import boustrophedon.domain.decomposer.enums.SubareaTypes;
import boustrophedon.domain.primitives.model.IPolygon;

public class PolygonAdapter {
    public static PolygonOptions toPolygonOptions(IPolygon polygon) {
        return PolygonAdapter.toPolygonOptions(polygon, SubareaTypes.NORMAL);
    }
    public static PolygonOptions toPolygonOptions(IPolygon polygon, SubareaTypes type) {
        int color = type == SubareaTypes.NORMAL ? Color.argb(100, 230,238, 156)
                : Color.argb(100, 255,82, 82);
        return new PolygonOptions().add(
                polygon.toLatLngArray()
        ).fillColor(color).strokeWidth(5F);
    }

    public static PolygonOptions addHole(PolygonOptions options, IPolygon hole) {
        return options.addHole(Arrays.asList(hole.toLatLngArray()));
    }
}
