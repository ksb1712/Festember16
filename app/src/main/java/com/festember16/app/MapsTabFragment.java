package com.festember16.app;

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

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    SupportMapFragment mapFragment = null;
    private static View view = null;

    private boolean isLocationEnabled = false;
    private boolean isPermissionGiven = true;
    public static boolean isFirstTime = false;


    public MapsTabFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "Inside onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Inside onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(LOG_TAG, "Inside onCreateView");

        if(!hasPermission()){
            callPermissionRequest();
        }

        else if (checkLocationEnabled())
            isLocationEnabled = true;

        else {
            isLocationEnabled = false;
        }

        if(view==null)
       {
           view = inflater.inflate(R.layout.mapsfragmentlayout, container, false);

           FragmentManager fm = getChildFragmentManager();
           mapFragment = (SupportMapFragment)
                   fm.findFragmentById(R.id.mapPreview);

           if(mapFragment == null)
           {
               mapFragment = SupportMapFragment.newInstance();
               fm.beginTransaction().replace(R.id.mapContainer, mapFragment);

               ViewGroup.LayoutParams layoutParams = mapFragment.getView().getLayoutParams();

               DisplayMetrics displayMetrics = new DisplayMetrics();
               getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

               layoutParams.height = (int) (displayMetrics.heightPixels * 0.7);
               layoutParams.width = (int) (displayMetrics.widthPixels * 0.9);

               mapFragment.getView().setLayoutParams(layoutParams);

           }
       }

        SharedPreferences preferences = getActivity().getSharedPreferences(MainMapsActivity.FIRST_TIME, Context.MODE_PRIVATE);

        isFirstTime = preferences.getBoolean(MainMapsActivity.IS_FIRST_TIME, true);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Inside onActivityCreated");

        mapFragment.getMapAsync(this);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        Log.d(LOG_TAG, "Inside setMenuVisibility\nmenuVisible = " + menuVisible);


        if(menuVisible){
            hasInternet();

            if(!hasPermission()){
                callPermissionRequest();
            }

            else if (checkLocationEnabled())
            {
                isLocationEnabled = true;
            }

            else {
                isLocationEnabled = false;
                enableLocationDialog();
            }

            enableLocationButtonListener();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        view = null;
        mapFragment = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

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
                    () -> {

                        Toast.makeText(getActivity(), "Loading...", Toast.LENGTH_SHORT).show();

                        return false;
                    }
            );
        }

        try {

            if(DetailsFragment.events!=null) {
                LatLng eventLocationLatLng = new LatLng(
                        Double.parseDouble(DetailsFragment.events.getLocationY()),
                        Double.parseDouble(DetailsFragment.events.getLocationX())
                );

                BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.locator_icon);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, 90, 135, false);

                mMap.addMarker(
                        new MarkerOptions()
                                .position(
                                        //MainMapsActivity.allLocations.get("BARN")
                                        eventLocationLatLng
                                )
                                .title(EventsAdapter.parseEventName(DetailsFragment.events.getName()) +
                                        " at " + DetailsFragment.events.getVenue())
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                );

                if(eventLocationLatLng!=null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        eventLocationLatLng,
                        16
                    ));

            }
        }
        catch (NullPointerException e){
            Log.e(LOG_TAG, e.getMessage());
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
                enableLocationButtonListener();
                break;

        }
    }

    private void enableLocationButtonListener() {
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

        if (isPermissionGiven && isLocationEnabled)
        {
            if (mMap != null) {
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
            else{
                Log.d(LOG_TAG, "mMap is null inside onActivityResult");
            }

        }
        else{
            if(mMap !=null){
                mMap.setMyLocationEnabled(false);
            }
        }
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
                    SharedPreferences preferences = getActivity().getSharedPreferences(MainMapsActivity.FIRST_TIME, Context.MODE_PRIVATE);
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
                            getActivity(),
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
