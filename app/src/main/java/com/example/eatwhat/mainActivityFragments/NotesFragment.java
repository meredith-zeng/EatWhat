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


import com.example.eatwhat.R;
import com.example.eatwhat.activity.post.PostDetailActivity;
import com.example.eatwhat.activity.restaurant.RestaurantPageActivity;
import com.example.eatwhat.adapter.PostAdapter;
import com.example.eatwhat.adapter.RestaurantAdapter;
import com.example.eatwhat.cardview.PostCard;
import com.example.eatwhat.databinding.ActivityMainBinding;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<PostCard> postCardArrayList;
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
        postCardArrayList = new ArrayList<>();
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

        // Inflate the layout for this fragment
        return rootView;
    }


    private void initRecyclerView(DataSnapshot dataSnapshot){

        // 2. set layoutManger
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            PostCard postCard = singleSnapshot.getValue(PostCard.class);
            postCardArrayList.add(postCard);
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


}