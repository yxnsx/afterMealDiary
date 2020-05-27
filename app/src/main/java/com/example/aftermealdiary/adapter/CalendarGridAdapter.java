package com.example.aftermealdiary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.aftermealdiary.R;
import com.example.aftermealdiary.item.PostData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarGridAdapter extends ArrayAdapter {

    ArrayList<PostData> postDataArrayList;
    ArrayList<Date> dateArrayList;;
    Calendar calendar;
    LayoutInflater inflater;

    TextView textView_date;

    public CalendarGridAdapter(@NonNull Context context, ArrayList<Date> dateArrayList, Calendar calendar, ArrayList<PostData> postDataArrayList) {
        super(context, R.layout.item_calendarcell);

        this.dateArrayList = dateArrayList;
        this.calendar = calendar;
        this.postDataArrayList = postDataArrayList;
        inflater = LayoutInflater.from(getContext());
        Log.d("디버깅", "CalendarGridAdapter - CalendarGridAdapter(): ");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date date = dateArrayList.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);

        int dayNumber = dateCalendar.get(Calendar.DAY_OF_MONTH);

        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.MONTH);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_calendarcell, parent, false);

            textView_date = convertView.findViewById(R.id.textView_date);
            textView_date.setText(String.valueOf(dayNumber));

            Log.d("디버깅", "CalendarGridAdapter - getView(): convertView == null");

        } else {

        }

        if ((displayMonth == currentMonth) && (displayYear == currentYear)) {
            textView_date.setTextColor(getContext().getResources().getColor(R.color.colorWhite));

        } else {
            textView_date.setTextColor(getContext().getResources().getColor(R.color.colorTransparentWhite));

        }


        Log.d("디버깅", "CalendarGridAdapter - getView(): " + dayNumber);
        Log.d("디버깅", "CalendarGridAdapter - getView(): " + textView_date);

        return convertView;
    }

    @Override
    public int getCount() {
        return dateArrayList.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dateArrayList.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dateArrayList.get(position);
    }
}
