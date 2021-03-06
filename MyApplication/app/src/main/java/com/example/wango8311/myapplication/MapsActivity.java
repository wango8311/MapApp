package com.example.wango8311.myapplication;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    int a = 0;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean isGPSenabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 5.0f;
    private LatLng here;
    private Location myLocation;
    private static final float MY_LOC_ZOOM_FACTOR = 17.0f;
    private boolean track;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        search = (EditText) findViewById(R.id.search);
        track = true;
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

     /*  if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        mMap.setMyLocationEnabled(true);
/*/
        getLocation();

        LatLng g = new LatLng(57.401219, 93.519662);
        mMap.addMarker(new MarkerOptions().position(g).title("Welcome to Gulag, comrade"));


    }

    public void switchView(View v) {


        if (a == 0) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            a = 1;

        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            a = 0;
        }
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            //get GPS status
            isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSenabled) Log.d("MyMaps", "getLocation:GPS is enabrued");

            //get network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) Log.d("MyMaps", "getLocation: network is enabrued");

            if (!isGPSenabled && !isNetworkEnabled) {
                Log.d("MyMaps", "getLocation: No provider is enabled!");
            } else {
                canGetLocation = true;
                if (isGPSenabled) {
                    Log.d("MyMaps", "getLocation: GPS enabled!-requesting location updates");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerGPS);
                    Log.d("MyMaps", "getLocation: Network GPS update request success");
                   // Toast.makeText(this, "Using GPS", Toast.LENGTH_SHORT).show();

                }
                if (isNetworkEnabled) {
                    Log.d("MyMaps", "getLocation: GPS enabled!-requesting location updates");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            locationListenerNetwork);
                    Log.d("MyMaps", "getLocation: Network GPS update request success");
                  //  Toast.makeText(this, "Using Network", Toast.LENGTH_SHORT).show();


                }
            }
        } catch (Exception e) {
            Log.d("My maps", "Caught an exception in getLocation");
            e.printStackTrace();
        }
    }

    public void tracking(View v) {



        if (track == true) {
            track = false;
            Toast.makeText(this, "Tracking disabled", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(locationListenerNetwork);
            locationManager.removeUpdates(locationListenerGPS);
        } else {
            track = true;
            getLocation();
            Toast.makeText(this, "tracking enabled", Toast.LENGTH_SHORT).show();

        }
    }

    public void dropMarker(String provider) {

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            myLocation = locationManager.getLastKnownLocation(provider);
        }

        if (myLocation == null) {
            //messgae in lod and toast
            Log.d("My maps", "Location null");
        } else {

            here = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            // dump coordiates
            Log.d("Corodinates", "myLocation.getLatitude(), myLocation.getLongitude()");
            // Add a marker here and move camera

            // mMap.addMarker(new MarkerOptions().position(here).title("here"));
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(here, MY_LOC_ZOOM_FACTOR);

            //Add shaoe fir marker
            if (provider.equals(locationManager.GPS_PROVIDER)) {
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(here)
                        .radius(5)
                        .strokeColor(Color.RED)
                        .strokeWidth(2)
                        .fillColor(Color.RED));
                mMap.animateCamera(update);
            } else {
                Circle circle = mMap.addCircle(new CircleOptions()
                        .center(here)
                        .radius(5)
                        .strokeColor(Color.GREEN)
                        .strokeWidth(2)
                        .fillColor(Color.GREEN));
                mMap.animateCamera(update);
            }
            // mMap.addMarker(new MarkerOptions().position(here).title("here"));

        }
        getLocation();
    }

    public void remove(View v) {
        mMap.clear();
    }

    public void searchLoc(View v) {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            myLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);

        }
        double hlat = myLocation.getLatitude();
        double hlong = myLocation.getLongitude();
        Toast.makeText(v.getContext(), hlat + hlong + " ", Toast.LENGTH_LONG).show();
        //double hlat=32.0;
        //double hlong=-117.0;
        String src = search.getText().toString();
        Geocoder loc = new Geocoder(this);
        List<Address> la = null;
        //LatLng he = new LatLng(0,0);
        if (src.length() != 0) {
            try {
                la = loc.getFromLocationName(src, 1000, hlat - .036, hlong - .036, hlat + .036, hlong + .036);

            } catch (IOException e) {
                Log.d("Search", "Caught an exception in SearchLoc (IO exception)");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                Log.d("Search", "Caught an exception in SearchLoc (Illegal Argument)");
                e.printStackTrace();
            }
            //Toast.makeText(v.getContext(), search.getText(), Toast.LENGTH_LONG).show();
        }
        if (src.length() == 0) {
            Toast.makeText(v.getContext(), "no search parameter", Toast.LENGTH_LONG).show();
        }
        if (la != null) {
            try {
                for (int i = 0; i < la.size(); i++) {
                    //Latitude lat = la.get(i).getLatitude();
                    //Long lng = la.get(i).getLongitude();

                    LatLng he = new LatLng(la.get(i).getLatitude(), la.get(i).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(he).title(src));
                /*Circle circle = mMap.addCircle(new CircleOptions()
                        .center(he)
                        .radius(10)
                        .strokeColor(Color.BLUE)
                        .strokeWidth(2)
                        .fillColor(Color.BLUE));*/

                }

            } catch (IllegalArgumentException e) {
                Log.d("Search", "Caught an exception in SearchLoc (Illegal Argument)");
                e.printStackTrace();
            }
        }
        if (la == null) {
            Toast.makeText(v.getContext(), "no locations found", Toast.LENGTH_LONG).show();
        }
    }

    private final android.location.LocationListener locationListenerGPS = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //output message logd toast
            Log.d("MyMaps", "getLocation: Changed location pin GPS");
            //drop marker with dropamarker
            dropMarker(locationManager.GPS_PROVIDER);
            //disable network updates (use location magnaer)
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //locationManager.removeUpdates(locationListenerNetwork);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //setup a switch statmunt on status
            Toast.makeText(MapsActivity.this, "GPS OSC", Toast.LENGTH_SHORT).show();
            switch (status) {
                //case loc prvider avaoble
                case LocationProvider.AVAILABLE:
                    Log.d("MyMaps", "Using location");
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Toast.makeText(MapsActivity.this, "Available RIP", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    break;

                //LP oos reqruest update from np
                case LocationProvider.TEMPORARILY_UNAVAILABLE:

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);

                    break;
                //Lp temp unavbvail req update from np
                case LocationProvider.OUT_OF_SERVICE:
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    break;
                default:
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private final android.location.LocationListener locationListenerNetwork = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //output message logd toast
            Log.d("MyMaps", "getLocation: Changed location pin network");
            //drop marker with dropamarker
            dropMarker(locationManager.NETWORK_PROVIDER);

            //realucnh requar for netwok location updates requestlocationupdates NETWORK PROIDER
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            //locationManager.removeUpdates(locationListenerNetwork);

            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListenerNetwork);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //setup a switch statmunt on status
            Log.d ("MyMaps", "using netwrk");
            //case loc prvider avaoble
            getLocation();
            //LP oos reqruest update from np

            //Lp temp unavbvail req update from np
        }

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };




}
