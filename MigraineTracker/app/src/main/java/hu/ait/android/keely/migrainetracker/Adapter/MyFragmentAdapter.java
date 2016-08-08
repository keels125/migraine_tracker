package hu.ait.android.keely.migrainetracker.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import hu.ait.android.keely.migrainetracker.Fragment.FragmentFood;
import hu.ait.android.keely.migrainetracker.Fragment.FragmentWeather;
import hu.ait.android.keely.migrainetracker.Fragment.FragmentMigraine;

/**
 * Created by Keely on 5/4/15.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {
    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    //Set up 3 page view

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new FragmentMigraine();
            case 1:
                return new FragmentWeather();
            case 2:
                return new FragmentFood();
            default:
                return new FragmentMigraine();
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Migraines";
            case 1:
                return "Weather";
            case 2:
                return "Food";
            default:
                return "Migraines";
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
