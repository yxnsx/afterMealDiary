package com.example.aftermealdiary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

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

    public Location getLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // GPS와 네트워크 불가시 소스 구현
        } else {
            this.getLocation = true;

            if (isNetworkEnabled) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_UPDATE_INTERVAL, MINIMUM_UPDATE_DISTANCE, this);
                }
                if (locationManager != null) {
                    // 퍼미션 요청 수락시
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                } else { // 퍼미션 요청 거절시
                    // 퍼미션 재요청

                }

            }
        }

        if (isGPSEnabled) {
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_UPDATE_INTERVAL, MINIMUM_UPDATE_DISTANCE, this);

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        }
        return location;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
