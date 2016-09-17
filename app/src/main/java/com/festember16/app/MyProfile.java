package com.festember16.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyProfile extends AppCompatActivity {

    public static final String ID = "ID";
    private String name;

    DBHandler db;
    int eventMenuPosBuf;
    Events eventMenuBuf;
    List<Events> eventses;

    @InjectView(R.id.hello)
    TextView hello;
    TextView credits;

    @InjectView(R.id.listView2)

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String temp2 = "";
        if (Utilities.username.contains("@")) {
            // Split it.
            String[] temp = Utilities.username.split("@");
            temp2 = temp[0];

            }

         else temp2 = Utilities.username;
      //  getSupportActionBar().setTitle("Welcome " + name + "!");
        hello = (TextView)findViewById(R.id.hello);
        credits = (TextView)findViewById(R.id.credits);
        hello.setText("Welcome " + temp2);
        Utilities.user_profile_name = temp2;
        ButterKnife.inject(this);

        db = new DBHandler(this);

        callCredits();

        eventses = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences(
                DetailsFragment.REGISTERED_EVENTS,
                MODE_PRIVATE
        );

        Map<String, Boolean> map = (Map<String, Boolean>) preferences.getAll();

        Iterator iterator = map.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String, Boolean> entry = (Map.Entry<String, Boolean>) iterator.next();

            int id = Integer.parseInt(entry.getKey());
            //Todo: test if call to database works
            eventses.add(db.getEvent(id));
        }

        listView.setAdapter(new EventsAdapter(
                MyProfile.this,
                0,
                eventses
        ));


      //  registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyProfile.this, DetailsActivity.class);
                intent.putExtra(ID, eventses.get(position).getId());
                startActivity(intent);
            }
        });
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//        if(v.getId()==R.id.listView2){
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//
//            //Get the contact that was LongClicked.
//            eventMenuPosBuf = info.position;
//            eventMenuBuf = eventses.get(info.position);
//
//            //populate menu
//           menu.add("Unregister");
//        }
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        switch (id){
//            case 0:
//                //Todo: Call api and ask it to unregiser
//                //Todo: if(response==true)
//
//                eventses.remove(eventMenuPosBuf);
//                listView.setAdapter(new EventsAdapter(
//                        this, 0, eventses
//                ));
//
//                SharedPreferences preferences = getSharedPreferences(
//                        DetailsFragment.REGISTERED_EVENTS,
//                        MODE_PRIVATE
//                );
//
//                SharedPreferences.Editor editor = preferences.edit();
//
//                editor.remove(String.valueOf(eventMenuBuf.getId()));
//
//                editor.apply();
//        }
//
//        return super.onContextItemSelected(item);
//    }

    @Override
    protected void onStop() {
        super.onStop();

        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.menu_profile,
                menu
        );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case R.id.qr_code:
                Intent i = new Intent(this,QR.class);
                startActivity(i);

                break;

            case R.id.logout:
                //Todo: Call api for logout
                //Todo: if(response==true)
               SharedPreferences pref = getSharedPreferences("user_auth", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("login_status",1);
                editor.apply();
                Intent in = new Intent(this,LoginActivity.class);
                startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }
    public void callCredits() {
        final ProgressDialog pDialog = new ProgressDialog(MyProfile.this,
                R.style.AppTheme_Dark_Dialog);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utilities.user_credits, new Response.Listener<String>() {
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
                if (status == 200) {

                    credits.setText("Your credits: " + message);
                    pDialog.dismiss();
                } else {
                    Toast.makeText(MyProfile.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(MyProfile.this, "Please check your internet and try again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                int pid = 2;
                params.put("token", Utilities.token);
                params.put("user_id", Utilities.user_id);

                return params;
            }

        };

        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(this).add(stringRequest);


//

    }

}
