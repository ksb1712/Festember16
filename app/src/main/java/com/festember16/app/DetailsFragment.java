package com.festember16.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    @InjectView(R.id.eventName)
    TextView eventName;
//    @InjectView(R.id.eventCluster)
//    TextView eventCluster;

    @InjectView(R.id.registerButton)
    Button registerButton;

    Retrofit retrofit;
    Observable<LoginRegister> registerObservable;
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
            events = DetailsActivity.detailedEvent;
            if(events == null){
                events = DetailsActivity.db.getEvent(id);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventVenue.setText("Venue: " + events.getVenue());
        eventStartTime.setText("Starts at: " + EventsAdapter.parseEventTime(events.getStartTime()));
        eventDesc.setText(events.getDescription());
        String name = events.getName();
        String temp2 = "";
        if (name.contains("_")) {
            // Split it.
            String[] temp = name.split("_");
            for (int j = 0; j < temp.length; j++) {
                String temp3 = temp[j].substring(0, 1).toUpperCase() + temp[j].substring(1);
                temp2 = temp2 + temp3 + " ";
            }

        } else temp2 = name.substring(0, 1).toUpperCase() + name.substring(1);
        eventName.setText(temp2);
       // eventCluster.setText(events.getCluster());

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "Error";
        String registered = sharedPref.getString("registered_events", defaultValue);

        if(registered.contains(" " + events.getId() + ",")){
            registered();
        }

        registerButton.setOnClickListener(
                view -> {
                    final ProgressDialog progress = new ProgressDialog(getContext(),
                            R.style.AppTheme_Dark_Dialog);
                    progress.setMessage("Registering...");
                    progress.setCancelable(false);
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();

                    retrofit = new Retrofit.Builder()
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(Utilities.base_url)
                            .build();

                    RegisterEventService registerEventService = retrofit.create(RegisterEventService.class);

                    registerObservable = registerEventService.registerEvent(Utilities.user_id, Utilities.token, events.getId());

                    registerObservable.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(registerEvent -> {
                                if(registerEvent.getStatusCode() == 200) {
                                    registered();
                                }
                                else{
                                    Toast.makeText(getContext(),"Error, Try again ",Toast.LENGTH_SHORT).show();
                                }
                                progress.dismiss();
                            });


                }
        );
    }

    public void registered() {
        registerButton.setEnabled(false);
        registerButton.setText(R.string.registered);
        if(Build.VERSION.SDK_INT>=23){
            registerButton.setBackgroundColor(getResources().getColor(R.color.aluminum, null));
        }
        else{
            registerButton.setBackgroundColor(getResources().getColor(R.color.aluminum));
        }
        registerButton.setTextColor(Color.BLACK);
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
