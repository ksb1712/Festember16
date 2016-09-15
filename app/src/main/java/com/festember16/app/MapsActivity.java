package com.festember16.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int LOCATION_ENABLE_REQUEST_CODE = 1002;
    public static final String ID = "ID";
    public static final String LOG_TAG = "MapsActivity";
    private GoogleMap mMap;
    private DBHandler db;
    private Events events;
    private boolean isLocationEnabled = false;
    private boolean isPermissionGiven = true;
    public static boolean isFirstTime = false;


    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new DBHandler(this);

        //Todo: get Event id from parent activity and take actual data from database
        events = db.getEvent(getIntent().getIntExtra(ID, 1));

        SharedPreferences preferences = getSharedPreferences(MainMapsActivity.FIRST_TIME, MODE_PRIVATE);

        isFirstTime = preferences.getBoolean(MainMapsActivity.IS_FIRST_TIME, true);

//        Gson gson = new Gson();
//
//        events = gson.fromJson(getIntent().getStringExtra(ListActivity.EVENT), Events.class);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 2) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        isPermissionGiven = true;
                    } else {

                        Toast.makeText(MapsActivity.this, "Location needs to be enabled",
                                Toast.LENGTH_SHORT).show();
                        setResult(RESULT_CANCELED);
                        finish();
                    }

                }
        }


    }

    private boolean checkLocationEnabled() {

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return ((
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ));

    }

    private void enableLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this)
                .setTitle("Enable Location?")
                .setMessage("You need to enable location to show your location on map")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        );

                        startActivityForResult(intent, LOCATION_ENABLE_REQUEST_CODE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isLocationEnabled = false;
                        dialog.dismiss();
                    }
                });

        builder.show();

    }
    private void hasInternet() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GooglePing.ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        GooglePing ping = retrofit.create(GooglePing.class);

        Observable<Void> results = ping.ping();

        Subscriber<Void> subscriber = new Subscriber<Void>() {
            @Override
            public void onCompleted() {
                Log.d(LOG_TAG, "Completed!");
<<<<<<< HEAD
=======

>>>>>>> 9b8e6eecbe56ff763e5c75dce872e972097dabba
                if(isFirstTime)
                {
                    SharedPreferences preferences = getSharedPreferences(MainMapsActivity.FIRST_TIME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(MainMapsActivity.IS_FIRST_TIME, false);
                    editor.apply();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOG_TAG, "Error!");

                if(isFirstTime){
                    Toast.makeText(
                            MapsActivity.this,
                            "Check internet connection to load map",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNext(Void o) {
                Log.d(LOG_TAG, "onNext");
            }
        };

        results.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case LOCATION_ENABLE_REQUEST_CODE:
                if (checkLocationEnabled()) {
                    isLocationEnabled = true;
                } else {
                    enableLocationDialog();
                }

                if (checkLocationEnabled())
                    isLocationEnabled = true;

                else
                    isLocationEnabled = false;

                if (isPermissionGiven && isLocationEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMyLocationButtonClickListener(
                            new GoogleMap.OnMyLocationButtonClickListener() {
                                @Override
                                public boolean onMyLocationButtonClick() {

                                    Toast.makeText(MapsActivity.this, "Loading...", Toast.LENGTH_SHORT).show();

                                    return false;
                                }
                            }
                    );
                }

        }
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            isPermissionGiven = false;
            isLocationEnabled = false;

            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    PERMISSION_REQUEST_CODE
            );

        }

        else if(checkLocationEnabled()){
            isLocationEnabled = true;
        }

        else{
            isLocationEnabled=false;
            enableLocationDialog();
        }

        if(isPermissionGiven&&isLocationEnabled)
        {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(
                    new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {

                            Toast.makeText(MapsActivity.this, "Loading...", Toast.LENGTH_SHORT).show();

                            return false;
                        }
                    }
            );
        }


//         Add a marker at event location and move the camera
        LatLng eventLocation = new LatLng(
                Double.parseDouble(events.getLocationY()),
                Double.parseDouble(events.getLocationX()));
        LatLngBounds centerBounds = new LatLngBounds(
                new LatLng(eventLocation.latitude, eventLocation.longitude),
                new LatLng(eventLocation.latitude, eventLocation.longitude)
        );

        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.locator_icon);
        Bitmap bitmap =  bitmapDrawable.getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 90, 135, false);

        mMap.addMarker(
                new MarkerOptions()
                        .position(eventLocation)
                        .title(
                                EventsAdapter.parseEventName(events.getName() +
                                        " at " + events.getVenue())
                        )
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        );
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerBounds.getCenter(), 15));

<<<<<<< HEAD

=======
>>>>>>> 9b8e6eecbe56ff763e5c75dce872e972097dabba


    }

}
