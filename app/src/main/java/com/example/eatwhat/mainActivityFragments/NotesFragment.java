package com.example.eatwhat.mainActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatwhat.R;
import com.example.eatwhat.adapter.PostAdapter;
import com.example.eatwhat.cardview.PostCard;

import java.util.ArrayList;

public class NotesFragment extends Fragment {
    private View view;
    private RecyclerView postRV;
    private ArrayList<PostCard> postCardArrayList;
    private PostAdapter postAdapter;


    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.using_post_card, container, false);
        initRecyclerView();
        initData();
        // Inflate the layout for this fragment
        return view;
    }

    private void initData() {
        postCardArrayList = new ArrayList<>();
        postCardArrayList.add(new PostCard("Santa clara", "great great great great", 100, R.drawable.post_photo));
        postCardArrayList.add(new PostCard("Java", "the best one", 10000, R.drawable.post_photo));
        postCardArrayList.add(new PostCard("Python", "great great great great", 102, R.drawable.post_photo));
        postCardArrayList.add(new PostCard("C++", "great great great great", 103, R.drawable.post_photo));
        postCardArrayList.add(new PostCard("C", "great great great great", 223, R.drawable.post_photo));
        postCardArrayList.add(new PostCard("JavaScript", "great great great great", 123, R.drawable.post_photo));
    }


    private void initRecyclerView() {
        postRV = (RecyclerView)view.findViewById(R.id.note_recyclerview);
        postAdapter = new PostAdapter(getActivity(), postCardArrayList);
        postRV.setAdapter(postAdapter);
        postRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        postRV.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

}