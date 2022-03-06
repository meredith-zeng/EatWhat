package com.example.eatwhat.activity.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    public static final String TAG = "PostDetailActivity";

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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
        Glide.with(this)
                .load(gsReference)
                .into(postDetailImage);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts");
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        mDatabase.child(postId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    DataSnapshot dataSnapshot = task.getResult();
                    PostCard card = dataSnapshot.getValue(PostCard.class);
                    String restaurant_name_str = card.getRestuarant_name();
                    String title_str = card.getPost_title();
                    float ratings = card.getStar();
                    String content_str = card.getPost_content();

                    title.setText(title_str);
                    restaurant_name.setText(restaurant_name_str);
                    comment.setText(content_str);
                    star.setRating(ratings);
                }
            }
        });



    }
}