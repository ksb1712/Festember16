package com.mygdx.game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by vishnu on 8/9/16.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    int eventId = 2;
    ContactsFragment contactsFragment;
    RulesFragment rulesFragment;
    MapsTabFragment mapsTabFragment;
    DetailsFragment detailsFragment;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyPagerAdapter(FragmentManager fm, int id){
        super(fm);

        eventId = id;
        contactsFragment = ContactsFragment.getInstance(eventId);
        rulesFragment = RulesFragment.getInstance(eventId);
        mapsTabFragment = new MapsTabFragment();
        detailsFragment = DetailsFragment.getInstance(eventId);

    }

    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                return contactsFragment;

            case 2:
                return rulesFragment;

            case 3:
                return mapsTabFragment;

            default:
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
