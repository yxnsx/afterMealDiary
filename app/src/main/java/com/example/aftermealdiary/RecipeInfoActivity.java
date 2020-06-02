package com.example.aftermealdiary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeInfoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButton_backArrow;
    ListView listView_recipeInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealmate);

        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        listView_recipeInfo = findViewById(R.id.listView_recipeInfo);

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
