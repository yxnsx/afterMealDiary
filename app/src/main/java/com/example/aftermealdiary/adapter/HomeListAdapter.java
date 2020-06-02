package com.example.aftermealdiary.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aftermealdiary.R;
import com.example.aftermealdiary.item.PostData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.HomeRecyclerViewHolder> {

    private ArrayList<PostData> postDataArrayList;
    private OnPostListener onPostListener;


    public HomeListAdapter(ArrayList<PostData> postDataArrayList, OnPostListener onPostListener) {

        this.postDataArrayList = postDataArrayList;
        this.onPostListener = onPostListener;
    }

    @NonNull
    @Override
    public HomeListAdapter.HomeRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 뷰홀더 item_homelist 설정
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homelist, parent,false);
        return new HomeRecyclerViewHolder(view, onPostListener);
    }

    // onBindViewHolder()는 모든 아이템에 대해 호출되므로 매번 반복할 필요가 없는 clickListener 등은 구현을 지양하는게 좋음
    @Override
    public void onBindViewHolder(@NonNull final HomeListAdapter.HomeRecyclerViewHolder holder, int position) {

        // 뷰홀더 position에 맞는 Post 객체 불러오기
        PostData postDataItem = postDataArrayList.get(position);

        // Post 객체에서 불러온 정보 뷰에 반영
        holder.imageView_postImage.setImageURI(Uri.parse(postDataItem.getPostImage()));
        holder.textView_postDate.setText(postDataItem.getPostDate());
        holder.textView_postTitle.setText(postDataItem.getPostTitle());

        Log.d("디버깅", "HomeListAdapter - onBindViewHolder(): " + Uri.parse(postDataItem.getPostImage()));
    }

    @Override
    public int getItemCount() {
        return postDataArrayList.size();
    }


    public void addPost(Context context, PostData postData) throws JSONException {

        // sharedPreferences에 저장된 arrayList 값 불러오기
        postDataArrayList = PostData.getArrayListFromSharedPreferences(context);
        Log.d("디버깅", "HomeListAdapter - addPost(): postDataArrayList = " + postDataArrayList.size());
        // 추가한 데이터 값 반영
        postDataArrayList.add(0, postData);
        notifyItemInserted(0);
    }


    public void editPost(Context context, int position, PostData postData) throws JSONException {

        // sharedPreferences에 저장된 arrayList 값 불러오기
        postDataArrayList = PostData.getArrayListFromSharedPreferences(context);
        // 수정한 데이터 값 반영
        postDataArrayList.set(position, postData);
        notifyItemInserted(position);
    }

    public void removePost(Context context, int position) throws JSONException {

        // sharedPreferences에 저장된 arrayList 값 불러오기
        postDataArrayList = PostData.getArrayListFromSharedPreferences(context);
        // 삭제한 데이터 값 반영
        postDataArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public PostData getPost(int position){
        return postDataArrayList.get(position);
    }

    public void updatePostSharedPreferences(Context context) throws JSONException {
        // SharedPreferences 객체, 에디터 생성
        SharedPreferences postSharedPreferences = context.getSharedPreferences("POST_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = postSharedPreferences.edit();

        // arrayList 값을 담을 JSONArray 생성
        JSONArray postJSONArray = new JSONArray();

        // arrayList의 아이템 개수만큼
        for (int i = 0; i < postDataArrayList.size(); i++) {

            // JSONObject 생성
            JSONObject postJSONObject = new JSONObject();

            // 생성된 JSONObject에 각 배열의 값 넣기
            postJSONObject.put("postImage", postDataArrayList.get(i).getPostImage());
            postJSONObject.put("postDate", postDataArrayList.get(i).getPostDate());
            postJSONObject.put("postTitle", postDataArrayList.get(i).getPostTitle());
            postJSONObject.put("postText", postDataArrayList.get(i).getPostText());

            // JSONArray에 각 JSONObject 담기
            postJSONArray.put(postJSONObject);
        }

        editor.putString("postData", postJSONArray.toString());
        editor.apply();
        editor.commit();
    }


    // 뷰홀더 클래스 ----------------------------------------------------------------------------------------

    public class HomeRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView imageView_postImage;
        protected TextView textView_postDate;
        protected TextView textView_postTitle;
        OnPostListener postListener;


        public HomeRecyclerViewHolder(@NonNull View itemView, OnPostListener onPostListener) {
            super(itemView);

            // 레이아웃 리소스 설정
            imageView_postImage = itemView.findViewById(R.id.imageView_postImage);
            textView_postDate = itemView.findViewById(R.id.textView_postDate);
            textView_postTitle = itemView.findViewById(R.id.textView_postTitle);
            postListener = onPostListener;

            itemView.setOnClickListener(this);

            Log.d("디버깅", "HomeRecyclerViewHolder - HomeRecyclerViewHolder(): ");
        }

        @Override
        public void onClick(View v) {

            postListener.onPostClick(getAdapterPosition());
            Log.d("디버깅", "HomeRecyclerViewHolder - onClick(): " + getAdapterPosition());
        }
    }

    public interface OnPostListener {
        void onPostClick(int position);
    }
}