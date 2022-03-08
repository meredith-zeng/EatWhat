package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    EditText password, email_addr;
    TextView noAccount, forgotPassword;
    Button signIn;
    boolean accepted = false;
    ImageButton rememberMe;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignInActivity";
    String email_str, password_str;
    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    boolean isRemember = false;
    boolean firstTimeSignIn = true;

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_sign_in);
        checkPermission();

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(slide);
        getWindow().setReenterTransition(explode);

        SharedPreferences sh2 = getApplicationContext().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        firstTimeSignIn = sh2.getBoolean("firstTimeSignIn", true);


        
        password = findViewById(R.id.password_in_signIn);
        email_addr = findViewById(R.id.email_addr_in_signIn);
        noAccount = findViewById(R.id.dont_have_account);
//        forgotPassword = findViewById(R.id.forgotPassword);
        rememberMe = findViewById(R.id.rememberMe_In_signIn);
        signIn = findViewById(R.id.btn_signIn);
        mAuth = FirebaseAuth.getInstance();

        rememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRemember){
                    rememberMe.setBackgroundResource(R.drawable.ic_baseline_check_24);
                    isRemember = true;
                }
                else{
                    isRemember = false;
                    rememberMe.setBackgroundResource(0);
                }
            }
        });
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

        if(!firstTimeSignIn){
            String email_address_sh = sh2.getString("email_address", "-1");
            String password_sh = sh2.getString("password", "-1");

            if(!email_address_sh.equals("-1") && !password_sh.equals("-1")){
                email_addr.setText(email_address_sh);
                password.setText(password_sh);
            }
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

                            // If its first time to sign in, and click remember me, put info into SH.
                            if(firstTimeSignIn){
                                if(isRemember){
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                    myEdit.putString("email_address", email_str);
                                    myEdit.putString("password", password_str);
                                    myEdit.putBoolean("firstTimeSignIn", false);
                                    myEdit.commit();
                                }
                            }

                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_exit_anim);
                            finish();

                        } else {

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
}