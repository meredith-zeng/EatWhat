package com.example.eatwhat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

public class SetPreference extends AppCompatActivity {
    private CheckedTextView Chinese;
    private CheckedTextView Japanese;
    private CheckedTextView Italy;
    private CheckedTextView French;
    private CheckedTextView Tai;
    private CheckedTextView American;
    private CheckedTextView Korean;
    private CheckedTextView Mexican;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preference);
        Chinese = (CheckedTextView) findViewById(R.id.Chinese);
        Japanese = (CheckedTextView) findViewById(R.id.Japanese);
        Tai = (CheckedTextView) findViewById(R.id.Tai);
        French = (CheckedTextView) findViewById(R.id.French);
        Italy = (CheckedTextView) findViewById(R.id.Italy);
        American = (CheckedTextView) findViewById(R.id.American);
        Korean = (CheckedTextView) findViewById(R.id.Korean);
        Mexican = (CheckedTextView) findViewById(R.id.Mexican);

        Chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Chinese.isChecked()){
                    Chinese.setBackgroundColor(Color.parseColor("#ba68c8"));
                    Chinese.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    Chinese.setBackgroundColor(Color.parseColor("@null"));
                    Chinese.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });

    }
    private void Chinese(){

    }

}