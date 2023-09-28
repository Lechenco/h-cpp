package com.dji.gsdemo.gmapsteste.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.dji.gsdemo.gmapsteste.R;
import com.dji.gsdemo.gmapsteste.adapter.map.PolygonAdapter;
import com.dji.gsdemo.gmapsteste.controller.coveragePathPlanning.DecomposerController;
import com.dji.gsdemo.gmapsteste.controller.map.MapController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.dji.gsdemo.gmapsteste.databinding.ActivityMapsBinding;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
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

        // Add a marker in Cornélio and move the camera
        LatLng cornelio = new LatLng(-23.1813, -50.648);
//        googleMap.addMarker(new MarkerOptions().position(cornelio).title("Cornélio Procópio"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cornelio));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));


        double squareSize = 0.005;

        double angle = Math.PI / 4;

//        Polygon square = new Polygon(
//                new Point(cornelio.latitude - 2 * squareSize, cornelio.longitude),
//                new Point(cornelio.latitude, cornelio.longitude + 2 * squareSize),
//                new Point(cornelio.latitude + 2 * squareSize, cornelio.longitude),
//                new Point(cornelio.latitude, cornelio.longitude - 2 * squareSize)
//        );

        Polygon middleOutPolygon = new Polygon(
                new Point(cornelio.latitude + 0, cornelio.longitude + 0),
                new Point(cornelio.latitude + 1, cornelio.longitude + 0),
                new Point(cornelio.latitude + 1.5, cornelio.longitude + 0.5), // MIDDLE Event
                new Point(cornelio.latitude + 2, cornelio.longitude + 0),
                new Point(cornelio.latitude + 3, cornelio.longitude + 0),
                new Point(cornelio.latitude + 3, cornelio.longitude + 3),
                new Point(cornelio.latitude + 0, cornelio.longitude + 3),
                new Point(cornelio.latitude + 0, cornelio.longitude + 2),
                new Point(cornelio.latitude + 0.5, cornelio.longitude + 1.5), // OUT Event
                new Point(cornelio.latitude + 0, cornelio.longitude + 1)
        );
        DecomposerController decomposerController = new DecomposerController();
        try {
            ArrayList<ICell> cells = decomposerController.decompose(middleOutPolygon);
            cells.forEach(cell -> mapController
                    .addPolygon(
                            PolygonAdapter
                                    .toPolygonOptions((Polygon) cell.getPolygon())
                                    .fillColor(Color.argb(33, 0, 0, 200))
                    ));
        } catch (ExceedNumberOfAttempts e) {
//            throw new RuntimeException(e);
            mapController.addPolygon(PolygonAdapter.toPolygonOptions(middleOutPolygon)
                    .fillColor(Color.argb(33, 0, 200, 0))
            );
        }

//        for (LatLng p : polylineOptions.getPoints()) {
//            mMap.addMarker(new MarkerOptions().position(p));
//        }
    }
}