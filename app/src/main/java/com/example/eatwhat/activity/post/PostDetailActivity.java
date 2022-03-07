package com.example.eatwhat.activity.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eatwhat.R;
import com.example.eatwhat.cardview.PostCard;
import com.example.eatwhat.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {

//    private ImageButton mCancelBtn;
    private TextView title;
    private TextView restaurant_name;
    private RatingBar star;
    private TextView mUserLocation;
    private TextView comment;
    private ConstraintLayout piclayout;
    private ImageView postDetailImage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageView isLikedIV;
    private boolean isLiked;
    private String uid;


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
        isLikedIV = (ImageView) findViewById(R.id.is_liked);
//        isLikedIV.setImageResource (R.drawable.ic_baseline_favorite_border_24);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts");

        initData(getIntent());

        uid = FirebaseAuth.getInstance().getUid();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("user").document(uid);

        checkIsLiked(docRef);
        setLikedListener(docRef);




//        cancel_button_init();
    }

//    private void cancel_button_init() {
//        mCancelBtn = findViewById(R.id.post_detail_cancel_btn);
//        mCancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }

    private void initData(Intent intent){
        String postId = String.valueOf(intent.getExtras().get("postId"));
        String imageUrl = String.valueOf(intent.getExtras().get("imageUrl"));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
        Glide.with(this)
                .load(gsReference)
                .into(postDetailImage);

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

    public void checkIsLiked(DocumentReference docRef) {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        User user = document.toObject(User.class);
                        List<String> likedList = user.getLiked_post();

                        Intent intent = getIntent();
                        String postId = intent.getStringExtra("postId");

                        if(likedList.contains(postId)) {
                            isLiked = true;
                            isLikedIV.setImageResource (R.drawable.ic_baseline_favorite_24);
                        }else {
                            isLikedIV.setImageResource (R.drawable.ic_baseline_favorite_border_24);
                        }

                    }
                }
            }
        });
    }

    public void setLikedListener(DocumentReference docRef){
        Intent intent = getIntent();
        String postId = intent.getStringExtra("postId");

        // ToDo: Update liked list in Realtime database




        isLikedIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                User user = document.toObject(User.class);
                                List<String> likedList = user.getLiked_post();
                                Log.d(TAG, "current liked_post" + likedList.size());

                                Intent intent = getIntent();
                                String id = intent.getStringExtra("postId");
                                if(!likedList.contains(id)){
                                    likedList.add(id);
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("liked_post", likedList);
                                    isLikedIV.setImageResource (R.drawable.ic_baseline_favorite_24);
                                }else {
                                    likedList.remove(id);
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("liked_post", likedList);
                                    isLikedIV.setImageResource (R.drawable.ic_baseline_favorite_border_24);
                                }

                                docRef
                                        .update("liked_post", likedList)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully updated!");

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
                                            }
                                        });
                            }
                        }
                    }
                });


                updatePostLike(postId);
            }
        });


    }

    public void updatePostLike(String postId){
        String curUserId = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "uid: " + curUserId);
        mDatabase.child(postId).get().addOnCompleteListener(
                new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot = task.getResult();
                        PostCard post = dataSnapshot.getValue(PostCard.class);
                        List<String> likeUidList = post.getLikedUidList();
                        if(!likeUidList.contains(curUserId)){
                            likeUidList.add(curUserId);
                        }
                        else{
                            likeUidList.remove(curUserId);
                        }

                        mDatabase.child(postId).child("likedUidList").setValue(likeUidList).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "likeUidList is updated!");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Something wrongs about likeUidList updates!");
            }
        });

    }



}