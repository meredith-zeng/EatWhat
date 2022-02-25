package com.example.eatwhat.activity.user;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.example.eatwhat.R;
import com.example.eatwhat.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class SignUpActivity extends AppCompatActivity {
    private final static String TAG = "SignUp Activity";
    EditText email_addr, username, password, repassword;
    Button nextStep;
    private FirebaseAuth mAuth;
    User currentUser;
    String username_str, email_str, password_str;
    boolean accepted = false;
    ImageButton imageBtn;
    ShapeableImageView imageView;
    Bitmap bitmap;



    private final static int CAMERA_PERMISSION_CODE = 1;
    private final static int STORAGE_PERMISSION_CODE = 2;
    private final static int WRITE_EXTERNAL_STORAGE_CODE = 3;
    private boolean camera_granted = false;
    private boolean write_storage_granted  = false;
    private boolean send_storage_granted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        bitmap = BitmapFactory.decodeResource(SignUpActivity.this.getResources(), R.drawable.ic_android_black_24dp);
        email_addr = findViewById(R.id.email_addr_in_signUp);
        username = findViewById(R.id.user_name_in_signUp);
        password = findViewById(R.id.password_in_signUp);
        repassword = findViewById(R.id.repassword_in_signUp);
        nextStep = findViewById(R.id.next_step_in_signUp);
        imageBtn = findViewById(R.id.image_btn_in_signUp);
        imageView = findViewById(R.id.image_in_signUp);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);

                if(camera_granted && send_storage_granted && write_storage_granted){
                    chooseImage(SignUpActivity.this);
                }
            }
        });


        imageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!accepted){
                    imageBtn.setBackgroundResource(R.drawable.ic_baseline_check_24);
                    accepted = true;
                }
                else{
                    imageBtn.setBackgroundResource(0);
                    accepted  = false;
                }
            }
        });
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(email_addr.getText())){
                    Toast.makeText(SignUpActivity.this, "Email Address Cannot be Empty!", Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(username.getText())){
                    Toast.makeText(SignUpActivity.this, "User name Cannot be Empty!", Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(password.getText())){
                    Toast.makeText(SignUpActivity.this, "password Cannot be Empty!", Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(repassword.getText())){
                    Toast.makeText(SignUpActivity.this, "re password Cannot be Empty!", Toast.LENGTH_LONG).show();
                }
                else if(!password.getText().toString().equals(repassword.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "password does not matched!", Toast.LENGTH_LONG).show();
                }
                else if(!accepted){
                    Toast.makeText(SignUpActivity.this, "Please accept our Terms and Conditions", Toast.LENGTH_LONG).show();
                }
                else if(bitmap == null){
                    Toast.makeText(SignUpActivity.this, "Please Set a Profile Image", Toast.LENGTH_LONG).show();
                }
                else{
                    email_str = email_addr.getText().toString().trim();
                    password_str = password.getText().toString().trim();
                    username_str = username.getText().toString().trim();
                    createAccount(email_str, password_str);
                }
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "create Account Successfully!");
                    FirebaseUser user = mAuth.getCurrentUser();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    String uid = user.getUid();
                    //Storage Image into firebase storage under profileImage
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child("profileImages").child(uid + ".jpeg");
                    reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownloadUrl(reference);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: " + e.getCause());
                        }
                    });

                    currentUser = new User(uid, username_str, email_str, "");
                    Intent intent = new Intent(SignUpActivity.this, SetPreferenceActivity.class);
                    intent.putExtra("currentUser", currentUser);
                    intent.putExtra("source", "signup");
                    startActivity(intent);
                    finish();
                }
                else{

                    Log.d(TAG, "something wrong! " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reload(){};


    // Gallery Permission
    public void checkPermission(String permission, int requestCode){
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SignUpActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(SignUpActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            camera_granted = true;
            send_storage_granted = true;
            write_storage_granted = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(!camera_granted){
                    // Showing the toast message
                    Toast.makeText(SignUpActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                    camera_granted = true;
                }

            }
            else {
                Toast.makeText(SignUpActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(!send_storage_granted){
                    Toast.makeText(SignUpActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                    send_storage_granted = true;
                }

            }
            else {
                Toast.makeText(SignUpActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == WRITE_EXTERNAL_STORAGE_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(!write_storage_granted){
                    Toast.makeText(SignUpActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                    write_storage_granted = true;
                }

            }
            else {
                Toast.makeText(SignUpActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
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
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");

                        Matrix mMatrix = new Matrix();
                        Matrix mat=imageView.getImageMatrix();
                        mMatrix.set(mat);
                        mMatrix.setRotate(90);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), mMatrix, false);
                        imageView.setImageBitmap(bitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                bitmap = (Bitmap)BitmapFactory.decodeFile(picturePath);
                                imageView.setImageBitmap(bitmap);

                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }


    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess: " + uri);
                setUserProfileUrl(uri);
            }
        });
    }

    private void setUserProfileUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
        user.updateProfile(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(SignUpActivity.this, "Profile Image successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Profile Image failed...", Toast.LENGTH_SHORT);
            }
        });
    }
}