package com.dji.gsdemo.gmapsteste.controllers.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapController {
    private final GoogleMap googleMap;

    public MapController(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
    }

    public void goToLocation(double latitude, double longitude) {
        LatLng cornelio = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cornelio));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void addPolygon(PolygonOptions options) {
        googleMap.addPolygon(options);
    }

    public void addPolyline(PolylineOptions options) {
        googleMap.addPolyline(options);
    }

    public void addPoint(MarkerOptions options) {
        googleMap.addMarker(options);
    }
}
