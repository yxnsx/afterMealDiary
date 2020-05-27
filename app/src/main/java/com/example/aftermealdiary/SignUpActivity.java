package com.example.aftermealdiary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SignUpActivity extends AppCompatActivity {

    EditText editText_email, editText_password;
    Button button_signUp;

    String signUpEmail;
    String signUpPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
        button_signUp = findViewById(R.id.button_signUp);
        button_signUp.setClickable(false);

        editText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    signUpEmail = s.toString();
                    button_signUp.setClickable(validation());

                    Log.d("텍스트 입력 ", signUpEmail + " / " + signUpPassword);

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    signUpPassword = s.toString();
                    button_signUp.setClickable(validation());

                    Log.d("텍스트 입력 ", signUpEmail + " / " + signUpPassword);

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button_signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signUpEmail = editText_email.getText().toString();
                signUpPassword = editText_password.getText().toString();

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.putExtra("signUpEmail", signUpEmail);
                intent.putExtra("signUpPassword", signUpPassword);
                startActivity(intent);
                Toast.makeText(SignUpActivity.this, "계정이 생성되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean validation() {
        if (signUpEmail != null && signUpPassword != null) {
            return true;
        }
        else {
            return false;
        }
    }
}


