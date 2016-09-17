package com.festember16.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.List;

public class MainMenu extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{

    MenuCanvas c;

    RelativeLayout LayoutRoot;

    private GestureDetectorCompat mDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

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


    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.mDetector.onTouchEvent(event);


        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e)
    {
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
