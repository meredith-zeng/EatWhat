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
                RestaurantFragment restaurantFragment = new RestaurantFragment();
                return restaurantFragment;
            case 1:
                NotesFragment notesFragment = new NotesFragment();
                return notesFragment;
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
