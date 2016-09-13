package com.festember16.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MenuCanvas extends View
{



    private Context context;

    float dpHeight, dpWidth, maxRadius, m;
    float vX , vY, acc = 20, dx, dy;

    //List of colours to choose random colours
    List<Paint> colours = new ArrayList<>();
    List<Circle> circles = new ArrayList<>();
    List<Circle> mainCircles =  new ArrayList<>();

    String titles[] = {"Events", "Map", "Game"  , "Profile" , "Schedule" , "Contacts" , "Notifications"};
    Paint textPaint = new Paint();

    boolean draw = true;


    public MenuCanvas(Context context)
    {
        super(context);
        this.context = context;
        // Set the gesture detector as the double tap

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels;
        dpWidth = displayMetrics.widthPixels;

        maxRadius = dpWidth/3/2*0.9f;
        m = dpHeight/dpWidth;

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.YELLOW);
        p.setShadowLayer( maxRadius , 20 , 20 , Color.RED);
        colours.add(p);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.rgb(255, 105, 180));
        colours.add(p);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.rgb(0, 191, 255));
        colours.add(p);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.CYAN);
        colours.add(p);

        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize((maxRadius/3));
        initCircles();

    }



    private float dist( float x1, float y1, float x2, float y2)
    {
        return (float) Math.sqrt( Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }

    private void initCircles()
    {
        float x , y , c = 0;
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


                index = random.nextInt(4);
                while( index == prevIndex )
                {
                    index = random.nextInt(4);
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


                index = random.nextInt(4);
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

                circles.add(circle);
            }
            c -= dpHeight/3;
        }
        Log.wtf("wtf", circles.size() + " size");

        Collections.sort(mainCircles);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(100,0,128));

        for( Circle c: circles)
        {
            canvas.drawCircle( c.cx, c.cy , c.radius , c.colour );

        }
        int div;
        if( draw )
        {
            for( int i = 0 ; i < mainCircles.size() ; i++)
            {
                Circle c = mainCircles.get(i);
                if( c.text.length() < 6)
                {
                    div = 4;
                }
                else if( c.text.length() < 9)
                {
                    div = 6;
                }
                else
                {
                    div = 7;
                }
                canvas.drawText( c.text , c.cx - c.text.length()/2.0f*maxRadius/div  , c.cy , textPaint );
            }
        }


    }


    // Tapped event handler
    public boolean tapped( float x , float y)
    {

        for(int i = 0; i < circles.size() ; i++)
        {
            Circle c = circles.get(i);
            if( Math.pow( ( x - c.cx) ,2) + Math.pow( ( y - c.cy) , 2) < c.radius*c.radius)
            {
                switch ( c.text )
                {
                    case "Map":
                        Toast.makeText(context, c.text, Toast.LENGTH_SHORT).show();
                        break;
                    case "Events":
                        // goto events Page
                        Toast.makeText(context, c.text, Toast.LENGTH_SHORT).show();
                        break;
                    // Add other cases
                    default:
                        // do nothing

                }
            }
        }
        return true;
    }





    public void fling(float distanceX, float distanceY)
    {
        int i , j;
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
                if( c.text == "")
                {
                    c.text = s;
                    mainCircles.set( i , c);
                    break;
                }
            }
        }

        //Collections.sort( mainCircles );
        //sort_circles();
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
