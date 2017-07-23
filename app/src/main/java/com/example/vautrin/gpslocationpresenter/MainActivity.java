package com.example.vautrin.gpslocationpresenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
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

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,fetchInbox());
        lv.setAdapter(adapter);
    }

    public ArrayList<String> fetchInbox(){

        ArrayList<String> sms=new ArrayList<String>();

        Uri uriSms=Uri.parse("content://sms/inbox");
        Cursor cursor=getContentResolver().query(uriSms, null, null, null, null);
        cursor.moveToFirst();
        while(cursor.moveToNext()){

            String body=cursor.getString(cursor.getColumnIndexOrThrow("body"));

            sms.add("Sms=>"+body);
        }
        return sms;
    }

    public void onGoToMapClick(View view){
        TextView textView = (TextView) (findViewById(R.id.lblWelcome));
        textView.setText("Welcome");

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
