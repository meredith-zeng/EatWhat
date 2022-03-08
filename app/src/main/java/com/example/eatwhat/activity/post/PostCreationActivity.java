package com.example.eatwhat.activity.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eatwhat.R;

import com.example.eatwhat.activity.MainActivity;
import com.example.eatwhat.activity.user.MyNotesActivity;
import com.example.eatwhat.cardview.PostCard;
import com.example.eatwhat.mainActivityFragments.NotesFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class PostCreationActivity extends AppCompatActivity {

    private ImageButton mCancelBtn;
    Button post;
    ImageView add_btn, imageView;
    RatingBar ratingBar;
    EditText res_title, res_name, res_comment;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Bitmap bitmap;
    String image_url;
    private final static int PICK_IMAGE_MULTIPLE = 1;
    private final static String TAG = "PostCreationActivity";
    List<Bitmap> listofImages = new ArrayList<>();
    private final static int LIMIT = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_creation);

        final String[] exist_postId = {null};
        String imageUrl = null;
        final String[] exist_pid = {null};
        if(getIntent().hasExtra("postId")) {
            exist_postId[0] = String.valueOf(getIntent().getExtras().get("postId"));
            imageUrl = String.valueOf(getIntent().getExtras().get("imageUrl"));
        }
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts");
        FirebaseUser user = mAuth.getCurrentUser();

        post = findViewById(R.id.post_creation_post_textView);
        ratingBar = findViewById(R.id.ratingBar1);
        res_title = findViewById(R.id.post_creation_title);
        res_name = findViewById(R.id.post_creation_restaurant);
        res_comment = findViewById(R.id.post_creation_body);
        add_btn = findViewById(R.id.post_creation_add_btn);
        imageView = (ImageView) findViewById(R.id.post_creation_thumbnail);
        mCancelBtn = findViewById(R.id.post_creation_cancel_btn);

        // if it is access from edit
        if(exist_postId[0] != null){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
            Glide.with(this)
                    .load(gsReference)
                    .into(imageView);
            Query q = mDatabase.orderByChild("postId").equalTo(exist_postId[0]);
            q.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        for(DataSnapshot singleSnapshot : task.getResult().getChildren()) {
                            PostCard postCard = singleSnapshot.getValue(PostCard.class);
                            exist_pid[0] = singleSnapshot.getKey();
                            res_title.setText(postCard.getPost_title());
                            res_name.setText(postCard.getRestuarant_name());
                            res_comment.setText(postCard.getPost_content());
                            ratingBar.setRating(postCard.getStar());
                        }
                    }
                }
            });
        }

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listofImages.size() <= LIMIT){
                    chooseImage(PostCreationActivity.this);
                }
                else{
                    Toast.makeText(PostCreationActivity.this, "Images Uploading Limitation is 6 !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(res_title.getText())){
                    Toast.makeText(PostCreationActivity.this, "Restaurant Title cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(res_name.getText())){
                    Toast.makeText(PostCreationActivity.this, "Restaurant Name cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(res_comment.getText())){
                    Toast.makeText(PostCreationActivity.this, "Restaurant Comment cannot be empty!", Toast.LENGTH_SHORT).show();
                }
                else if(ratingBar.getRating() == 0.0f){
                    Toast.makeText(PostCreationActivity.this, "Please rate the restaurant!", Toast.LENGTH_SHORT).show();
                }
                else if(imageView.getDrawable() == null){
                    Toast.makeText(PostCreationActivity.this, "Please upload an image!", Toast.LENGTH_SHORT).show();
                }
                else{
                    float ratings = ratingBar.getRating();
//                    int number_of_likes = ratingBar.getNumStars();
                    String title_str = res_title.getText().toString();
                    String name_str = res_name.getText().toString();
                    String comment_str = res_comment.getText().toString();
                    String uid = user.getUid();
                    String postId = UUID.randomUUID().toString();
                    if(exist_postId[0] != null) {
                        postId = exist_postId[0];
                    }
                    String image_id = UUID.randomUUID().toString();
                    StorageReference reference = FirebaseStorage.getInstance().getReference()
                            .child("postImages").child(postId).child(image_id + ".jpeg");

                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();


                    image_url = "gs://project-43404.appspot.com/postImages/" + postId + "/" + image_id + ".jpeg";
                    if(exist_postId[0] != null) {
                        StorageReference reference1 = FirebaseStorage.getInstance().getReference()
                                .child("postImages").child(postId);
                        reference1.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {
                                for (StorageReference item : listResult.getItems()) {
                                    item.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
//                                            Log.e("Error while updating image!", String.valueOf(exception));
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Log.e("Error while updating image!", String.valueOf(e));
                                    }
                                });

                        UploadTask uploadTask = reference.putBytes(data);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Posts").child(exist_pid[0]);
                        Map<String, Object> updates = new HashMap<String,Object>();
                        updates.put("post_content", comment_str);
                        updates.put("star", ratings);
                        updates.put("restuarant_name", name_str);
                        updates.put("post_image_url", image_url);
                        updates.put("post_title", title_str);
                        Log.d("Update database", exist_postId[0]);
                        ref.updateChildren(updates);
                    }else {
                        UploadTask uploadTask = reference.putBytes(data);
                        List<String> likedList = new ArrayList<>();
                        likedList.add("0");
                        PostCard postCard = new PostCard(uid, postId, title_str, comment_str, 0, image_url, name_str, ratings, likedList);

                        mDatabase.child(postId).setValue(postCard).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "Successfully upload into real time database");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Upload into real time database failed");
                            }
                        });
                    }
                    Log.d(TAG, "Size: " + listofImages.size());
                }
                Intent intent = new Intent(PostCreationActivity.this, MyNotesActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void chooseImage(Context context){

        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"};

        //create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);



        //set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                //Take Photo
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");

                        Matrix mMatrix = new Matrix();
                        Matrix mat = imageView.getImageMatrix();
                        mMatrix.set(mat);
                        mMatrix.setRotate(90);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), mMatrix, false);
                        imageView.setImageBitmap(bitmap);
                    }
                    break;// Choose from Gallery
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        if(data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for(int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                //do what do you want to do
                                try {
                                    Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                                    // add all images into list
                                    listofImages.add(bitmap2);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            imageView.setImageBitmap(listofImages.get(0));

                        }
                        else if(data.getData() != null) {
                            Uri selectedImageUri = data.getData();

                            try {
                                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                                imageView.setImageBitmap(bitmap2);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
    }
}

