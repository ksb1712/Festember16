package com.mygdx.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailsActivity extends AppCompatActivity {

    public static final int MAP_REQUEST_CODE = 1001;
    public static final String ID = "ID";
    int eventId = 2;

   // public static DBHandler db;
    public static Events detailedEvent;

    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.inject(this);

        //db = new DBHandler(this);

        //Todo: get id from parent activity// eventId = getIntent().getIntExtra("ID", 0);
        //Todo: Test out dbhandler call
        //detailedEvent = db.getEvent(eventId);

        pager.setAdapter(
                new MyPagerAdapter(getSupportFragmentManager(), eventId)
        );
        pager.setCurrentItem(1);

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

    //    db.close();
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

               intent.putExtra(ID, eventId);

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
