package com.example.eatwhat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eatwhat.R;
import com.example.eatwhat.activity.user.SignInActivity;

import org.w3c.dom.Text;

public class SlpashActivity extends AppCompatActivity {
    ImageView slpashImg, logoImg;
    TextView logoText;
    CharSequence charSequence;
    int index;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slpash);

        logoImg = findViewById(R.id.logo_img);
        slpashImg = findViewById(R.id.slpash_img);
        logoText = findViewById(R.id.eatwhat_logo_text);

        slpashImg.animate().translationY(1600).setDuration(1000).setStartDelay(1500);
        logoImg.animate().translationY(-1400).setDuration(1000).setStartDelay(1500);

        animatText("EatWhat");
        logoText.animate().translationY(-1400).setDuration(1000).setStartDelay(1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SlpashActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);

    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            logoText.setText(charSequence.subSequence(0, index++));
            if (index <= charSequence.length()){

                handler.postDelayed(runnable, 200);
            }
        }
    };

    public void animatText(CharSequence cs){
        charSequence = cs;
        index = 0;
        logoText.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 200);

    }
}