// MainActivity.java is the home screen where the user can access all pages

// Imports
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


// The main class that handles the home screen
public class MainActivity extends AppCompatActivity {

    // Initialises the location manager
    LocationManager locationManager;
    LocationListener locationListener = new MyLocationListener();

    // Function that runs when the screen is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialises the buttons
        Button reportBridgeButton = findViewById(R.id.report_bridge_button);
        Button savedBridgeButton = findViewById(R.id.saved_bridge_reports);

        // Sets what the buttons do when they are clicked
        reportBridgeButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ReportBridgeActivity.class)));
        savedBridgeButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SavedBridgeActivity.class)));

        // Sets the location manager to the system gps
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Gets the gps location
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        } catch (SecurityException e) {
            System.out.println(e.toString());
        }
    }

    // Class for displaying the location
    public class MyLocationListener implements LocationListener {

        // Function that runs when the location changes
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(Location location) {

            // Initialises variables
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();

            // Gets the location of the display text
            TextView textLocation = findViewById(R.id.location);

            // Sets the text to the current location
            textLocation.setText("Lat:" + currentLatitude + " Lon:" + currentLongitude);
        }
    }

    // Stops updating the location on close
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
