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

    Integer amountCoordinatesInt = 0;
    String number = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readAllSms();
    }

    public ArrayList<String> fetchInbox(String number){
        ArrayList<String> sms=new ArrayList<String>();
        String[] phoneNumber = new String[] { number };
        Uri uriSms=Uri.parse("content://sms/inbox");
        EditText amountCoordinates = (EditText) findViewById(R.id.inputAmountCoordinates);
        amountCoordinatesInt = Integer.parseInt(amountCoordinates.getText().toString());
        Cursor cursor = getContentResolver().query(uriSms, new String[] { "_id", "thread_id", "address", "person", "date","body", "type" }, "address=?", phoneNumber, null);
        cursor.moveToFirst();
        while(cursor.moveToNext() && amountCoordinatesInt > 0){
            String body=cursor.getString(cursor.getColumnIndexOrThrow("body"));
            body = body.replaceAll("\\r|\\n", "");
            if(body.contains("lat:")){
                int indexOfLatitude = body.indexOf("lat:")+4;
                int indexOfLongitude = body.indexOf("lon:")+4;
                String latitude = body.substring(indexOfLatitude,indexOfLatitude+9);
                String longitude = body.substring(indexOfLongitude,indexOfLongitude+9);
                sms.add(latitude+"&"+longitude);
                amountCoordinatesInt -= 1;
            }
        }
        return sms;
    }

    public void onGoToMapClick(View view){
        goToMap();
    }
    public void onGetNewClick(View view){
        readAllSms();
    }
    public void onGetConfigWindow(View view){ showConfiguration();}
    private void goToMap(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putStringArrayListExtra("listView",fetchInbox(number));
        EditText amountCoordinates = (EditText) findViewById(R.id.inputAmountCoordinates);
        intent.putExtra("amountCoordinatesInt",Integer.parseInt(amountCoordinates.getText().toString()));
        ArrayList<String> listView = intent.getStringArrayListExtra("listView");
        if (!listView.isEmpty()){
            startActivity(intent);
        }
    }
    private void readAllSms(){
        ListView lv=(ListView)findViewById(R.id.listLastCoordinates);
        //Amount of coordinates to read
        EditText amountCoordinates = (EditText) findViewById(R.id.inputAmountCoordinates);
        amountCoordinatesInt = Integer.parseInt(amountCoordinates.getText().toString());
        //Phone number
        EditText inputPhone = (EditText) findViewById(R.id.inputPhone);
        number = inputPhone.getText().toString();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,fetchInbox(number));
        lv.setAdapter(adapter);
    }
    private void showConfiguration(){
        Intent intent = new Intent(this, ConfigActivity.class);
    }
}
