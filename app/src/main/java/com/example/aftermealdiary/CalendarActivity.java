package com.example.aftermealdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aftermealdiary.adapter.CalendarAdapter;
import com.example.aftermealdiary.item.PostData;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarActivity extends AppCompatActivity implements View.OnClickListener, CompactCalendarView.CompactCalendarViewListener, AdapterView.OnItemClickListener {

    TextView textView_calendarInfo;
    Button button_home;
    Button button_calendar;
    Button button_map;
    Button button_setting;
    ListView listView_calendar;

    Date currentDate;
    CalendarAdapter calendarAdapter;
    ArrayList<PostData> postDataForCalendar;
    ArrayList<PostData> postDataForEvent;
    CompactCalendarView compactCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // 레이아웃 리소스
        textView_calendarInfo = findViewById(R.id.textView_calendarInfo);
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
        compactCalendarView.setListener(this);
        compactCalendarView.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendarView.setUseThreeLetterAbbreviation(true); // 각 날짜에 점으로 포스트 개수 표시
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 현재 날짜 정보 가져오기
        currentDate = new Date();

        // 달력 상단의 월 표시에 출력할 날짜 포맷 지정
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat calendarInfoFormatter = new SimpleDateFormat("yyyy/MM");
        String calendarInfo = calendarInfoFormatter.format(currentDate);
        textView_calendarInfo.setText(calendarInfo);

        // 각 날짜에 해당하는 포스트 객체를 가져오기 위한 날짜 포맷 지정
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        String selectedDate = formatter.format(currentDate);

        // [리사이클러뷰] postDataArrayList 값 가져오기
        postDataForCalendar = new ArrayList<>();

        try {
            // 지정한 날짜 포맷에 해당하는 postDataArrayList 값 가져오기
            postDataForCalendar = PostData.getDateArrayFromSharedPreferences(getApplicationContext(), selectedDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 리스트뷰 데이터 변동 반영 - 구조변경, 아이템변경 둘 다 포함
        calendarAdapter = new CalendarAdapter(getApplicationContext(), postDataForCalendar);
        calendarAdapter.notifyDataSetChanged();
        listView_calendar.setAdapter(calendarAdapter);

        try {
            setPostEvents(); // 달력에 포스트 개수 표시
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

            // 지도 버튼을 클릭했을 경우
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

    public void setPostEvents() throws JSONException, ParseException {

        // 전체 포스트 데이터 가져오기
        postDataForEvent = PostData.getArrayListFromSharedPreferences(getApplicationContext());

        for (int i = 0; i < postDataForEvent.size(); i++) {
            PostData postData = postDataForEvent.get(i);

            // 이벤트 객체 생성을 위해 Date를 long으로 변환
            @SuppressLint("SimpleDateFormat")
            Date postDate = new SimpleDateFormat("yy/MM/dd").parse(postData.getPostDate());
            long postDateTime = postDate.getTime();

            // 이벤트 객체 생성 후 뷰에 추가
            Event postEvent = new Event(R.color.colorAccent, postDateTime, "");
            compactCalendarView.addEvent(postEvent);
        }
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    public void onDayClick(Date dateClicked) {

        // 선택한 날짜에 해당하는 포스트를 가져오기 위해 SimpleDateFormat을 String으로 변환
        SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        String selectedDate = formatter.format(dateClicked);

        try {
            // 선택한 날짜에 해당하는 포스트 어레이리스트 생성
            postDataForCalendar = PostData.getDateArrayFromSharedPreferences(getApplicationContext(), selectedDate);
            Log.d("디버깅", "CalendarActivity - onDayClick(): postDataForCalendar = " + postDataForCalendar.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 리스트뷰 데이터 변동 반영 - 구조변경, 아이템변경 둘 다 포함
        calendarAdapter = new CalendarAdapter(getApplicationContext(), postDataForCalendar);
        calendarAdapter.notifyDataSetChanged();
        listView_calendar.setAdapter(calendarAdapter);
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    public void onMonthScroll(Date dayOfMonth) {

        // 달력 상단의 월 표시에 출력할 날짜 포맷 지정
        SimpleDateFormat calendarInfoFormatter = new SimpleDateFormat("yyyy/MM");
        String calendarInfo = calendarInfoFormatter.format(dayOfMonth);
        textView_calendarInfo.setText(calendarInfo);

        onDayClick(dayOfMonth);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // 어떤 포스트 객체를 클릭했는지 알려주기 위한 인텐트 설정
        Intent intentPost = new Intent(this, PostActivity.class);
        intentPost.putExtra("intentFrom", "calendarList");
        intentPost.putExtra("selectedPost", postDataForCalendar.get(position));
        intentPost.putExtra("position", position);
        startActivity(intentPost);
    }
}
