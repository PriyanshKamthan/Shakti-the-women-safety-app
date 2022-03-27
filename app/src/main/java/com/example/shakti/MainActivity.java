package com.example.shakti;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "";
    private static final int REQUEST_LOCATION = 1;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Button helpButton;
    FloatingActionButton contactFAB;
    double longitude, latitude;
    String location_url = "https://maps.google.com/?q=" + latitude + "," + longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        helpButton = (Button) findViewById(R.id.help_button);
        contactFAB = (FloatingActionButton) findViewById(R.id.contact_fab);

        contactFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        DataBaseHandler db = new DataBaseHandler(this);
        final ArrayList<String>[] contactList = new ArrayList[]{new ArrayList<>()};

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, getText(R.string.location_not_granted), Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactList[0] = db.phoneNumberList();
                ArrayList<String> finalContactList = contactList[0];
                if (finalContactList.isEmpty()) {
                    Toast.makeText(MainActivity.this, getString(R.string.no_contact_added), Toast.LENGTH_SHORT).show();
                    callNumber("100");
                    return;
                }
                callNumber(finalContactList.get(1));
                Toast.makeText(MainActivity.this, getString(R.string.try_alert_message), Toast.LENGTH_SHORT).show();
                String messageToSend = getString(R.string.alert_message) + location_url;
                for (String number : finalContactList) {
                    SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
                }
            }
        });
    }

    public void callNumber(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+number));
        startActivity(callIntent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        location_url = "https://maps.google.com/?q=" + latitude + "," + longitude;
        TextView textView = (TextView) findViewById(R.id.location);
        textView.setText(latitude + " : " + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

}