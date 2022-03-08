package com.example.eatwhat.activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.eatwhat.R;

public class SearchActivity extends AppCompatActivity {
    String[] data = {"Sumiya","ToBang","Thailand Cuisine","Nicole C."};
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

       /* l = (ListView) findViewById(R.id.ListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.single_row,R.id.textView, data);
        l.setAdapter(adapter);*/
    }
}