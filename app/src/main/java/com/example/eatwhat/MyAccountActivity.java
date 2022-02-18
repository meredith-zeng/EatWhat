package com.example.eatwhat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_list_view);

        String[] items = new String[]{"    My Profile", "    My Posts", "    Review History"};
        final ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < items.length; i++) {
            list.add(items[i]);
        }

        ListView listview = findViewById(R.id.myList);
        listview.setAdapter(new ArrayAdapter<String>(this, R.layout.my_account_item, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (view.findViewById(R.id.listItem));
                textView.setMinHeight(0);
                textView.setMinimumHeight(0);
                textView.setHeight(150);
                return view;
            }
        });
    }
}