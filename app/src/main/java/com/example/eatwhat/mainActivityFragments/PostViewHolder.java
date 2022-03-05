package com.example.eatwhat.mainActivityFragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eatwhat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostViewHolder extends RecyclerView.ViewHolder{
    ImageView postImageV, like_btn;
    TextView postTitleV, postContentV, like_text;
    View root;
    DatabaseReference likereference;

    public PostViewHolder(@NonNull View root) {
        super(root);
        this.root = root;
        postImageV = root.findViewById(R.id.idPostImage);
        postTitleV = root.findViewById(R.id.idPostTitle);
        postContentV = root.findViewById(R.id.idPostContent);
        like_btn = root.findViewById(R.id.numberOfLike);
        like_text = root.findViewById(R.id.is_liked);
    }

    public void getlikebuttonstatus(final String postId, final String userid)
    {
        likereference= FirebaseDatabase.getInstance().getReference("likes");
        likereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postId).hasChild(userid))
                {
                    int likecount=(int)snapshot.child(postId).getChildrenCount();
                    like_text.setText(likecount+" likes");
                    like_btn.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                else
                {
                    int likecount=(int)snapshot.child(postId).getChildrenCount();
                    like_text.setText(likecount+" likes");
                    like_btn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
