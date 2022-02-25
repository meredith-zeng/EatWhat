package com.example.eatwhat.activity.user;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatwhat.activity.MainActivity;
import com.example.eatwhat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    EditText password, email_addr;
    TextView noAccount, forgotPassword;
    Button signIn;
    boolean accepted = false;
    ImageButton rememberMe;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignInActivity";
    String email_str, password_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        password = findViewById(R.id.password_in_signIn);
        email_addr = findViewById(R.id.email_addr_in_signIn);
        noAccount = findViewById(R.id.dont_have_account);
        forgotPassword = findViewById(R.id.forgotPassword);
        rememberMe = findViewById(R.id.rememberMe_In_signIn);
        signIn = findViewById(R.id.btn_signIn);
        mAuth = FirebaseAuth.getInstance();
        rememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!accepted){
                    rememberMe.setBackgroundResource(R.drawable.ic_baseline_check_24);
                    accepted = true;
                }
                else{
                    accepted = false;
                    rememberMe.setBackgroundResource(0);
                }
            }
        });


        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent();
            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_str = email_addr.getText().toString().trim();
                password_str = password.getText().toString().trim();
                if(TextUtils.isEmpty(email_addr.getText())){
                    Toast.makeText(SignInActivity.this, "Email Address Cannot be Empty!", Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(password.getText())){
                    Toast.makeText(SignInActivity.this, "Password Cannot be Empty!", Toast.LENGTH_LONG).show();
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
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
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


}