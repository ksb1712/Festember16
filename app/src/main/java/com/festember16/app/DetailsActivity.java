package com.festember16.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailsActivity extends AppCompatActivity {

    public static final int MAP_REQUEST_CODE = 1001;
    public static final String ID = "ID";
    public static final String LOG_TAG = "DetailsActivity";
    int eventId = 2;

    public static DBHandler db;
    public static Events detailedEvent;

    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        ButterKnife.inject(this);
        int id = getIntent().getIntExtra("event_id",0);
        db = new DBHandler(this);

        //Todo: get id from parent activity// eventId = getIntent().getIntExtra("ID", 0);
        //Todo: Test out dbhandler call
        eventId = id;
        detailedEvent = db.getEvent(id);

        try {
            if (detailedEvent != null) {
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitleTextColor(Color.WHITE);
                toolbar.setTitle(EventsAdapter.parseEventName(detailedEvent.getName()));
                setSupportActionBar(toolbar);
            }
        }
        catch (NullPointerException e){
            Log.e(LOG_TAG, e.getMessage());
        }
        pager.setAdapter(
                new MyPagerAdapter(getSupportFragmentManager(), eventId)
        );
//        pager.setCurrentItem(1);

        tabs.setupWithViewPager(pager);

//        tabs.setSelectedTabIndicatorColor(new SlidingTabLayout.TabColorizer() {
//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public int getIndicatorColor(int position) {
//                if(Build.VERSION.SDK_INT>=23){
//                    return getResources().getColor(R.color.colorAccent, null);
//                }
//                else{
//                    return getResources().getColor(R.color.colorAccent);
//                }
//            }
//        });

        tabs.setSelectedTabIndicatorColor(
                getResources().getColor(R.color.colorAccent)
        );

    }

    @Override
    protected void onStop() {
        super.onStop();

        //db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_event_details, menu);

        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//
//            case R.id.linkToMap:
//                Intent intent = new Intent(
//                        DetailsActivity.this,
//                        MapsActivity.class
//                );
//
//               intent.putExtra(ID, eventId);
//
//                startActivityForResult(intent, MAP_REQUEST_CODE);
//
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch(requestCode){
//            case MAP_REQUEST_CODE:
//                //Handle some action if necessary
//                break;
//        }
//    }
}
