package com.example.aftermealdiary;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    int index;
    boolean isRunning;


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

        imageButton_backArrow.setOnClickListener(this);
        button_startPicker.setOnClickListener(this);
        button_stopPicker.setOnClickListener(this);

        // AsyncTask 상태를 제어하기 위한 boolean 값
        isRunning = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageButton_backArrow:
                onBackPressed();
                break;

            case R.id.button_startPicker:
                // AsyncTask 상태를 제어하기 위한 boolean 값
                isRunning = true;

                // 새로운 AsyncTask 생성 후 실행
                mealMatePickerTask = new MealMatePickerTask();
                mealMatePickerTask.execute();
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

    private void getContacts() {
        cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        contactDataArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

            contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
            if (contactCursor.moveToFirst()) {

                String contact = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                ContactData contactData = new ContactData(name, contact);
                contactDataArrayList.add(contactData);
            }

            contactCursor.close();
        }
        cursor.close();
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

        @Override // publishProgress()의 값이 넘어옴
        protected void onProgressUpdate(ContactData... values) {
            super.onProgressUpdate(values);

            /*// 얻은 데이터 바탕으로 레이아웃 리소스 재설정
            imageView_menuImage.setBackground(values[0].getMenuIcon());
            textView_menuPickerInfo.setText(values[0].getPickerInfo());
            textView_menuInfo.setText(values[0].getMenuName());
            textView_additionalInfo.setText(values[0].getAdditionalInfo());*/

        }

        @Override
        protected void onPostExecute(ContactData contactData) {
            super.onPostExecute(contactData);
        }
    }
}
