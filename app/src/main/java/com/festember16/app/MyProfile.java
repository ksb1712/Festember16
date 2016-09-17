package com.festember16.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyProfile extends AppCompatActivity {

    public static final String ID = "ID";
    private String name;
    private String rollNo = "123456789";

    DBHandler db;
    int eventMenuPosBuf;
    Events eventMenuBuf;
    List<Events> eventses;

    @InjectView(R.id.hello)
    TextView hello;

    @InjectView(R.id.listView2)

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Welcome!");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        ButterKnife.inject(this);

        db = new DBHandler(this);

        //Todo: Parse email from login activity to get the name of user
        SharedPreferences preferences = getSharedPreferences(LoginActivity.USER_AUTH, MODE_PRIVATE);

        name = preferences.getString(LoginActivity.USER_EMAIL, "there");
        if(name!=null)
        {
            name.trim();
        }
        if(name.equals(""))
            name = "there";

        hello.setText("Hey " + name + "!");

        eventses = new ArrayList<>();
//
//        SharedPreferences preferences = getSharedPreferences(
//                DetailsFragment.REGISTERED_EVENTS,
//                MODE_PRIVATE
//        );

//        Map<String, Boolean> map = (Map<String, Boolean>) preferences.getAll();
//
//        Iterator iterator = map.entrySet().iterator();
//
//        while(iterator.hasNext()){
//            Map.Entry<String, Boolean> entry = (Map.Entry<String, Boolean>) iterator.next();
//
//            int id = Integer.parseInt(entry.getKey());
//            eventses.add(db.getEvent(id));
//        }

        //Todo: API call for getting user registered events

        if(eventses.size()>0) {

            listView.setAdapter(new EventsAdapter(
                    MyProfile.this,
                    0,
                    eventses
            ));

        }
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

                Intent intent = new Intent(MyProfile.this, LoginActivity.class);
                startActivity(intent);

//                SharedPreferences preferences = getSharedPreferences
//                        (DetailsFragment.REGISTERED_EVENTS, MODE_PRIVATE);
//
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.clear();
//                editor.apply();

        }

        return super.onOptionsItemSelected(item);
    }
}
