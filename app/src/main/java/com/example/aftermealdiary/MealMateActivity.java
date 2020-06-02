package com.example.aftermealdiary;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MealMateActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imageButton_backArrow;
    ListView listView_contact;

    Cursor cursor;
    Cursor contactCursor;
    HashMap<String, String> contactHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mealmate);

        imageButton_backArrow = findViewById(R.id.imageButton_backArrow);
        listView_contact = findViewById(R.id.listView_contact);

        imageButton_backArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageButton_backArrow:
                onBackPressed();
        }

    }

    private void getContact() {
        cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);

        while (cursor.moveToNext()) {
            contactHashMap = new HashMap<String, String>();

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

            contactHashMap.put("name", name);

            contactCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

            if (contactCursor.moveToFirst()) {
                String contact = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactHashMap.put("contact", contact);
            }

            contactCursor.close();
        }
        cursor.close();

    }
}
