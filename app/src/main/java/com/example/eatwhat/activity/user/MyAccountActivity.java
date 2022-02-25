package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eatwhat.activity.MainActivity;
import com.example.eatwhat.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setToolBar();
        setItemList();
    }


    private void setToolBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("My Account");
        }
    }

    private void setItemList() {
        String[] items = new String[]{"    My Profile", "    My Posts", "    Review History"};
        final ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            list.add(items[i]);
        }

        ListView listview = findViewById(R.id.myList);
        listview.setAdapter(new ArrayAdapter<String>(this, R.layout.my_account_item, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (view.findViewById(R.id.listItem));
                textView.setMinHeight(0);
                textView.setMinimumHeight(0);
                textView.setHeight(150);
                return view;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}