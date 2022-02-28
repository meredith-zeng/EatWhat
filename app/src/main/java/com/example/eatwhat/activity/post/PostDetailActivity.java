package com.example.eatwhat.activity.post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Intent i = getIntent();
        PostCard content = (PostCard)i.getSerializableExtra("card");
        title = findViewById(R.id.post_detail_title);
        restaurant_name = findViewById(R.id.post_detail_restaurant);
        star = findViewById(R.id.ratingBar1);
        comment = findViewById(R.id.post_detail_body);
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
}