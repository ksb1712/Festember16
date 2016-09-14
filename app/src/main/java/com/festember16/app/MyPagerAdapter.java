package com.festember16.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by vishnu on 8/9/16.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    int eventId = 2;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyPagerAdapter(FragmentManager fm, int id){
        super(fm);

        eventId = id;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                ContactsFragment contactsFragment = ContactsFragment.getInstance(eventId);
                return contactsFragment;

            case 2:
                RulesFragment rulesFragment = RulesFragment.getInstance(eventId);
                return rulesFragment;

            case 3:
                MapsTabFragment mapsTabFragment = new MapsTabFragment();
                return mapsTabFragment;

            default:
                DetailsFragment detailsFragment = DetailsFragment.getInstance(eventId);
                return detailsFragment;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return "Contacts";

            case 2:
                return "Rules";

            case 3: return "Map";

            default:
                return "Details";

        }

    }
}
