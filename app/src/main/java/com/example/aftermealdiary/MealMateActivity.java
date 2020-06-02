package com.example.aftermealdiary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.aftermealdiary.item.ContactData;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MealMateActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButton_backArrow;
    ImageView imageView_image;
    TextView textView_mealMateInfo;
    TextView textView_contactInfo;
    TextView textView_additionalInfo;
    Button button_sendMessage;
    Button button_startPicker;
    Button button_stopPicker;
    LottieAnimationView lottie_confetti;
    CoordinatorLayout coordinatorLayout_snackBarHolder;

    Cursor cursor;
    Cursor contactCursor;
    ArrayList<ContactData> contactDataArrayList;

    MealMatePickerTask mealMatePickerTask;
    ContactsTask contactsTask;

    int index;
    boolean isRunning;
    String phoneNumber;
    int PERMISSION_CONTACTS = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealmate);

        // 레이아웃 리소스 설정
        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        imageView_image = findViewById(R.id.imageView_image);
        textView_mealMateInfo = findViewById(R.id.textView_mealMateInfo);
        textView_contactInfo = findViewById(R.id.textView_contactInfo);
        textView_additionalInfo = findViewById(R.id.textView_additionalInfo);
        button_sendMessage = findViewById(R.id.button_sendMessage);
        button_startPicker = findViewById(R.id.button_startPicker);
        button_stopPicker = findViewById(R.id.button_stopPicker);
        lottie_confetti = findViewById(R.id.lottie_confetti);
        coordinatorLayout_snackBarHolder = findViewById(R.id.coordinatorLayout_snackBarHolder);

        // 클릭리스너 설정
        imageButton_backArrow.setOnClickListener(this);
        button_sendMessage.setOnClickListener(this);
        button_startPicker.setOnClickListener(this);
        button_stopPicker.setOnClickListener(this);

        // AsyncTask 상태를 제어하기 위한 boolean 값 설정
        isRunning = false;

        // 메시지 보내기 버튼 안보이도록 설정
        button_sendMessage.setVisibility(View.GONE);

        // 연락처 접근 퍼미션 요청 수락시
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // 연락처 가져오는 AsyncTask 생성 후 실행
            contactsTask = new ContactsTask();
            contactsTask.execute();

        } else { // 퍼미션 요청 거절시
            // 퍼미션 재요청
            ActivityCompat.requestPermissions(MealMateActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_CONTACTS);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 뒤로가기 버튼 클릭시
            case R.id.imageButton_backArrow:
                onBackPressed();
                break;

            // 룰렛 시작하기 버튼 클릭시
            case R.id.button_startPicker:

                // 연락처 값이 null이 아닐 경우
                if (contactDataArrayList != null) {

                    // AsyncTask 상태를 제어하기 위한 boolean 값
                    isRunning = true;

                    // 식사 메이트를 뽑는 AsyncTask 생성 후 실행
                    mealMatePickerTask = new MealMatePickerTask();
                    mealMatePickerTask.execute();

                    // 메시지 보내기 버튼 안보이도록 설정
                    button_sendMessage.setVisibility(View.GONE);

                } else {
                    Toast.makeText(this, "불러올 연락처 정보가 없습니다", Toast.LENGTH_SHORT).show();
                }
                break;

            // 룰렛 멈추기 버튼 클릭시
            case R.id.button_stopPicker:

                // AsyncTask가 생성된 상태일 경우
                if (mealMatePickerTask != null) {

                    // AsyncTask 상태를 제어하기 위한 boolean 값
                    isRunning = false;

                    // 식사 메이트를 뽑는 AsyncTask 중지
                    mealMatePickerTask.cancel(true);

                    // 애니메이션 재생
                    lottie_confetti.setAnimation("confetti.json");
                    lottie_confetti.playAnimation();

                    // 메시지 보내기 버튼 보이도록 설정
                    button_sendMessage.setVisibility(View.VISIBLE);

                } else { // AsyncTask가 null일 경우
                    Toast.makeText(this, "룰렛 시작하기 버튼을 먼저 눌러주세요", Toast.LENGTH_SHORT).show();
                }
                break;

            // 문자로 연락하기 버튼 클릭시
            case R.id.button_sendMessage:

                // 해당 연락처로 문자 보내는 인텐트 설정
                Uri uri = Uri.parse("smsto:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_CONTACTS) {

            // 퍼미션을 허용했을 경우
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // 연락처 가져오는 AsyncTask 생성 후 실행
                contactsTask = new ContactsTask();
                contactsTask.execute();

            } else { // 퍼미션을 거절했을 경우
                Snackbar.make( // 스낵바 생성
                        coordinatorLayout_snackBarHolder,
                        "식사 메이트 뽑기를 위해서는 \n연락처 접근 권한이 필요합니다",
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("설정", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // 앱 설정으로 이동하는 인텐트 설정
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }

        }
    }

    private class ContactsTask extends AsyncTask<ArrayList, ContactData, ArrayList> {

        @Override
        protected void onPreExecute() {
            contactDataArrayList = new ArrayList<>();
        }

        @Override
        protected ArrayList doInBackground(ArrayList... arrayLists) {

            // 연락처를 탐색해서 가져오는 커서 설정
            cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

            while (cursor.moveToNext()) {
                // id값 가져오기
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                // 가져온 id값을 바탕으로 연락처 가져오기
                contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                if (contactCursor.moveToFirst()) {

                    // 이름과 연락처 가져와서 새 ContactData 객체 생성
                    String name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String contact = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ContactData contactData = new ContactData(name, contact);

                    // 생성한 contactData 객체를 어레이리스트에 추가
                    contactDataArrayList.add(contactData);
                }
                contactCursor.close();
            }
            cursor.close();
            return contactDataArrayList;
        }
    }

    @SuppressLint("StaticFieldLeak") // doInBackground, onProgressUpdate, onPostExecute의 매개변수 자료형
    private class MealMatePickerTask extends AsyncTask<Integer, ContactData, ContactData> {

        @Override // 변수 초기화
        protected void onPreExecute() {
            index = 0;
        }

        @Override // 스레드의 백그라운드 작업 구현
        protected ContactData doInBackground(Integer... integers) {

            // contactDataArrayList의 크기만큼 반복
            while (index < contactDataArrayList.size()) {

                try {
                    synchronized (this) {
                        // onProgressUpdate로 넘겨줄 값 설정
                        publishProgress(contactDataArrayList.get(index));

                        // 인덱스 값을 100 밀리세컨즈마다 증가시켜 보여지는 데이터가 바뀌도록 설정
                        index++;
                        this.wait(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // index값이 contactDataArrayList 최대값과 같다면 0으로 초기화해서 다시 while문을 돌 수 있도록 함
                if (index == contactDataArrayList.size()) {
                    index = 0;
                }
                if (!isRunning) break;
            }
            return contactDataArrayList.get(index);
        }

        @SuppressLint("SetTextI18n")
        @Override // publishProgress()의 값이 넘어옴
        protected void onProgressUpdate(ContactData... values) {
            super.onProgressUpdate(values);

            // 얻은 데이터 바탕으로 레이아웃 리소스 재설정
            textView_mealMateInfo.setText("오늘 식사는");
            textView_contactInfo.setText(values[0].getName() + " 님과");
            textView_additionalInfo.setText("함께하면 어떨까요?");

            // 문자로 연락하기 버튼으로 넘겨줄 연락처 정보 추출
            phoneNumber = values[0].getPhoneNumber();

        }

        @Override
        protected void onPostExecute(ContactData contactData) {
           super.onPostExecute(contactData);
        }
    }
}
