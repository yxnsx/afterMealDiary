package com.example.aftermealdiary.item;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.aftermealdiary.CalendarListActivity;
import com.example.aftermealdiary.R;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomCalendarView extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener {

    ImageButton imageButton_left;
    ImageButton imageButton_right;
    TextView textView_header;
    GridView gridView_calendar;

    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    CalendarGridAdapter gridAdapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yy", Locale.ENGLISH);
    SimpleDateFormat headerFormat = new SimpleDateFormat("yyyy/MM", Locale.ENGLISH);

    private static final int MAX_CALENDAR_DAYS = 42;

    ArrayList<Date> dateArrayList;
    ArrayList<PostData> postDataArrayList;
    ArrayList<PostData> postDataForCalendar;


    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeLayout();

        imageButton_left.setOnClickListener(this);
        imageButton_right.setOnClickListener(this);
        gridView_calendar.setOnItemClickListener(this);
    }

    private void initializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_calendarview, this);

        imageButton_left = view.findViewById(R.id.imageButton_left);
        imageButton_right = view.findViewById(R.id.imageButton_right);
        textView_header = view.findViewById(R.id.textView_header);
        gridView_calendar = view.findViewById(R.id.gridView_calendar);

        setCalendar();
    }

    private void setCalendar() {
        String currentMonth = headerFormat.format(calendar.getTime());

        textView_header.setText(currentMonth);

        dateArrayList = new ArrayList<>();
        dateArrayList.clear();

        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDay);

        for (int i = 0; i < MAX_CALENDAR_DAYS; i++) {
            dateArrayList.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridAdapter = new CalendarGridAdapter(context, dateArrayList, calendar, postDataArrayList);
        gridView_calendar.setAdapter(gridAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageButton_left:
                calendar.add(Calendar.MONTH, -1);
                setCalendar();
                break;

            case R.id.imageButton_right:
                calendar.add(Calendar.MONTH, +1);
                setCalendar();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String year = yearFormat.format(dateArrayList.get(position));
        String month = monthFormat.format(dateArrayList.get(position));
        String date = dateFormat.format(dateArrayList.get(position));

        String selectedDate = year + "/" + month + "/" + date;
        Log.d("디버깅", "CustomCalendarView - onItemClick(): " + selectedDate);

        try {
            postDataForCalendar = PostData.getDateArrayFromSharedPreferences(getContext(), selectedDate);
            Log.d("디버깅", "CustomCalendarView - onItemClick(): " + postDataForCalendar.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intentCalendarList = new Intent(v.getContext(), CalendarListActivity.class);
        intentCalendarList.putExtra("selectedDate", selectedDate);

        if(postDataForCalendar != null) {
            intentCalendarList.putParcelableArrayListExtra("selectedDatePost", postDataForCalendar);
        } else {
            Log.d("디버깅", "CustomCalendarView - onItemClick(): postDataForCalendar == null");
        }

        context.startActivity(intentCalendarList);
    }
}
