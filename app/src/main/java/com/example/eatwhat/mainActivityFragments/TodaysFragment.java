package com.example.eatwhat.mainActivityFragments;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eatwhat.R;
import com.example.eatwhat.activity.restaurant.RestaurantPageActivity;
import com.example.eatwhat.cardview.RestaurantCard;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.pojo.Business;
import com.example.eatwhat.service.pojo.Restaurant;
import com.example.eatwhat.util.RandomUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaysFragment extends Fragment {
    private final static String TAG = "Today's Fragment";
    private SensorManager sensorManager;
    private ShakeSensorListener shakeListener;
    private boolean isShake = false;

    private ImageView imgHand;
    private ObjectAnimator anim;

    private ImageView iv_image;
    private TextView tvTitle;

    private RestaurantCard restaurantCard;
    private String restaurantId;

    private Button bt_today;
    private static final String TOKEN = "RO1Oxxrhr0ZE2nvxEvJ0ViejBTWKcLLhPQ7wg6GGPlGiHvjwaLPU2eWlt4myH3BC1CP4RSzIQ7UCFjZ-FBaF_4ToUYHfs6FF6FwipyMuz47xVvlpEr6gDv-2YRQUYnYx";

    public TodaysFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_todays, container, false);

        imgHand = (ImageView) view.findViewById(R.id.imgHand);


        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        shakeListener = new ShakeSensorListener();

        anim = ObjectAnimator.ofFloat(imgHand,"rotation",0f,45f,-30f,0f);
        anim.setDuration(500);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        return view;
    }

    @Override
    public void onResume() {
        //注册监听加速度传感器
        Log.e(TAG, "register shakeListener");
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(TAG, "unregisterListener");
        sensorManager.unregisterListener(shakeListener);
        super.onPause();
    }

    private class ShakeSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            //避免一直摇
            if (isShake) {
                return;
            }
            // 开始动画
            anim.start();
            float[] values = event.values;
            float x = Math.abs(values[0]);
            float y = Math.abs(values[1]);
            float z = Math.abs(values[2]);

            if (x > 45 || y > 45 || z > 100000) {
                isShake = true;
                playSound(getContext());
                vibrate( 500);

                RetrofitClient retrofitClient = new RetrofitClient();
                RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);

                String location = "Santa Clara";
                int offset = RandomUtil.getRandomNumber(0, 30);
                Call<Restaurant> call = methods.queryRestaurantByLocation(location, 1, offset);
                Log.d(TAG, "run: " + call.toString());
                call.enqueue(new Callback<Restaurant>() {
                    @Override
                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                        if (response.code() == 200){
                            Business business = response.body().getBusinesses().get(0);
                            restaurantCard = new RestaurantCard(business.getImageUrl(), business.getName(), business.getCategories().get(0).toString(), false);
                            restaurantId = business.getId();
                            Log.e("Shake res" , restaurantCard.getRestaurantImageUrl());
                        }
                    }

                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {
                        Log.e("Shake res" ,t.toString());
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showDialog();
                        anim.cancel();
                    }
                },1000);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private void playSound(Context context) {
        MediaPlayer player = MediaPlayer.create(context,R.raw.shake_sound);
        player.start();
    }

    private void vibrate(long milliseconds) {
        Vibrator vibrator = (Vibrator)getContext().getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    private void showDialog() {
        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog,null);
        mAlertDialog.setContentView(view);

        iv_image = (ImageView) view.findViewById(R.id.shake_image);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        bt_today = (Button) view.findViewById(R.id.bt_today);



        tvTitle.setText(restaurantCard.getTitle());

        GlideUrl glideUrl = new GlideUrl(restaurantCard.getRestaurantImageUrl(), new LazyHeaders.Builder()
                .addHeader("Authorization", " Bearer " + TOKEN)
                .build());
        Glide.with(getContext())
                .load(glideUrl)
                .into(iv_image);

        bt_today.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), RestaurantPageActivity.class);
                intent.putExtra("title", restaurantCard.getTitle());
                intent.putExtra("content", restaurantCard.getContent());
                intent.putExtra("imageUrl", restaurantCard.getRestaurantImageUrl());
                intent.putExtra("id", restaurantId);
                getContext().startActivity(intent);
            }
        });

        mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isShake = false;
                mAlertDialog.cancel();
            }
        });
        Window window = mAlertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }



}