package com.example.eatwhat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;


import com.example.eatwhat.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetPreferenceActivity extends AppCompatActivity {
    private static final String TAG = "SetPreferenceActivity";
    private Button startToExplore;
    private List<String> personalPreference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, User> users = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preference);
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
        startToExplore = findViewById(R.id.startToExplore);

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
                        //Log.d(TAG, "onClick: +++++++++++++++++++++++++++");
                    } else{
                        item.setBackground(getResources().getDrawable(R.drawable.round_rectangular));
                        item.setTextColor(Color.parseColor("#978C8C"));
                        personalPreference.remove(name);
                    }
                }
            });
        }

        startToExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                User curUser = (User) intent.getSerializableExtra("currentUser");
                curUser.setPreference(personalPreference);
                //Log.d(TAG, "set preference: " + personalPreference.size() + "  " + personalPreference.toString());
                saveUserInfoToFireStore(curUser);
                setStartToExplore();
            }
        });
    }

    private void setStartToExplore(){
        System.out.println(Arrays.toString(personalPreference.toArray()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveUserInfoToFireStore(User curUser){
        String uid = curUser.getUid();
        users.put(uid, curUser);
        db.collection("user").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

}