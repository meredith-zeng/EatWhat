package com.example.eatwhat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPreferenceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Button startToExplore;
    private List<String> personalPreference;
    private Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preference);
        String source = getIntent().getStringExtra("source");

        Button btn = (Button)findViewById(R.id.setPreferenceButton);
        if (source.equals("home")){
            setToolBar();
            btn.setText("Submit");
        }
        else if(source.equals("signup")) {
            btn = (Button)findViewById(R.id.setPreferenceButton);
            btn.setText("Start to explore");
        }
        else {
            //
        }

        List<CheckedTextView> itemList = new ArrayList<>();
        personalPreference = new ArrayList<>();
        itemList.add((CheckedTextView)findViewById(R.id.Chinese));
        itemList.add((CheckedTextView)findViewById(R.id.Japanese));
        itemList.add((CheckedTextView)findViewById(R.id.Tai));
        itemList.add((CheckedTextView)findViewById(R.id.French));
        itemList.add((CheckedTextView)findViewById(R.id.Italian));
        itemList.add((CheckedTextView)findViewById(R.id.American));
        itemList.add((CheckedTextView)findViewById(R.id.Korean));
        itemList.add((CheckedTextView)findViewById(R.id.Mexican));
        itemList.add((CheckedTextView)findViewById(R.id.Indian));
        //startToExplore = findViewById(R.id.setPreferenceButton);

        for (int i = 0; i < itemList.size(); i++) {
            CheckedTextView item = itemList.get(i);
            String name = item.getText().toString();
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getCurrentTextColor() == Color.parseColor("#978C8C")){
                        item.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                        item.setTextColor(Color.parseColor("#FFFFFF"));
                        personalPreference.add(name);
                    } else{
                        item.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                        item.setTextColor(Color.parseColor("#978C8C"));
                        personalPreference.remove(name);
                    }
                }
            });
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (source.equals("signup")) {
                    jumpToMainActivity();
                }
                else if (source.equals("home")) {
                    jumpToMainActivity();
                    finish();
                }
            }
        });
    }

    private void jumpToMainActivity(){
        System.out.println(Arrays.toString(personalPreference.toArray()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.setPreferenceToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
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