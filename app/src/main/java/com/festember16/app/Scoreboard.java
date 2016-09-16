package com.festember16.app;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard   extends AppCompatActivity{

    private GridLayoutManager lLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rView;
     ProgressDialog progressDialog;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
            setContentView(R.layout.activity_scoreboard);
            setTitle(null);


            rView = (RecyclerView)findViewById(R.id.RecyclerView);
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            refreshItems();
                        }
                    }
            );
           progressDialog = new ProgressDialog(Scoreboard.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            callScore();


        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement


            return super.onOptionsItemSelected(item);
        }

        private List<Score_class> getAllItemList(String data){

            List<Score_class> allItems = new ArrayList<Score_class>();

            JSONArray jsonarray = null;
            try {
                if (data != null) {
                    jsonarray = new JSONArray(data);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = null;
                        try {
                            jsonobject = jsonarray.getJSONObject(i);
                            String rank = jsonobject.getString("rank");
                            String college = jsonobject.getString("college");
                            String points = jsonobject.getString("points");
                            allItems.add(new Score_class(college,points,rank));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
            }
            }catch (JSONException e) {
                e.printStackTrace();


            }


            return allItems;
        }
    void refreshItems() {
        // Load items
        // ...
        callScore();
        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation

    }

    public void callScore()
    {



        // TODO: Implement your own authentication logic here.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utilities.scoreboard_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String message = null;
                        try {


                            message = jsonResponse.getString("data");
                            List<Score_class> rowListItem = getAllItemList(message);
                            lLayout = new GridLayoutManager(Scoreboard.this, 1);
                            CustomAdapter rcAdapter = new CustomAdapter(Scoreboard.this.getApplicationContext(), rowListItem);
                            rView.setAdapter(rcAdapter);
                            Log.e("Data ",message);
                            rView.setHasFixedSize(true);
                            rView.setLayoutManager(lLayout);
                            mSwipeRefreshLayout.setRefreshing(false);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Scoreboard.this,error.toString(),Toast.LENGTH_LONG).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
