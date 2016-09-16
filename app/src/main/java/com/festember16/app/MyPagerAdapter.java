package com.festember16.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by vishnu on 8/9/16.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    int eventId = 2;

    RulesFragment rulesFragment;
    MapsTabFragment mapsTabFragment;
    DetailsFragment detailsFragment;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyPagerAdapter(FragmentManager fm, int id){
        super(fm);

        eventId = id;

        rulesFragment = RulesFragment.getInstance(eventId);
        mapsTabFragment = new MapsTabFragment();
        detailsFragment = DetailsFragment.getInstance(eventId);

    }

    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                return rulesFragment;

            case 2:
                return mapsTabFragment;

            default:
                return detailsFragment;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return "Rules";

            case 2: return "Map";

            default:
                return "Details";

        }

    }
}
