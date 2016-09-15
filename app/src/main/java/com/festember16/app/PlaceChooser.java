package com.festember16.app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PlaceChooser extends AppCompatActivity {


    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    Location mLastLocation = new Location("gps");

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private static final int LOCATION_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;
    private static final String POI_TYPE = "POI_TYPE";
    private static final String HOSTEL = "HOSTEL";
    private static final String UTILS = "UTILS";
    private static final String EVENTS = "EVENTS";
    private static final String LAT = "LAT";
    private static final String LON = "LON";
    private static final String LOCATION = "LOC";

    private boolean gotLocation = false, isLoading = false;

    Button HostelButton, EventsButton, UtilitiesButton, LocationDisabled;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_chooser);

        HostelButton = (Button) findViewById(R.id.HostelButton);
        EventsButton = (Button) findViewById(R.id.EventsButton);
        UtilitiesButton = (Button) findViewById(R.id.UtilitiesButton);

        HostelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlaceChooser.this, ActivityAR.class);
                i.putExtra(POI_TYPE, HOSTEL);
                i.putExtra(LOCATION, mLastLocation);
                i.putExtra(LAT, mLastLocation.getLatitude());
                i.putExtra(LON, mLastLocation.getLongitude());
                startActivity(i);
            }
        });

        UtilitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlaceChooser.this, ActivityAR.class);
                i.putExtra(POI_TYPE, UTILS);
                i.putExtra(LOCATION, mLastLocation);
                i.putExtra(LAT, mLastLocation.getLatitude());
                i.putExtra(LON, mLastLocation.getLongitude());
                startActivity(i);
            }
        });

        EventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlaceChooser.this, ActivityAR.class);
                i.putExtra(POI_TYPE, EVENTS);
                i.putExtra(LOCATION, mLastLocation);
                i.putExtra(LAT, mLastLocation.getLatitude());
                i.putExtra(LON, mLastLocation.getLongitude());
                startActivity(i);
            }
        });

    }

    private void checkPermissions() {


        initializeLocationManager();

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(PlaceChooser.this);
            dialog.setMessage("Please enable GPS and network based location for using AR");
            dialog.setPositiveButton("Enable location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps

                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Toast toast = Toast.makeText(PlaceChooser.this, "Can't use AR", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            dialog.show();
        } else {
            int fine_location_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int camera_permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

            if (camera_permission != PackageManager.PERMISSION_GRANTED) {
                makeRequestCamera();
            }
            if (fine_location_permission == PackageManager.PERMISSION_GRANTED) {
                try {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);

                } catch (SecurityException ex) {
                    ex.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    ex.getMessage();
                }


                try {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                } catch (IllegalArgumentException ex) {
                    ex.getMessage();
                }
                if (!gotLocation && !isLoading) {
                    showGettingLocation();
                    isLoading = true;
                }
            } else {
                makeRequestLocation();

            }


        }
    }

    @Override
    protected void onResume() {
        checkPermissions();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(mLocationListeners[0]);
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
        mLocationManager.removeUpdates(mLocationListeners[1]);
        isLoading = false;
        gotLocation = false;
        dialog.hide();
    }

    private void showGettingLocation()
    {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Finding your Location");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    private void makeRequestCamera()
    {
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

    }
    private void makeRequestLocation()
    {
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch ( requestCode )
        {
            case LOCATION_REQUEST_CODE:
                if( grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED )
                {
                    HostelButton.setEnabled(false);
                    UtilitiesButton.setEnabled(false);
                    EventsButton.setEnabled(false);

                }
                else
                {
                    HostelButton.setEnabled(true);
                    UtilitiesButton.setEnabled(true);
                    EventsButton.setEnabled(true);
                    try {
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
                    }
                    catch (SecurityException ex)
                    {
                        ex.printStackTrace();
                    }
                    catch (IllegalArgumentException ex)
                    {
                        ex.getMessage();
                    }


                    try {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
                    }
                    catch (SecurityException ex)
                    {
                        ex.printStackTrace();
                    }
                    catch (IllegalArgumentException ex)
                    {
                        ex.getMessage();
                    }
                    if( ! gotLocation && !isLoading )
                    {
                        showGettingLocation();
                        isLoading = true;
                    }

                }
                break;
            case CAMERA_REQUEST_CODE:
                if( grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED )
                {
                    HostelButton.setEnabled(false);
                    UtilitiesButton.setEnabled(false);
                    EventsButton.setEnabled(false);

                }
                break;
        }

    }

    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            mLastLocation.set(location);
            // Update UI Only if Location is Accurate
            //if (location.getAccuracy() < 400)
            {
                if (ActivityCompat.checkSelfPermission(PlaceChooser.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PlaceChooser.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocationManager.removeUpdates(mLocationListeners[0]);
                mLocationManager.removeUpdates(mLocationListeners[1]);
                gotLocation = true;
                dialog.hide();
            }
        }

        @Override
        public void onProviderDisabled(String provider)
        {
        }

        @Override
        public void onProviderEnabled(String provider)
        {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }
    }

    private void initializeLocationManager()
    {
        if (mLocationManager == null)
        {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}
