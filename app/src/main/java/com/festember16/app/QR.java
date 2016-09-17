package com.festember16.app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

public class QR extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
       // Log.e("profile *", Utilities.user_profile_name);

        if(isStoragePermissionGranted())
        {
            if(Utilities.user_profile_name == null)
                Utilities.user_profile_name = Utilities.username;
            {
                ImageView qrCodeImage = (ImageView) findViewById(R.id.qr_code_image);
                qrCodeImage.setVisibility(View.VISIBLE);
                Bitmap bitmap = new SaveImage(Utilities.user_profile_name, null).loadFromCacheFile();


                if (bitmap == null) GetQR();
                else {
                    qrCodeImage.setImageBitmap(bitmap);
                }

            }
        }

    }


    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            if(Utilities.user_profile_name == null)
                Utilities.user_profile_name = Utilities.username;
            {
                ImageView qrCodeImage = (ImageView) findViewById(R.id.qr_code_image);
                qrCodeImage.setVisibility(View.VISIBLE);
                Bitmap bitmap = new SaveImage(Utilities.user_profile_name, null).loadFromCacheFile();


                if (bitmap == null) GetQR();
                else {
                    qrCodeImage.setImageBitmap(bitmap);
                }

            }
        }
    }

    public void GetQR() {

        final ProgressDialog pDialog = new ProgressDialog(this,  R.style.AppTheme_Dark_Dialog);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utilities.user_qr, new Response.Listener<String>() {
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

                    byte[] imageAsBytes = Base64.decode(message.getBytes(), Base64.DEFAULT);
                    ImageView image = (ImageView) QR.this.findViewById(R.id.qr_code_image);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                    image.setImageBitmap(bitmap);
                    SaveImage save = new SaveImage(Utilities.user_profile_name, bitmap);
                    save.saveToCacheFile(bitmap);
                    addImageToGallery(save.getCacheFilename(), QR.this);
                    pDialog.dismiss();
                }
                else{
                    Toast.makeText(QR.this,"Try again ",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(QR.this, "Please check your internet and try again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                int pid = 2;
                params.put("token",Utilities.token);
                params.put("user_id",Utilities.user_id);
                return params;
            }

        };

        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(this).add(stringRequest);

    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }


    }
}



































