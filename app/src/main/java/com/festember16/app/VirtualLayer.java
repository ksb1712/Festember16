package com.festember16.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class VirtualLayer extends View
{
    // Bearing from the compass [0,360]
    double compassBearing;
    double shiftBy = 0;
    Location currentLocation = new Location("gps");

    // List of POIs in View of the user
    List<PointOfInterest> poisInView = new ArrayList<>();
    List<PointOfInterest> allPOIs = new ArrayList<>();
    PointOfInterest touched = new PointOfInterest("NULL" , 0.0 , 0.0);
    Context context;

    Bitmap green, blue;
    double maxH , minH = 0;
    float dpHeight, dpWidth;

    // Indicates if accurate location is available
    boolean isLoc = false, isTouch = false;

    private static final String HOSTEL = "HOSTEL";
    private static final String UTILS = "UTILS";
    private static final String EVENTS = "EVENTS";



    public VirtualLayer(Context context, String poi_type)
    {
        super(context);
        this.context = context;
        // Set default location
        currentLocation.setLatitude(10.761027);
        currentLocation.setLongitude(78.814204);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels ;
        dpWidth = displayMetrics.widthPixels;

        green = BitmapFactory.decodeResource(context.getResources(), R.drawable.green);
        blue = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue);

        switch (poi_type)
        {
            case HOSTEL:
                allPOIs = PointOfInterestManager.getHostels();
                break;
            case UTILS:
                allPOIs = PointOfInterestManager.getUtils();
                break;
            case EVENTS:
                allPOIs = PointOfInterestManager.getEvents();
                break;
        }

    }

    public void shiftScroll( float distY)
    {
        Log.wtf("wtf", distY + "");
        if( shiftBy + distY < -minH && shiftBy + distY >= 0)
        {
            shiftBy += distY;
        }

    }

    public void touch( float x , float y )
    {
        for (PointOfInterest p : poisInView)
        {
            if( x > p.left && x < p.left + dp(100) && y > p.top && y < p.top + dp(100))
            {
                isTouch = true;
                touched = p;
            }
        }
        invalidate();
    }

    private int dp(double px)
    {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = (float) (px * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return (int)dp;
    }

    // Sets compass bearings and updates layout
    public void setCompassBearing(double bearing)
    {
        compassBearing = bearing;
        updateTiles();
    }

    // sets current location and updates layout
    public void setCurrentLocation(Location  _currentLocation)
    {
        currentLocation = _currentLocation;
        updateTiles();
        isLoc = true;
    }

    //  Update tiles
    public void updateTiles()
    {
        getPoisInRange();
        int i, j;
        maxH = dpHeight - dp(100);
        //minH = 0;
        // Change top of POIs in View so that they dont overlap
        for ( i = 0 ; i < poisInView.size(); i++)
        {
            PointOfInterest poi = poisInView.get(i);

            for (j = 0; j < poisInView.size(); j++)
            {
                PointOfInterest _poi = poisInView.get(j);
                if ( Math.abs(poi.left - _poi.left) < dp(100) && Math.abs(poi.top - _poi.top) < dp(100) && (poi.location != _poi.location))
                {


                    double dist1 = LocationData.getDistance(currentLocation, poi.location);
                    double dist2 = LocationData.getDistance(currentLocation, _poi.location);

                    if( dist2 < dist1 )
                    {
                        _poi.top += dp(110);
                        if( _poi.top > maxH )
                        {
                            maxH = _poi.top;
                        }
                        poisInView.set(j , _poi);
                    }
                    else
                    {
                        _poi.top -= dp(110);
                        if( minH > _poi.top)
                        {
                            minH = _poi.top;
                        }
                        poisInView.set(j, _poi);
                        if( _poi.top < minH )
                        {
                            minH = _poi.top;
                        }
                    }


                    j = 0;
                }
            }

        }

        if(isLoc)
        {
            invalidate();
        }
    }


    // Gets POIs in Range
    private void getPoisInRange()
    {
        //List<PointOfInterest> allPOIs = PointOfInterestManager.getPOIs();
        poisInView.clear();
        for( PointOfInterest p: allPOIs)
        {
            //double bearing = LocationData.getBearing(currentLocation ,p.location);
            double bearing = currentLocation.bearingTo(p.location);
            double diff = Math.abs(compassBearing - bearing);

            if( diff < 30 || diff > 330)
            {
                p.top = dpHeight - dp(130);
                p.left = 0;
                p.left = getLeft(dpWidth , compassBearing , bearing);
                p.dist = LocationData.getDistance(currentLocation, p.location);
                p.distance = String.format("%.2f km", LocationData.getDistance(currentLocation, p.location));
                //p.eta = String.format("ETA %.2f mins", 1.5 * LocationData.getDistance( currentLocation , p.location)*10);
                p.eta = "ETA " + Math.round(1.4 * LocationData.getDistance( currentLocation , p.location)* 10) + " mins";
                poisInView.add(p);

            }
        }

        Collections.sort(poisInView);

    }

    // Gets Left of POI tile from the difference in bearings
    private double getLeft(double actualWidth, double compass, double bearing)
    {
        double mid = actualWidth / 2;
        double div = mid / 30;
        if( compass < 180)
        {
            bearing -= compass;
        }
        else
        {
            bearing += (360 - compass);
            if(bearing > 90)
            {
                bearing = -(360 - bearing);
            }
        }
        if( bearing > 0)
        {
            double dist = mid + div * bearing - 35;
            return dist;
        }
        else
        {
            double dist =  mid - div *(-bearing) - 35;
            return dist;
        }
    }

    //Draw
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(70);
        paint.setAlpha(60);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        Paint p1 = new Paint();
        p1.setColor(Color.RED);
        p1.setTextSize(dp(12));
        p.setTextSize(dp(18));

        double maxHeight = canvas.getHeight(), maxWidth = canvas.getWidth();


        if(isLoc && ! isTouch)
        {

            canvas.drawColor(Color.TRANSPARENT);
            for( PointOfInterest poi: poisInView)
            {

                canvas.drawBitmap( green , (float)poi.left, (float)poi.top + (float)(shiftBy), paint);
                String title[] = poi.Title.split(" ");
                int top = dp(18);
                for ( String str: title)
                {
                    canvas.drawText( str ,(float)poi.left + dp(10) , (float)poi.top + top + (float)(shiftBy), p);
                    top += dp(18);
                }


                canvas.drawText( poi.eta ,(float)poi.left + dp(10) , (float)poi.top + dp(100) - dp(18) +(float)(shiftBy) , p1);
            }
        }
        else if( isLoc && isTouch)
        {
            canvas.drawColor(Color.BLUE);
            canvas.drawText(touched.Title , (float) maxWidth/2 - 100 , (float) maxHeight/2 , p);
        }
        else
        {
            canvas.drawColor( Color.argb( 100 , 0 , 200 , 0));
            canvas.drawText("Finding your Location",(float) maxWidth/2 - 100 , (float) maxHeight/2, p);

        }


    }


}
