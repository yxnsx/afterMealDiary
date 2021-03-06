package com.example.aftermealdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView_menuPicker;
    TextView textView_alarm;
    TextView textView_mealMate;
    TextView textView_sendOpinion;
    TextView textView_setting;

    Button button_home;
    Button button_calendar;
    Button button_map;
    Button button_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 레이아웃 리소스 설정
        textView_menuPicker = findViewById(R.id.textView_menuPicker);
        textView_alarm = findViewById(R.id.textView_alarm);
        textView_mealMate = findViewById(R.id.textView_mealMate);
        textView_sendOpinion = findViewById(R.id.textView_sendOpinion);
        textView_setting = findViewById(R.id.textView_setting);

        button_home = findViewById(R.id.button_home);
        button_calendar = findViewById(R.id.button_calendar);
        button_map = findViewById(R.id.button_map);
        button_setting = findViewById(R.id.button_setting);

        // 클릭리스너 설정
        textView_menuPicker.setOnClickListener(this);
        textView_alarm.setOnClickListener(this);
        textView_mealMate.setOnClickListener(this);
        textView_sendOpinion.setOnClickListener(this);
        textView_setting.setOnClickListener(this);

        button_home.setOnClickListener(this);
        button_calendar.setOnClickListener(this);
        button_map.setOnClickListener(this);
        button_setting.setOnClickListener(this);

        Log.d("디버깅", "MyPageActivity - onCreate(): ");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 홈 버튼을 클릭했을 경우
            case R.id.button_home:
                Intent toHome = new Intent(v.getContext(), HomeActivity.class);
                startActivity(toHome);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;

            // 캘린더 버튼을 클릭했을 경우
            case R.id.button_calendar:
                Intent toCalendar = new Intent(v.getContext(), CalendarActivity.class);
                startActivity(toCalendar);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;

            // 지도 버튼을 클릭했을 경우
            case R.id.button_map:
                Intent intentMap = new Intent(this, MapActivity.class);
                startActivity(intentMap);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;

            // 세팅 버튼을 클릭했을 경우
            case R.id.button_myPage:
                Intent toMyPage = new Intent(v.getContext(), SettingActivity.class);
                startActivity(toMyPage);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;

            // 메뉴 룰렛 버튼을 클릭했을 경우
            case R.id.textView_menuPicker:
                Intent intentMenuPicker = new Intent(getApplicationContext(), MenuPickerActivity.class);
                intentMenuPicker.putExtra("intentFrom", "setting");
                startActivity(intentMenuPicker);
                break;

            // 알람 설정하기 버튼을 클릭했을 경우
            case R.id.textView_alarm:
                Intent intentAlarm = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intentAlarm);
                break;

            // 식사메이트 뽑기 버튼을 클릭했을 경우
            case R.id.textView_mealMate:
                Intent intentMealMate = new Intent(getApplicationContext(), MealMateActivity.class);
                startActivity(intentMealMate);
                break;

            // 의견보내기 버튼을 클릭했을 경우
            case R.id.textView_sendOpinion:
                Intent intentMail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:cw2cu96@gmail.com"));
                startActivity(intentMail);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
