package com.festember16.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyProfile extends AppCompatActivity {

    private String name;
    private String rollNo = "123456789";

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

        getSupportActionBar().setTitle("Welcome " + rollNo + "!");

        ButterKnife.inject(this);

        name = "John Doe";
        hello.setText("Hey " + name + "!");

        EventResponse eventResponse = new Gson().fromJson(
                ListActivity.API_RESPONSE,
                EventResponse.class
        );

        List<Events> eventses = eventResponse.getData();

        listView.setAdapter(new EventsAdapter(
                MyProfile.this,
                0,
                eventses
        ));


        registerForContextMenu(listView);
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
                Toast.makeText(MyProfile.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
