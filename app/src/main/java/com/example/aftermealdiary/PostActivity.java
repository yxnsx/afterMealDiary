package com.example.aftermealdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aftermealdiary.adapter.HomeListAdapter;
import com.example.aftermealdiary.item.PostData;

import org.json.JSONException;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity implements View.OnClickListener, HomeListAdapter.OnPostListener {

    ImageButton imageButton_backArrow;
    Button button_popupMenu;

    ImageView imageView_postImage;
    TextView textView_postDate;
    TextView textView_postTitle;
    TextView textView_postText;

    int position;
    String intentFrom;
    Uri imageUri;

    int REQUEST_EDIT = 100;

    ArrayList<PostData> postDataArrayList;
    HomeListAdapter homeListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // 레이아웃 리소스 설정
        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        button_popupMenu = findViewById(R.id.button_popupMenu);

        imageView_postImage = findViewById(R.id.imageView_postImage);
        textView_postDate = findViewById(R.id.textView_postDate);
        textView_postTitle = findViewById(R.id.textView_postTitle);
        textView_postText = findViewById(R.id.textView_postText);

        // 클릭리스너 설정
        imageButton_backArrow.setOnClickListener(this);
        button_popupMenu.setOnClickListener(this);

        // 보여줄 내용 받아오기
        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", position);
            intentFrom = getIntent().getStringExtra("intentFrom");
            PostData getPostData = getIntent().getParcelableExtra("selectedPost");

            // String값으로 넘어온 이미지 정보를 URI 포맷으로 변환
            imageUri = Uri.parse(getPostData.getPostImage());

            // 받아온 데이터 레이아웃 리소스에 반영
            imageView_postImage.setImageURI(Uri.parse(getPostData.getPostImage()));
            textView_postDate.setText(getPostData.getPostDate());
            textView_postTitle.setText(getPostData.getPostTitle());
            textView_postText.setText(getPostData.getPostText());

        } else {
            Log.d("디버깅", "PostActivity - onCreate(): getIntent == null");
        }

        homeListAdapter = new HomeListAdapter(postDataArrayList, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 뒤로가기 버튼 클릭시
            case R.id.imageButton_backArrow:
                onBackPressed();
                break;

            // 팝업메뉴 버튼 클릭시
            case R.id.button_popupMenu:
                Log.d("디버깅", "PostActivity - onClick(): 팝업메뉴 클릭됨");

                // 팝업메뉴 생성
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.item_popupmenu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            // 수정하기를 클릭했을 경우
                            case R.id.popupMenu_edit:

                                // 넘겨줄 데이터 추출
                                String postImage = imageUri.toString();
                                String postDate = textView_postDate.getText().toString();
                                String postTitle = textView_postTitle.getText().toString();
                                String postText = textView_postText.getText().toString();

                                // 넘겨줄 데이터 인텐트에 담기
                                Intent editPost = new Intent(PostActivity.this, EditActivity.class);
                                editPost.putExtra("postImage", postImage);
                                editPost.putExtra("postDate", postDate);
                                editPost.putExtra("postTitle", postTitle);
                                editPost.putExtra("postText", postText);
                                editPost.putExtra("intentFrom", intentFrom);
                                startActivityForResult(editPost, REQUEST_EDIT);

                                break;

                            // 삭제하기를 클릭했을 경우
                            case R.id.popupMenu_delete:

                                // 삭제한 Post 값 반영 (arrayList, sharedPreferences)
                                try {
                                    homeListAdapter.removePost(getApplicationContext(), position);
                                    homeListAdapter.updatePostSharedPreferences(getApplicationContext());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d("디버깅", "PostActivity - onMenuItemClick(): " + position);

                                onBackPressed();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
        }
    }

    @Override // onResume 전에 실행
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 게시글 수정 후 결과값 받아오기
        if (requestCode == REQUEST_EDIT) {

            // 게시글이 수정 되었을 경우
            if (resultCode == RESULT_OK) {

                try {
                    // 수정된 결과값 꺼내기
                    String postImage = data.getStringExtra("postImage");
                    String postDate = data.getStringExtra("postDate");
                    String postTitle = data.getStringExtra("postTitle");
                    String postText = data.getStringExtra("postText");

                    // 꺼낸 결과값 레이아웃 리소스에 반영
                    imageView_postImage.setImageURI(Uri.parse(postImage));
                    textView_postDate.setText(postDate);
                    textView_postTitle.setText(postTitle);
                    textView_postText.setText(postText);

                    // 수정한 Post 값 반영 (arrayList, sharedPreferences)
                    PostData postData = new PostData(postImage, postDate, postTitle, postText);
                    postDataArrayList.set(position, postData);
                    homeListAdapter.notifyItemChanged(position);

                    Log.d("디버깅", "PostActivity - onActivityResult(): " + postDataArrayList.get(position).getPostText());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        } else {

        }
    }

    @Override
    public void onBackPressed() {

        // 넘어온 인텐트의 출처가 홈 액티비티인 경우
        if(intentFrom.equals("home")){

            // 홈으로 되돌아가는 인텐트 설정
            Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intentHome);
            Log.d("디버깅", "PostActivity - onBackPressed(): " + intentFrom);

        // 넘어온 인텐트의 출처가 캘린더리스트 액티비티인 경우
        } else if(intentFrom.equals("calendarList")) {

            // 캘린더 리스트로 되돌아가는 인텐트 설정
            Intent intentCalendarList = new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(intentCalendarList);
            Log.d("디버깅", "PostActivity - onBackPressed(): " + intentFrom);

        } else {
            Log.d("디버깅", "PostActivity - onMenuItemClick(): 예외 발생");
        }
    }

    @Override
    public void onPostClick(int position) {

    }
}
