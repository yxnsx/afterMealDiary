package com.example.aftermealdiary;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;


public class AlarmData {

    private String alarmHour;
    private String alarmMinute;
    private String alarmInfo;

    private static int amountOfAlarm;
    private static String stringForSplit = "@@@###&&&";

    private static SharedPreferences alarmSharedPreferences;

    AlarmData(String alarmHour, String alarmMinute, String alarmInfo) {
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;
        this.alarmInfo = alarmInfo;
    }

    static void updateAlarmSharedPreferences(Context context, ArrayList<AlarmData> alarmDataArrayList) {

        // SharedPreferences 객체, 에디터 생성
        alarmSharedPreferences = context.getSharedPreferences("ALARM_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = alarmSharedPreferences.edit();

        // arrayList의 아이템 개수만큼
        for (int i = 0; i < alarmDataArrayList.size(); i++) {

            // SharedPreferences에 값 담기 (키값 = alarmData + i)
            editor.putString("alarmData" + i, convertArrayListToString(alarmDataArrayList.get(i)));
        }

        // amountOfAlarm 값은 getAlarmArrayListFromSharedPreferences()에서 사용 - amountOfAlarm 만큼 for문 반복
        editor.putInt("amountOfAlarm", alarmDataArrayList.size());
        editor.apply();
        editor.commit();
    }

    static ArrayList<AlarmData> getAlarmArrayListFromSharedPreferences(Context context) {

        // SharedPreferences 객체 생성
        alarmSharedPreferences = context.getSharedPreferences("ALARM_DATA", Context.MODE_PRIVATE);
        ArrayList<AlarmData> alarmDataArrayList = new ArrayList<>();

        // SharedPreferences에 저장된 알람 개수만큼 반복
        for (amountOfAlarm = 0; amountOfAlarm < alarmSharedPreferences.getInt("amountOfAlarm", 0); amountOfAlarm++) {

            // string 값 꺼내서 객체로 변환 후 alarmDataArrayList에 담기
            String alarmDataString = alarmSharedPreferences.getString("alarmData" + amountOfAlarm, "");
            alarmDataArrayList.add(convertStringToObject(alarmDataString));
        }

        return alarmDataArrayList;
    }

    private static String convertArrayListToString(AlarmData alarmData) {

        // 각 알람 객체를 담을 어레이리스트 생성
        ArrayList<String> stringAlarmData = new ArrayList<>();

        // 어레이리스트에 각 객체의 데이터 담기
        stringAlarmData.add(alarmData.getAlarmHour()); // 알람 시간
        stringAlarmData.add(alarmData.getAlarmMinute()); // 알람 분
        stringAlarmData.add(alarmData.getAlarmInfo()); // 알람 정보

        // 스트링빌더 통해서 배열을 스트링으로 변환
        StringBuilder stringBuilder = new StringBuilder();

        // 배열의 각 값을 stringForSplit을 사이에 두고 구분해서 stringBuilder에 담기 (stringForSplit = "@@@###&&&")
        for (String string : stringAlarmData) {
            stringBuilder.append(string);
            stringBuilder.append(stringForSplit);
        }
        return stringBuilder.toString();
    }


    private static AlarmData convertStringToObject(String stringAlarmData) {

        // 넘어온 stringAlarmData를 stringForSplit 기준으로 구분하여 배열에 담기 (stringForSplit = "@@@###&&&")
        String[] alarmDataString = stringAlarmData.split(stringForSplit);

        // 배열에 담긴 순서대로 각 데이터 꺼내기
        String hour = alarmDataString[0];
        String minute = alarmDataString[1];
        String info = alarmDataString[2];

        // 꺼낸 데이터 바탕으로 새 알람 객체 리턴
        return new AlarmData(hour, minute, info);
    }

    public String getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(String alarmHour) {
        this.alarmHour = alarmHour;
    }

    public String getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmMinute(String alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public String getAlarmInfo() {
        return alarmInfo;
    }

    public void setAlarmInfo(String alarmInfo) {
        this.alarmInfo = alarmInfo;
    }
}
