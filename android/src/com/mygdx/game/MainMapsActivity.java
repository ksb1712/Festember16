package com.mygdx.game;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int LOCATION_ENABLE_REQUEST_CODE = 1002;
    public static final String LOG_TAG = "MainMapsActivity";
    public static final String FIRST_TIME = "First time";
    public static final String IS_FIRST_TIME = "IS FIRST TIME";

    private GoogleMap mMap;

    private boolean isLocationEnabled = false;
    private boolean isPermissionGiven = true;
    public static boolean isFirstTime = false;

    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final Map<String, LatLng> allLocations;
    static{
        allLocations = new HashMap<>();

        allLocations.put("BARN", new LatLng(
                10.7592931, 78.8132237
        ));
        allLocations.put("OAT", new LatLng(
                10.7614920, 78.8106944
        ));
        allLocations.put("LHC", new LatLng(
                10.7611251, 78.8139496
        ));
        allLocations.put("ORION", new LatLng(
                10.7596952, 78.8111098
        ));
        allLocations.put("EEE AUDI", new LatLng(
                10.758921, 78.814679
        ));
        allLocations.put("ADMIN", new LatLng(
                10.757987, 78.813314
        ));
        allLocations.put("ARCHI DEPT", new LatLng(
                10.7601962, 78.8099749
        ));
        allLocations.put("SAC", new LatLng(
                10.760196, 78.809975
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SharedPreferences preferences = getSharedPreferences(FIRST_TIME, MODE_PRIVATE);

        isFirstTime = preferences.getBoolean(IS_FIRST_TIME, true);

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

        hasInternet();

        mMap = googleMap;

        if(!hasPermission()){
            callPermissionRequest();
        }

        else if(checkLocationEnabled()){
            isLocationEnabled=true;
        }
        else
        {
            isLocationEnabled=false;
            enableLocationDialog();
        }

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

                            Toast.makeText(MainMapsActivity.this, "Loading...", Toast.LENGTH_SHORT).show();

                            return false;
                        }
                    }
            );
        }

        BitmapDrawable bitmapDrawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.locator_icon, null);
        }
        else{
            bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.locator_icon);

        }
        Bitmap bitmap =  bitmapDrawable.getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 90, 135, false);


        // Add a marker in Sydney and move the camera

        Iterator iterator = allLocations.entrySet().iterator();

        while(iterator.hasNext()){

            Map.Entry<String, LatLng> entry = (Map.Entry<String, LatLng>) iterator.next();

            mMap.addMarker(
                    new MarkerOptions()
                    .position(entry.getValue())
                    .title(entry.getKey())
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
            );

        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(allLocations.get("ADMIN"), 16));


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
                if(isFirstTime)
                {
                    SharedPreferences preferences = getSharedPreferences(FIRST_TIME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(IS_FIRST_TIME, false);
                    editor.apply();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(LOG_TAG, "Error!");

                if(isFirstTime){
                    Toast.makeText(
                        MainMapsActivity.this,
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

    public boolean hasPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            isPermissionGiven = false;
            isLocationEnabled = false;

            return false;
        }

        else
            return true;
    }

    private void callPermissionRequest() {
        ActivityCompat.requestPermissions(
                this,
                PERMISSIONS,
                PERMISSION_REQUEST_CODE
        );
    }

    public boolean checkLocationEnabled() {


        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean buf = ((
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ));
        Log.e(LOG_TAG, "Inside checkLocation Enabled " + buf);

        return buf;

    }
    private void enableLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Enable Location?")
                .setMessage("You need to enable location to show your location on map")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        );

                        Log.d(LOG_TAG, "Starting settings implicit activity");

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case PERMISSION_REQUEST_CODE:


                if(grantResults.length==2){
                    if(
                            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                            )
                        isPermissionGiven = true;
                }
                break;
        }
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
                {
                    isLocationEnabled = false;
                }

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

                                    Toast.makeText(MainMapsActivity.this, "Loading...", Toast.LENGTH_SHORT).show();

                                    return false;
                                }
                            }
                    );
                }

        }
    }


}
