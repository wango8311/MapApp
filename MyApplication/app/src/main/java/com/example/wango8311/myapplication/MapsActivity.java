package com.example.wango8311.myapplication;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
int a = 0;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Vancouver General Hospital, BC, Canada and move the camera
        LatLng van = new LatLng(49.2611813, -123.12541899999997);
        mMap.addMarker(new MarkerOptions().position(van).title("Born here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(van));


        LatLng here = new LatLng(49.2611813, -123.12541899999997);
        mMap.addMarker(new MarkerOptions().position(here).title("You are here"));

    }
    public void switchView(GoogleMap googleMap) {
        mMap = googleMap;

        if(a==0) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            a=1;

        }
        else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            a=0;
        }
    }
}
