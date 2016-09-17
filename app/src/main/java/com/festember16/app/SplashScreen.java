package com.festember16.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashScreen extends Activity {

    SharedPreferences prefs;
    Retrofit retrofit;
    Observable<Data> eventsObservable;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

       // TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Sansation-Regular.ttf");
        //TODO shared prefs time stamp
        prefs = getSharedPreferences(
               "Time_stamp", Context.MODE_PRIVATE);

        int numberOfClouds = 5;

        Animation downAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.panda_fall);
        Animation upLongAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.object_rise);
        final Animation logoEntrance = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.logo_entrance);

        ImageView polo = (ImageView) findViewById(R.id.polo);
        final ImageView plane = (ImageView) findViewById(R.id.fPlaneImageView);
        ImageView clouds[] = new ImageView[numberOfClouds];
        for (int i = 0; i < numberOfClouds; i++) {
            clouds[i] = (ImageView) findViewById(getResources().getIdentifier("cloud" + i, "id", getPackageName()));
        }
        final ImageView festemberLogo = (ImageView) findViewById(R.id.festember_logo);

        //TODO Retrofit for Events dB
        db = new DBHandler(this);

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Utilities.base_url)
                .build();

        EventsInterface eventsInterface = retrofit.create(EventsInterface.class);

        eventsObservable = eventsInterface.getEvents();

        eventsObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.getStatusCode()== 200) {
                        for (Events event : data.getEvents()) {
                            db.addEvent(event);
                        }
                    } else Log.e("fest", "RetroError");
        });


//TODO called volley
/*
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utilities.event_details_url,
                response -> {

                    JSONObject jsonResponse = null;
                    Log.e("Response ",response);
                    SharedPreferences.Editor editor = prefs.edit();
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
                    String formattedDate = sdf.format(date);
                    System.out.println(formattedDate);
                    editor.putString("time","Event last updated at "+formattedDate);
                    editor.apply();
                },
                error -> {

                    String defaultValue = "Events not yet updated";
                    String time = prefs.getString("time", defaultValue);
                    Toast.makeText(SplashScreen.this,time,Toast.LENGTH_LONG).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
*/
        polo.startAnimation(downAnimation);
        downAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                festemberLogo.setVisibility(View.VISIBLE);
                festemberLogo.setAnimation(logoEntrance);
                logoEntrance.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        for (int i = 0; i < numberOfClouds; i++) clouds[i].startAnimation(upLongAnimation);
        plane.startAnimation(upLongAnimation);

    }

}
