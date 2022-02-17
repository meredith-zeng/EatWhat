package com.example.eatwhat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Welcomepage extends AppCompatActivity {
    private TextView welcome;
    private Button startToExplore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);

        welcome = (TextView) findViewById(R.id.welcome);
        startToExplore = (Button) findViewById(R.id.startToExplore);
        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartToExplore();
            }
        });

    }

    private void setStartToExplore(){
        Intent intent = new Intent(this,SetPreference.class);
        startActivity(intent);
    }
}