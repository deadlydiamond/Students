package com.example.seekm.studemts;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.widget.Toast.LENGTH_SHORT;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.maps));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.setOnInfoWindowClickListener(this);
        }
    }


    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 16.5f;
    private static final float DEFAULT_RADIUS = 250;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    /*__________________________________________________WIDGETS_________________________________________________________*/

    private ImageButton mNext, mGps;

    /*__________________________________________________VARIABLES_________________________________________________________*/

    SharedPreferences Profile_preferences;
    FirebaseAuth mAuth;
    Marker marker;
    int count;
    Circle circle;
    String FirstName, LastName;
    private double latitude, longitude;
    private double latitudeDevice, longitudeDevice;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient mGoogleApiClient;
    SeekBar seekbar;
    Float radius = DEFAULT_RADIUS, zoom = DEFAULT_ZOOM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        statusCheck();
        getDeviceLocation();
        mGps = (ImageButton) findViewById(R.id.ic_gps);
        mNext = (ImageButton) findViewById(R.id.next);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        getLocationPermission();
        Profile_preferences = getApplicationContext().getSharedPreferences("Profile_Preferecens", 0);
        mAuth = FirebaseAuth.getInstance();
    }

    public class SeekBarHint extends SeekBar {
        public SeekBarHint (Context context) {
            super(context);
        }

        public SeekBarHint (Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public SeekBarHint (Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }



    /*__________________________________________________CUSTOM LOCATION POPUP_________________________________________________________*/

    public boolean statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            getDeviceLocation();
            return true;
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_custom_location, null);
        builder.setView(dialogView);
        builder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        dialog.dismiss();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        getDeviceLocation();
                    }
                })
                .setNegativeButton("NO THANKS   ", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        //dialog.dismiss();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
        getDeviceLocation();
    }

    /*__________________________________________________INIT_________________________________________________________*/

    private void init() {

        Log.d(TAG, "init: initializing");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        /*__________________________________________________NEXT BUTTON_________________________________________________________*/
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, Drawer.class);
                startActivity(intent);
            }
        });

        /*__________________________________________________GPS BUTTON_________________________________________________________*/
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                circle.remove();
                radius = DEFAULT_RADIUS;
                zoom = DEFAULT_ZOOM;
                seekbar.setProgress(0);

                if (statusCheck() == true) {
                    getDeviceLocation();
                    circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(latitudeDevice, longitudeDevice))
                            .radius(DEFAULT_RADIUS)
                            .strokeWidth(5)
                            .strokeColor(Color.rgb(7, 160, 225))
                            .fillColor(0x22FFFFFF));
                }
                Log.d(TAG, "onClick:  Clicked gps icon");
                if (mLocationPermissionsGranted) {
                    getDeviceLocation();
                } else {
                    try {
                        Log.d(TAG, "getLocationPermission: getting location permissions");
                        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION};

                        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            if (ContextCompat.checkSelfPermission(MapsActivity.this,
                                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                mLocationPermissionsGranted = true;
                                initMap();
                            } else {
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        permissions,
                                        LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        } else {
                            ActivityCompat.requestPermissions(MapsActivity.this,
                                    permissions,
                                    LOCATION_PERMISSION_REQUEST_CODE);
                        }

                    } catch (Error e) {
                        Log.d(TAG, "onClick: Error on Click" + e.getMessage());
                    }
                }
            }
        });

        /*__________________________________________________SEEKBAR_________________________________________________________*/

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (circle!=null){
                    circle.remove();
                }
                float factor = 0.15f;
                if (radius >= DEFAULT_RADIUS) {
                    updateCircle(radius * progress);
                    updateZoom((float) (zoom - (progress * factor)));
                    factor = factor * 0.002f;
                    //updateZoom(zoom * progress)
                    // ;
                } else {
                    updateCircle(radius / progress);
                    updateZoom((float) (zoom * 0.5));
                    //updateZoom(zoom * progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /*__________________________________________________SEEKBAR ZOOM_________________________________________________________*/

    private void updateZoom(float zoom) {
        if (zoom > DEFAULT_ZOOM) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
            moveCameraToUserLocation(new LatLng(latitudeDevice, longitudeDevice),
                    zoom);

        } else {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
            moveCameraToUserLocation(new LatLng(latitudeDevice, longitudeDevice),
                    zoom);
        }
    }

    /*__________________________________________________GET DEVICE LOCATION_________________________________________________________*/

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        try {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: found location!");
                                Location currentLocation = (Location) task.getResult();
                                latitudeDevice = currentLocation.getLatitude();
                                longitudeDevice = currentLocation.getLongitude();
                                moveCameraToUserLocation(new LatLng(latitudeDevice, longitudeDevice),
                                        DEFAULT_ZOOM+2);

                            } else {
                                Log.d(TAG, "onComplete: current location is null");
                                Toast.makeText(MapsActivity.this, "unable to get current location", LENGTH_SHORT).show();
                            }
                        } catch (NullPointerException e) {
                            Log.d(TAG, "onComplete: Nullpointer" + e.getMessage());
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    /*__________________________________________________MOVING CAMERA TO TARGET LOCATIONS_____________________________________________________*/

    private void moveCameraToTargetLocation(LatLng latLng, float defaultZoom) {
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(FirstName + " " + LastName)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.teacher)));
        mMap.setOnInfoWindowClickListener(this);
    }

    /*__________________________________________________UPDATE BLUE CIRCLE_________________________________________________________*/

    private void updateCircle(float r) {
        if (r >= DEFAULT_RADIUS) {
            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(latitudeDevice, longitudeDevice))
                    .radius(r)
                    .strokeWidth(5)
                    .strokeColor(Color.rgb(7, 160, 225))
                    .fillColor(0x22FFFFFF));
            //Toast.makeText(MapsActivity.this, "radius: " + r, Toast.LENGTH_LONG).show();
        }
    }

    /*__________________________________________________MOVE CAMERA TO USER's LOCATION_________________________________________________________*/

    private void moveCameraToUserLocation(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (circle == null) {
            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(latitude, longitude))
                    .radius(250)
                    .strokeWidth(5)
                    .strokeColor(Color.rgb(7, 160, 225))
                    .fillColor(0x220000FF));
        }

        if (marker != null) {
            marker.remove();
        }
    }

    /*__________________________________________________INITIALIZING MAP_________________________________________________________*/

    private void initMap() {
        //reading data from FIREBASE
        retrieveData();

        getDeviceLocation();
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    /*__________________________________________________LOADING DATA FROM FIREBASE_________________________________________________________*/

    private void retrieveData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Tutors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.get("Latitude"));
                                try {
                                    latitude = Double.parseDouble(document.get("Latitude").toString());
                                    longitude = Double.parseDouble(document.get("Longitude").toString());
                                    toastME("longitude: " + longitude+latitude);
                                    moveCameraToTargetLocation(new LatLng(latitude, longitude), DEFAULT_ZOOM);
                                } catch (NullPointerException e) {
                                    Log.d(TAG, "onComplete: Exception" + e.getMessage());
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /*__________________________________________________GET LOCATION PERMISSION_________________________________________________________*/

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        initMap();
        init();
    }

    /*__________________________________________________ON REQUEST PERMISSION GRANTED_________________________________________________________*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    /*__________________________________________________MARKER's INFO WINDOW_________________________________________________________*/

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    /*__________________________________________________HIDING SOFT KEYBOARD_________________________________________________________*/

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /*__________________________________________________TOAST_________________________________________________________*/

    private void toastME(String msg){
        Toast.makeText(MapsActivity.this,msg,Toast.LENGTH_LONG).show();
    }
}

