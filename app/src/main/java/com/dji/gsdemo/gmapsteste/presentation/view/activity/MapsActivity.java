package com.dji.gsdemo.gmapsteste.presentation.view.activity;

import static java.security.AccessController.getContext;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.dji.gsdemo.gmapsteste.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.dji.gsdemo.gmapsteste.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import boustrophedon.Boustrophedon;
import boustrophedon.model.IPoint;
import boustrophedon.provider.Point;
import boustrophedon.provider.Polygon;
import boustrophedon.provider.Polyline;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng cornelio = new LatLng(-23.1813, -50.648);
//        mMap.addMarker(new MarkerOptions().position(cornelio).title("Cornélio Procópio"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cornelio));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));


        double squareSize = 0.005;

        double angle = Math.PI / 4;
        //Call Boustrophedon Function
//        Polygon square = new Polygon(
//                new Point(cornelio.latitude - squareSize, cornelio.longitude + squareSize),
//                new Point(cornelio.latitude + squareSize, cornelio.longitude + squareSize),
//                new Point(cornelio.latitude + squareSize, cornelio.longitude - squareSize),
//                new Point(cornelio.latitude - squareSize, cornelio.longitude - squareSize)
//        );

        Polygon square = new Polygon(
                new Point(cornelio.latitude - 2 * squareSize, cornelio.longitude),
                new Point(cornelio.latitude, cornelio.longitude + 2 * squareSize),
//                new Point(cornelio.latitude + 2 * squareSize, cornelio.longitude),
                new Point(cornelio.latitude, cornelio.longitude - 2 * squareSize)
        );

        Polyline path = (Polyline) new Boustrophedon(square).generatePath();

        PolygonOptions polyOptions = new PolygonOptions().add(
                square.toLatLngArray()
        ).fillColor(Color.GREEN);
        mMap.addPolygon(polyOptions);

        PolylineOptions polylineOptions = new PolylineOptions().add(
                path.toLatLngArray()
        ).color(Color.RED);

        mMap.addPolyline(polylineOptions);

//        for (LatLng p : polylineOptions.getPoints()) {
//            mMap.addMarker(new MarkerOptions().position(p));
//        }
    }
}