package com.example.eatwhat.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;


import com.example.eatwhat.activity.MainActivity;
import com.example.eatwhat.R;
import com.example.eatwhat.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SetPreferenceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "SetPreferenceActivity";
    private Button startToExplore;
    private List<String> personalPreference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    Map<String, User> users = new HashMap<>();
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
        //startToExplore = findViewById(R.id.setPreferenceButton);
        itemList.add((CheckedTextView)findViewById(R.id.Chinese));
        itemList.add((CheckedTextView)findViewById(R.id.japanese));
        itemList.add((CheckedTextView)findViewById(R.id.Tai));
        itemList.add((CheckedTextView)findViewById(R.id.French));
        itemList.add((CheckedTextView)findViewById(R.id.Italian));
        itemList.add((CheckedTextView)findViewById(R.id.American));
        itemList.add((CheckedTextView)findViewById(R.id.Korean));
        itemList.add((CheckedTextView)findViewById(R.id.Mexican));
        itemList.add((CheckedTextView)findViewById(R.id.Indian));
        itemList.add((CheckedTextView) findViewById(R.id.tradamerican));
        itemList.add((CheckedTextView)findViewById(R.id.asianfusion));
        itemList.add((CheckedTextView)findViewById(R.id.brazilian));
        itemList.add((CheckedTextView)findViewById(R.id.barbeque));
        itemList.add((CheckedTextView)findViewById(R.id.creperies));
        itemList.add((CheckedTextView)findViewById(R.id.buffets));
        itemList.add((CheckedTextView)findViewById(R.id.burgers));
        itemList.add((CheckedTextView)findViewById(R.id.cafes));
        itemList.add((CheckedTextView)findViewById(R.id.cheesesteaks));
        itemList.add((CheckedTextView)findViewById(R.id.dimsum));
        itemList.add((CheckedTextView)findViewById(R.id.hotdogs));
        itemList.add((CheckedTextView)findViewById(R.id.diners));

        String [] categoryArray  = new String[]{"tradamerican", "asianfusion", "brazilian",
                "barbeque", "breakfast_brunch",  "buffets", "burgers", "cafes",
                "cheesesteaks", "chinese", "chicken_wings", "creperies", "dimsum", "diners",
                "hotdogs", "foodstands", "french", "german", "gluten_free", "greek", "indpak",
                "irish", "italian", "japanese", "korean", "latin", "raw_food", "mediterranean",
                "mexican", "russian", "salad", "pizza", "steak", "thai",
                "seafood", "spanish", "vegetarian", "vietnamese"};

        for (int i = 0; i < itemList.size(); i++) {
            CheckedTextView item = itemList.get(i);
            String name = item.getText().toString();
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getCurrentTextColor() == Color.parseColor("#978C8C")){
                        if (personalPreference.size() < 3) {
                            item.setBackground(getResources().getDrawable(R.drawable.afterclickbox));
                            item.setTextColor(Color.parseColor("#FFFFFF"));
                            personalPreference.add(name);
                        } else {
                            Toast.makeText(SetPreferenceActivity.this,"preference cannot over 3 items", Toast.LENGTH_SHORT).show();
                        }
                        //Log.d(TAG, "onClick: +++++++++++++++++++++++++++");
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
                    Intent intent = getIntent();
                    User curUser = (User) intent.getSerializableExtra("currentUser");
                    curUser.setPreference(personalPreference);
                    saveUserInfoToFireStore(curUser);
                    finish();
                }
                else if (source.equals("home")) {
                    finish();
                }
            }
        });
    }

    public void saveUserInfoToFireStore(User curUser){
        String uid = curUser.getUid();
        db.collection("user").document(uid).set(curUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "User upload successfully!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    private void setToolBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.setPreferenceToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
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