package com.festember16.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.internal.Util;

public class Events_list extends AppCompatActivity {
    private GridLayoutManager lLayout;
    DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_events_list);
        setTitle(null);
        db = new DBHandler(this);
        List<ItemObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(Events_list.this, 2);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter2 rcAdapter = new RecyclerViewAdapter2(Events_list.this, rowListItem);
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
        for(int i = 0; i < Utilities.events.size();i++) {
            String name = Utilities.events.get(i).getName();
            String temp2 = "";
            if (name.contains("_")) {
                // Split it.
                String[] temp = name.split("_");
                for (int j = 0; j < temp.length; j++) {
                    String temp3 = temp[j].substring(0, 1).toUpperCase() + temp[j].substring(1);
                    temp2 = temp2 + temp3 + " ";
                }

            } else temp2 = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (temp2.equals("Live Photography"))
                temp2 = "Live Photo graphy";
            List valid = Arrays.asList(Utilities.map_events);
            if (valid.contains(temp2)) {
                // is valid

            } else {
                // not valid
                Utilities.map_events[Utilities.global_index] = temp2;
                Utilities.global_index++;
                Utilities.map_events[Utilities.global_index] = ""+Utilities.events.get(i).getId();
                Utilities.global_index++;

            }


            allItems.add(new ItemObject(temp2));
        }


        return allItems;
    }

}
