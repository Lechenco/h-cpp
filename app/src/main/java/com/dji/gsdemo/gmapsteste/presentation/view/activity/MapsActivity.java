package com.dji.gsdemo.gmapsteste.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.dji.gsdemo.gmapsteste.R;
import com.dji.gsdemo.gmapsteste.adapter.map.PolygonAdapter;
import com.dji.gsdemo.gmapsteste.adapter.map.PolylineAdapter;
import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.controllers.coveragePathPlanning.CoveragePathPlanningController;
import com.dji.gsdemo.gmapsteste.runnables.coveragePathPlanning.WalkerRunnable;
import com.dji.gsdemo.gmapsteste.controllers.map.MapController;
import com.dji.gsdemo.gmapsteste.factories.PolygonFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.dji.gsdemo.gmapsteste.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IPolygon;
import boustrophedon.domain.primitives.model.IPolyline;
import boustrophedon.provider.primitives.Point;
import boustrophedon.provider.primitives.Polygon;
import dk.brics.automaton.RunAutomaton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final double AREA_SIZE = 0.005;
    private MapController mapController;
    private CoveragePathPlanningController coveragePathPlanningController;
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


    Handler handler = new Handler();

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapController = new MapController(googleMap);
        coveragePathPlanningController = new CoveragePathPlanningController(handler);

        double latitude = Double.parseDouble(getString(R.string.CORNELIO_LATITUDE));
        double longitude = Double.parseDouble(getString(R.string.CORNELIO_LONGITUDE));

        mapController.goToLocation(latitude, longitude);

        IPolygon middleOutPolygon = PolygonFactory.generateMiddleOut(latitude, longitude, AREA_SIZE);
        work(middleOutPolygon);
    }

    ArrayList<ICell> cells;
    private void work(IPolygon polygon) {
        Runnable runnable = () -> {
            try {
                IPolyline path = coveragePathPlanningController.generateFinalPathSync(polygon, new Point(0,0));
                handler.post(() -> mapController.addPolyline(
                    PolylineAdapter
                        .toPolylineOptions(path)
                        .color(Color.argb(200, 54, 173, 56))
                    )
                );

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        this.coveragePathPlanningController.decompose(polygon, new RunnableCallback<ArrayList<ICell>>() {
            final ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.MAGENTA, Color.YELLOW));
            @Override
            public void onComplete(ArrayList<ICell> result) {
                cells = result;
                for (int i = 0; i < result.size(); i++) {
                    mapController.addPolygon(
                            PolygonAdapter
                                    .toPolygonOptions((Polygon) result.get(i).getPolygon())
                                    .fillColor(colors.get(i)));
                }

//                new Thread(runnable).start();
                walkCell(0, 3);
            }
            @Override
            public void onError(Exception e) {
                mapController.addPolygon(PolygonAdapter.toPolygonOptions(polygon)
                        .fillColor(Color.argb(33, 0, 200, 0))
                );
            }
        });
    }

    public void walkCell(int i, int finish) {
        coveragePathPlanningController.walk(cells.get(i), new RunnableCallback<IPolyline>() {
            @Override
            public void onComplete(IPolyline result) {
                handler.post(() -> mapController.addPolyline(
                                PolylineAdapter
                                        .toPolylineOptions(result)
                                        .color(Color.argb(200, 54, 173, 56))
                        )
                );

                if (i < finish) walkCell(i +1, finish);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

}