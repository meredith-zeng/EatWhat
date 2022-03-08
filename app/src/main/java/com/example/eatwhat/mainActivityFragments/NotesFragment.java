package com.example.eatwhat.mainActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.eatwhat.R;
import com.example.eatwhat.activity.post.PostDetailActivity;
import com.example.eatwhat.adapter.PostAdapter;
import com.example.eatwhat.cardview.PostCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedList;

public class NotesFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinkedList<PostCard> postCardArrayList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public static final String TAG = "NotesFragments";

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        postCardArrayList = new LinkedList<>();
        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.note_recyclerview);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    DataSnapshot dataSnapshot = task.getResult();
                    initRecyclerView(dataSnapshot);
                }
            }
        });
        pullDownRefresh(rootView);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initRecyclerView(DataSnapshot dataSnapshot){

        // 2. set layoutManger
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Log.d("NotesFragment", "initRecyclerView");

        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            PostCard postCard = singleSnapshot.getValue(PostCard.class);

            String subContent = postCard.getPost_content();
            if (subContent.length() >= 70){
                subContent = subContent.substring(0, 70)  + "...";
            }
            String subtitle = postCard.getPost_title();
            if (subtitle.length() >= 40){
                subtitle = subtitle.substring(0, 40) + "...";
            }
            postCard.setPost_title(subtitle);
            postCard.setPost_content(subContent);
            postCardArrayList.addFirst(postCard);
            Log.d("NotesFragment", "added post");
        }
        // 3. create an adapter
        PostAdapter postAdapter = new PostAdapter(getActivity(), postCardArrayList);
        postAdapter.setRecyclerViewOnItemClickListener(new PostAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Log.e(TAG, "click one post");
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra("postId", postCardArrayList.get(position).getPostId());
                intent.putExtra("imageUrl", postCardArrayList.get(position).getPost_image_url());
                getContext().startActivity(intent);
            }
        });
        recyclerView.setAdapter(postAdapter);
    }

    private void pullDownRefresh(View view) {
        SwipeRefreshLayout swipe = view.findViewById(R.id.swiperefresh_notes);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        postCardArrayList.clear();
                        PostAdapter postAdapter = new PostAdapter(getActivity(), postCardArrayList);
                        recyclerView.setAdapter(postAdapter);
                        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else {
                                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                    DataSnapshot dataSnapshot = task.getResult();
                                    initRecyclerView(dataSnapshot);
                                }
                            }
                        });
                        Log.d("NotesFragment", "finished refresh");
                        swipe.setRefreshing(false);
                    }
                });
            }
        });
    }
}