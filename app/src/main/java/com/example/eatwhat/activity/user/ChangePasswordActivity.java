package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eatwhat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    EditText original, password, repassword;
    Button submit;
    public static final String TAG = "ChangePasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setToolBar();

        original = findViewById(R.id.original_password);
        password = findViewById(R.id.new_password);
        repassword = findViewById(R.id.repassword);
        submit = findViewById(R.id.submit_btn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(original.getText())){
                    Toast.makeText(ChangePasswordActivity.this, "Original Password cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password.getText())){
                    Toast.makeText(ChangePasswordActivity.this, "New Password cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(repassword.getText())){
                    Toast.makeText(ChangePasswordActivity.this, "Re-enter Password cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!password.getText().toString().equals(repassword.getText().toString())){
                    Toast.makeText(ChangePasswordActivity.this, "Password does not match!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), original.getText().toString().trim());

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(password.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ChangePasswordActivity.this, "Password updated!!", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "Password updated");
                                                    Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    Log.d(TAG, "Error password not updated");
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d(TAG, "Error auth failed");
                                    }
                                }
                            });
                }
            }
        });
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.change_password_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("");
        }
    }

    @Override
        public boolean onOptionsItemSelected(MenuItem menuItem) {
            if (menuItem.getItemId() == android.R.id.home) {
                finish();
                return true;
            }

            return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}