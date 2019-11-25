package com.example.medicalapp.reminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicalapp.R;


public class SplashActivityPillsReminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_pills_reminder);
        TextView im = findViewById(R.id.sp);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.myanim);
        im.startAnimation(animation);
        new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    Intent iuu = new Intent(SplashActivityPillsReminder.this, MainActivityPillsReminder.class);
                    startActivity(iuu);
                    finish();
                }
            }
        }.start();
    }
}
