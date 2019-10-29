package com.prantology.birthdayhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {
    long delayTime = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        if(DefaultSettings.splashScreenEnabled(SplashScreen.this)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToMainActivity();
                }
            },delayTime);
        }
        else{
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        Intent i = new Intent(SplashScreen.this, MainActivity.class);

        startActivity(i);
        finish();
    }
}
