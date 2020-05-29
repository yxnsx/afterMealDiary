package com.example.aftermealdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aftermealdiary.adapter.HomeListAdapter;
import com.example.aftermealdiary.item.BannerData;
import com.example.aftermealdiary.item.PostData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, HomeListAdapter.OnPostListener {

    Parcelable recyclerViewState;
    RecyclerView recyclerView;
    HomeListAdapter homeListAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<PostData> postDataArrayList;

    ConstraintLayout banner;
    ArrayList<BannerData> bannerDataArrayList;
    ImageView imageView_bannerBackground;
    ImageView imageView_bannerCircle;
    TextView textView_bannerTitle;
    Thread bannerThread;

    int index = 0;
    String bannerInfo;
    boolean isHome;
    public static final int SEND_BANNERDATA = 0;
    public static final int SEND_STOP = 1;

    Button button_home;
    Button button_calendar;
    Button button_setting;
    FloatingActionButton floatingActionButton_write;

    long pressedTime = 0;


    @Override // 레이아웃 리소스 파일 세팅과 변수 초기화 작업 수행
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("디버깅", "HomeActivity - onCreate(): ");

        // 레이아웃 리소스 설정
        banner = findViewById(R.id.banner);
        imageView_bannerBackground = findViewById(R.id.imageView_bannerBackground);
        imageView_bannerCircle = findViewById(R.id.imageView_bannerCircle);
        textView_bannerTitle = findViewById(R.id.textView_bannerTitle);

        button_home = findViewById(R.id.button_home);
        button_calendar = findViewById(R.id.button_calendar);
        button_setting = findViewById(R.id.button_setting);
        floatingActionButton_write = findViewById(R.id.floatingActionButton_write);

        // 클릭리스너 설정
        banner.setOnClickListener(this);
        button_home.setOnClickListener(this);
        button_calendar.setOnClickListener(this);
        button_setting.setOnClickListener(this);
        floatingActionButton_write.setOnClickListener(this);
    }


    @Override // 앱이 UI를 관리하는 코드 초기화
    protected void onStart() {

        super.onStart();
        Log.d("디버깅", "HomeActivity - onStart(): ");

    }


    // 포그라운드에서 사용자에게 보이는 동안 실행해야 하는 모든 기능을 활성화
    // onPause() 중에 해제하는 구성요소를 초기화 / Activity가 재개됨 상태로 전환될 때마다 필요한 다른 초기화 작업 수행
    @Override
    protected void onResume() {

        super.onResume();
        Log.d("디버깅", "HomeActivity - onResume(): ");

        // [리사이클러뷰] postDataArrayList 값 가져오기
        postDataArrayList = new ArrayList<>();

        try {
            postDataArrayList = PostData.getArrayListFromSharedPreferences(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // [리사이클러뷰] 레이아웃매니저, 어댑터 설정
        layoutManager = new LinearLayoutManager(this);
        homeListAdapter = new HomeListAdapter(postDataArrayList, this);

        // [리사이클러뷰] 리사이클러뷰 설정
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(homeListAdapter);
        recyclerView.setLayoutManager(layoutManager);

        // [리사이클러뷰] 스크롤 상태 불러오기
        if (recyclerViewState != null) {
            layoutManager.onRestoreInstanceState(recyclerViewState);
        } else {
            Log.d("디버깅", "HomeActivity - onResume(): 리사이클러뷰 스크롤 상태 null");
        }

        // 배너 핸들러 상태를 제어하기 위한 boolean 값
        isHome = true;

        // 배너에 사용할 배너 어레이리스트 값 설정
        setBannerArrayList();

        // 배너 스레드 시작
        bannerThread = new Thread();
        bannerThread.start();
    }


    // 구성요소가 포그라운드에 있지 않을 때 실행할 필요가 없는 기능을 모두 정지
    // 기능 정지
    @Override
    protected void onPause() {

        super.onPause();
        Log.d("디버깅", "HomeActivity - onPause(): ");

        // 핸들러 정지
        bannerHandler.sendEmptyMessage(SEND_STOP);
    }


    // 구성요소가 화면에 보이지 않을 때 실행할 필요가 없는 기능을 모두 정지
    // 데이터 저장
    @Override
    protected void onStop() {

        super.onStop();
        Log.d("디버깅", "HomeActivity - onStop(): ");
    }


    // 앱 전반적인 스레드 등 작업 종료
    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.d("디버깅", "HomeActivity - onDestroy(): ");
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.banner:

                // "요즘 뜨는 맛집들" 배너 클릭시
                if(bannerInfo.equals("search")){

                    // 네이버에서 '맛집' 키워드로 검색
                    Intent intentSearch = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?sm=mtp_hty.top&where=m&query=내+주변+맛집"));
                    startActivity(intentSearch);

                // "오늘 먹을 메뉴가 고민이라면" 배너 클릭시
                } else if (bannerInfo.equals("mealPicker")) {

                    // 메뉴 룰렛 액티비티로 이동
                    Intent intentMealPicker = new Intent(getApplicationContext(), MenuPickerActivity.class);
                    intentMealPicker.putExtra("intentFrom", "home");
                    startActivity(intentMealPicker);

                // "간단 요리 레시피" 배너 클릭시
                } else if (bannerInfo.equals("recipes")) {

                    // 네이버에서 '집에서 간단한 요리' 키워드로 검색
                    Intent intentRecipes = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.search.naver.com/search.naver?sm=mtb_sug.top&where=m&oquery=맛집&tqi=UV1dNlp0JxossK%2Fxm14ssssss5h-227868&query=집에서+간단한+요리&acq=간단한&acr=1&qdt=0"));
                    startActivity(intentRecipes);

                } else {
                    Log.d("디버깅", "HomeActivity - onClick(): banner error");
                }
                break;

            // 홈 버튼을 클릭했을 경우
            case R.id.button_home:
                Intent intentHome = new Intent(this, HomeActivity.class);
                startActivity(intentHome);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);
                break;

            // 캘린더 버튼을 클릭했을 경우
            case R.id.button_calendar:
                Intent intentCalendar = new Intent(this, CalendarActivity.class);
                startActivity(intentCalendar);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);

                bannerHandler.sendEmptyMessage(SEND_STOP);
                break;

            // 세팅 버튼을 클릭했을 경우
            case R.id.button_setting:
                Intent intentMypage = new Intent(this, SettingActivity.class);
                startActivity(intentMypage);
                // 화면 트랜지션 없도록 설정
                overridePendingTransition(0, 0);

                bannerHandler.sendEmptyMessage(SEND_STOP);
                break;

            // 글 작성하기 버튼을 클릭했을 경우
            case R.id.floatingActionButton_write:
                Log.d("디버깅", "HomeActivity - onClick(): 글 작성하기");

                Intent intentWrite = new Intent(this, WriteActivity.class);
                startActivity(intentWrite);

                bannerHandler.sendEmptyMessage(SEND_STOP);
                break;
        }
    }

    // onSaveInstanceState()를 통해 저장된 정보가 있을 때만 호출
    // onCreate()내의 savedInstanceState Bundle과 같지만 이 경우는 null 체크할 필요 없음
    // onStart()가 완료되었을 때 사용 가능함
    // 복구 생명 주기 : onCreate() -> onStart() -> onRestoreInstanceState() -> onResume()
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        // 저장된 리사이클러뷰 스크롤 상태 불러오기
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("recyclerViewState");
        } else {
            Log.d("디버깅", "HomeActivity - onRestoreInstanceState(): null");
        }
    }

    // 액티비티가 임시적으로 사라졌을 때 호출됨 - 인스턴스 상태 저장
    // 사용자가 명시적으로 액티비티를 종료하는 경우 또는 finish() 가 호출된 경우엔 호출되지 않음
    // 저장 생명 주기 : onPause() -> onSaveInstanceState() -> onStop() -> onDestroy()
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // 리사이클러뷰 스크롤 상태 저장
        recyclerViewState = layoutManager.onSaveInstanceState();
        outState.putParcelable("recyclerViewState", recyclerViewState);
    }

    @Override
    public void onPostClick(int position) {

        // 각 Post 객체 클릭시 상세보기로 이동
        Intent intentPost = new Intent(this, PostActivity.class);
        intentPost.putExtra("intentFrom", "home");
        intentPost.putExtra("selectedPost", postDataArrayList.get(position));
        intentPost.putExtra("position", position);
        startActivity(intentPost);

        Log.d("디버깅", "HomeActivity - onPostClick(): ");
    }

    @Override
    public void onBackPressed() {

        // 최초 pressedTime = 0
        if (System.currentTimeMillis() > pressedTime + 2000) {

            // 뒤로가기 버튼 누르면 현재 시간으로 pressedTime 재설정
            pressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르시면 앱이 종료됩니다", Toast.LENGTH_LONG).show();
            return;
        }
        // 재설정된 pressedTime + 2000이 현재시간보다 클 경우 앱 종료(2초 내에 다시 뒤로가기 버튼 눌렀을 경우)
        if (System.currentTimeMillis() <= pressedTime + 2000) {
            bannerHandler.sendEmptyMessage(SEND_STOP);
            finish();
        }
    }

    public void setBannerArrayList() {
        bannerDataArrayList = new ArrayList<>();

        bannerDataArrayList.add(new BannerData // 녹색, 주황색 배너
                (Color.parseColor("#003205"), Color.parseColor("#FF6000"), "요즘 뜨는 맛집들", "search"));
        bannerDataArrayList.add(new BannerData // 녹색, 노란색 배너
                (Color.parseColor("#003205"), Color.parseColor("#FFA700"), "오늘의 메뉴가 고민이라면", "mealPicker"));
        bannerDataArrayList.add(new BannerData // 녹색, 연두색 배너
                (Color.parseColor("#003205"), Color.parseColor("#B7C500"), "간단 요리 레시피", "recipes"));

        Log.d("디버깅", "HomeActivity - setBannerArrayList(): bannerArrayList.size() = " + bannerDataArrayList.size());
    }

    @SuppressLint("HandlerLeak")
    final Handler bannerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case SEND_BANNERDATA:

                    // Thread에서 변화하는 bannerCount 값 바탕으로 배너 리소스 설정
                    imageView_bannerBackground.setBackgroundColor
                            (bannerDataArrayList.get(index).getBackgroundColor());
                    imageView_bannerCircle.setColorFilter
                            (bannerDataArrayList.get(index).getCircleColor());
                    textView_bannerTitle.setText
                            (bannerDataArrayList.get(index).getTitle());
                    bannerInfo = bannerDataArrayList.get(index).getInfo();

                    break;

                case SEND_STOP:
                    bannerThread.stopThread();
                    break;

                default:
                    break;
            }
        }
    };

    class Thread extends java.lang.Thread {

        public void stopThread() {
            isHome = false;
        }

        @Override
        public void run() {
            super.run();

            while (isHome) {

                if (index < bannerDataArrayList.size()) {

                    // 메시지 객체 생성
                    Message message = bannerHandler.obtainMessage();

                    // 메시지 ID, 내용 설정 후 핸들러로 메시지 보내기
                    message.what = SEND_BANNERDATA;
                    message.obj = bannerDataArrayList.get(index);
                    bannerHandler.sendMessage(message);

                    try { // 3초에 한 번 배너 바뀌도록 설정
                        sleep(3000);
                        Log.d("디버깅", "Thread - run(): " + textView_bannerTitle.getText());
                        index++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else { // bannerCount를 0으로 초기화해서 다시 while문 돌 수 있도록 함
                    index = 0;
                }
            }
        }
    }
}
