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
    private CheckedTextView Indian;

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
        Indian = (CheckedTextView) findViewById(R.id.Indian);

        Chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Chinese.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Chinese.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Chinese.setTextColor(Color.parseColor("#FFFFFF"));

                } else{
                    Chinese.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Chinese.setTextColor(Color.parseColor("#978C8C"));

                }
            }
        });

        Japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Japanese.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Japanese.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Japanese.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    Japanese.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Japanese.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });
        Tai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tai.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Tai.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Tai.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    Tai.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Tai.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });
        French.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (French.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    French.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    French.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    French.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    French.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });
        Italy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Italy.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Italy.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Italy.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    Italy.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Italy.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });
        American.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (American.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    American.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    American.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    American.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    American.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });
        Korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Korean.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Korean.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Korean.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    Korean.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Korean.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });
        Mexican.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Mexican.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Mexican.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Mexican.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    Mexican.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Mexican.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });
        Indian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Indian.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Indian.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Indian.setTextColor(Color.parseColor("#FFFFFF"));
                } else{
                    Indian.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Indian.setTextColor(Color.parseColor("#978C8C"));
                }
            }
        });
    }

}