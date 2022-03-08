package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatwhat.activity.MainActivity;
import com.example.eatwhat.R;
import com.example.eatwhat.activity.SlpashActivity;
import com.example.eatwhat.notification.NotificationData;
import com.example.eatwhat.notification.Response;
import com.example.eatwhat.notification.Sender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SignInActivity extends AppCompatActivity {

    EditText password, email_addr;
    TextView noAccount, forgotPassword;
    Button signIn;
    boolean accepted = false;
    //    ImageButton rememberMe;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignInActivity";
    private String token = " ";
    String email_str, password_str;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_sign_in);
        checkPermission();


        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(slide);
        getWindow().setReenterTransition(explode);

        password = findViewById(R.id.password_in_signIn);
        email_addr = findViewById(R.id.email_addr_in_signIn);
        noAccount = findViewById(R.id.dont_have_account);
//        forgotPassword = findViewById(R.id.forgotPassword);
//        rememberMe = findViewById(R.id.rememberMe_In_signIn);
        signIn = findViewById(R.id.btn_signIn);
        mAuth = FirebaseAuth.getInstance();
//        rememberMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!accepted){
//                    rememberMe.setBackgroundResource(R.drawable.ic_baseline_check_24);
//                    accepted = true;
//                }
//                else{
//                    accepted = false;
//                    rememberMe.setBackgroundResource(0);
//                }
//            }
//        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignInActivity.this).toBundle());

                overridePendingTransition(R.transition.explode, R.anim.nav_default_exit_anim);
            }
        });

//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent  = new Intent();
//            }
//        });

        if (!email_addr.getText().toString().equals("")){
            signIn.setBackgroundColor(Color.parseColor("#F98426"));
        }
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email_str = email_addr.getText().toString().trim();
                password_str = password.getText().toString().trim();
                if(TextUtils.isEmpty(email_addr.getText())){
                    Toast.makeText(SignInActivity.this, "Email Address Cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password.getText())){
                    Toast.makeText(SignInActivity.this, "Password Cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    signIn(email_str, password_str);
                    checkToken();
                }

            }
        });



    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_exit_anim);
                            finish();

                        } else {

                            Log.d(TAG, "" + task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkPermission(){
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(SignInActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(SignInActivity.this, permissions
                    , MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 ) {
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            Toast.makeText(SignInActivity.this, per + " Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignInActivity.this, per + " Permission Granted", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return;
            }
        }
    }


    public void checkToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        String curUserId = mAuth.getCurrentUser().getUid();
                        databaseReference.child("Tokens").child(curUserId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                String curToken = String.valueOf(task.getResult().getValue());
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
                                databaseReference.child(curUserId).setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "Token is successfully uploaded to Firebase");
                                    }
                                });

                                Log.d(TAG, "Current Token: " + curToken);
                            }
                        });
                    }
                });


    }


}