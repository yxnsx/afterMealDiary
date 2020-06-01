package com.example.aftermealdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;


public class SplashActivity extends AppCompatActivity {

    LottieAnimationView lottie_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        lottie_splash = findViewById(R.id.lottie_splash);
        lottie_splash.setAnimation("splash.json");
        lottie_splash.playAnimation();

        Handler handler = new Handler();
        handler.postDelayed(new Splash(), 2000);

    }

    private class Splash implements Runnable{
        public void run(){

            Intent splash = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(splash);

            finish();
        }
    }
}
