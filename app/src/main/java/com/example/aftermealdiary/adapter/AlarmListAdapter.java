package com.example.aftermealdiary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aftermealdiary.AlarmData;
import com.example.aftermealdiary.R;

import java.util.ArrayList;

public class AlarmListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<AlarmData> alarmDataArrayList;


    public AlarmListAdapter(Context context, ArrayList<AlarmData> alarmDataArrayList) {
        this.context = context;
        this.alarmDataArrayList = alarmDataArrayList;
    }

    @Override
    public int getCount() {
        return alarmDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_alarmlist, null);

        // 레이아웃 리소스 설정
        TextView textView_hour = view.findViewById(R.id.textView_hour);
        TextView textView_minute = view.findViewById(R.id.textView_minute);
        TextView textView_alarmInfo = view.findViewById(R.id.textView_alarmInfo);

        // Alarm 객체에서 불러온 정보를 각 뷰에 넣기
        textView_hour.setText(alarmDataArrayList.get(position).getAlarmHour());
        textView_minute.setText(alarmDataArrayList.get(position).getAlarmMinute());
        textView_alarmInfo.setText(alarmDataArrayList.get(position).getAlarmInfo());

        return view;
    }
}
