package com.example.aftermealdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.aftermealdiary.item.MenuData;

import java.util.ArrayList;

public class MenuPickerActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButton_backArrow;
    ImageView imageView_menuImage;
    Button button_startPicker;
    Button button_stopPicker;
    TextView textView_menuPickerInfo;
    TextView textView_menuInfo;
    TextView textView_additionalInfo;
    LottieAnimationView lottie_confetti;

    ArrayList<MenuData> menuDataArrayList;
    MenuPickerTask menuPickerTask;

    int index;
    boolean isRunning;
    String intentFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menupicker);

        // 레이아웃 리소스 설정
        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        imageView_menuImage = findViewById(R.id.imageView_menuImage);
        button_startPicker = findViewById(R.id.button_startPicker);
        button_stopPicker = findViewById(R.id.button_stopPicker);
        textView_menuPickerInfo = findViewById(R.id.textView_menuPickerInfo);
        textView_menuInfo = findViewById(R.id.textView_menuInfo);
        textView_additionalInfo = findViewById(R.id.textView_additionalInfo);
        lottie_confetti = findViewById(R.id.lottie_confettie);

        // 클릭리스너 설정
        imageButton_backArrow.setOnClickListener(this);
        button_startPicker.setOnClickListener(this);
        button_stopPicker.setOnClickListener(this);

        // AsyncTask 상태를 제어하기 위한 boolean 값
        isRunning = false;

        // 메뉴 룰렛에 사용할 메뉴 어레이리스트 값 설정
        setMenuDataArrayList();

        // 넘어온 인텐트 출처 받아오기
        if (getIntent() != null) {
            intentFrom = getIntent().getStringExtra("intentFrom");
        } else {
            Log.d("디버깅", "PostActivity - onCreate(): getIntent == null");
        }
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
                menuPickerTask = new MenuPickerTask();
                menuPickerTask.execute();
                break;

            case R.id.button_stopPicker:

                // AsyncTask가 생성된 상태일 경우
                if (menuPickerTask != null) {
                    isRunning = false;
                    lottie_confetti.setAnimation("confetti.json");
                    lottie_confetti.playAnimation();
                    menuPickerTask.cancel(true);

                } else { // AsyncTask가 null일 경우
                    Toast.makeText(this, "룰렛 시작하기 버튼을 먼저 눌러주세요", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                Log.d("디버깅", "MealPickerActivity - onClick(): default");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // AsyncTask가 생성된 상태일 경우
        if (menuPickerTask != null) {

            // AsyncTask 상태를 제어하기 위한 boolean 값
            isRunning = false;
            menuPickerTask.cancel(true);
        }

        // 넘어온 인텐트의 출처가 홈 액티비티인 경우
        if (intentFrom.equals("home")) {

            // 홈으로 되돌아가는 인텐트 설정
            Intent intentHome = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intentHome);

        // 넘어온 인텐트의 출처가 설정 액티비티인 경우
        } else if (intentFrom.equals("setting")) {

            // 설정으로 되돌아가는 인텐트 설정
            Intent intentMypage = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intentMypage);

        } else {
            Log.d("디버깅", "MenuPickerActivity - onBackPressed(): 예외 발생");
        }
    }

    public void setMenuDataArrayList() {
        menuDataArrayList = new ArrayList<>();

        // Drawable 이미지 가져오기
        Drawable icon_curry_128 = getResources().getDrawable(R.drawable.icon_curry_128);
        Drawable icon_hotpot_128 = getResources().getDrawable(R.drawable.icon_hotpot_128);
        Drawable icon_spaghetii_128 = getResources().getDrawable(R.drawable.icon_spaghetii_128);
        Drawable icon_hamburger_128 = getResources().getDrawable(R.drawable.icon_hamburger_128);
        Drawable icon_noodle_128 = getResources().getDrawable(R.drawable.icon_noodle_128);
        Drawable icon_sandwich_128 = getResources().getDrawable(R.drawable.icon_sandwich_128);
        Drawable icon_sushi_128 = getResources().getDrawable(R.drawable.icon_sushi_128);

        // menuDataArrayList에 각 MenuData 객체 추가
        menuDataArrayList.add(new MenuData(icon_curry_128, "오늘은", "든든한 덮밥류", "어떠신가요?"));
        menuDataArrayList.add(new MenuData(icon_hotpot_128, "오늘은", "든든한 찌개류", "어떠신가요?"));
        menuDataArrayList.add(new MenuData(icon_spaghetii_128, "오늘은", "든든한 스파게티", "어떠신가요?"));
        menuDataArrayList.add(new MenuData(icon_hamburger_128, "오늘은", "간단한 햄버거", "어떠신가요?"));
        menuDataArrayList.add(new MenuData(icon_noodle_128, "오늘은", "간단한 면류", "어떠신가요?"));
        menuDataArrayList.add(new MenuData(icon_sandwich_128, "오늘은", "간단한 샌드위치", "어떠신가요?"));
        menuDataArrayList.add(new MenuData(icon_sushi_128, "오늘은", "깔끔한 초밥", "어떠신가요?"));

        Log.d("디버깅", "MenuPickerActivity - setMenuArrayList(): menuDataArrayList.size() = " + menuDataArrayList.size());
    }

    @SuppressLint("StaticFieldLeak") // doInBackground, onProgressUpdate, onPostExecute의 매개변수 자료형
    private class MenuPickerTask extends AsyncTask<Integer, MenuData, MenuData> {

        @Override // 변수 초기화
        protected void onPreExecute() {
            index = 0;
        }

        @Override // 스레드의 백그라운드 작업 구현
        protected MenuData doInBackground(Integer... integers) {

            while (index < menuDataArrayList.size()) {

                try {
                    synchronized (this) {
                        // onProgressUpdate로 넘겨줄 값 설정
                        publishProgress(menuDataArrayList.get(index));

                        // 인덱스 값을 100 밀리세컨즈마다 증가시켜 보여지는 데이터가 바뀌도록 설정
                        index++;
                        this.wait(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (index == menuDataArrayList.size()) {
                    index = 0;
                }
                if (!isRunning) break;
            }
            return menuDataArrayList.get(index);
        }

        @Override // publishProgress()의 값이 넘어옴
        protected void onProgressUpdate(MenuData... values) {
            super.onProgressUpdate(values);

            // 얻은 데이터 바탕으로 레이아웃 리소스 재설정
            imageView_menuImage.setBackground(values[0].getMenuIcon());
            textView_menuPickerInfo.setText(values[0].getPickerInfo());
            textView_menuInfo.setText(values[0].getMenuName());
            textView_additionalInfo.setText(values[0].getAdditionalInfo());

            Log.d("디버깅", "MenuPickerTask - onProgressUpdate(): " + values[0].getMenuName());
        }

        @Override
        protected void onPostExecute(MenuData menuData) {
            super.onPostExecute(menuData);
        }
    }
}
