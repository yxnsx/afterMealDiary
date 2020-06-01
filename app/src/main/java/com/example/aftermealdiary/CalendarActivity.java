package com.example.aftermealdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aftermealdiary.adapter.CalendarAdapter;
import com.example.aftermealdiary.item.PostData;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarActivity extends AppCompatActivity implements View.OnClickListener, CompactCalendarView.CompactCalendarViewListener, AdapterView.OnItemClickListener {

    Button button_home;
    Button button_calendar;
    Button button_map;
    Button button_setting;
    ListView listView_calendar;

    CalendarAdapter calendarAdapter;
    ArrayList<PostData> postDataForCalendar;
    CompactCalendarView compactCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // 레이아웃 리소스
        button_home = findViewById(R.id.button_home);
        button_calendar = findViewById(R.id.button_calendar);
        button_map = findViewById(R.id.button_map);
        button_setting = findViewById(R.id.button_setting);
        listView_calendar = findViewById(R.id.listView_calendar);
        compactCalendarView = findViewById(R.id.compactCalendarView);

        // 클릭리스너 설정
        button_home.setOnClickListener(this);
        button_calendar.setOnClickListener(this);
        button_map.setOnClickListener(this);
        button_setting.setOnClickListener(this);
        listView_calendar.setOnItemClickListener(this);

        // 캘린더뷰 설정
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendarView.setListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        @SuppressLint("SimpleDateFormat")
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        String selectedDate = formatter.format(currentDate);
        Log.d("디버깅", "CalendarActivity - onResume(): selectedDate = " + selectedDate);

        // [리사이클러뷰] postDataArrayList 값 가져오기
        postDataForCalendar = new ArrayList<>();

        try {
            postDataForCalendar = PostData.getDateArrayFromSharedPreferences(getApplicationContext(), selectedDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 리스트뷰 데이터 변동 반영 - 구조변경, 아이템변경 둘 다 포함
        calendarAdapter = new CalendarAdapter(getApplicationContext(), postDataForCalendar);
        calendarAdapter.notifyDataSetChanged();

        listView_calendar.setAdapter(calendarAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onDayClick(Date dateClicked) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        String selectedDate = formatter.format(dateClicked);

        try {
            postDataForCalendar = PostData.getDateArrayFromSharedPreferences(getApplicationContext(), selectedDate);
            Log.d("디버깅", "CalendarActivity - onDayClick(): postDataForCalendar = " + postDataForCalendar.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // todo 리스트 어댑터.. 아이템.. 클릭시 어댑터에 값 보내주기..
        Log.d("디버깅", "CalendarActivity - onDayClick(): date = " + selectedDate);

    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intentPost = new Intent(this, PostActivity.class);
        intentPost.putExtra("intentFrom", "calendarList");
        intentPost.putExtra("selectedPost", postDataForCalendar.get(position));
        intentPost.putExtra("position", position);
        startActivity(intentPost);
    }
}
