package com.example.aftermealdiary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.aftermealdiary.item.ContactData;

import java.util.ArrayList;

public class MealMateActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButton_backArrow;
    ImageView imageView_image;
    TextView textView_mealMateInfo;
    TextView textView_contactInfo;
    TextView textView_additionalInfo;
    Button button_startPicker;
    Button button_stopPicker;
    LottieAnimationView lottie_confetti;

    Cursor cursor;
    Cursor contactCursor;
    ArrayList<ContactData> contactDataArrayList;

    MealMatePickerTask mealMatePickerTask;
    ContactsTask contactsTask;

    int index;
    boolean isRunning;
    int PERMISSION_CONTACTS = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealmate);

        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        imageView_image = findViewById(R.id.imageView_image);
        textView_mealMateInfo = findViewById(R.id.textView_mealMateInfo);
        textView_contactInfo = findViewById(R.id.textView_contactInfo);
        textView_additionalInfo = findViewById(R.id.textView_additionalInfo);
        button_startPicker = findViewById(R.id.button_startPicker);
        button_stopPicker = findViewById(R.id.button_stopPicker);
        lottie_confetti = findViewById(R.id.lottie_confetti);

        imageButton_backArrow.setOnClickListener(this);
        button_startPicker.setOnClickListener(this);
        button_stopPicker.setOnClickListener(this);

        // AsyncTask 상태를 제어하기 위한 boolean 값
        isRunning = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 퍼미션 요청 수락시
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            // 새로운 AsyncTask 생성 후 실행
            contactsTask = new ContactsTask();
            contactsTask.execute();

        } else { // 퍼미션 요청 거절시
            // 퍼미션 재요청
            requestContactsPermission();
            Log.d("디버깅", "MealMateActivity - onStart(): requestContactsPermission");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageButton_backArrow:
                onBackPressed();
                break;

            case R.id.button_startPicker:

                if (contactDataArrayList != null) {
                    // AsyncTask 상태를 제어하기 위한 boolean 값
                    isRunning = true;

                    // 새로운 AsyncTask 생성 후 실행
                    mealMatePickerTask = new MealMatePickerTask();
                    mealMatePickerTask.execute();
                } else {
                    Toast.makeText(this, "불러올 연락처 정보가 없습니다", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_stopPicker:
                // AsyncTask가 생성된 상태일 경우
                if (mealMatePickerTask != null) {
                    isRunning = false;
                    lottie_confetti.setAnimation("confetti.json");
                    lottie_confetti.playAnimation();
                    mealMatePickerTask.cancel(true);

                } else { // AsyncTask가 null일 경우
                    Toast.makeText(this, "룰렛 시작하기 버튼을 먼저 눌러주세요", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void requestContactsPermission() {

        // 사용자가 이전에 요청을 거부한 경우 true를 반환하고 사용자가 권한을 거부하고 권한 요청 대화상자에서 다시 묻지 않음 옵션을 선택했거나 기기 정책상 이 권한을 금지하는 경우 false를 반환
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

            // 다이얼로그 설정
            new AlertDialog.Builder(this)
                    .setTitle("접근 권한 설정")
                    .setMessage("식사 메이트 뽑기를 위해서는 연락처 접근 권한이 필요합니다.")

                    // 권한 허용 버튼 클릭시
                    .setPositiveButton("허용", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 퍼미션 창 보여주기
                            ActivityCompat.requestPermissions(MealMateActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_CONTACTS);
                        }
                    })

                    // 권한 거부 버튼 클릭시
                    .setNegativeButton("거부", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 토스트 메시지 출력 후 다이얼로그 종료
                            Toast.makeText(MealMateActivity.this, "권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else { // TODO 이 부분 알아보기
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_CONTACTS);
        }
    }

    private class ContactsTask extends AsyncTask<ArrayList, ContactData, ArrayList> {

        @Override
        protected void onPreExecute() {
            contactDataArrayList = new ArrayList<>();
        }

        @Override
        protected ArrayList doInBackground(ArrayList... arrayLists) {
            cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                if (contactCursor.moveToFirst()) {

                    String contact = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    ContactData contactData = new ContactData(name, contact);

                    Log.d("디버깅", "MealMateActivity - getContacts(): name = " + name);

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
            //imageView_menuImage.setBackground(values[0].getMenuIcon());
            textView_mealMateInfo.setText("오늘 식사는");
            textView_contactInfo.setText(values[0].getName() + "님과");
            textView_additionalInfo.setText("함께하면 어떨까요?");

        }

        @Override
        protected void onPostExecute(ContactData contactData) {
            super.onPostExecute(contactData);
        }
    }
}
