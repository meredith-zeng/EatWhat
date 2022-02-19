package com.example.eatwhat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;

import com.example.eatwhat.mainActivityFragments.Profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPreference extends AppCompatActivity {
    private CheckedTextView Chinese;
    private CheckedTextView Japanese;
    private CheckedTextView Italian;
    private CheckedTextView French;
    private CheckedTextView Tai;
    private CheckedTextView American;
    private CheckedTextView Korean;
    private CheckedTextView Mexican;
    private CheckedTextView Indian;
    private Button startToExplore;
    private List<String> personalPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preference);
        personalPreference = new ArrayList<>();
        Chinese = (CheckedTextView) findViewById(R.id.Chinese);
        Japanese = (CheckedTextView) findViewById(R.id.Japanese);
        Tai = (CheckedTextView) findViewById(R.id.Tai);
        French = (CheckedTextView) findViewById(R.id.French);
        Italian = (CheckedTextView) findViewById(R.id.Italian);
        American = (CheckedTextView) findViewById(R.id.American);
        Korean = (CheckedTextView) findViewById(R.id.Korean);
        Mexican = (CheckedTextView) findViewById(R.id.Mexican);
        Indian = (CheckedTextView) findViewById(R.id.Indian);
        startToExplore = (Button)findViewById(R.id.startToExplore);

        Chinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Chinese.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Chinese.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Chinese.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("Chinese"));
                } else{
                    Chinese.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Chinese.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("Chinese");
                }
            }
        });

        Japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Japanese.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Japanese.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Japanese.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("Japanese"));
                } else{
                    Japanese.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Japanese.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("Japanese");
                }
            }
        });
        Tai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Tai.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Tai.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Tai.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("Tai"));
                } else{
                    Tai.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Tai.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("Tai");
                }
            }
        });
        French.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (French.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    French.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    French.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("French"));
                } else{
                    French.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    French.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("French");
                }
            }
        });
        Italian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Italian.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Italian.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Italian.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("Italian"));
                } else{
                    Italian.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Italian.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("Italian");
                }
            }
        });
        American.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (American.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    American.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    American.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("American"));
                } else{
                    American.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    American.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("American");
                }
            }
        });
        Korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Korean.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Korean.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Korean.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("Korean"));
                } else{
                    Korean.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Korean.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("Korean");
                }
            }
        });
        Mexican.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Mexican.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Mexican.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Mexican.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("Mexican"));
                } else{
                    Mexican.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Mexican.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("Mexican");
                }
            }
        });
        Indian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Indian.getCurrentTextColor() == Color.parseColor("#978C8C")){
                    Indian.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                    Indian.setTextColor(Color.parseColor("#FFFFFF"));
                    personalPreference.add(new String("Indian"));
                } else{
                    Indian.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                    Indian.setTextColor(Color.parseColor("#978C8C"));
                    personalPreference.remove("Indian");
                }
            }
        });
        startToExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartToExplore();
            }
        });
    }

    private void setStartToExplore(){
        System.out.println(Arrays.toString(personalPreference.toArray()));
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

}