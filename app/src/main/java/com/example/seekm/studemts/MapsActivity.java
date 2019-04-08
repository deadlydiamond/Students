package com.example.seekm.studemts;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seekm.studemts.Models.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.widget.Toast.LENGTH_SHORT;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,



        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener{ //GoogleMap.OnMarkerDragListener {
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            //getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
//            mMap.setMyLocationEnabled(true);
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
    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageButton mNext, mGps;

    //vars
    SharedPreferences Profile_preferences ;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    Circle circle;
    String FirstName, LastName;
    Dialog locationDialog;
    private double latitude, longitude;
    private double latitudeDevice, longitudeDevice;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    TextView OK, Cancel;
    SeekBar seekbar;
    Float radius = DEFAULT_RADIUS, zoom = DEFAULT_ZOOM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        statusCheck();
        getDeviceLocation();
        mGps = (ImageButton) findViewById(R.id.ic_gps);
        mNext = (ImageButton)findViewById(R.id.next);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        getLocationPermission();

        Profile_preferences = getApplicationContext().getSharedPreferences("Profile_Preferecens",0);
        mAuth = FirebaseAuth.getInstance();

    }


    //Location Alert
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
//        locationDialog = new Dialog(this);
//        locationDialog.setContentView(R.layout.activity_custom_location);
//        OK = (TextView) locationDialog.findViewById(R.id.OK);
//        Cancel = (TextView) locationDialog.findViewById(R.id.NO);
//
//
//        OK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//        });
//        Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                locationDialog.hide();
//            }
//        });
//        locationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        locationDialog.show();
    }

    private void init() {

        Log.d(TAG, "init: initializing");
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        //mSearchText.setOnItemClickListener(mAutocompleteClickListener);

        //mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
        //      LAT_LNG_BOUNDS, null);

        //mSearchText.setAdapter(mPlaceAutocompleteAdapter);
//        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH
//                        || actionId == EditorInfo.IME_ACTION_DONE
//                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
//                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
//
//                    //execute our method for searching
//                    geoLocate();
//                }
//
//                return false;
//            }
//        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusCheck() == true) {
                    getDeviceLocation();
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
        //seekbar
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circle.remove();
                float factor = 0.15f;
                if (radius >= DEFAULT_RADIUS) {
                    updateCircle(radius * progress);
                    updateZoom((float) (zoom-(progress*factor)));
                    factor = factor * 0.002f;
                    //updateZoom(zoom * progress)
                    // ;
                } else {
                        updateCircle(radius / progress);
                        updateZoom((float) (zoom*0.5));
                    //updateZoom(zoom * progress);
                }
                //updateZoom(zoom /progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
        //update zoom
     private void updateZoom(float zoom){
        if (zoom>DEFAULT_ZOOM){
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom),2000,null);
        }
        else{
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom),2000,null);

        }
    }
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
                                moveCameraToMyLocation(new LatLng(latitudeDevice, longitudeDevice),
                                        DEFAULT_ZOOM);

                            } else {
                                Log.d(TAG, "onComplete: current location is null");
                                Toast.makeText(MapsActivity.this, "unable to get current location", LENGTH_SHORT).show();
                            }
                        }catch (NullPointerException e){
                            Log.d(TAG, "onComplete: Nullpointer" + e.getMessage());
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }



    private void moveCameraToTutorsLocation(LatLng latLng, float defaultZoom) {
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom));
        Marker marker= mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(FirstName +" " + LastName)

                //.draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pinpoint)));
        mMap.setOnInfoWindowClickListener(this);
        //mMap.setOnMarkerDragListener(this);

    }

    //update Circle
    private void updateCircle(float r){
        if (r>=DEFAULT_RADIUS) {
            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(latitudeDevice, longitudeDevice))
                    .radius(r)
                    .strokeWidth(5)
                    .strokeColor(Color.rgb(7, 160, 225))
                    .fillColor(0x220000FF));

            Toast.makeText(MapsActivity.this, "radius: " + r, Toast.LENGTH_LONG).show();
        }
        else{
            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(latitudeDevice, longitudeDevice))
                    .radius(DEFAULT_RADIUS)
                    .strokeWidth(5)
                    .strokeColor(Color.rgb(7, 160, 225))
                    .fillColor(0x220000FF));

        }
    }

    private void moveCameraToMyLocation(LatLng latLng, float zoom) {
        mSearchText.setText("");
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if (circle==null) {
            circle = mMap.addCircle(new CircleOptions()
                    .center(new LatLng(latitude, longitude))
                    .radius(250)
                    .strokeWidth(5)
                    .strokeColor(Color.rgb(7, 160, 225))
                    .fillColor(0x220000FF));
        }

// }
        Marker marker= mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("")
////                .snippet("Population: 4,137,400")
//.draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)));
        marker.showInfoWindow();
// mMap.setOnInfoWindowClickListener(this);
//        //mMap.setOnMarkerDragListener(this);
    }

//    private void moveCameraToSearchLocation(LatLng latLng, float zoom) {
//        mMap.clear();
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//        Marker marker= mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("Is this you? Hard press me to locate precisely")
//                .draggable(true)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pinpoint)));
//        marker.showInfoWindow();
//        hideSoftKeyboard();
//        mMap.setOnInfoWindowClickListener(this);
//        mMap.setOnMarkerDragListener(this);
//
//    }

    private void initMap() {
        //statusCheck();
        //reading data
        retrieveData();

        getDeviceLocation();
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);


    }

    //reading data from firestore
    private void retrieveData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Tutors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.get("Latitude"));

                                try {
                                    FirstName = document.get("FirstName").toString();
                                    LastName = document.get("LastName").toString();
                                    latitude = Double.parseDouble(document.get("Latitude").toString());
                                    longitude = Double.parseDouble(document.get("Longitiude").toString());
                                    moveCameraToTutorsLocation(new LatLng(latitude, longitude), DEFAULT_ZOOM);
                                }catch (NullPointerException e) {
                                    Log.d(TAG, "onComplete: Exception" + e.getMessage());
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

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

    @Override
    public void onInfoWindowClick(Marker marker) {
        //marker.showInfoWindow();
    }

//    @Override
//    public void onMarkerDragStart(Marker marker) {
//        LatLng position0 = marker.getPosition();
//
//        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
//                position0.latitude,
//                position0.longitude));
//    }
//
//    @Override
//    public void onMarkerDrag(Marker marker) {
//        LatLng position0 = marker.getPosition();
//        Log.d(getClass().getSimpleName(),
//                String.format("Dragging to %f:%f", position0.latitude,
//                        position0.longitude));
//    }
//
//    @Override
//    public void onMarkerDragEnd(Marker marker) {
//        LatLng position = marker.getPosition();
//        latitude = position.latitude;
//        longitude = position.latitude;
//        Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
//                position.latitude,
//                position.longitude));
//    }


    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    /*
            ----------------------------------------- google places API Autocomplete suggestions -------------------------------------------------
     */

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: Place query did not complete succeesfully" + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            try {
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setAttributions(place.getAttributions().toString());
                mPlace.setId(place.getId());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setRatings(place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: place : " + mPlace.toString());

            } catch (NullPointerException e) {
                Log.d(TAG, "onResult: NullPointerException" + e.getMessage());
            }

            latitude = place.getViewport().getCenter().latitude;
            longitude = place.getViewport().getCenter().longitude;


//            moveCameraToSearchLocation(new LatLng(place.getViewport().getCenter().latitude,
//                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM);
//            places.release();
        }
    };
}