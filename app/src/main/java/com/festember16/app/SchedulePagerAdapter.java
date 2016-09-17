package com.festember16.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by santh on 17-09-2016.
 */
public class SchedulePagerAdapter extends FragmentPagerAdapter {

    public SchedulePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                FragmentDay0 fragmentDay0 = new FragmentDay0();
                return fragmentDay0;

            case 1:
                FragmentDay1 fragmentDay1 = new FragmentDay1();
                return fragmentDay1;

            case 2:
                FragmentDay2 fragmentDay2 = new FragmentDay2();
                return fragmentDay2;

            case 3:
                FragmentDay3 fragmentDay3 = new FragmentDay3();
                return fragmentDay3;

        }

        return null;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "DAY 0";

            case 1:
                return "DAY 1";

            case 2:
                return "DAY 2";

            case 3:
                return "DAY 3";

        }

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}
