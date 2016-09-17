package com.festember16.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ClusterPage extends AppCompatActivity {


    private GridLayoutManager lLayout;
    DBHandler db;
    String clusters[] = new String[15];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cluster_page);
        setTitle(null);
        Log.e("In cluster"," cluster");
        db = new DBHandler(this);

      clusters = db.getClusters();
      //  Log.e("clusters ",s);
        List<ItemObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(ClusterPage.this, 2);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(ClusterPage.this, rowListItem);
        rView.setAdapter(rcAdapter);
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

    private List<ItemObject> getAllItemList(){


        List<ItemObject> allItems = new ArrayList<ItemObject>();
        if(clusters != null) {
            for (int i = 0; i < clusters.length; i++) {
                if (clusters[i] != null) {
                    if (clusters[i].equals("workshops") || clusters[i].equals("informals") || (clusters[i].equals("pro_shows")))
                        ;
                    else
                    {
                        String name =clusters[i];
                        String temp2="";
                        if (name.contains("_")) {
                            // Split it.
                            String[] temp = name.split("_");
                            for(int j=0;j<temp.length;j++) {
                                String temp3 = temp[j].substring(0,1).toUpperCase() + temp[j].substring(1);
                                temp2 = temp2 + temp3 + " ";
                            }

                        }else temp2=name.substring(0,1).toUpperCase() + name.substring(1);
                        if(temp2.equals("Photography"))
                            temp2 = "Photo\ngraphy";
                        allItems.add(new ItemObject(temp2));
                    }
                }
            }
        }

        else
            allItems.add(new ItemObject("No events"));

        Utilities.clusters = clusters;
        return allItems;
    }
}
