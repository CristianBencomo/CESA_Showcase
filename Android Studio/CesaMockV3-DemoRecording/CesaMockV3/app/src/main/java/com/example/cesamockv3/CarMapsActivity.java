package com.example.cesamockv3;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String latitude;
    String longitude;
    static float lat;
    static float log;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_car_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragCarLocation);
        mapFragment.getMapAsync(CarMapsActivity.this);

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        reff = FirebaseDatabase.getInstance().getReference().child("RPI_01").child("01-setTest");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                latitude = snapshot.child("latitude").getValue().toString();
                longitude = snapshot.child("longitude").getValue().toString();

                lat = Float.parseFloat(latitude);
                log = Float.parseFloat(longitude);
//                Log.d("MapLoc", "latitude"  + lat);
//                Log.d("MapLoc", "longitude"  + log);
                LatLng CarLoc = new LatLng(lat, log);
                mMap.addMarker(new MarkerOptions().position(CarLoc).title("Car Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CarLoc, 15f));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}