package com.example.aftermealdiary;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, PlacesListener {

    Button button_home;
    Button button_calendar;
    Button button_map;
    Button button_setting;
    Button button_refresh;
    CoordinatorLayout coordinatorLayout_snackBarHolder;

    LatLng currentPosition;
    private GoogleMap googleMap;
    private Marker currentMarker = null;
    int PERMISSION_LOCATION = 30;
    long MINIMUM_UPDATE_INTERVAL = 3000; // 3초
    long MAXIMUM_UPDATE_INTERVAL = 1000; // 1초
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;

    Location location;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient locationProviderClient;

    boolean needRequest = false;
    List<Marker> previous_marker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 레이아웃 리소스 설정
        button_home = findViewById(R.id.button_home);
        button_calendar = findViewById(R.id.button_calendar);
        button_map = findViewById(R.id.button_map);
        button_setting = findViewById(R.id.button_setting);
        button_refresh = findViewById(R.id.button_refresh);
        coordinatorLayout_snackBarHolder = findViewById(R.id.coordinatorLayout_snackBarHolder);

        // 클릭리스너 설정
        button_home.setOnClickListener(this);
        button_calendar.setOnClickListener(this);
        button_map.setOnClickListener(this);
        button_setting.setOnClickListener(this);
        button_refresh.setOnClickListener(this);

        // 마커 어레이리스트 설정
        previous_marker = new ArrayList<>();

        // 지도 표시할 프래그먼트 설정
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_mapView);
        mapFragment.getMapAsync(this);

        // 위치 요청 설정
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(MAXIMUM_UPDATE_INTERVAL)
                .setFastestInterval(MINIMUM_UPDATE_INTERVAL);

        // 생성한 위치 요청 추가
        LocationSettingsRequest.Builder builder
                = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        // 위치정보를 가져오는 FusedLocationProviderClient 설정
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 위치정보를 가져오는 퍼미션이 허용되지 않은 상태일 경우
        if (!checkPermissions()) {
            // 퍼미션 요청하기
            requestPermissions();
        }
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

            // 내 주변 음식점 보기 버튼을 클릭했을 경우
            case R.id.button_refresh:
                showPlaceInformation(currentPosition);
                break;
        }
    }

    @Override // 지도가 준비되었을 떄 자동으로 실행되는 콜백 메서드
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        setDefaultLocation(); // 서울로 기본 위치 설정

        // 위치정보를 가져오는 퍼미션이 허용되지 않은 상태일 경우
        if (!checkPermissions()) {
            // 퍼미션 요청하기
            requestPermissions();
        }

        // 위치정보 업데이트 요청을 위한 LocationCallback 설정
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // 위치정보 리스트 형태로 가져오기
                List<Location> locationList = locationResult.getLocations();

                // 가져올 위치정보가 리스트에 있을 경우
                if (locationList.size() > 0) {

                    // 리스트에서 위치정보 가져오기
                    location = locationList.get(locationList.size() - 1);

                    // 가져온 위치정보로 현재 위치의 위도, 경도 값 지정
                    currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                    // 현재위치를 나타내는 마커에 설정할 정보 지정
                    String markerTitle = getCurrentAddress(currentPosition);
                    String markerSnippet = "위도:" + location.getLatitude() + " 경도:" + location.getLongitude();

                    //현재 위치에 마커 생성
                    setCurrentLocation(location, markerTitle, markerSnippet);
                }
            }
        };
        // 위치 업데이트
        startLocationUpdates();
    }

    private void startLocationUpdates() {

        // GPS가 사용가능한 상태가 아닐 경우
        if (!checkLocationServicesStatus()) {

            // GPS 재설정 여부를 묻는 다이얼로그 띄워주기
            showDialogForLocationServiceSetting();

        } else { // GPS가 사용 가능한 상태일 경우

            // 위치정보를 가져오는 퍼미션이 허용되지 않은 상태일 경우
            if (!checkPermissions()) {
                // 퍼미션 요청하기
                requestPermissions();

            } else {
                // 위치정보를 가져오는 FusedLocationProviderClient 설정
                locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                googleMap.setMyLocationEnabled(true);
            }
        }
    }

    private void showDialogForLocationServiceSetting() {

        new AlertDialog.Builder(MapActivity.this)
                .setTitle("GPS 설정")
                .setMessage("지도를 사용하기 위해서는 GPS 기능이 필요합니다.")
                .setCancelable(true) // 뒤로가기나 다이얼로그 외의 범위를 터치하여 다이얼로그 창을 종료할 수 있음

                // 설정 버튼 클릭시
                .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        // GPS 설정 페이지로 이동하는 인텐트 설정
                        Intent callGPSSettingIntent
                                = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
                    }
                })
                // 취소 버튼 클릭시
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // 다이얼로그 종료
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    @Override // TODO 에러발생
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            // GPS 설정 페이지로 이동하는 인텐트의 결과
            case GPS_ENABLE_REQUEST_CODE:

                // 사용자가 GPS 활성 시켰을 경우
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        needRequest = true;
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public String getCurrentAddress(LatLng latlng) {

        // 위도, 경도 값을 주소로 변환하기 위한 Geocoder 설정
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            // 위도 경도 값 받아오기
            addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);

        } catch (IOException ioException) {
            //네트워크 문제
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            return "잘못된 GPS 좌표";

        }

        // 받아온 주소 값이 없을 경우
        if (addresses == null || addresses.size() == 0) {
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }
    }

    public void setDefaultLocation() {

        // 기본 위치 서울로 지정
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);

        String markerTitle = "위치정보 없음";
        String markerSnippet = "위치 접근 권한을 허용해주세요.";

        // 지도에 기본 위치 마커 표시하기
        currentMarker = googleMap.addMarker(new MarkerOptions()
                .position(DEFAULT_LOCATION)
                .title(markerTitle)
                .snippet(markerSnippet)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        if (currentMarker != null) currentMarker.remove();

        // 지도 위치 지정
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 16);
        googleMap.moveCamera(cameraUpdate);

    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        if (currentMarker != null) currentMarker.remove();

        // 현재 위치의 위도, 경도 값 받아오기
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // 현재 위치의 위도, 경도 값으로 지도 이동
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        googleMap.moveCamera(cameraUpdate);
    }

    private boolean checkPermissions() {
        int fineLocationPermissionState = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);

        int coarseLocationPermissionState = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION);

        return (fineLocationPermissionState == PackageManager.PERMISSION_GRANTED) &&
                (coarseLocationPermissionState == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {

        boolean permissionAccessFineLocationApproved =
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean coarseLocationPermissionApproved =
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

        boolean shouldProvideRationale =
                permissionAccessFineLocationApproved && coarseLocationPermissionApproved;

        // 위치정보 접근 퍼미션이 허용되지 않은 경우
        if (!shouldProvideRationale) {

            // 퍼미션 요청하기
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION);
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_LOCATION) {

            // 퍼미션을 허용했을 경우
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // 위치정보를 가져오는 FusedLocationProviderClient 설정
                locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                googleMap.setMyLocationEnabled(true);

            } else {
                Snackbar.make(coordinatorLayout_snackBarHolder, "지도를 사용하기 위해서는 \n위치 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("설정", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // 애플리케이션 ID 받아오기
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);

                                // ID를 바탕으로 앱의 위치정보 설정 페이지로 이동하는 인텐트 설정
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
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

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // 받아올 위치정보의 개수만큼 반복
                for (noman.googleplaces.Place place : places) {

                    // 위도, 경도 값 받아오기
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());

                    // 마커 스니펫에 넣을 위도, 경도 값 지정
                    String markerSnippet = getCurrentAddress(latLng);

                    // 각 위치정보에 대한 마커 설정
                    Marker item = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .snippet(markerSnippet));

                    previous_marker.add(item);
                }

                // 마커 정보를 저장할 해쉬셋 설정 후 받아온 마커 값 추가
                HashSet<Marker> hashSet = new HashSet<>();
                hashSet.addAll(previous_marker);

                // 해쉬셋에 마커 값 추가 후 마커 값 초기화
                previous_marker.clear();

                // 초기화 후 마커값 업데이트
                previous_marker.addAll(hashSet);
            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    public void showPlaceInformation(LatLng location) {

        // 지도 초기화
        googleMap.clear();

        // 받아올 마커 정보가 있을 경우 마커값 초기화
        if (previous_marker != null)
            previous_marker.clear();

        // 가져올 장소 정보 설정
        new NRPlaces.Builder()
                .listener(MapActivity.this)
                .key("AIzaSyAnd_XZyiiRYucd6_cnj0fwK7vUPRHlA5g")
                .latlng(location.latitude, location.longitude) // 현재 위치
                .radius(500) // 반경 500 미터 내에서 검색
                .type(PlaceType.RESTAURANT) // 음식점
                .build()
                .execute();
    }
}