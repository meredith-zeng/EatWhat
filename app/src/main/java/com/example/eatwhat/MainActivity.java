package com.example.eatwhat;

import android.content.Intent;
import android.os.Bundle;

import com.example.eatwhat.adapter.MainTabAdapter;
import com.example.eatwhat.mainActivityFragments.MyPostsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.example.eatwhat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;

    private DrawerLayout myDrawerLayout;
    private NavigationView myNavigationView;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFloatingButton();
        createDrawer();
        createTabsFragment();
    }

    private void createFloatingButton() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FloatingActionButton fab = binding.fab;
    }

    private void createDrawer() {
        setContentView(R.layout.drawer_layout);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myNavigationView = (NavigationView) findViewById(R.id.drawer_view);
        myNavigationView.setNavigationItemSelectedListener(this);
    }

    private void createTabsFragment() {
        //navigation button
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);

        tabLayout = (TabLayout) findViewById(R.id.mainTabBar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //add tab names
        tabLayout.addTab(tabLayout.newTab().setText("Notes"));
        tabLayout.addTab(tabLayout.newTab().setText("Restaurants"));
        tabLayout.addTab(tabLayout.newTab().setText("Today's"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MainTabAdapter adapter = new MainTabAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                myDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_search:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.drawer_home:
                return true;
            case R.id.drawer_profile:
                Intent  mainToAccountIntent = new Intent(this, MyAccountActivity.class);
                startActivity(mainToAccountIntent);
                finish();
                return true;
            case R.id.drawer_postes:
                myDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            case R.id.drawer_history:
                return true;
            case R.id.drawer_logout:
                return true;
        }
        return false;
    }
}