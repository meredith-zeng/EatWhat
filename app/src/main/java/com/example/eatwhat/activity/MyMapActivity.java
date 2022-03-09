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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.eatwhat.R;
import com.example.eatwhat.mainActivityFragments.RestaurantFragment;
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
import java.util.ArrayList;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);
        initSearchLocation();
        resetToMyCurrentLocation();
        myLong = getIntent().getDoubleExtra("Longitude", 0);
        myLat = getIntent().getDoubleExtra("Latitude", 0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void initSearchLocation() {
        EditText inputLocation = (EditText) findViewById(R.id.input_location);
        inputLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int ActionId, KeyEvent keyEvent) {
                if (ActionId == EditorInfo.IME_ACTION_DONE
                        || ActionId == EditorInfo.IME_ACTION_SEARCH
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENVELOPE) {
                    goToInputLocation(inputLocation);
                }
                return false;
            }
        });
    }

    private void goToInputLocation(EditText input) {
        String inputLocation = input.getText().toString();
        System.out.println("get location   " + inputLocation);
        Geocoder myGeocoder = new Geocoder(this);
        List<Address> myAddressList = new ArrayList<>();
        try {
            myAddressList = myGeocoder.getFromLocationName(inputLocation, 1);
            if (myAddressList != null && myAddressList.size() >= 1) {
                Address address = myAddressList.get(0);
                myLong = address.getLongitude();
                myLat = address.getLatitude();
                LatLng latLng = new LatLng(myLat, myLong);

                if (myMarker != null) {
                    myMarker.remove();
                }
                myMarker = myGoogleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Marker"));
                myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void resetToMyCurrentLocation() {
        Button myLocation = (Button) findViewById(R.id.my_location);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            myLong = location.getLongitude();
                            myLat = location.getLatitude() ;
                            LatLng latLng = new LatLng(myLat, myLong);

                            if (myMarker != null) {
                                myMarker.remove();
                            }
                            myMarker = myGoogleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Marker"));
                            myGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));

                        }
                    }
                });
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap) {
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
        Intent backMain = new Intent(this, RestaurantFragment.class);
        backMain.putExtra("Longitude", myLong);
        backMain.putExtra("Latitude", myLat);
        setResult(Activity.RESULT_OK, backMain);
        System.out.println(myLong + "     " + myLat + "FROM  on back pressed");
        super.onBackPressed();
    }
}