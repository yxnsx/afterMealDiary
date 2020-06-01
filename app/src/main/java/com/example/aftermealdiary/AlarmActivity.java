package com.example.aftermealdiary;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aftermealdiary.adapter.AlarmListAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    ImageButton imageButton_backArrow;
    TimePicker timePicker;
    EditText editText_alarmInfo;
    Button button_setAlarm;
    ListView listView;

    ArrayList<AlarmData> alarmDataArrayList;
    AlarmListAdapter alarmListAdapter;

    int alarmHour;
    int alarmMinute;
    String alarmInfo;
    int alarmIndex;


    @Override // 레이아웃 리소스 초기화
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // 레이아웃 리소스 설정
        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        timePicker = findViewById(R.id.timePicker);
        editText_alarmInfo = findViewById(R.id.editText_alarmInfo);
        button_setAlarm = findViewById(R.id.button_setAlarm);
        listView = findViewById(R.id.listView);

        // timePicker 24시간 뷰로 설정
        timePicker.setIs24HourView(true);

        // 클릭리스너, 아이템 롱클릭리스너 설정
        imageButton_backArrow.setOnClickListener(this);
        button_setAlarm.setOnClickListener(this);
        listView.setOnItemLongClickListener(this);

        Log.d("디버깅", "AlarmActivity - onCreate(): ");
    }


    @Override // 값 불러오기
    protected void onStart() {
        super.onStart();

        // [리스트뷰] 저장된 alarmDataArrayList 값 가져오기
        alarmDataArrayList = new ArrayList<>();
        alarmDataArrayList = AlarmData.getAlarmArrayListFromSharedPreferences(getApplicationContext());

        // [리스트뷰] 어댑터 설정
        alarmListAdapter = new AlarmListAdapter(getApplicationContext(), alarmDataArrayList);
        listView.setAdapter(alarmListAdapter);

        Log.d("디버깅", "AlarmActivity - onStart(): ");
    }


    @Override // 변동 반영
    protected void onResume() {
        super.onResume();

        // [리스트뷰] 데이터 변동 반영 - 구조변경, 아이템변경 둘 다 포함
        alarmListAdapter.notifyDataSetChanged();
        AlarmData.updateAlarmSharedPreferences(getApplicationContext(), alarmDataArrayList);

        Log.d("디버깅", "AlarmActivity - onResume(): ");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageButton_backArrow:
                onBackPressed();
                break;

            // 알람 저장하기 버튼 클릭시
            case R.id.button_setAlarm:

                // 알람 데이터 얻기 (시, 분)
                if (Build.VERSION.SDK_INT >= 23) {
                    alarmHour = timePicker.getHour();
                    alarmMinute = timePicker.getMinute();

                } else {
                    alarmHour = timePicker.getCurrentHour();
                    alarmMinute = timePicker.getCurrentMinute();
                }
                // 토스트 메시지 출력
                Toast.makeText(getApplicationContext(), alarmHour + "시 " + alarmMinute + "분에 식사 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

                // 알람 데이터 얻기 (알람정보) + editText_alarmInfo 초기화
                alarmInfo = editText_alarmInfo.getText().toString();
                editText_alarmInfo.setText("");

                // 얻은 데이터 바탕으로 새 알람데이터 객체 생성
                AlarmData alarmData = new AlarmData(Integer.toString(alarmHour), Integer.toString(alarmMinute), alarmInfo);

                // alarmDataArrayList에 알람 추가
                alarmDataArrayList.add(0, alarmData);

                // [리스트뷰] 어댑터, sharedPreferences 갱신
                alarmListAdapter.notifyDataSetChanged();
                AlarmData.updateAlarmSharedPreferences(getApplicationContext(), alarmDataArrayList);

                // 갱신한 데이터 바탕으로 알람 업데이트
                updateAlarmManager();

                // 알람 저장 후 키보드 숨기기
                hideKeyboard(this);

                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        // 삭제 여부 다이얼로그 설정
        new AlertDialog.Builder(this)
                .setTitle("식사 알람 삭제")
                .setMessage("알람을 삭제하시겠습니까?")

                // 예 버튼 클릭시
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // alarmDataArrayList에서 알람 삭제
                        alarmDataArrayList.remove(position);

                        // [리스트뷰] 어댑터, sharedPreferences 갱신
                        alarmListAdapter.notifyDataSetChanged();
                        AlarmData.updateAlarmSharedPreferences(getApplicationContext(), alarmDataArrayList);

                        // 다이얼로그 종료 후 토스트 메시지 출력
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "알람이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    }
                })

                // 아니오 버튼 클릭시
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 다이얼로그 종료
                        dialog.dismiss();
                    }
                })
                .create().show();
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateAlarmManager() {

        // sharedPreferences에서 alarmDataArrayList 가져오기
        alarmDataArrayList = AlarmData.getAlarmArrayListFromSharedPreferences(getApplicationContext());
        Log.d("디버깅", "AlarmActivity - updateAlarm(): alarmDataArrayList == " + alarmDataArrayList.size());

        for (int i = 0; i < alarmDataArrayList.size(); i++) {

            // alarmDataArrayList에 저장된 알람 데이터 꺼내오기
            AlarmData alarmData = alarmDataArrayList.get(i);
            alarmHour = Integer.parseInt(alarmData.getAlarmHour());
            alarmMinute = Integer.parseInt(alarmData.getAlarmMinute());
            alarmInfo = alarmData.getAlarmInfo();
            alarmIndex = i;

            // 꺼내온 값 바탕으로 알람 시간 설정
            Calendar alarmTime = Calendar.getInstance();
            alarmTime.setTimeInMillis(System.currentTimeMillis());
            alarmTime.set(Calendar.HOUR_OF_DAY, alarmHour);
            alarmTime.set(Calendar.MINUTE, alarmMinute);
            alarmTime.set(Calendar.SECOND, 0);

            // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
            if (alarmTime.before(Calendar.getInstance())) {
                alarmTime.add(Calendar.DATE, 1);
            }

            // 알람매니저 설정
            setAlarmManager(alarmTime, alarmIndex);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void setAlarmManager(Calendar alarmTime, int alarmIndex) {

        // AlarmNotification 인텐트 설정
        Intent alarmIntent = new Intent(this, AlarmNotification.class);

        // AlarmManager를 통해 지정된 시간에 인텐트가 시작되도록 하기 위해 사용
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmIndex, alarmIntent, 0);

        // 알람매니저 설정
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            // pendingIntent를 포함해서 알람매니저에 저장
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);

        } else {
            Log.d("디버깅", "AlarmActivity - setNotification(): alarmManager == null");
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


