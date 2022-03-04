package com.example.eatwhat.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.user.MyNotesActivity;
import com.example.eatwhat.activity.post.PostCreationActivity;
import com.example.eatwhat.activity.user.ReviewHistoryActivity;
import com.example.eatwhat.activity.user.SetPreferenceActivity;
import com.example.eatwhat.activity.user.SignInActivity;
import com.example.eatwhat.adapter.MainTabAdapter;
import com.example.eatwhat.activity.user.ProfileActivity;
import com.example.eatwhat.mainActivityFragments.RestaurantFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private CircleImageView circleImageView;

    private DrawerLayout myDrawerLayout;
    private NavigationView myNavigationView;
    private static int MAP_LOCATION_CODE = 1;
    private double lng = 0;
    private double lat = 0;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "MainActivity";


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
        circleImageView = (CircleImageView) findViewById(R.id.drawer_avatar);


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MAP_LOCATION_CODE && data != null) {
            if(resultCode == Activity.RESULT_OK){
                super.onActivityResult(requestCode, resultCode, data);
                lng = data.getDoubleExtra("Longitude", 0);
                lat = data.getDoubleExtra("Latitude", 0);
                System.out.println("regertgertgerger");
                Bundle args = new Bundle();
                args.putDouble("Longitude", lng);
                args.putDouble("Latitude", lat);
                try {
                    RestaurantFragment fragment = (RestaurantFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(R.id.viewPager, 1));
                    fragment.putArguments(args);
                } catch(ClassCastException e) {
                    System.out.println(e);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }


    private String getFragmentTag(int viewPagerId, int fragmentPosition) {
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
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
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.action_map:
                Intent mapIntent = new Intent(this, MyMapActivity.class);
                mapIntent.putExtra("Longitude", lng);
                mapIntent.putExtra("Latitude", lat);
                startActivityForResult(mapIntent, MAP_LOCATION_CODE);
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
                return true;
            case R.id.drawer_postes:
                Intent toMyNotes = new Intent(this, MyNotesActivity.class);
                startActivity(toMyNotes);
                return true;
            case R.id.drawer_history:
                Intent toReviewHistory = new Intent(this, ReviewHistoryActivity.class);
                startActivity(toReviewHistory);
                return true;
            case R.id.drawer_preference:
                Intent toSetPreference = new Intent(this, SetPreferenceActivity.class);
                toSetPreference.putExtra("source", "home");
                startActivity(toSetPreference);
                return true;
            case R.id.drawer_logout:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(this, SignInActivity.class);
                startActivity(logoutIntent);
                return true;
        }
        return false;
    }
}