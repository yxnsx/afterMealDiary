package com.example.aftermealdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aftermealdiary.item.PostData;

import java.util.ArrayList;


public class CalendarListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ImageButton imageButton_backArrow;
    ListView listView;
    TextView textView_calendarListDate;
    CalendarListAdapter calendarListAdapter;
    ArrayList<PostData> postDataForCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarlist);

        // 레이아웃 리소스 설정
        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        textView_calendarListDate = findViewById(R.id.textView_calendarListDate);
        listView = findViewById(R.id.listView);

        // 클릭리스너 설정
        imageButton_backArrow.setOnClickListener(this);
        listView.setOnItemClickListener(this);

        Log.d("디버깅", "CalendarListActivity - onCreate(): ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("디버깅", "CalendarListActivity - onStart(): ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("디버깅", "CalendarListActivity - onResume(): ");

        if (getIntent() != null) {
            // 캘린더로부터 인텐트 받아서 클릭한 날짜로 리스트의 헤더 텍스트 설정
            textView_calendarListDate.setText(getIntent().getStringExtra("selectedDate"));
            postDataForCalendar = getIntent().getParcelableArrayListExtra("selectedDatePost");

        } else {
            Log.d("디버깅", "PostActivity - onCreate(): getIntent == null");
        }

        // 리사이클러뷰 데이터 변동 반영 - 구조변경, 아이템변경 둘 다 포함
        calendarListAdapter = new CalendarListAdapter(getApplicationContext(), postDataForCalendar);
        calendarListAdapter.notifyDataSetChanged();

        listView.setAdapter(calendarListAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageButton_backArrow:
                onBackPressed();
                Log.d("디버깅", "CalendarListActivity - onClick(): 뒤로가기 클릭됨");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // 각 Post 객체 클릭시 상세보기로 이동
        Intent intentPost = new Intent(this, PostActivity.class);
        intentPost.putExtra("intentFrom", "calendarList");
        intentPost.putExtra("selectedPost", postDataForCalendar.get(position));
        intentPost.putExtra("position", position);
        startActivity(intentPost);
    }

    @Override
    public void onBackPressed() {
        Intent intentCalendar = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intentCalendar);

    }
}
