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
import android.content.pm.ActivityInfo;
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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.eatwhat.R;

public class TodaysFragment extends Fragment {
    private SensorManager sensorManager;
    private ShakeSensorListener shakeListener;
    private boolean isShake = false;

    private ImageView imgHand;
    private ObjectAnimator anim;

    public TodaysFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_todays, container, false);

        imgHand = (ImageView) view.findViewById(R.id.imgHand);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        shakeListener = new ShakeSensorListener();


        anim = ObjectAnimator.ofFloat(imgHand,"rotation",0f,45f,-30f,0f);
        anim.setDuration(500);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        return view;
    }

    @Override
    public void onResume() {
        //注册监听加速度传感器
        sensorManager.registerListener(shakeListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    public void onPause() {
        /**
         * 资源释放
         */
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
            /*
             * x : x轴方向的重力加速度，向右为正
             * y : y轴方向的重力加速度，向前为正
             * z : z轴方向的重力加速度，向上为正
             */
            float x = Math.abs(values[0]);
            float y = Math.abs(values[1]);
            float z = Math.abs(values[2]);
            //加速度超过19，摇一摇成功
            if (x > 30 || y > 30 || z > 30) {
                isShake = true;
                playSound(getContext());
                vibrate( 500);
                //仿网络延迟操作，这里可以去请求服务器...
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //弹框
                        showDialog();
                        //动画取消
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
        Vibrator vibrator = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(milliseconds);
    }

    private void showDialog() {
        final AlertDialog mAlertDialog = new AlertDialog.Builder(getContext()).show();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog,null);
        mAlertDialog.setContentView(view);
        mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //这里让弹框取消后，才可以执行下一次的摇一摇
                isShake = false;
                mAlertDialog.cancel();
            }
        });
        Window window = mAlertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }
}