package com.example.bharath17.festember16;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int MAP_REQUEST_CODE = 1001;
    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int LOCATION_ENABLE_REQUEST_CODE = 1002;
    public static final String LOG_TAG = "EventDetailsActivity";

    private GoogleMap mMap;

    private boolean isPermissionGiven = true;
    private boolean isLocationEnabled = true;

    Event event;

    @InjectView(R.id.eventVenue)
    TextView eventVenue;
    @InjectView(R.id.eventStartTime)
    TextView eventStartTime;
    @InjectView(R.id.eventCluster)
    TextView eventCluster;

    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.inject(this);

        String eventData = getIntent().getStringExtra(ListActivity.EVENT);

        Gson gson = new Gson();
        event = gson.fromJson(eventData, Event.class);

        getSupportActionBar().setTitle(EventsAdapter.parseEventName(event.getEventName()));

        eventVenue.setText("Venue: " + event.getEventVenue());

        eventStartTime.setText("Starts at: " + event.getEventStartTime() + " on " +
                EventsAdapter.parseEventDate(event.getEventDate()));

        eventCluster.setText("Cluster: " + event.getEventCluster());

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapPreview);
        mapFragment.getMapAsync(this);

    }

    private boolean checkLocationEnabled() {

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return ((
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ));

    }

    private void enableLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailsActivity.this)
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.menu_event_details,
                menu
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.linkToMap:

                Intent intent = new Intent(
                        EventDetailsActivity.this,
                        MapsActivity.class
                );

                Gson gson = new Gson();

                intent.putExtra(ListActivity.EVENT, gson.toJson(event));

                startActivityForResult(intent, MAP_REQUEST_CODE);

                break;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {

            case MAP_REQUEST_CODE:
                break;

            case LOCATION_ENABLE_REQUEST_CODE:

                Log.d(LOG_TAG, "Inside onActivityResult");

                if (checkLocationEnabled()) {
                    isLocationEnabled = true;
                    Log.d(LOG_TAG, "Location enabled");
                } else {
                    isLocationEnabled = false;
                    Log.d(LOG_TAG, "Location not enabled");
                }

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (isPermissionGiven && isLocationEnabled && mMap != null)
                    mMap.setMyLocationEnabled(true);


                break;

        }



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

        if(checkLocationEnabled()){
            isLocationEnabled = true;
        }

        else{
            enableLocationDialog();
        }

        if(isPermissionGiven&&isLocationEnabled)
        {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(
                    new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {

                            Toast.makeText(EventDetailsActivity.this, "Loading...", Toast.LENGTH_SHORT).show();

                            return false;
                        }
                    }
            );
        }

        LatLng eventLocationLatLng = new LatLng(
                Double.parseDouble(event.getEventLocY()),
                Double.parseDouble(event.getEventLocX())
        );

        mMap.addMarker(
                new MarkerOptions()
                        .position(
                                eventLocationLatLng
                        )
                        .title("Event at " + event.getEventVenue())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_marker_icon_olympics))
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                eventLocationLatLng,
                16
        ));
    }

}


