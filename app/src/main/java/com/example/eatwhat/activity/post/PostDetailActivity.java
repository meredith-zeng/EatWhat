package com.example.eatwhat.activity.post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class PostDetailActivity extends AppCompatActivity {

    private ImageButton mCancelBtn;
    private TextView title;
    private TextView restaurant_name;
    private RatingBar star;
    private TextView mUserLocation;
    private TextView comment;
    private ConstraintLayout piclayout;
    private ImageView postDetailImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        title = findViewById(R.id.post_detail_title);
        restaurant_name = findViewById(R.id.post_detail_restaurant);
        star = findViewById(R.id.ratingBar1);
        comment = findViewById(R.id.post_detail_body);
        postDetailImage = (ImageView) findViewById(R.id.post_detail_thumbnail);

        initData(getIntent());
        /**
        title.setText(content.getPost_title());
        star.setRating((float) content.getstar());
        restaurant_name.setText(content.getRestuarant_name());
        comment.setText(content.getPost_content());
         **/

        cancel_button_init();
    }

    private void cancel_button_init() {
        mCancelBtn = findViewById(R.id.post_detail_cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(Intent intent){
        String postId = String.valueOf(intent.getExtras().get("postId"));
        String imageUrl = String.valueOf(intent.getExtras().get("imageUrl"));

        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .build());
        Glide.with(this)
                .load(glideUrl)
                .into(postDetailImage);

        restaurant_name.setText("PostId" + postId);
        comment.setText("Comment" + postId);
    }
}