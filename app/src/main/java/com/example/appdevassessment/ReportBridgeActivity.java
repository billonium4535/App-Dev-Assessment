package com.example.appdevassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class ReportBridgeActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener = new MyReportLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bridge);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        } catch (SecurityException e) {
            System.out.println(e.toString());
        }
    }

    public class MyReportLocationListener implements LocationListener {

        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(Location location) {
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();

            TextView textLocation = (TextView) findViewById(R.id.location2);
            textLocation.setText("Lat:" + currentLatitude + " Lon:" + currentLongitude);
        }
    }

    public void onCLick_setDate(View view) {
        Calendar c = Calendar.getInstance();

        @SuppressLint("SetTextI18n") DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, day) -> {
            month = month + 1;
            EditText selectedDateText = (EditText)findViewById(R.id.selected_date_text);
            selectedDateText.setText(day + "/" + (month) + "/" + year);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}
