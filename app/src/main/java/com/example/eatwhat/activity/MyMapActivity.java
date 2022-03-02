package com.example.eatwhat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.eatwhat.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// Implement OnMapReadyCallback.
public class MyMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private String inputLocation = "";
    private Marker myMarker;
    private double myLong = 0;
    private double myLat = 0;
    GoogleMap myGoogleMap = null;


    private void resetToMyCurrentLocation() {
        Button myLocation = (Button) findViewById(R.id.my_location);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);
        myLong = getIntent().getDoubleExtra("Longitude", 0);
        myLat = getIntent().getDoubleExtra("Latitude", 0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    // Get a handle to the GoogleMap object and display marker.
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap) {
        //updateLocationUI();
        //getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("do not have permission");
            return;
        }
        System.out.println("have permission");
        myGoogleMap = googleMap;
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        LatLng latLng = null;
                        if (myLong != 0 && myLat != 0) {
                            if (myMarker != null) {
                                myMarker.remove();
                            }
                            latLng = new LatLng(myLat, myLong);
                            myMarker = googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Marker"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
                            return;
                        }

                        if (location != null) {
                            myLong = location.getLongitude();
                            myLat = location.getLatitude() ;
                            latLng = new LatLng(myLat, myLong);

                            myMarker = googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Marker"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));

                            Geocoder geocoder = new Geocoder(MyMapActivity.this, Locale.getDefault());

                            try {
                                List<Address> addresses = null;
                                addresses = geocoder.getFromLocation(myLat, myLong, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                myLong = latLng.longitude;
                myLat = latLng.latitude;
                myMarker.remove();
                myMarker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Marker"));

                System.out.println(myLong + "     " + myLat + "FROM change location");
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent backMain = new Intent();
        backMain.putExtra("Longitude", myLong);
        backMain.putExtra("Latitude", myLat);
        setResult(Activity.RESULT_OK, backMain);
        System.out.println(myLong + "     " + myLat + "FROM  on back pressed");
        super.onBackPressed();
    }
}