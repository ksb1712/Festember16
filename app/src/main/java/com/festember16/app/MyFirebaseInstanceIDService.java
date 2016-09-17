package com.festember16.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ajay Nataraj on 9/14/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    public static Context context;

    public static void register(Context cont) {
        context = cont;
        Intent intent = new Intent(cont, MyFirebaseInstanceIDService.class);
        cont.startService(intent);
    }


    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

       sendRegistrationToServer(refreshedToken);
    }

    static String msg;

    private void sendRegistrationToServer(final String token) {
        VolleySingleton volleySingleton = VolleySingleton.getsInstance();
        final RequestQueue requestQueue = volleySingleton.getmRequestQueue();

        String serverUrl = "https://festember.com/~kousik/simple-gcm/register.php";
        Log.e("gcm_status", "Attempt to register");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                msg = response;
                try {

                    JSONObject js = new JSONObject(response);
                    int fcm = js.getInt("status_code");
                    Log.i("returned status code",String.valueOf(fcm));
                   // Utilities.gcm_registered = 1;
//                    SharedPreferences.Editor editor = Utilities.sp.edit();
//                    editor.putInt("gcm_registered", 1);
//                    editor.apply();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("gcm_status", "Failed to register :" + error);

                Toast.makeText(context,"Please check your internet and Try again",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                //TODO p_id from myprofile
                params.put("p_id", String.valueOf(1234));
                params.put("gcm_id", token);
                return params;
            }
        };

        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }

}
