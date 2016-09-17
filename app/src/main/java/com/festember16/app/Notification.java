package com.festember16.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Notification extends AppCompatActivity {

    private Toolbar toolbar;
    private NotificationDBHandler notificationDBHandler = new NotificationDBHandler(this);
    private DrawerLayout DrawerLayoutRoot;

    private ActionBarDrawerToggle drawerListener;
    private ActionBarDrawerToggle drawerToggle;
    public ListView NavListView;
    SharedPreferences preferences;
    NotificationDetails notif;
    private List<String> title = new ArrayList<>();
    private List<String> text = new ArrayList<>();
    private List<String> time = new ArrayList<>();
    private static final String TAG = "FCM";

    int flag=0;
    private String choice ="All";


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_notification);



            DrawerLayoutRoot = (DrawerLayout) findViewById(R.id.DrawerLayoutRoot);
            toolbar= (Toolbar) findViewById(R.id.app_bar);
            final DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayoutRoot);
            setSupportActionBar(toolbar);
            { notif = new NotificationDetails();
                notif.title="Test";
                notif.text="Success";
                Calendar c = Calendar.getInstance();
                String currentDateandTime = new SimpleDateFormat("HH:mm").format(new Date());

                String t=currentDateandTime+" , Day "+String.valueOf(c.get(Calendar.DATE)-22);
                notif.time = t;
            }
            drawerToggle = new ActionBarDrawerToggle( this, DrawerLayoutRoot, toolbar , R.string.app_name, R.string.app_name)
            {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    toolbar.setTitle("Festember 16");
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    toolbar.setTitle("Clusters");
                    invalidateOptionsMenu();
                }
            };
            NavListView = (ListView) findViewById(R.id.ClusterList);
            final List<String> list = new ArrayList<String>();
            list.add("All");
            list.add("General");
            list.add("Arts");
            list.add("Dance");
            list.add("Dramatics");
            list.add("English Lits");
            list.add("Fashion");
            list.add("Ghibran Talent Hunt");
            list.add("Hindi Lits");
            list.add("Music");
            list.add("Photography");
            list.add("Shruthilaya");
            list.add("Tamil Lits");
            list.add("Telugu Lits");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, list);
            NavListView.setAdapter(adapter);
            final TextView nullbox = (TextView) findViewById(R.id.null_box);
            nullbox.setText("No Notifications");



            final ListView mainListView = (ListView) findViewById(R.id.mainList);
            final List<NotificationDetails> notificationDetailsArrayList = new ArrayList<>();
            notificationDetailsArrayList.addAll(notificationDBHandler.getAllNotifications());
            Collections.reverse(notificationDetailsArrayList);
            for (int i=0;i<notificationDetailsArrayList.size();i++){
                title.add(notificationDetailsArrayList.get(i).title);
                text.add(notificationDetailsArrayList.get(i).text);
                time.add(notificationDetailsArrayList.get(i).time);
            }
            if (notificationDetailsArrayList.isEmpty()){
                nullbox.setVisibility(View.VISIBLE);
            }
            else{
                nullbox.setVisibility(View.GONE);
            }



            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            drawerToggle.setDrawerIndicatorEnabled(true);
            DrawerLayoutRoot.setDrawerListener(drawerToggle);
            final CustomList customadapter = new CustomList(this,title,text,time);
            //nullbox.setVisibility(View.GONE);
            mainListView.setAdapter(customadapter);
            NavListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getApplication(),listView.getItemAtPosition(i).toString(),Toast.LENGTH_SHORT).show();
                    choice = NavListView.getItemAtPosition(i).toString();
                    //choice is the event cluster name

                    notificationDetailsArrayList.clear();
                    customadapter.clear();
                    mainListView.setAdapter(customadapter);


                    if(choice.equals("All")){

                        notificationDetailsArrayList.clear();

                        notificationDetailsArrayList.addAll(notificationDBHandler.getAllNotifications());
                        customadapter.clear();
                        Collections.reverse(notificationDetailsArrayList);
                        if (notificationDetailsArrayList.size()==0){
                            nullbox.setVisibility(View.VISIBLE);
                        }
                        else{
                            nullbox.setVisibility(View.GONE);
                        }
                        for (int j=0;j<notificationDetailsArrayList.size();j++){
                            title.add(notificationDetailsArrayList.get(j).title);
                            text.add(notificationDetailsArrayList.get(j).text);
                            time.add(notificationDetailsArrayList.get(j).time);
                        }
                        customadapter.notifyDataSetChanged();
                    }else{
                      notificationDetailsArrayList.clear();
                        customadapter.clear();
                        notificationDetailsArrayList.addAll(notificationDBHandler.getByCluster(choice.toUpperCase()));
                        Collections.reverse(notificationDetailsArrayList);
                        for (int j=0;j<notificationDetailsArrayList.size();j++){
                            title.add(notificationDetailsArrayList.get(j).title);
                            text.add(notificationDetailsArrayList.get(j).text);
                            time.add(notificationDetailsArrayList.get(j).time);
                        }


                        if (notificationDetailsArrayList.isEmpty()){
                            nullbox.setVisibility(View.VISIBLE);
                            nullbox.setText("No Notifications");
                        }
                        else {
                            nullbox.setVisibility(View.GONE);


                            customadapter.notifyDataSetChanged();
                        }

                    }

                    mDrawerLayout.closeDrawers();

                    customadapter.notifyDataSetChanged();



                    //CustomList adapter = new CustomList(this,notif_adapter_list);

                }
            });




        }

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            drawerToggle.syncState();
        }



}
