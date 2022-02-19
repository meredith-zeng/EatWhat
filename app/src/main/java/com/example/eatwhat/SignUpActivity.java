package com.example.eatwhat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.eatwhat.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private final static String TAG = "SignUp Activity";
    EditText email_addr, username, password, repassword;
    Button nextStep;
    private FirebaseAuth mAuth;
    User currentUser;
    String username_str, email_str, password_str;
    boolean accepted = false;
    ImageButton imageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        email_addr = findViewById(R.id.email_addr_in_signUp);
        username = findViewById(R.id.user_name_in_signUp);
        password = findViewById(R.id.password_in_signUp);
        repassword = findViewById(R.id.repassword_in_signUp);
        nextStep = findViewById(R.id.next_step_in_signUp);
        imageBtn = findViewById(R.id.image_btn_in_signUp);
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
                else{
                    email_str = email_addr.getText().toString();
                    password_str = password.getText().toString();
                    username_str = username.getText().toString();
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
                    currentUser = new User(username_str, email_str, "");
                    Intent intent = new Intent(SignUpActivity.this, SetPreferenceActivity.class);
                    intent.putExtra("currentUser", currentUser);
                    startActivity(intent);
                }
                else{
                    Log.d(TAG, "something wrong! " + task.getException());
                }
            }
        });
    }

    public void reload(){};

}