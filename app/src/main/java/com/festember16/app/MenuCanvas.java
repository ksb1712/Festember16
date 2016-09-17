package com.festember16.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MenuCanvas extends View
{


    public static final String LOG_TAG = "MenuCanvas";
    private Context context;

    public float dpHeight, dpWidth, maxRadius, m;

    //List of colours to choose random colours
    List<Paint> colours = new ArrayList<>();
    List<Circle> circles = new ArrayList<>();
    List<Circle> mainCircles =  new ArrayList<>();
    Circle clickedCircle;

    String titles[] = {"Events", "Map", "Upcoming"  , "Pr" +
            "ofile" , "Schedule" , "Scoreboard" , "Notifications"};
    Paint textPaint = new Paint();
    Paint backgroundPaint = new Paint();
    Paint titlePaint = new Paint();
    Paint menuPaint = new Paint();
    float scaleRadius;

    Intent callIntent;

    boolean draw = true, touched = false;



    public MenuCanvas(Context context)
    {
        super(context);
        this.context = context;
        // Set the gesture detector as the double tap
        scaleRadius = 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels;
        dpWidth = displayMetrics.widthPixels;

        maxRadius = dpWidth/3/2*0.9f;
        m = dpHeight/dpWidth;

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.rgb(255 , 204 , 0));
        //p.setShadowLayer( maxRadius , 20 , 20 , Color.RED);
        colours.add(p);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.rgb(0, 191, 255));
        colours.add(p);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.rgb(255, 105, 180));
        colours.add(p);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.CYAN);
        colours.add(p);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.LTGRAY);
        colours.add(p);

        backgroundPaint.setColor(Color.rgb(100,0,100));
        textPaint.setColor(Color.BLACK);
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am , String.format(Locale.US, "fonts/%s" , "HARLOWSI.TTF"));
        textPaint.setTypeface(custom_font);
        textPaint.setTextSize((maxRadius/3));

        titlePaint.setColor(Color.WHITE);
        titlePaint.setTypeface(custom_font);
        titlePaint.setTextSize(maxRadius);

        // TODO change menu paint as needed
        menuPaint.setColor(Color.WHITE);
        menuPaint.setTypeface(custom_font);
        menuPaint.setTextSize(maxRadius);
        menuPaint.setAntiAlias(true);
        menuPaint.setTextAlign(Paint.Align.CENTER);

        initCircles();

    }


    private float dist( float x1, float y1, float x2, float y2)
    {
        return (float) Math.sqrt( Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    private void initCircles()
    {
        float x , y , c;
        scaleRadius = 0;
        Circle circle;
        circles.clear();
        Random random = new Random();
        Paint p = new Paint();
        p.setColor(Color.RED);
        int index, prevIndex = -1, i = 0;
        c = 0;
        //mainCircles.clear();
        while( c < dpHeight)
        {
            for( y = dpHeight/6; y <= dpHeight ; y += dpHeight/6)
            {
                circle = new Circle(dpWidth);
                x = (y - c)/m;
                circle.cx = x;
                circle.cy = y;


                index = random.nextInt(5);
                while( index == prevIndex )
                {
                    index = random.nextInt(5);
                }
                prevIndex = index;
                circle.colour = colours.get(index);
                //circle.radius = maxRadius;
                if( x < 0 || y < 0 || y > dpHeight || x > dpWidth)
                {
                    continue;
                }

                if( (x-dpWidth/2)*(x-dpWidth/2) + (y-dpHeight/2)*(y-dpHeight/2) < dpWidth*dpWidth/4)
                {
                    circle.radius = maxRadius;
                    circle.text = titles[i++];
                    mainCircles.add(circle);
                }
                else if( (x-dpWidth/2)*(x-dpWidth/2) + (y-dpHeight/2)*(y-dpHeight/2) < dpWidth*dpWidth/2)
                {
                    circle.text = "";
                    circle.radius = (float) Math.pow(dpWidth/2/dist( dpWidth/2, dpHeight/2, x , y), 6)*maxRadius;
                    mainCircles.add(circle);

                }
                else
                {
                    circle.text = "";
                    circle.radius = px(20)*maxRadius/dist( dpWidth/2, dpHeight/2, x , y);
                }

                if( circle.cx == 0)
                {
                    circle.visibility = false;
                }
                circles.add(circle);
            }
            c += dpHeight/3;

        }
        c = -dpHeight/3;
        Log.wtf("wtf", circles.size() + " size");
        while( c  > -dpHeight)
        {
            for( y = dpHeight/6; y <= dpHeight ; y += dpHeight/6)
            {
                circle = new Circle(dpWidth);
                x = (y - c)/m;
                circle.cx = x;
                circle.cy = y;


                index = random.nextInt(5);
                while( index == prevIndex )
                {
                    index = random.nextInt(5);
                }
                prevIndex = index;
                circle.colour = colours.get(index);
                //circle.radius = maxRadius;
                if( x < 0 || y < 0 || y > dpHeight || x >= dpWidth)
                {
                    continue;
                }

                if( (x-dpWidth/2)*(x-dpWidth/2) + (y-dpHeight/2)*(y-dpHeight/2) < dpWidth*dpWidth/4)
                {
                    circle.radius = maxRadius;
                    circle.text = titles[i++];
                    mainCircles.add(circle);
                }
                else if( (x-dpWidth/2)*(x-dpWidth/2) + (y-dpHeight/2)*(y-dpHeight/2) < dpWidth*dpWidth/2)
                {
                    circle.text = "";
                    circle.radius = (float) Math.pow(dpWidth/2/dist( dpWidth/2, dpHeight/2, x , y), 6)*maxRadius;
                    mainCircles.add(circle);
                }
                else
                {
                    circle.text = "";
                    circle.radius = px(20)*maxRadius/dist( dpWidth/2, dpHeight/2, x , y);
                }
                if( circle.cx == 0)
                {
                    circle.visibility = false;
                }
                circles.add(circle);
            }
            c -= dpHeight/3;
        }
        Log.wtf("wtf", circles.size() + " size");

        Collections.sort(mainCircles);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(100, 0, 100));
        for (Circle c : circles) {
            if (c.visibility) {
                canvas.drawCircle(c.cx, c.cy, c.radius, c.colour);
            } else {
                c.visibility = true;
            }

        }
        float div;
        if (draw) {
            for (int i = 0; i < mainCircles.size(); i++) {
                Circle c = mainCircles.get(i);
                if (c.text.length() < 4) {
                    textPaint.setTextSize(maxRadius / 2.5f);
                    div = 3.5f;
                } else if (c.text.length() < 6) {
                    textPaint.setTextSize(maxRadius / 2.5f);
                    div = 4.3f;
                } else if (c.text.length() < 9) {
                    textPaint.setTextSize(maxRadius / 2.5f);
                    div = 5.3f;
                } else {
                    div = 7;
                    textPaint.setTextSize(maxRadius / 3);
                }
                canvas.drawText(c.text, c.cx - c.text.length() / 2.0f * maxRadius / div, c.cy, textPaint);
            }
        }
        canvas.drawRect(0, 0, dpWidth, maxRadius * 1.4f, backgroundPaint);
        canvas.drawText("Festember 2016", maxRadius * 0.1f, maxRadius, titlePaint);
        if( touched )
        {
            if(scaleRadius == 0){
                scaleRadius = clickedCircle.radius;
                invalidate();
            } else {
                if ((dist(clickedCircle.cx,clickedCircle.cy,0,0) > scaleRadius)
                        || (dist(clickedCircle.cx,clickedCircle.cy,0,dpHeight) > scaleRadius)
                        || (dist(clickedCircle.cx,clickedCircle.cy,dpWidth,dpHeight) > scaleRadius)
                        || (dist(clickedCircle.cx,clickedCircle.cy,dpWidth,0) > scaleRadius) ) {
                    canvas.drawCircle(clickedCircle.cx, clickedCircle.cy, scaleRadius, clickedCircle.colour);
//                    canvas.drawText("Festember 2016", maxRadius * 0.1f, maxRadius, titlePaint);
                    scaleRadius += dpHeight / 50;
                    invalidate();
                } else {
                    canvas.drawCircle(clickedCircle.cx, clickedCircle.cy, scaleRadius, clickedCircle.colour);
//                    canvas.drawText("Festember 2016", maxRadius * 0.1f, maxRadius, titlePaint);
                    canvas.drawText(clickedCircle.text,dpWidth/2,dpHeight/2,menuPaint);
                }
            }
        }
    }


    //Call other activities
    public boolean tapped( float x , float y)
    {
        int waitTime = 1000;
        for(int i = 0; i < circles.size() ; i++)
        {
            Circle c = circles.get(i);
            if( Math.pow( ( x - c.cx) ,2) + Math.pow( ( y - c.cy) , 2) < c.radius*c.radius)
            {
                switch ( c.text )
                {
                    case "Map":
                        // Call Intents
//                        Log.d(LOG_TAG, "You clicked Map");
//                        Toast.makeText(context, c.text, Toast.LENGTH_SHORT).show();
                        callIntent = new Intent(context, MainMapsActivity.class);
                        touchEffect(c);
                        break;
                    case "Events":
                        // goto events Page
//                        Log.e(LOG_TAG, "You click Events");
                        callIntent = new Intent(context, ClusterPage.class);
                        touchEffect(c);
//                        DBHandler db;
//                        db = new DBHandler(context);
                        //String s = db.getCluster();
                        //Log.e("Cluster ",s);
//                        Toast.makeText(context, c.text, Toast.LENGTH_SHORT).show();
                        break;
                    case "Upcoming":
                        // goto events Page
                        callIntent = new Intent(context, UpcomingActivity.class);
                        touchEffect(c);
//                        Log.d(LOG_TAG, "You clicked Game");
//                        Toast.makeText(context, c.text, Toast.LENGTH_SHORT).show();
                        break;
                    case "Profile":
                        callIntent = new Intent(context,QR.class);
                        touchEffect(c);
//                        Log.d(LOG_TAG, "You clicked Profile");
//                        Toast.makeText(context, c.text, Toast.LENGTH_SHORT).show();
                        break;

                    // Add other cases

                    case "Schedule":
                        Log.d(LOG_TAG, "You clicked Schedule");
                        //callIntent = new Intent(context,UpcomingActivity.class);
                        callIntent = new Intent(context , ScheduleActivity.class);
                        touchEffect(c);
                        break;

                    case "Scoreboard":
                        callIntent = new Intent(context,Scoreboard.class);
                        touchEffect(c);
//                        Log.d(LOG_TAG, "You clicked ScoreBoard");
                        break;

                    case "Notifications":

                        Log.d(LOG_TAG,"You clicked Notifications");
                        callIntent = new Intent(context,Notification.class);
                        touchEffect(c);
                        break;
                    default:
                        // do nothing
                }
                new Handler().postDelayed(() -> {
                    touched = false;
                    scaleRadius = 0;
                    if(callIntent != null)
                        context.startActivity(callIntent);
                    else
                        invalidate();
                }, waitTime);
            }
        }
        return true;
    }

    void touchEffect(Circle c)
    {
        Log.d(LOG_TAG,"touchEffect called");
        clickedCircle = c;
        touched = true;
        invalidate();
    }

    public void fling(float distanceX, float distanceY)
    {
        int i;
        float x, y;
        Circle c;
        mainCircles.clear();
        List<String> leftOutTitles = new ArrayList<>();
        for( i = 0 ; i < circles.size(); i++)
        {
            c = circles.get(i);

            if( c.cx - distanceX < 0)
            {
                c.cx = c.cx - distanceX + dpWidth;
            }
            else
            {
                c.cx = (c.cx - distanceX) % dpWidth;
            }
            if( c.cy - distanceY < 0)
            {
                c.cy = c.cy - distanceY + dpHeight;
            }
            else
            {
                c.cy = (c.cy - distanceY) % dpHeight;
            }
            x = c.cx;
            y = c.cy;
            if( (x-dpWidth/2)*(x-dpWidth/2) + (y-dpHeight/2)*(y-dpHeight/2) < dpWidth*dpWidth/4)
            {
                c.radius = maxRadius;
                if( c.cx < maxRadius || c.cx > dpWidth - maxRadius)
                {
                    if( !c.text.equals("") )
                    {
                        leftOutTitles.add(c.text);
                        c.text = "";
                    }
                }
                mainCircles.add(c);
            }
            else if( (x-dpWidth/2)*(x-dpWidth/2) + (y-dpHeight/2)*(y-dpHeight/2) < dpWidth*dpWidth/2)
            {
                c.radius = (float) Math.pow(dpWidth/2/dist( dpWidth/2, dpHeight/2, x , y), 6)*maxRadius;
                if( ! c.text.equals("") )
                {
                    leftOutTitles.add( c.text);
                    c.text = "";
                }
                mainCircles.add(c);
            }
            else
            {
                c.radius = px(20)*maxRadius/dist( dpWidth/2, dpHeight/2, x , y);
                if( ! c.text.equals("") )
                {
                    leftOutTitles.add( c.text);
                    c.text = "";
                }
            }
            circles.set( i , c);

        }

        Collections.sort(mainCircles);


        for( String s: leftOutTitles)
        {
            for( i = 0 ; i < mainCircles.size() ; i++)
            {
                c = mainCircles.get(i);
                if( c.text.isEmpty() )
                {
                    c.text = s;
                    mainCircles.set( i , c);
                    break;
                }
            }
        }

        invalidate();
    }



    private int px(double pix)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = (float) (pix * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return (int)dp;
    }



}
