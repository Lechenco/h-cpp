package com.dji.gsdemo.gmapsteste.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.dji.gsdemo.gmapsteste.R;
import com.dji.gsdemo.gmapsteste.adapter.map.PolygonAdapter;
import com.dji.gsdemo.gmapsteste.controller.map.MapController;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.dji.gsdemo.gmapsteste.databinding.ActivityMapsBinding;

import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private MapController mapController;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapController = new MapController(googleMap);

        // Add a marker in Sydney and move the camera
        LatLng cornelio = new LatLng(-23.1813, -50.648);
//        mMap.addMarker(new MarkerOptions().position(cornelio).title("Cornélio Procópio"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(cornelio));
//        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));


        double squareSize = 0.005;

        double angle = Math.PI / 4;

        Polygon square = new Polygon(
                new Point(cornelio.latitude - 2 * squareSize, cornelio.longitude),
                new Point(cornelio.latitude, cornelio.longitude + 2 * squareSize),
//                new Point(cornelio.latitude + 2 * squareSize, cornelio.longitude),
                new Point(cornelio.latitude, cornelio.longitude - 2 * squareSize)
        );

        mapController.addPolygon(PolygonAdapter.toPolygonOptions(square));

//        for (LatLng p : polylineOptions.getPoints()) {
//            mMap.addMarker(new MarkerOptions().position(p));
//        }
    }
}