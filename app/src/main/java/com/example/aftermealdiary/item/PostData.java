package com.example.aftermealdiary.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostData implements Parcelable {

    private String postImage;
    private String postDate;
    private String postTitle;
    private String postText;
    private int index;

    private static SharedPreferences postSharedPreferences;


    public PostData(String postImage, String postDate, String postTitle, String postText) {
        this.postImage = postImage;
        this.postDate = postDate;
        this.postTitle = postTitle;
        this.postText = postText;
    }

    public PostData(String postImage, String postDate, String postTitle, String postText, int index) {
        this.postImage = postImage;
        this.postDate = postDate;
        this.postTitle = postTitle;
        this.postText = postText;
        this.index = index;
    }

    protected PostData(Parcel in) {
        postImage = in.readString();
        postDate = in.readString();
        postTitle = in.readString();
        postText = in.readString();
    }

    public static final Creator<PostData> CREATOR = new Creator<PostData>() {
        @Override
        public PostData createFromParcel(Parcel in) {
            return new PostData(in);
        }

        @Override
        public PostData[] newArray(int size) {
            return new PostData[size];
        }
    };

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postImage);
        dest.writeString(postDate);
        dest.writeString(postTitle);
        dest.writeString(postText);
    }

    public static ArrayList<PostData> getArrayListFromSharedPreferences(Context context) throws JSONException {

        // SharedPreferences 객체 생성
        postSharedPreferences = context.getSharedPreferences("POST_DATA", Context.MODE_PRIVATE);
        ArrayList<PostData> postDataArrayList = new ArrayList<>();

        // SharedPreferences에서 JSONArray 받아오기
        JSONArray postJSONArray = new JSONArray(postSharedPreferences.getString("postData", "0"));

        // 받아온 JSONArray에서 position 값으로 JSONObject 생성
        for (int i = 0; i < postJSONArray.length(); i++) {
            JSONObject postJSONObject = (JSONObject) postJSONArray.get(i);

            // JSONObject에서 값 꺼내서 postData 객체 생성
            String postImage = (String) postJSONObject.get("postImage");
            String postDate = (String) postJSONObject.get("postDate");
            String postTitle = (String) postJSONObject.get("postTitle");
            String postText = (String) postJSONObject.get("postText");

            // 생성한 postData 객체 postDataArrayList에 추가
            PostData postData = new PostData(postImage, postDate, postTitle, postText);
            postDataArrayList.add(postData);
        }

        return postDataArrayList;
    }

    public static ArrayList<PostData> getDateArrayFromSharedPreferences(Context context, String selectedDate) throws JSONException {

        // SharedPreferences 객체 생성
        postSharedPreferences = context.getSharedPreferences("POST_DATA", Context.MODE_PRIVATE);
        ArrayList<PostData> postDataForCalendar = new ArrayList<>();

        // SharedPreferences에서 JSONArray 받아오기
        JSONArray postJSONArray = new JSONArray(postSharedPreferences.getString("postData", "0"));

        // 받아온 JSONArray에서 position 값으로 JSONObject 생성
        for (int index = 0; index < postJSONArray.length(); index++) {
            JSONObject postJSONObject = (JSONObject) postJSONArray.get(index);

            // JSONObject에서 값 꺼내서 postData 객체 생성
            String postImage = (String) postJSONObject.get("postImage");
            String postDate = (String) postJSONObject.get("postDate");
            String postTitle = (String) postJSONObject.get("postTitle");
            String postText = (String) postJSONObject.get("postText");

            Log.d("디버깅", "PostData - getDateSharedPreferences(): " + selectedDate);
            Log.d("디버깅", "PostData - getDateSharedPreferences(): " + postDate);

            if(selectedDate.equals(postDate)) {
                PostData postData = new PostData(postImage, postDate, postTitle, postText, index);
                postDataForCalendar.add(postData);
            } else {
                Log.d("디버깅", "PostData - getDateSharedPreferences(): " + selectedDate + "에 해당하는 데이터가 없음");
            }
        }

        return postDataForCalendar;
    }
}
