package com.example.aftermealdiary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aftermealdiary.R;
import com.example.aftermealdiary.item.PostData;

import java.util.ArrayList;

public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PostData> postDataForCalendar;


    public CalendarAdapter(Context context, ArrayList<PostData> postDataForCalendar) {
        this.context = context;
        this.postDataForCalendar = postDataForCalendar;
    }

    @Override
    public int getCount() {
        return postDataForCalendar.size();
    }

    @Override
    public Object getItem(int position) {
        return postDataForCalendar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_calendarlist, null);

        // 레이아웃 리소스 설정
        ImageView imageView_postImage = view.findViewById(R.id.imageView_postImage);
        TextView textView_postDate = view.findViewById(R.id.textView_postDate);
        TextView textView_title = view.findViewById(R.id.textView_postTitle);

        // Post 객체에서 불러온 정보를 각 뷰에 넣기
        imageView_postImage.setImageURI(Uri.parse(postDataForCalendar.get(position).getPostImage()));
        textView_postDate.setText(postDataForCalendar.get(position).getPostDate());
        textView_title.setText(postDataForCalendar.get(position).getPostTitle());

        return view;
    }
}
