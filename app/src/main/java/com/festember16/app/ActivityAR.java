package com.festember16.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

// AR Activity
public class ActivityAR extends AppCompatActivity implements SensorEventListener, SurfaceHolder.Callback, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    public static final String TAG = "Geo";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    public SensorManager sensorManager;
    static final float ALPHA = 0.25f;
    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] rotation = new float[9];
    private float[] orientation = new float[3];
    private float[] smoothed = new float[3];

    private Sensor sensorGravity;
    private Sensor sensorMagnetic;
    private double bearing = 0;
    Location mLastLocation = new Location("gps");


    // Transparent Canvas to Draw Tiles
    VirtualLayer virtualLayer;
    RelativeLayout VirtualScroll;
    FrameLayout preview;

    SurfaceView camera_surface;
    SurfaceHolder holder;

    Camera camera;

    private GestureDetectorCompat mDetector;

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private double prevBearing = 999;

    private static final String POI_TYPE = "POI_TYPE";
    private static final String LAT = "LAT";
    private static final String LON = "LON";
    private static final String LOCATION = "LOC";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ar);

        Bundle poi_data = getIntent().getExtras();
        if (poi_data == null)
        {
            return;
        }

        String poi_type = poi_data.getString(POI_TYPE);
        mLastLocation = (Location) poi_data.get(LOCATION);

        preview = (FrameLayout) findViewById(R.id.camera_preview);
        camera_surface = (SurfaceView) findViewById(R.id.camera_surface);
        VirtualScroll = (RelativeLayout) findViewById(R.id.VirtualScroll);

        mDetector = new GestureDetectorCompat(this, this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);


        holder = camera_surface.getHolder();
        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);

        //preview.addView(mPreview);
        virtualLayer = new VirtualLayer(this, poi_type);
        VirtualScroll.addView(virtualLayer);
        virtualLayer.setCurrentLocation(mLastLocation);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

        initializeLocationManager();

        try
        {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
        }
        catch (SecurityException ex)
        {
            Log.e(TAG, "fail to request location update, ignore", ex);
        }
        catch (IllegalArgumentException ex)
        {
            Log.e(TAG, "network provider does not exist, " + ex.getMessage());
        }


        try
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
        }
        catch (SecurityException ex)
        {
            Log.e(TAG, "fail to request location update, ignore", ex);
        }
        catch (IllegalArgumentException ex)
        {
            Log.e(TAG, "gps provider does not exist " + ex.getMessage());
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorGravity, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            camera = Camera.open();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        camera.stopPreview();
        camera.release();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e)
    {
        virtualLayer.touch(e.getX(), e.getY());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e)
    {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e)
    {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        virtualLayer.shiftScroll(-distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        return false;
    }

    private class LocationListener implements android.location.LocationListener
    {

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener: " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            // Update UI Only if Location is Accurate
            if (location.getAccuracy() < 400)
            {
                virtualLayer.setCurrentLocation(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    private void initializeLocationManager()
    {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null)
        {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void onSensorChanged(SensorEvent event)
    {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            smoothed = LowPassFilter(event.values, gravity);
            gravity[0] = smoothed[0];
            gravity[1] = smoothed[1];
            gravity[2] = smoothed[2];

        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            smoothed = LowPassFilter(event.values, geomagnetic);
            geomagnetic[0] = smoothed[0];
            geomagnetic[1] = smoothed[1];
            geomagnetic[2] = smoothed[2];

            SensorManager.getRotationMatrix(rotation, null, gravity, geomagnetic);
            SensorManager.getOrientation(rotation, orientation);
            bearing = orientation[0];
            bearing = Math.toDegrees(bearing);

            if (bearing < 0)
            {
                bearing += 360;
            }
            if (bearing < 270)
            {
                bearing += 90;
            }
            else
            {
                bearing += 90;
                bearing %= 360;
            }
            //Log.wtf(TAG, "" + bearing);
            if( Math.abs( prevBearing - bearing) > 1)
            {
                // Set Compass Bearing

                virtualLayer.setCompassBearing(bearing);
                prevBearing = bearing;
            }

        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    protected float[] LowPassFilter(float[] input, float[] output)
    {
        if (output == null) return input;

        for (int i = 0; i < input.length; i++)
        {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null)
        {
            for (LocationListener mLocationListener : mLocationListeners) {
                try
                {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListener);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "fail to remove location listeners, ignore", e);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, sensorGravity);
        sensorManager.unregisterListener(this, sensorMagnetic);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.removeUpdates(mLocationListeners[0]);
        mLocationManager.removeUpdates(mLocationListeners[1]);
        finish();
    }
}

