package com.example.bharath17.festember16;

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

            default:
                DetailsFragment detailsFragment = DetailsFragment.getInstance(eventId);
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
                return "Contacts";

            case 2:
                return "Rules";

            default:
                return "Details";

        }

    }
}
