package com.festember16.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;


public class ScheduleActivity extends AppCompatActivity
{
    private DrawerLayout DrawerLayoutRoot;
    private ListView ClusterList;

    private ActionBarDrawerToggle drawerListener;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

    private DBHandler handler;

    TabLayout tabs;
    ViewPager pager;

    List<String> clusters = new ArrayList<>();
    List<String> clustersUnformatted = new ArrayList<>();
    List<Events> allEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_schedule);

        handler = new DBHandler(this);

        DrawerLayoutRoot = (DrawerLayout) findViewById(R.id.DrawerLayoutRoot);
        toolbar= (Toolbar) findViewById(R.id.app_bar);

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.viewPager);

        //List View Stuff
        ClusterList = (ListView) findViewById(R.id.ClusterList);
        setSupportActionBar(toolbar);


        clusters.add("All");
        clustersUnformatted.add("All");
        String [] clusterStrings = handler.getClusters();
        String temp2 = "";
        for( int i = 0 ; i < clusterStrings.length ; i++)
        {
            clustersUnformatted.add(clusterStrings[i]);
            if (clusterStrings[i].contains("_"))
            {
                // Split it.
                temp2 = "";
                String[] temp = clusterStrings[i].split("_");
                for (int j = 0; j < temp.length; j++) {
                    String temp3 = temp[j].substring(0, 1).toUpperCase() + temp[j].substring(1);
                    temp2 = temp2 + temp3 + " ";
                }

            }
            else temp2 = clusterStrings[i].substring(0, 1).toUpperCase() + clusterStrings[i].substring(1);

            clusters.add(temp2);
        }

        Log.wtf("wtf" , clusters.size() + "");
        ClusterAdapter clusterAdapter = new ClusterAdapter(this , clusters);

        allEvents = handler.getAllEvents();

        ClusterList.setAdapter(clusterAdapter);
        populateLists(0);

        ClusterList.setItemChecked( 0 , true);
        ClusterList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                EventsListByDay.eventsDay0.clear();
                EventsListByDay.eventsDay1.clear();
                EventsListByDay.eventsDay2.clear();
                EventsListByDay.eventsDay3.clear();
                populateLists(position);
                //pager.getAdapter().notifyDataSetChanged();
                pager.setAdapter(new SchedulePagerAdapter(getSupportFragmentManager()));
                tabs.setupWithViewPager(pager);
                DrawerLayoutRoot.closeDrawers();
            }
        });

        //End of List View Stuff
        drawerToggle = new ActionBarDrawerToggle( this, DrawerLayoutRoot, toolbar , R.string.app_name, R.string.app_name)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                toolbar.setTitle("Schedule");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle("Clusters");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.setDrawerIndicatorEnabled(true);
        DrawerLayoutRoot.setDrawerListener(drawerToggle);

        pager.setAdapter(new SchedulePagerAdapter(getSupportFragmentManager()));
        pager.getAdapter().notifyDataSetChanged();
        tabs.setupWithViewPager(pager);

    }

    private void populateLists( int pos )
    {
        //TODO get events by clusters and day and populate EventsListByDay
        // clear lists and add new items

        List<Events> eventsByCluster;
        if( pos == 0)
        {
            eventsByCluster = allEvents;
        }
        else
        {
            eventsByCluster = getEventsByCluster(pos);
        }
        String temp2 = "";
        for( Events e: eventsByCluster)
        {
            String s = e.name;
            if (s.contains("_"))
            {
                // Split it.
                temp2 = "";
                String[] temp = s.split("_");
                for (int j = 0; j < temp.length; j++) {
                    String temp3 = temp[j].substring(0, 1).toUpperCase() + temp[j].substring(1);
                    temp2 = temp2 + temp3 + " ";
                }

            }
            else temp2 = s.substring(0, 1).toUpperCase() + s.substring(1);
            s = temp2;
            e.name = s;
            switch( getDay(e.date) )
            {
                case 0:
                    EventsListByDay.eventsDay0.add(e);
                    break;
                case 1:
                    EventsListByDay.eventsDay1.add(e);
                    break;
                case 2:
                    EventsListByDay.eventsDay2.add(e);
                    break;
                case 3:
                    EventsListByDay.eventsDay3.add(e);
                    break;
            }
        }

        if( EventsListByDay.eventsDay0.size() == 0)
        {
        }
    }

    private List<Events> getEventsByCluster( int pos )
    {
        List<Events> eve = new ArrayList<>();
        for( Events e: allEvents)
        {
            if( e.cluster.equals( clustersUnformatted.get(pos)))
            {
                eve.add(e);
            }
        }
        return eve;
    }

    private int getDay(String date)
    {
        String day = date.substring( ((date.length() - 2)>0?date.length() - 2: 0 ));
        Log.wtf("wtf1", day);
        if( day.equals("22"))
        {
            return 0;
        }
        else if(day.equals("23"))
        {
            return 1;
        }
        else if(day.equals("24"))
        {
            return 2;
        }
        else if(day.equals("25"))
        {
            return 3;
        }
        return -1;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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


}
