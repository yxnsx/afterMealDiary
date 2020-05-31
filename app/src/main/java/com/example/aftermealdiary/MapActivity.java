package com.example.aftermealdiary;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    Button button_home;
    Button button_calendar;
    Button button_map;
    Button button_setting;
    CoordinatorLayout coordinatorLayout_snackBarHolder;

    private GoogleMap googleMap;
    private Marker currentMarker = null;
    int PERMISSION_LOCATION = 30;
    long MINIMUM_UPDATE_INTERVAL = 3000; // 3초
    long MAXIMUM_UPDATE_INTERVAL = 1000; // 1초

    Location location;
    LocationRequest locationRequest;
    FusedLocationProviderClient locationProviderClient;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 레이아웃 리소스
        button_home = findViewById(R.id.button_home);
        button_calendar = findViewById(R.id.button_calendar);
        button_map = findViewById(R.id.button_map);
        button_setting = findViewById(R.id.button_setting);
        coordinatorLayout_snackBarHolder = findViewById(R.id.coordinatorLayout_snackBarHolder);

        // 클릭리스너 설정
        button_home.setOnClickListener(this);
        button_calendar.setOnClickListener(this);
        button_map.setOnClickListener(this);
        button_setting.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_mapView);
        mapFragment.getMapAsync(this);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(MAXIMUM_UPDATE_INTERVAL)
                .setFastestInterval(MINIMUM_UPDATE_INTERVAL);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        }

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
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

            case R.id.button_map:
                Intent intentMap = new Intent(this, MapActivity.class);
                startActivity(intentMap);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;

            // 마이페이지 버튼을 클릭했을 경우
            case R.id.button_setting:
                Intent toMyPage = new Intent(v.getContext(), SettingActivity.class);
                startActivity(toMyPage);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        setDefaultLocation();

        checkPermissions();
        requestPermissions();
    }

    public void setDefaultLocation() {

        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 없음";
        String markerSnippet = "위치 접근 권한을 허용해주세요.";

        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        googleMap.moveCamera(cameraUpdate);

    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        googleMap.moveCamera(cameraUpdate);

    }

    private boolean checkPermissions() {
        int fineLocationPermissionState = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);

        int backgroundLocationPermissionState = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION);

        return (fineLocationPermissionState == PackageManager.PERMISSION_GRANTED) &&
                (backgroundLocationPermissionState == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {

        boolean permissionAccessFineLocationApproved =
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean backgroundLocationPermissionApproved =
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean shouldProvideRationale =
                permissionAccessFineLocationApproved && backgroundLocationPermissionApproved;

        if (shouldProvideRationale) {
            Snackbar.make(
                    coordinatorLayout_snackBarHolder,
                    "지도를 사용하기 위해서는 \n위치 접근 권한이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("허용", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MapActivity.this,
                                    new String[] {
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION },
                                    PERMISSION_LOCATION);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_LOCATION) {
            // 퍼미션을 허용했을 경우
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.

                Intent intent = new Intent(this, LocationBroadcastReceiver.class);
                intent.setAction(LocationBroadcastReceiver.ACTION_PROCESS_UPDATES);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                locationProviderClient.requestLocationUpdates(locationRequest, pendingIntent); // 에러나면 requestLocationUpdates 메서드로 빼서 구현

            } else {
                Snackbar.make(
                        coordinatorLayout_snackBarHolder,
                        "지도를 사용하기 위해서는 \n위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("설정", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
