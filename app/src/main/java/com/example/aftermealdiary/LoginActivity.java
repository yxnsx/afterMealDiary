package com.example.aftermealdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    EditText editText_email, editText_password;
    Button button_login;
    TextView textView_signUp;

    String signUpEmail;
    String signUpPassword;
    String inputEmail;
    String inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(null, "LoginActivity - onCreate(): ");

        // 사용될 id 선언
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        button_login = findViewById(R.id.button_login);
        textView_signUp = findViewById(R.id.textView_signUp);

        // 이메일 입력 값 반영
        editText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    inputEmail = s.toString();
                    button_login.setClickable(validation());

                    Log.d("텍스트 입력 ", inputEmail + " / " + inputPassword);

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 패스워드 입력값 반영
        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    inputPassword = s.toString();
                    button_login.setClickable(validation());

                    Log.d("텍스트 입력 ", inputEmail + " / " + inputPassword);

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 로그인 버튼이 동작하지 않도록 디폴트 설정
        button_login.setClickable(false);

        // 로그인 버튼 클릭리스너 설정
        button_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((inputEmail != null) && (inputPassword != null)){
                    inputEmail = editText_email.getText().toString();
                    inputPassword = editText_password.getText().toString();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("email", inputEmail);
                    intent.putExtra("password", inputPassword);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 계정 생성하기 클릭리스너 설정
        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    protected void onStart() {
        super.onStart();
        Log.d(null, "LoginActivity - onStart(): ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(null, "LoginActivity - onResume(): ");

        // 계정 생성하기 후 SignUpActivity 클래스에서 값 받아오기
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            signUpEmail = bundle.getString("signUpEmail");
            signUpPassword = bundle.getString("signUpPassword");
        }
    }

    protected void onPause() {
        super.onPause();
        Log.d(null, "LoginActivity - onPause(): ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(null, "LoginActivity - onStop(): ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(null, "LoginActivity - onDestroy(): ");
    }

    // 사용자의 입력값과 계정 생성하기에서 생성한 값 비교를 통해 클릭 가능 여부 결정
    private boolean validation() {
        if ((inputEmail != null) && inputPassword != null) {
            return (inputEmail.equals(signUpEmail)) && (inputPassword.equals(signUpPassword));
        }
        else {
            return false;
        }
    }
}


