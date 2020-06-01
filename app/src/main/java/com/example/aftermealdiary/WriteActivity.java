package com.example.aftermealdiary;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.aftermealdiary.adapter.HomeListAdapter;
import com.example.aftermealdiary.item.PostData;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener, HomeListAdapter.OnPostListener {

    ImageButton imageButton_close;
    Button button_savePost;
    EditText editText_postTitle;
    EditText editText_postText;
    ImageView imageView_addImage;

    PostData postData;
    ArrayList<PostData> postDataArrayList;
    HomeListAdapter homeListAdapter;

    Date date = Calendar.getInstance().getTime();

    String postImage;
    String postDate;
    String postTitle;
    String postText;

    int REQUEST_ALBUM = 3;

    Uri imageUri;
    int PERMISSION_EXTERNAL_STORAGE = 10;

    // 레이아웃 리소스 파일 세팅과 변수 초기화 작업 수행
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        // 레이아웃 리소스
        imageButton_close = findViewById(R.id.imageButton_close);
        button_savePost = findViewById(R.id.button_savePost);
        editText_postTitle = findViewById(R.id.editText_title);
        editText_postText = findViewById(R.id.editText_bodyText);
        imageView_addImage = findViewById(R.id.imageView_addImage);

        // 클릭리스너 설정
        imageButton_close.setOnClickListener(this);
        button_savePost.setOnClickListener(this);
        imageView_addImage.setOnClickListener(this);

        postDataArrayList = new ArrayList<>();
        homeListAdapter = new HomeListAdapter(postDataArrayList, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            // 닫기 버튼 클릭시
            case R.id.imageButton_close:
                onBackPressed();
                break;

            // 이미지 추가 버튼 클릭시
            case R.id.imageView_addImage:

                // 퍼미션 요청 수락시
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    // 이미지 가져오는 암시적 인텐트 생성
                    // TODO ACTION_OPEN_DOCUMENT와 ACTION_GET_CONTENT과의 차이점 + ACTION_PICK은 사용하면 안되는 이유
                    Intent toAlbum = new Intent();
                    toAlbum.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    toAlbum.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(Intent.createChooser(toAlbum, "toAlbum"), REQUEST_ALBUM);

                    Log.d("디버깅", "WriteActivity - onClick(): 이미지 추가 버튼");

                } else { // 퍼미션 요청 거절시
                    // 퍼미션 재요청
                    requestExternalStoragePermission();
                }
                break;

            // 작성완료 클릭시
            case R.id.button_savePost:
                // Post 데이터 값 설정
                postImage = imageUri.toString(); // 별도 패스 추출 메소드 사용하지 않고 .toString으로 넘겨주니 이미지 뜸..
                postDate = new SimpleDateFormat("yy/MM/dd", Locale.getDefault()).format(date);
                postTitle = editText_postTitle.getText().toString();
                postText = editText_postText.getText().toString();

                // 새 Post 객체 생성
                postData = new PostData(postImage, postDate, postTitle, postText);

                // 생성한 Post 값 반영 (arrayList, sharedPreferences)
                try {
                    homeListAdapter.addPost(getApplicationContext(), postData);
                    homeListAdapter.updatePostSharedPreferences(this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 게시글 작성 후 홈 액티비티로 이동
                Intent intentPost = new Intent(getApplicationContext(), HomeActivity.class);
                /*intentPost.putExtra("selectedPost", homeListAdapter.getPost(0));
                intentPost.putExtra("position", 0);*/
                startActivity(intentPost);

                finish();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 앨범 접근 후 결과값 받아오기
        if (requestCode == REQUEST_ALBUM) {

            // 앨범에서 이미지 가져왔을 경우
            if (resultCode == RESULT_OK) {

                try {
                    // 데이터 꺼내고 이미지뷰에 넣기
                    imageUri = data.getData();
                    imageView_addImage.setImageURI(imageUri);

                    Log.d("디버깅", "WriteActivity - onActivityResult(): " + imageUri);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void requestExternalStoragePermission() {

        // 사용자가 이전에 요청을 거부한 경우 true를 반환하고 사용자가 권한을 거부하고 권한 요청 대화상자에서 다시 묻지 않음 옵션을 선택했거나 기기 정책상 이 권한을 금지하는 경우 false를 반환
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            // 다이얼로그 설정
            new AlertDialog.Builder(this)
                    .setTitle("접근 권한 설정")
                    .setMessage("사진을 첨부하기 위해서는 미디어 접근 권한이 필요합니다.")

                    // 권한 허용 버튼 클릭시
                    .setPositiveButton("허용", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 퍼미션 창 보여주기
                            ActivityCompat.requestPermissions(WriteActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_STORAGE);
                        }
                    })

                    // 권한 거부 버튼 클릭시
                    .setNegativeButton("거부", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 토스트 메시지 출력 후 다이얼로그 종료
                            Toast.makeText(WriteActivity.this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else { // TODO 이 부분 알아보기
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_EXTERNAL_STORAGE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // 퍼미션 체크 후 결과값 받아오기
        if (requestCode == PERMISSION_EXTERNAL_STORAGE) {
            // 권한 허용시
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한이 허용되었습니다", Toast.LENGTH_SHORT).show();

                // 이미지를 가져오는 암시적 인텐트
                Intent toAlbum = new Intent();
                toAlbum.setAction(Intent.ACTION_OPEN_DOCUMENT); // 에러 발생할 경우 ACTION_GET_CONTENT로 바꿔보기..
                toAlbum.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(Intent.createChooser(toAlbum, "toAlbum"), REQUEST_ALBUM);

            } else {
                Toast.makeText(this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // onSaveInstanceState()를 통해 저장된 정보가 있을 때만 호출
    // onCreate()내의 savedInstanceState Bundle과 같지만 이 경우는 null 체크할 필요 없음
    // onStart()가 완료되었을 때 사용 가능함
    // 복구 생명 주기 : onCreate() -> onStart() -> onRestoreInstanceState() -> onResume()
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // 작성 중이던 글 복구
        editText_postTitle.setText(savedInstanceState.getString(postTitle));
        editText_postText.setText(savedInstanceState.getString(postText));
    }

    // 액티비티가 임시적으로 사라졌을 때 호출됨 - 인스턴스 상태 저장
    // 사용자가 명시적으로 액티비티를 종료하는 경우 또는 finish() 가 호출된 경우엔 호출되지 않음
    // 저장 생명 주기 : onPause() -> onSaveInstanceState() -> onStop() -> onDestroy()
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // 작성 중이던 글 저장
        outState.putString(postText, String.valueOf(editText_postText.getText()));
        outState.putString(postTitle, String.valueOf(editText_postTitle.getText()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPostClick(int position) {

    }
}

