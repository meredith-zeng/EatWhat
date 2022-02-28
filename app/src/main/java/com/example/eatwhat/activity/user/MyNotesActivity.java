package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.eatwhat.activity.MainActivity;
import com.example.eatwhat.R;
import com.example.eatwhat.adapter.MyNotesAdapter;
import com.example.eatwhat.cardview.MyNotesCard;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MyNotesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerView;
    private ArrayList<MyNotesCard> myNotesCardArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
        setToolBar();
        setRecyclerView();
    }


    private void setToolBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_notes_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("My Notes");
        }
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.my_notes_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyNotesActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        initData();
        MyNotesAdapter myAdapter = new MyNotesAdapter(MyNotesActivity.this, myNotesCardArrayList);
        recyclerView.setAdapter(myAdapter);
    }

    private void initData(){
        myNotesCardArrayList = new ArrayList<>();
        myNotesCardArrayList.add(new MyNotesCard("Santa clara", "great great great great", 100, R.drawable.post_photo, 3.5));
        myNotesCardArrayList.add(new MyNotesCard("Java", "the best one", 10000, R.drawable.post_photo,3.5));
        myNotesCardArrayList.add(new MyNotesCard("Python", "great great great great", 102, R.drawable.post_photo, 3.5));
        myNotesCardArrayList.add(new MyNotesCard("C++", "great great great great", 103, R.drawable.post_photo,3.5));
        myNotesCardArrayList.add(new MyNotesCard("C", "great great great great", 223, R.drawable.post_photo, 3.5));
        myNotesCardArrayList.add(new MyNotesCard("JavaScript", "great great great great", 123, R.drawable.post_photo, 3.5));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
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