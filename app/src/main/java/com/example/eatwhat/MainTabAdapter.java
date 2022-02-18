package com.example.eatwhat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MainTabAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public MainTabAdapter(Context context, FragmentManager fm,  int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NotesFragment notesFragment = new NotesFragment();
                return notesFragment;
            case 1:
                RestaurantFragment restaurantFragment = new RestaurantFragment();
                return restaurantFragment;
            case 2:
                TodaysFragment todaysFragment = new TodaysFragment();
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
