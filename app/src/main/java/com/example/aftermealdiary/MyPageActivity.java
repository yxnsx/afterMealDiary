package com.example.aftermealdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MyPageActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView_toolBarTitle;
    TextView textView_userInfo;
    TextView textView_login;
    TextView textView_logout;
    TextView textView_menuPicker;
    TextView textView_alarm;
    TextView textView_sendOpinion;
    TextView textView_setting;

    Button button_home;
    Button button_calendar;
    Button button_myPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        // 레이아웃 리소스
        textView_toolBarTitle = findViewById(R.id.textView_toolBarTitle);
        button_home = findViewById(R.id.button_home);
        button_calendar = findViewById(R.id.button_calendar);
        button_myPage = findViewById(R.id.button_myPage);

        textView_userInfo = findViewById(R.id.textView_userInfo);
        textView_login = findViewById(R.id.textView_login);
        textView_logout = findViewById(R.id.textView_logout);
        textView_menuPicker = findViewById(R.id.textView_menuPicker);
        textView_alarm = findViewById(R.id.textView_alarm);
        textView_sendOpinion = findViewById(R.id.textView_sendOpinion);
        textView_setting = findViewById(R.id.textView_setting);

        // 클릭리스너 설정
        button_home.setOnClickListener(this);
        button_calendar.setOnClickListener(this);
        button_myPage.setOnClickListener(this);

        textView_login.setOnClickListener(this);
        textView_logout.setOnClickListener(this);
        textView_menuPicker.setOnClickListener(this);
        textView_alarm.setOnClickListener(this);
        textView_sendOpinion.setOnClickListener(this);
        textView_setting.setOnClickListener(this);

        Log.d("디버깅", "MyPageActivity - onCreate(): ");
    }

    @Override
    protected void onStart() {
        
        super.onStart();
        Log.d("디버깅", "MyPageActivity - onStart(): ");
    }

    @Override
    protected void onResume() {
        
        super.onResume();
        Log.d("디버깅", "MyPageActivity - onResume(): ");
    }

    @Override
    protected void onPause() {
        
        super.onPause();
        Log.d("디버깅", "MyPageActivity - onPause(): ");
    }

    @Override
    protected void onStop() {
        
        super.onStop();
        Log.d("디버깅", "MyPageActivity - onStop(): ");
    }

    @Override
    protected void onDestroy() {
        
        super.onDestroy();
        Log.d("디버깅", "MyPageActivity - onDestroy(): ");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // TODO 로그인/회원가입 빼고 그냥 식사알람, 룰렛, 의견보내기를 로그인 레이아웃으로 배치해도 좋을듯,,
            // TODO sharedPreferences 하면서 생각해보기..
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

            // 마이페이지 버튼을 클릭했을 경우
            case R.id.button_myPage:
                Intent toMyPage = new Intent(v.getContext(), MyPageActivity.class);
                startActivity(toMyPage);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;

            case R.id.textView_login:
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                break;

            case R.id.textView_logout:

                break;

            case R.id.textView_menuPicker:
                Intent intentMenuPicker = new Intent(getApplicationContext(), MenuPickerActivity.class);
                intentMenuPicker.putExtra("intentFrom", "mypage");
                startActivity(intentMenuPicker);
                break;

            case R.id.textView_alarm:
                Intent intentAlarm = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intentAlarm);
                break;

            case R.id.textView_sendOpinion:
                Intent intentMail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:cw2cu96@gmail.com"));
                startActivity(intentMail);
                break;

            /*case R.id.textView_setting:
                break;*/
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
