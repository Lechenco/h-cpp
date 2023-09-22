package com.dji.gsdemo.gmapsteste.controller.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapController {
    private final GoogleMap googleMap;

    public MapController(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void addPolygon(PolygonOptions options) {
        googleMap.addPolygon(options);
    }

    public void addPolyline(PolylineOptions options) {
        googleMap.addPolyline(options);
    }
}
