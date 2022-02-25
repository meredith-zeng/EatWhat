package com.example.eatwhat.mainActivityFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eatwhat.R;
import com.example.eatwhat.adapter.PostAdapter;
import com.example.eatwhat.cardview.PostCard;
import com.example.eatwhat.databinding.ActivityMainBinding;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<PostCard> postCardArrayList;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) rootView.findViewById(R.id.note_recyclerview);
        // 2. set layoutManger
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // this is data from recycler view
        initData();

        // 3. create an adapter
        PostAdapter postAdapter = new PostAdapter(getContext(), postCardArrayList);
        recyclerView.setAdapter(postAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }


    private void initData(){
        postCardArrayList = new ArrayList<>();
        RestaurantService methods = RetrofitClient.getRetrofit().create(RestaurantService.class);

        postCardArrayList.add(new PostCard("Santa clara", "great great great great", 100, R.drawable.post_photo, "Panda", 3.5));
        postCardArrayList.add(new PostCard("Java", "the best one", 10000, R.drawable.post_photo, "Panda", 3.5));
        postCardArrayList.add(new PostCard("Python", "great great great great", 102, R.drawable.post_photo, "Panda", 3.5));
        postCardArrayList.add(new PostCard("C++", "great great great great", 103, R.drawable.post_photo, "Panda", 3.5));
        postCardArrayList.add(new PostCard("C", "great great great great", 223, R.drawable.post_photo, "Panda", 3.5));
        postCardArrayList.add(new PostCard("JavaScript", "great great great great", 123, R.drawable.post_photo, "Panda", 3.5));
    }
}