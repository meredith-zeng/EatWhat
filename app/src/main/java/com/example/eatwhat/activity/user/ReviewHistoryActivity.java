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
import com.example.eatwhat.adapter.ReviewHistoryAdapter;
import com.example.eatwhat.cardview.ReviewHistoryCard;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ReviewHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Intent homeIntent;
    private ArrayList<ReviewHistoryCard> ReviewHistoryCardArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_history);
        setToolBar();
        setRecyclerView();
    }


    private void setToolBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.review_history_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Review History");
        }
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.review_history_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReviewHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        initData();
        ReviewHistoryAdapter myAdapter = new ReviewHistoryAdapter(ReviewHistoryActivity.this, ReviewHistoryCardArrayList);
        recyclerView.setAdapter(myAdapter);
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


    private void initData(){
        ReviewHistoryCardArrayList = new ArrayList<>();
        ReviewHistoryCardArrayList.add(new ReviewHistoryCard("title", "content"));
        ReviewHistoryCardArrayList.add(new ReviewHistoryCard("title", "content"));
        ReviewHistoryCardArrayList.add(new ReviewHistoryCard("title", "content"));
        ReviewHistoryCardArrayList.add(new ReviewHistoryCard("title", "content"));
        ReviewHistoryCardArrayList.add(new ReviewHistoryCard("title", "content"));
        ReviewHistoryCardArrayList.add(new ReviewHistoryCard("title", "content"));
        ReviewHistoryCardArrayList.add(new ReviewHistoryCard("title", "content"));
    }
}