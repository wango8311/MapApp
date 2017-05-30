package com.example.wango8311.myapplication;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    int a = 0;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean isGPSenabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_TIME_BW_UPDATES = 1000*15;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 5f;
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},2);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }
        mMap.setMyLocationEnabled(true);



        LatLng g = new LatLng(57.401219, 93.519662);
        mMap.addMarker(new MarkerOptions().position(g).title("Welcome to Gulag, comrade"));



    }
    public void switchView(View v) {


        if(a==0) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            a=1;

        }
        else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            a=0;
        }
    }

    public void getLocation() {
        try {
            locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);

            //get GPS status (make sure that russia hasn't nuked out satellites)
            isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSenabled) Log.d ("MyMaps", "getLocation:GPS is enabrued");

            //get network status (make sure no EMP has occured)
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) Log.d ("MyMaps", "getLocation: network is enabrued");

            if(!isGPSenabled&&!isNetworkEnabled){
                Log.d ("MyMaps", "getLocation: No provider is enabled!");
            }  else{
                canGetLocation = true;
                if(isGPSenabled){
                    Log.d ("MyMaps", "getLocation: GPS enabled!-requesting location updates");
                    LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        locationListenerGPS);
                    Log.d ("MyMaps", "getLocation: Network GPS update request success");
                    Toast.makeText(this, "Using GPS",Toast.LENGTH_SHORT);

                }
                if(isNetworkEnabled){
                    Log.d ("MyMaps", "getLocation: GPS enabled!-requesting location updates");
                    LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerNetwork);
                    Log.d ("MyMaps", "getLocation: Network GPS update request success");
                    Toast.makeText(this, "Using Network",Toast.LENGTH_SHORT);

                }
            }
        }
        catch (Exception e){
          Log.d("My maps",  "Caughtan exeption in get Location");
          e.printStackTrace();
        }
    }
}
