package com.example.eatwhat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatwhat.PostDetailActivity;
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Viewholder> {

    private Context context;
    private ArrayList<PostCard> postCardArrayList;
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";


    // Constructor
    public PostAdapter(Context context, ArrayList<PostCard> postCardArrayList) {
        this.context = context;
        this.postCardArrayList = postCardArrayList;
    }

    @NonNull
    @Override
    public PostAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = View.inflate(context, R.layout.using_post_card,null);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.using_post_card, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        PostCard model = postCardArrayList.get(position);
        holder.postTitleV.setText(model.getPost_title());
        holder.postContentV.setText(model.getPost_content());
        holder.numberOfLikeV.setText("" + model.getNumber_of_likes());
        holder.postTitleV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilePageIntent = new Intent(context, PostDetailActivity.class);
                profilePageIntent.putExtra("card", model);
                context.startActivity(profilePageIntent);
            }
        });

        holder.numberOfLikeV.setText(String.valueOf(model.getNumber_of_likes()));

//        GlideUrl glideUrl = new GlideUrl(model.getPost_image_url(), new LazyHeaders.Builder()
//                .addHeader("Authorization", " Bearer " + TOKEN)
//                .build());
//
//        Glide.with(this.context).load(glideUrl).into(holder.postImageV);

    }

    @Override
    public int getItemCount() {
        return postCardArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView postImageV;
        private TextView postTitleV, postContentV,numberOfLikeV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            postImageV = itemView.findViewById(R.id.idPostImage);
            postTitleV = itemView.findViewById(R.id.idPostTitle);
            postContentV = itemView.findViewById(R.id.idPostContent);
            numberOfLikeV = itemView.findViewById(R.id.numberOfLike);
        }


    }
}
