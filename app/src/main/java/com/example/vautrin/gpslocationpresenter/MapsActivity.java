package com.example.vautrin.gpslocationpresenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            Intent intent = getIntent();
            ArrayList<String> listView = intent.getStringArrayListExtra("listView");
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (int i = 0; i < listView.size(); i++) {
                String coordinates = listView.get(i);
                String address = "";
                String[] coordinateAfterSplit = coordinates.split("&");
                LatLng location = new LatLng(new Float(coordinateAfterSplit[0]), new Float(coordinateAfterSplit[1]));
                Location loc = new Location("");
                Geocoder geocoder = new Geocoder(this);
                List<android.location.Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                    address = addresses.get(0).getThoroughfare() + " " + addresses.get(0).getSubThoroughfare();
                }
                catch(IOException e) {
                }

                mMap.addMarker(new MarkerOptions().position(location).title(address));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                builder.include(location);
            }
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }
//    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//
//                loc.distanceBetween(location.latitude,location.longitude,locationCurrent.latitude,locationCurrent.longitude, results);

    public String readLastSms(String number)
    {
        String sms=new String();
        String[] phoneNumber = new String[] { number };
        Uri uriSms=Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[] { "_id", "thread_id", "address", "person", "date","body", "type" }, "address=?", phoneNumber, null);
        cursor.moveToFirst();
        String body=cursor.getString(cursor.getColumnIndexOrThrow("body"));
        body = body.replaceAll("\\r|\\n", "");
        if(body.contains("lat:")){
            int indexOfLatitude = body.indexOf("lat:")+4;
            int indexOfLongitude = body.indexOf("lon:")+4;
            String latitude = body.substring(indexOfLatitude,indexOfLatitude+9);
            String longitude = body.substring(indexOfLongitude,indexOfLongitude+9);
            sms = latitude+"&"+longitude;
        }
        return sms;
    }


}
