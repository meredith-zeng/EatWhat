package com.example.eatwhat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.function.IntConsumer;

public class Welcomepage extends AppCompatActivity {
    private TextView welcome;
    private ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);

        welcome = (TextView) findViewById(R.id.welcome);
        icon = (ImageView) findViewById(R.id.icon);
        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartToExplore();
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpringAnimationY(view, 1200f);

                SpringAnimationY(view, 0f);


            }
        });

//        icon.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View view, DragEvent dragEvent) {
//                SpringAnimationY(view, 1200f);
//                SpringAnimationY(view, 0f);
//                return false;
//            }
//        });

    }

    private void setStartToExplore(){
        Intent intent = new Intent(this,SetPreference.class);
        startActivity(intent);
    }

    private void SpringAnimationY(View view, float position){
        SpringAnimation springAnimation = new SpringAnimation(view, DynamicAnimation.TRANSLATION_Y);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(SpringForce.STIFFNESS_LOW);
        springForce.setFinalPosition(position);
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation.setSpring(springForce);
        springAnimation.start();
    }
    private void SpringAnimationX(View view, float position){
        SpringAnimation springAnimation = new SpringAnimation(view, DynamicAnimation.TRANSLATION_X);
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(SpringForce.STIFFNESS_LOW);
        springForce.setFinalPosition(position);
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimation.setSpring(springForce);
        springAnimation.start();
    }
}