package com.example.aftermealdiary;

import android.Manifest;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView_menuPicker;
    TextView textView_alarm;
    TextView textView_nutrientInfo;
    TextView textView_sendOpinion;
    TextView textView_setting;

    ImageView imageView_weatherIcon;
    TextView textView_temperature;
    TextView textView_weatherInfo;
    TextView textView_cityInfo;

    Button button_home;
    Button button_calendar;
    Button button_setting;

    int PERMISSION_LOCATION = 30;
    double latitude; // 위도
    double longitude; // 경도
    long MINIMUM_UPDATE_DISTANCE = 1000; // 1000미터
    long MINIMUM_UPDATE_INTERVAL = 1000 * 60; // 1000밀리세컨즈 * 60 = 1분
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean getLocation = false;
    String weatherApiKey = "6c0499aec54f347ed22ed4480d83151a";

    Location location;
    LocationRequest locationRequest;
    FusedLocationProviderClient locationProviderClient;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 레이아웃 리소스
        textView_menuPicker = findViewById(R.id.textView_menuPicker);
        textView_alarm = findViewById(R.id.textView_alarm);
        textView_nutrientInfo = findViewById(R.id.textView_nutrientInfo);
        textView_sendOpinion = findViewById(R.id.textView_sendOpinion);
        textView_setting = findViewById(R.id.textView_setting);

        imageView_weatherIcon = findViewById(R.id.imageView_weatherIcon);
        textView_temperature = findViewById(R.id.textView_temperature);
        textView_weatherInfo = findViewById(R.id.textView_weatherInfo);
        textView_cityInfo = findViewById(R.id.textView_cityInfo);

        button_home = findViewById(R.id.button_home);
        button_calendar = findViewById(R.id.button_calendar);
        button_setting = findViewById(R.id.button_setting);

        // 클릭리스너 설정
        textView_menuPicker.setOnClickListener(this);
        textView_alarm.setOnClickListener(this);
        textView_nutrientInfo.setOnClickListener(this);
        textView_sendOpinion.setOnClickListener(this);
        textView_setting.setOnClickListener(this);

        button_home.setOnClickListener(this);
        button_calendar.setOnClickListener(this);
        button_setting.setOnClickListener(this);

        Log.d("디버깅", "MyPageActivity - onCreate(): ");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 위치정보 퍼미션 허용이 아닐 경우
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 퍼미션 요청
            requestLocationPermission();

        } else {
        }
        // 위치정보 받아오기
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        Log.d("디버깅", "MyPageActivity - onStart(): ");
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*imageView_weatherIcon.setImageDrawable();
        textView_temperature.setText();
        textView_weatherInfo.setText();
        textView_cityInfo.setText();*/
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

            // 세팅 버튼을 클릭했을 경우
            case R.id.button_myPage:
                Intent toMyPage = new Intent(v.getContext(), SettingActivity.class);
                startActivity(toMyPage);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;


            case R.id.textView_menuPicker:
                Intent intentMenuPicker = new Intent(getApplicationContext(), MenuPickerActivity.class);
                intentMenuPicker.putExtra("intentFrom", "setting");
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

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(MINIMUM_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(MINIMUM_UPDATE_INTERVAL * 5);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationBroadcastReceiver.class);
        intent.setAction(LocationBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    

    public void requestLocationPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            // 다이얼로그 설정
            new AlertDialog.Builder(this)
                    .setTitle("접근 권한 설정")
                    .setMessage("날씨 정보를 사용하기 위해서는 위치 접근 권한이 필요합니다.")

                    // 권한 허용 버튼 클릭시
                    .setPositiveButton("허용", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 퍼미션 창 보여주기
                            ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                        }
                    })

                    // 권한 거부 버튼 클릭시
                    .setNegativeButton("거부", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 토스트 메시지 출력 후 다이얼로그 종료
                            Toast.makeText(SettingActivity.this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else { // TODO 이 부분 알아보기
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
        }
    }
}
