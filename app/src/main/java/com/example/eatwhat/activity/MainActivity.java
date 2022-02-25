package com.example.eatwhat.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.user.MyNotesActivity;
import com.example.eatwhat.activity.post.PostCreationActivity;
import com.example.eatwhat.activity.user.ReviewHistoryActivity;
import com.example.eatwhat.activity.SearchActivity;
import com.example.eatwhat.activity.user.SetPreferenceActivity;
import com.example.eatwhat.adapter.MainTabAdapter;
import com.example.eatwhat.mainActivityFragments.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;

    private DrawerLayout myDrawerLayout;
    private NavigationView myNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDrawer();
        createFloatingButton();
        createTabsFragment();

    }

    private void createFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToPost();
            }
        });
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

    public void jumpToPost() {
        Intent intent = new Intent(this, PostCreationActivity.class);
        startActivity(intent);
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
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
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
                Intent toProfile = new Intent(this, ProfileActivity.class);
                startActivity(toProfile);
                finish();
                return true;
            case R.id.drawer_postes:
                Intent toMyNotes = new Intent(this, MyNotesActivity.class);
                startActivity(toMyNotes);
                return true;
            case R.id.drawer_history:
                Intent toReviewHistory = new Intent(this, ReviewHistoryActivity.class);
                startActivity(toReviewHistory);
                finish();
                return true;
            case R.id.drawer_preference:
                Intent toSetPreference = new Intent(this, SetPreferenceActivity.class);
                toSetPreference.putExtra("source", "home");
                startActivity(toSetPreference);
                finish();
                return true;
            case R.id.drawer_logout:
                return true;
        }
        return false;
    }




}