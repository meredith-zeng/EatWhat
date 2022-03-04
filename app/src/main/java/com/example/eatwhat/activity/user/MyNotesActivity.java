package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.post.PostDetailActivity;
import com.example.eatwhat.adapter.PostAdapter;
import com.example.eatwhat.cardview.PostCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyNotesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ArrayList<PostCard> postCardArrayList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String TAG = "MyNotesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts");
        postCardArrayList = new ArrayList<>();

        setContentView(R.layout.activity_my_notes);
        setToolBar();
        initData();
        initRecyclerView();
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

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_notes_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyNotesActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        PostAdapter postAdapter = new PostAdapter(this, postCardArrayList);
        postAdapter.setRecyclerViewOnItemClickListener(new PostAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                intent.putExtra("postId", postCardArrayList.get(position).getPostId());
                intent.putExtra("imageUrl", postCardArrayList.get(position).getPost_image_url());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(postAdapter);
    }

    private void initData(){
        String uid = mAuth.getCurrentUser().getUid();
        Query q = mDatabase.orderByChild("uid").equalTo(uid);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, snapshot.getChildren().getClass() + " ");
                for(DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    PostCard postCard = singleSnapshot.getValue(PostCard.class);
                    postCardArrayList.add(postCard);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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