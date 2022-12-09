package com.example.appdevassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button reportBridgeButton = (Button) findViewById(R.id.report_bridge_button);
        Button savedBridgeButton = (Button) findViewById(R.id.saved_bridge_reports);
        Button editBridgeButton = (Button) findViewById(R.id.edit_bridge_report);
        Button singleBridgeButton = (Button) findViewById(R.id.single_bridge_report);

        reportBridgeButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ReportBridgeActivity.class)));
        savedBridgeButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SavedBridgeActivity.class)));
        editBridgeButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, EditBridgeActivity.class)));
        singleBridgeButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SingleBridgeActivity.class)));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        } catch (SecurityException e) {
            System.out.println(e.toString());
        }
    }

    public class MyLocationListener implements LocationListener {

        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(Location location) {
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();

            TextView textLocation = (TextView) findViewById(R.id.location);
            textLocation.setText("Lat:" + currentLatitude + " Lon:" + currentLongitude);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
