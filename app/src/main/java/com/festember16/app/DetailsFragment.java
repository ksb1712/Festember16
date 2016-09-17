package com.festember16.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
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
    @InjectView(R.id.eventName)
    TextView eventName;
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
            events = DetailsActivity.detailedEvent;
            //Just in case
            if(events == null){
                events = DetailsActivity.db.getEvent(id);
            }
//
////       /*     //Hardcoded data for testing
//            events = new Gson().fromJson("{\"event_id\":\"2\",\"event_name\":\"choreo_nite_eastern\",\"event_start_time\":\"16:30:00\",\"event_end_time\":\"21:30:00\",\"event_venue\":\"OAT\",\"event_last_update_time\":\"03:16:16\",\"event_loc_x\":\"78.817\",\"event_loc_y\":\"10.7646\",\"event_max_limit\":\"0\",\"event_cluster\":\"dance\",\"event_date\":\"2015-09-25\"}",
//                    Events.class);
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
                    public void onClick(View view) {

                        final ProgressDialog pDialog = new ProgressDialog(getContext(),
                                R.style.AppTheme_Dark_Dialog);
                        pDialog.setMessage("Loading...");
                        pDialog.setCancelable(false);
                        pDialog.setCanceledOnTouchOutside(false);
                        pDialog.show();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utilities.event_register, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pDialog.dismiss();
                                JSONObject jsonResponse = null;
                                try {
                                    jsonResponse = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                int status = 0;
                                try {
                                    status = jsonResponse.getInt("status_code");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String message = null;
                                try {
                                    message = jsonResponse.getString("message");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("QR ", message);
                                if(status == 200) {

                                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                    registerButton.setEnabled(false);
                                    pDialog.dismiss();
                                }
                                else{
                                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismiss();
                                error.printStackTrace();
                                Toast.makeText(getContext(), "Please check your internet and try again", Toast.LENGTH_LONG).show();

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                // the POST parameters:
                                int pid = 2;
                                params.put("token",Utilities.token);
                                params.put("user_id",Utilities.user_id);
                                params.put("event_id",""+events.getId());
                                return params;
                            }

                        };

                        int socketTimeout = 10000;//10 seconds
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequest.setRetryPolicy(policy);
                        Volley.newRequestQueue(getContext()).add(stringRequest);

                    }


//

                });
    }


             //   )





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
