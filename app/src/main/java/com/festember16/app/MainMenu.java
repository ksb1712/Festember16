package com.festember16.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainMenu extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{

    MenuCanvas c;

    RelativeLayout LayoutRoot;
    private static final String TAG = "FCM";
    private GestureDetectorCompat mDetector;

    Retrofit retrofit;
    Observable<LoginRegister> registeredObservable;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyFirebaseInstanceIDService.context=this;
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        String token = FirebaseInstanceId.getInstance().getToken();


        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg);


//        DBHandler db = new DBHandler(this);
//        String[] clusters = db.getClusters();
//        List<Events> e = db.getEventsByCluster("english_lits");
//        Log.e("test" , clusters[5] + "size " + clusters.length);
//        Log.e("events", e.get(0).getName());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);
        SharedPreferences pref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
        int status = pref.getInt("login_status",1);
        if(status == 2 )
        {
            Utilities.token = pref.getString("token","");
            Utilities.user_id = pref.getString("user_id","");
            Utilities.username = pref.getString("user_email","");

        }
        Log.e("status ",""+status );
        LayoutRoot = (RelativeLayout) findViewById(R.id.LayoutRoot);
        c = new MenuCanvas(this);
        LayoutRoot.addView(c);
        mDetector = new GestureDetectorCompat(this,this);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        checkForRegisteredEvents();
    }

    public void checkForRegisteredEvents (){

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Utilities.base_url)
                .build();

        RegisterEventService registerEventService = retrofit.create(RegisterEventService.class);

        registeredObservable = registerEventService.getRegistered(Utilities.user_id, Utilities.token);

        registeredObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(registeredEvent -> {
                    if(registeredEvent.getStatusCode() == 200) {
                        editor = sharedPref.edit();
                        editor.putString("registered_events", registeredEvent.getMessage());
                    }
                    else{
                    }
                    editor.commit();
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        c.callIntent = null;
        c.tapped( e.getX() , e.getY());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        if( e1.getY() > c.maxRadius*1.4)
        {
            c.fling( distanceX, distanceY);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
        c.tapped( e.getX() , e.getY());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        //vX = velocityX;
        //vY = velocityY;
        //flingThread.start();
        return false;
    }
}
