package com.example.aftermealdiary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;


public class SplashActivity extends AppCompatActivity {

    LottieAnimationView lottie_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 애니메이션 리소스 설정 후 재생
        lottie_splash = findViewById(R.id.lottie_splash);
        lottie_splash.setAnimation("splash.json");
        lottie_splash.playAnimation();

        // 2초 딜레이 후 Splash 클래스 실행
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
