package com.dji.gsdemo.gmapsteste.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.dji.gsdemo.gmapsteste.R;
import com.dji.gsdemo.gmapsteste.adapter.map.PolygonAdapter;
import com.dji.gsdemo.gmapsteste.adapter.map.PolylineAdapter;
import com.dji.gsdemo.gmapsteste.controller.coveragePathPlanning.DecomposerController;
import com.dji.gsdemo.gmapsteste.controller.coveragePathPlanning.WalkerController;
import com.dji.gsdemo.gmapsteste.controller.map.MapController;
import com.dji.gsdemo.gmapsteste.factories.PolygonFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.dji.gsdemo.gmapsteste.databinding.ActivityMapsBinding;

import java.util.ArrayList;

import boustrophedon.domain.decomposer.error.ExceedNumberOfAttempts;
import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.provider.primitives.Polygon;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final double LATITUDE = R.fraction.CORNELIO_LATITUDE;
    public static final double LONGITUDE = R.fraction.CORNELIO_LONGITUDE;
    public static final double AREA_SIZE = 0.005;
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

        mapController.goToLocation(LATITUDE, LONGITUDE);


        Polygon middleOutPolygon = (Polygon) PolygonFactory.generateMiddleOut(LATITUDE, LONGITUDE, AREA_SIZE);
        DecomposerController decomposerController = new DecomposerController();
        try {
            ArrayList<ICell> cells = decomposerController.decompose(middleOutPolygon);
            cells.forEach(cell ->
                mapController
                    .addPolygon(
                        PolygonAdapter
                            .toPolygonOptions((Polygon) cell.getPolygon())
                            .fillColor(Color.argb(33, 0, 0, 200))
                        )
            );

            ArrayList<IPolyline> lines = WalkerController.walkAll(cells);
            lines.forEach(line -> mapController
                .addPolyline(
                    PolylineAdapter
                        .toPolylineOptions(line)
                        .color(Color.argb(200, 54, 173, 56))
            ));

        } catch (ExceedNumberOfAttempts e) {
            mapController.addPolygon(PolygonAdapter.toPolygonOptions(middleOutPolygon)
                    .fillColor(Color.argb(33, 0, 200, 0))
            );
        }
    }

}