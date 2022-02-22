package com.example.eatwhat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class PostCreationActivity extends AppCompatActivity {

    private ImageButton mCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_creation);
        cancel_button_init();
    }

    private void cancel_button_init() {
        mCancelBtn = findViewById(R.id.post_creation_cancel_btn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}