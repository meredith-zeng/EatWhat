package com.example.eatwhat.mainActivityFragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eatwhat.ChangePasswordActivity;
import com.example.eatwhat.R;

public class ProfileActivity extends AppCompatActivity {
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        change = (Button) findViewById(R.id.changepassword);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepassword();
            }
        });
    }
    public void changepassword(){
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }
}