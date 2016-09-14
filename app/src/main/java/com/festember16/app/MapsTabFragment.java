package com.festember16.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by vishnu on 14/9/16.
 */
public class MapsTabFragment extends Fragment implements OnMapReadyCallback{

    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int LOCATION_ENABLE_REQUEST_CODE = 1002;
    public static final String LOG_TAG = "MapsTabFragment";

    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    };

    public GoogleMap mMap;
    SupportMapFragment mapFragment;

    private boolean isLocationEnabled = true;
    private boolean isPermissionGiven = true;

    public MapsTabFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mapsfragmentlayout, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment)
                fm.findFragmentById(R.id.mapPreview);

        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapContainer, mapFragment);
        }

        ViewGroup.LayoutParams layoutParams = mapFragment.getView().getLayoutParams();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        layoutParams.height = (int) (displayMetrics.heightPixels * 0.7);
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.7);

        mapFragment.getView().setLayoutParams(layoutParams);
    }

    @Override
    public void onResume() {
        super.onResume();

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(!hasPermission()){
            callPermissionRequest();
        }

        else if (checkLocationEnabled())
            isLocationEnabled = true;
        else {
            isLocationEnabled = false;
            enableLocationDialog();
        }

        if (isPermissionGiven && isLocationEnabled) {
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(
                    new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {

                            Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();

                            return false;
                        }
                    }
            );
        }


//        LatLng eventLocationLatLng = new LatLng(
//                Double.parseDouble(DetailsFragment.events.getLocationY()),
//                Double.parseDouble(DetailsFragment.events.getLocationX())
//        );

        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.festember_logo);
        Bitmap bitmap =  bitmapDrawable.getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);

        mMap.addMarker(
                new MarkerOptions()
                        .position(
                                MainMapsActivity.allLocations.get("BARN")
                                //eventLocationLatLng
                        )
                        .title( "BARN")//EventsAdapter.parseEventName(DetailsFragment.events.getName()) +
//                                " at " + DetailsFragment.events.getVenue())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
        );

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                MainMapsActivity.allLocations.get("BARN"), //eventLocationLatLng,
                16
        ));



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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case LOCATION_ENABLE_REQUEST_CODE:

                Log.d(LOG_TAG, "Inside onActivityResult");

                if (checkLocationEnabled()) {
                    isLocationEnabled = true;
                    Log.d(LOG_TAG, "Location enabled");
                } else {
                    isLocationEnabled = false;
                    Log.d(LOG_TAG, "Location not enabled");
                }

                if (
                        ActivityCompat.checkSelfPermission(getActivity(),
                                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getActivity(),
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                    isPermissionGiven = false;
                    isLocationEnabled = false;
                    return;
                }

                if (isPermissionGiven && isLocationEnabled && mMap != null)
                {
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMyLocationButtonClickListener(
                            new GoogleMap.OnMyLocationButtonClickListener() {
                                @Override
                                public boolean onMyLocationButtonClick() {

                                    Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();

                                    return false;
                                }
                            }
                    );
                }


                break;

        }
    }

    public boolean checkLocationEnabled() {

        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        return ((
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ));

    }
    private void enableLocationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
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

    public boolean hasPermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
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
                getActivity(),
                PERMISSIONS,
                PERMISSION_REQUEST_CODE
        );
    }

}
