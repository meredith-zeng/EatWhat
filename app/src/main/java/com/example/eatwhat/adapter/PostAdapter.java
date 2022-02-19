package com.example.eatwhat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.myViewHolder> {

    private Context context;
    private ArrayList<PostCard> postCardArrayList;

    // Constructor
    public PostAdapter(Context context, ArrayList<PostCard> postCardArrayList) {
        this.context = context;
        this.postCardArrayList = postCardArrayList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.using_post_card,null);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        // to set data to textview and imageview of each card layout
        PostCard model = postCardArrayList.get(position);
        holder.postImage.setImageResource(model.getCourse_image());
        holder.postTitle.setText(model.getPost_title());
        holder.postContent.setText(model.getPost_content());
        holder.numberOfLike.setText(model.getNumber_of_likes());
    }

    @Override
    public int getItemCount() {
        return postCardArrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private ImageView postImage;
        private TextView postTitle, postContent,numberOfLike;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.idPostImage);
            postTitle = itemView.findViewById(R.id.idPostTitle);
            postContent = itemView.findViewById(R.id.idPostContent);
            numberOfLike = itemView.findViewById(R.id.numberOfLike);
        }


    }
}
