package com.dji.gsdemo.gmapsteste.presentation.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.dji.gsdemo.gmapsteste.R;
import com.dji.gsdemo.gmapsteste.adapter.map.PolygonAdapter;
import com.dji.gsdemo.gmapsteste.adapter.map.PolylineAdapter;
import com.dji.gsdemo.gmapsteste.app.RunnableCallback;
import com.dji.gsdemo.gmapsteste.controllers.coveragePathPlanning.CoveragePathPlanningController;
import com.dji.gsdemo.gmapsteste.controllers.map.MapController;
import com.dji.gsdemo.gmapsteste.databinding.ActivityMapsBinding;
import com.dji.gsdemo.gmapsteste.utils.samples.JSONReader;
import com.dji.gsdemo.gmapsteste.utils.samples.SampleFile;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import boustrophedon.domain.decomposer.model.ICell;
import boustrophedon.domain.primitives.model.IArea;
import boustrophedon.domain.primitives.model.IPoint;
import boustrophedon.domain.primitives.model.IPolyline;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private MapController mapController;
    private CoveragePathPlanningController coveragePathPlanningController;
    private ActivityMapsBinding binding;
    String logTag = "Boustrophedon";

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

        SampleFile sample = this.loadSample("sample1");
        IArea area = sample.generateArea();
        IPoint startedPoint = sample.generateStartPosition();

        mapController.goToLocation(startedPoint.getX(), startedPoint.getY());
        mapController.addPolygon(PolygonAdapter.toPolygonOptions(area.getGeometry()));

        work(area, startedPoint);
    }

    ArrayList<ICell> cells;
    private void work(IArea area, IPoint startedPoint) {
        Runnable runnable = () -> {
            try {
                IPolyline finalPath = coveragePathPlanningController.generateFinalPathSync(area);
                getLength(finalPath);
                handler.post(() -> mapController.addPolyline(
                    PolylineAdapter
                        .toPolylineOptions(finalPath)
                    )
                );

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        this.coveragePathPlanningController.setStartPoint(startedPoint);
        this.coveragePathPlanningController.onDecomposeCallback = new RunnableCallback<ArrayList<ICell>>() {
            final ArrayList<Integer> colors = new ArrayList<>(Arrays.asList(Color.BLUE, Color.RED, Color.MAGENTA, Color.YELLOW));
            @Override
            public void onComplete(ArrayList<ICell> result) {
                cells = result;
                for (int i = 0; i < result.size(); i++) {
                    mapController.addPolygon(
                            PolygonAdapter
                                    .toPolygonOptions(result.get(i).getPolygon(), result.get(i).getSubarea().getSubareaType()));
                }
            }
            @Override
            public void onError(Exception e) {
                mapController.addPolygon(PolygonAdapter.toPolygonOptions(area.getGeometry()));
            }
        };

        new Thread(runnable).start();
    }

    private void getLength(IPolyline path) {
        if (path == null) {
            Log.e(logTag, "Path not found");
            return;
        }

        double distance = SphericalUtil.computeLength(Arrays.asList(path.toLatLngArray()));
        Log.i(logTag, "Path Length: " + distance + " meters");

    }

    public void walkCell(int i, int finish) {
        coveragePathPlanningController.onWalkCallback = new RunnableCallback<IPolyline>() {
            @Override
            public void onComplete(IPolyline result) {
                handler.post(() -> mapController.addPolyline(
                                PolylineAdapter
                                        .toPolylineOptions(result)
                        )
                );

                if (i < finish) walkCell(i +1, finish);
            }

            @Override
            public void onError(Exception e) {

            }
        };
        coveragePathPlanningController.walk(cells.get(i));
    }

    private SampleFile loadSample(String sampleName) {
        String jsonFileString = JSONReader.getJsonFromAssets(getApplicationContext(), "examples/" + sampleName + ".json");
        Log.i("data", jsonFileString);

        Gson gson = new Gson();
        Type sampleType = new TypeToken<SampleFile>() { }.getType();

        return gson.fromJson(jsonFileString, sampleType);
    }
}