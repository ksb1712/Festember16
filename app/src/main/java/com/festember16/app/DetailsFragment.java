package com.festember16.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vishnu on 8/9/16.
 */
public class DetailsFragment extends Fragment {


    public static final String ID = "ID";
    public static final String LOG_TAG = "DetailsFragment";


    public static final String REGISTERED_EVENTS = "REGISTERED EVENTS";

    private boolean hasRegistered;
    public static Events events;

    @InjectView(R.id.eventVenue)
    TextView eventVenue;
    @InjectView(R.id.eventDesc)
    TextView eventDesc;
    @InjectView(R.id.eventStartTime)
    TextView eventStartTime;
//    @InjectView(R.id.eventCluster)
//    TextView eventCluster;

    @InjectView(R.id.registerButton)
    Button registerButton;

    public DetailsFragment() {

    }

    public static DetailsFragment getInstance(int id) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.details_layout, container, false);
        ButterKnife.inject(this, view);



        Bundle bundle = getArguments();

        if (bundle != null) {
            int id = bundle.getInt(ID);
            //Todo: test out if db works here
            //events = DetailsActivity.detailedEvent;
            //Just in case
            if(events == null){
                //events = DetailsActivity.db.getEvent(id);
            }

//            //Hardcoded data for testing
            events = new Gson().fromJson("{\"event_id\":\"2\",\"event_name\":\"choreo_nite_eastern\",\"event_start_time\":\"16:30:00\",\"event_end_time\":\"21:30:00\",\"event_venue\":\"OAT\",\"event_last_update_time\":\"03:16:16\",\"event_loc_x\":\"78.817\",\"event_loc_y\":\"10.7646\",\"event_max_limit\":\"0\",\"event_cluster\":\"dance\",\"event_date\":\"2015-09-25\"}",
                    Events.class);

        }


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventVenue.setText("Venue: " + events.getVenue());
        eventStartTime.setText("Starts at: " + EventsAdapter.parseEventTime(events.getStartTime()));
       // eventCluster.setText(events.getCluster());


        final SharedPreferences preferences = getActivity().getSharedPreferences(
                REGISTERED_EVENTS,
                Context.MODE_PRIVATE
        );

        //Storage format in shared preferences is going to be of type: eventId ---> true/false

        Map<String, Boolean> map = (Map<String, Boolean>) preferences.getAll();

        Iterator iterator = map.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String, Boolean> pair = (Map.Entry) iterator.next();

            if(events.getId() == Integer.parseInt(pair.getKey()))
                hasRegistered = pair.getValue();
        }

        if(hasRegistered){
            registerButton.setText("REGISTERED");
            if(Build.VERSION.SDK_INT>=23){
            registerButton.setBackgroundColor(getResources().getColor(R.color.aluminum, null));
            }
            else{
                registerButton.setBackgroundColor(getResources().getColor(R.color.aluminum));
            }
            registerButton.setTextColor(Color.BLACK);
        }

        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!hasRegistered) {
                            //Todo: Send a post request to api and set ProgressBar to visible
                            //ProgressBar not in current layout
                            //Todo: if(response == true)
                            registerButton.setText("REGISTERED");
                            if (Build.VERSION.SDK_INT >= 23) {
                                registerButton.setBackgroundColor(getResources().getColor(
                                        R.color.aluminum, null
                                ));
                            }
                            else {
                                registerButton.setBackgroundColor(getResources().getColor(R.color.aluminum));
                            }
                            registerButton.setTextColor(Color.BLACK);
                            hasRegistered = true;

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("" + events.getId(), true);
                            editor.commit();

                        }

                        //User wants to unregister

                        else
                        {
                            //Todo: Send a post request to api and set ProgressBar to visible
                            //ProgressBar not in current layout
                            //Todo: if(response == true)
                            registerButton.setText("REGISTER");
                            if (Build.VERSION.SDK_INT >= 23) {
                                registerButton.setBackgroundColor(getResources().getColor(
                                        R.color.buttonBackground, null
                                ));
                            } else {
                                registerButton.setBackgroundColor(getResources().
                                        getColor(R.color.buttonBackground));
                            }
                            registerButton.setTextColor(ContextCompat.getColor
                                    (getActivity(), R.color.textColorLight));
                            hasRegistered = false;

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove(String.valueOf(events.getId()));
                            editor.commit();
                        }

                    }


                }
        );

    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
