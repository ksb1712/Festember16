package com.example.bharath17.festember16;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailsActivity extends AppCompatActivity {

    public static final int MAP_REQUEST_CODE = 1001;
    int eventId = 2;

    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.inject(this);

        //Todo: get id from parent activity

        pager.setAdapter(
                new MyPagerAdapter(getSupportFragmentManager(), eventId)
        );
        pager.setCurrentItem(1);
        tabs.setDistributeEvenly(true);
        tabs.setViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_details, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.linkToMap:
                Intent intent = new Intent(
                        DetailsActivity.this,
                        MapsActivity.class
                );

                Gson gson = new Gson();

                startActivityForResult(intent, MAP_REQUEST_CODE);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case MAP_REQUEST_CODE:
                //Handle some action if necessary
                break;
        }
    }
}
