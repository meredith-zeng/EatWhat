package com.example.eatwhat.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.eatwhat.mainActivityFragments.NotesFragment;
import com.example.eatwhat.mainActivityFragments.RestaurantFragment;
import com.example.eatwhat.mainActivityFragments.TodaysFragment;

public class MainTabAdapter extends FragmentPagerAdapter {
    private Context myContext;
    private FragmentManager myManager;
    int totalTabs;

    public MainTabAdapter(Context context, FragmentManager fm,  int totalTabs) {
        super(fm);
        myManager = fm;
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NotesFragment notesFragment = new NotesFragment();
                //myManager.beginTransaction().add(notesFragment, "notesFragment").commit();
                return notesFragment;
            case 1:
                RestaurantFragment restaurantFragment = new RestaurantFragment();
                //myManager.beginTransaction().add(restaurantFragment, "restaurantFragment").commit();
                return restaurantFragment;
            case 2:
                TodaysFragment todaysFragment = new TodaysFragment();
                //myManager.beginTransaction().add(todaysFragment, "todaysFragment").commit();
                return todaysFragment;
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return totalTabs;
    }
}
