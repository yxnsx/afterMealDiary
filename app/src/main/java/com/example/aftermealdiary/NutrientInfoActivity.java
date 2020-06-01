package com.example.aftermealdiary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class NutrientInfoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButton_backArrow;
    ListView listview_nutrientInfo;

    String key = "bfe27fe32dde406c8969";


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
