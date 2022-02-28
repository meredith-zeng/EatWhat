package com.example.eatwhat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// Implement OnMapReadyCallback.
public class MyMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationClient;
    private String inputLocation = "";


//    private void getLocation() {
//        EditText mEdit  = (EditText)findViewById(R.id.search_location);
//        Button mButton = (Button)findViewById(R.id.confirm_location);
//
//        mButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                inputLocation = mEdit.getText().toString();
//                System.out.println(inputLocation);
//            }
//        });
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    // Get a handle to the GoogleMap object and display marker.
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
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            double myLong = location.getLongitude();
                            double myLat = location.getLatitude() ;
                            LatLng latLng = new LatLng(myLat, myLong);

                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Marker"));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));

                            Geocoder geocoder = new Geocoder(MyMapActivity.this, Locale.getDefault());

                            try {
                                List<Address> addresses = null;
                                addresses = geocoder.getFromLocation(myLat, myLong, 1);
                                System.out.println(addresses.get(0).getLocality().toString());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }
}