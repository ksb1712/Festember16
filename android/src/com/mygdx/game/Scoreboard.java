package com.mygdx.game;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard   extends AppCompatActivity{

    private GridLayoutManager lLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
            setContentView(R.layout.activity_scoreboard);
            setTitle(null);

            List<Score_class> rowListItem = getAllItemList();
            lLayout = new GridLayoutManager(Scoreboard.this, 1);

            RecyclerView rView = (RecyclerView)findViewById(R.id.RecyclerView);
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            refreshItems();
                        }
                    }
            );
            rView.setHasFixedSize(true);
            rView.setLayoutManager(lLayout);

            CustomAdapter rcAdapter = new CustomAdapter(Scoreboard.this, rowListItem);
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

        private List<Score_class> getAllItemList(){

            List<Score_class> allItems = new ArrayList<Score_class>();

            allItems.add(new Score_class("NIT","200"));
            allItems.add(new Score_class("NITT","200"));
            allItems.add(new Score_class("INIT","200"));



            return allItems;
        }
    void refreshItems() {
        // Load items
        // ...

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
