package com.festember16.app;

import android.graphics.Paint;


public class Circle implements Comparable<Circle>
{
    float cx;
    float cy;
    float radius;
    Paint colour;
    boolean visibility;
    public String text;

    float width;
    Circle(float dpWidth)
    {
        colour = new Paint();
        visibility = true;
        width = dpWidth;
    }

    @Override
    public int compareTo(Circle o)
    {
        if( (int)radius > (int)o.radius)
        {
            return -1;
        }
        else if( (int)radius < (int)o.radius )
        {
            return 1;
        }
        float min1 = ( (cx<width-cx)?cx:width-cx);
        float min2 = ( (o.cx<width-o.cx)?o.cx:width-o.cx);
        if( min1 > min2)
        {
            return -1;
        }
        else if( min2 > min1 )
        {
            return 1;
        }

        return  0;
    }
}
