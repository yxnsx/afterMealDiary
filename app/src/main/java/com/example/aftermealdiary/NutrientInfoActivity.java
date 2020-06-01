package com.example.aftermealdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class NutrientInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Button imageButton_backArrow;
    ListView listview_nutrientInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrient_info);

        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        listview_nutrientInfo = findViewById(R.id.listview_nutrientInfo);

        imageButton_backArrow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageButton_backArrow:
                onBackPressed();
        }

    }
}
