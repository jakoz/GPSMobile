package com.example.vautrin.gpslocationpresenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv=(ListView)findViewById(R.id.listLastCoordinates);

        EditText inputPhone = (EditText) findViewById(R.id.inputPhone);
        String number = inputPhone.getText().toString();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,fetchInbox(number));
        lv.setAdapter(adapter);
    }

    public ArrayList<String> fetchInbox(String number){

        ArrayList<String> sms=new ArrayList<String>();
        String[] phoneNumber = new String[] { number };
        Uri uriSms=Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[] { "_id", "thread_id", "address", "person", "date","body", "type" }, "address=?", phoneNumber, null);
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            String body=cursor.getString(cursor.getColumnIndexOrThrow("body"));
            body = body.replaceAll("\\r|\\n", "");
            if(body.contains("lat:")){
                int indexOfLatitude = body.indexOf("lat:")+4;
                int indexOfLongitude = body.indexOf("lon:")+4;
                String latitude = body.substring(indexOfLatitude,indexOfLatitude+9);
                String longitude = body.substring(indexOfLongitude,indexOfLongitude+9);
                sms.add(latitude+"L"+longitude);
            }
        }
        return sms;
    }

    public void onGoToMapClick(View view){
        TextView textView = (TextView) (findViewById(R.id.lblWelcome));
        EditText inputPhone = (EditText) findViewById(R.id.inputPhone);
        String number = inputPhone.getText().toString();

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putStringArrayListExtra("listView",fetchInbox(number));
        startActivity(intent);
    }
}
