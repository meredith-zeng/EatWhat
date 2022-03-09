package com.example.eatwhat.mainActivityFragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.media.MediaPlayer;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.eatwhat.R;
import com.example.eatwhat.activity.restaurant.RestaurantPageActivity;
import com.example.eatwhat.cardview.RestaurantCard;
import com.example.eatwhat.model.User;
import com.example.eatwhat.service.RestaurantService;
import com.example.eatwhat.service.RetrofitClient;
import com.example.eatwhat.service.RestaurantPojo.Business;
import com.example.eatwhat.service.RestaurantPojo.Restaurant;
import com.example.eatwhat.util.RandomUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private FusedLocationProviderClient fusedLocationClient;
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

            if (x > 70 || y > 70 || z > 100) {
                isShake = true;
                playSound(getContext());
                vibrate( 500);

                RetrofitClient retrofitClient = new RetrofitClient();
                RestaurantService methods = retrofitClient.getRetrofit().create(RestaurantService.class);

                String location = "Santa Clara";

                DocumentReference docRef = FirebaseFirestore
                        .getInstance()
                        .collection("user")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            User curUser = document.toObject(User.class);
                            int index = RandomUtil.getRandomNumber(curUser.getPreference().size() - 1, 0);
                            if (index < 0 || index > curUser.getPreference().size()){
                                index = 0;
                            }
                            String category = curUser.getPreference().get(index);
                            int offset = RandomUtil.getRandomNumber(0, 30);
                            Call<Restaurant> call = methods.queryRestaurantByCategory(null, location, category, null, 1, offset);


                            Log.d(TAG, "run: " + call.toString());
                            call.enqueue(new Callback<Restaurant>() {
                                @Override
                                public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                                    if (response.code() == 200){
                                        Business business;
                                        if (response.body().getBusinesses() == null || response.body().getBusinesses().size() == 0) {
                                            //Todo: Add exception handling
                                            RestaurantCard newCard = new RestaurantCard("https://s3-media2.fl.yelpcdn.com/bphoto/lya4BuaaKfI6zN1LrmCA8g/o.jpg"
                                                    ,"Colosseum New York Pizza", "Pizza", 4, "5hoAAdJ2XUVwFnmX49nyqw");
                                        }else {
                                            business = response.body().getBusinesses().get(0);
                                            restaurantCard = new RestaurantCard(business.getImageUrl(), business.getName(), business.getCategories().get(0).getTitle(), business.getRating(), business.getId());
                                            restaurantId = business.getId();
                                        }

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
                });

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

    @SuppressLint("MissingPermission")
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

        if (restaurantCard == null){
            restaurantId = "nHLD88-716MTMoV1kjK-wQ";
            String url = "https://s3-media3.fl.yelpcdn.com/bphoto/rebiw-Xttu2iHPDNaBliLA/o.jpg";
            String title = "Smoking Pig BBQ";
            String content ="It's the best one!";
            restaurantCard = new RestaurantCard(url, title, content, 0, restaurantId);
        }


        tvTitle.setText(restaurantCard.getTitle());
        if (restaurantCard.getRestaurantImageUrl().toString().length() != 0) {
            GlideUrl glideUrl = new GlideUrl(restaurantCard.getRestaurantImageUrl(), new LazyHeaders.Builder()
                    .addHeader("Authorization", " Bearer " + TOKEN)
                    .build());
            Glide.with(getContext())
                    .load(glideUrl)
                    .into(iv_image);
        }
        else {
            Log.d("From TodaysFragment", "getRestaurantImageUrl() is empty");
        }

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