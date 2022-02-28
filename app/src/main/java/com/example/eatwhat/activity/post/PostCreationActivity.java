package com.example.eatwhat.activity.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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

import com.example.eatwhat.R;

import com.example.eatwhat.cardview.PostCard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PostCreationActivity extends AppCompatActivity {

    private ImageButton mCancelBtn;
    Button post, cancel;
    ImageView add_btn, imageShow;
    RatingBar ratingBar;
    EditText res_title, res_name, res_comment;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Bitmap bitmap;
    private final static int PICK_IMAGE_MULTIPLE = 1;
    private final static String TAG = "PostCreationActivity";
    List<Bitmap> listofImages = new ArrayList<>();
    private final static int LIMIT = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_creation);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts");
        FirebaseUser user = mAuth.getCurrentUser();


        post = findViewById(R.id.post_creation_post_textView);
        ratingBar = findViewById(R.id.ratingBar1);
        res_title = findViewById(R.id.post_creation_title);
        res_name = findViewById(R.id.post_creation_restaurant);
        res_comment = findViewById(R.id.post_creation_body);
        add_btn = findViewById(R.id.post_creation_add_btn);
        imageShow = findViewById(R.id.post_creation_thumbnail);
        mCancelBtn = findViewById(R.id.post_creation_cancel_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
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
                else if(listofImages.size() == 0){
                    Toast.makeText(PostCreationActivity.this, "Please upload some images!", Toast.LENGTH_SHORT).show();
                }
                else{
                    float ratings = ratingBar.getRating();
                    int number_of_likes = ratingBar.getNumStars();
                    String title_str = res_title.getText().toString();
                    String name_str = res_name.getText().toString();
                    String comment_str = res_comment.getText().toString();
                    String uid = user.getUid();
                    String postId = UUID.randomUUID().toString();
                    List<String> imageIds = new ArrayList<>();


                    Log.d(TAG, "Go Here!!!");


                    //store image into Storage
                    for(int i = 0; i < listofImages.size(); i++){
                        imageIds.add(postId + i);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        listofImages.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        StorageReference reference = FirebaseStorage.getInstance().getReference()
                                .child("postImages").child(postId).child(postId + "_" + i + ".jpeg");
                        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "Upload Successfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getCause());
                            }
                        });
                    }

                    //Store postCard into realtime database
                    PostCard postCard = new PostCard(uid, postId, title_str,
                            comment_str, number_of_likes, name_str, ratings, imageIds);


                    mDatabase.child(uid).child(postId).setValue(postCard)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Successfully upload into real time database");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Upload into real time database failed");
                        }
                    });


                    Log.d(TAG, "Size: " + listofImages.size());
                }
            }
        });


        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

            }
        });
    }
/**
    private void cancel_button_init() {
        mCancelBtn = findViewById(R.id.post_creation_cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
**/
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
                        Matrix mat=imageShow.getImageMatrix();
                        mMatrix.set(mat);
                        mMatrix.setRotate(90);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), mMatrix, false);
                        imageShow.setImageBitmap(bitmap);
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
                        }
                        else if(data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            //do what do you want to do
                        }
                    }
                    break;
            }
        }
    }
}

