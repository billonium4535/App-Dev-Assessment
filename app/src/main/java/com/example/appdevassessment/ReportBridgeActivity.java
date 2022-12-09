package com.example.appdevassessment;

import static com.example.appdevassessment.database_handler.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

public class ReportBridgeActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter listAdapter;


    LocationManager locationManager;
    LocationListener locationListener = new MyReportLocationListener();

    private static final String DB_NAME = "bridges.db";

    public class MyReportLocationListener implements LocationListener {

        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(Location location) {

            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();

            TextView textLocation = (TextView) findViewById(R.id.location2);
            textLocation.setText("Lat:" + currentLatitude + " Lon:" + currentLongitude);

            TextView latLocation = (TextView) findViewById(R.id.latitude_var);
            latLocation.setText(Double.toString(currentLatitude));
            TextView lonLocation = (TextView) findViewById(R.id.longitude_var);
            lonLocation.setText(Double.toString(currentLongitude));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bridge);

        Button reportBridgeButton = (Button) findViewById(R.id.report_bridge_button);

        SQLiteOpenHelper database_handler = new database_handler(getApplicationContext());
        SQLiteDatabase db = database_handler.getReadableDatabase();

        cursor = db.query("BRIDGES", new String[]{"_id", "LATITUDE", "LONGITUDE", "DATE", "CONDITION"}, null, null, null, null, null);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        } catch (SecurityException e) {
            System.out.println(e.toString());
        }

        reportBridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText date = findViewById(R.id.selected_date_text);
                EditText latitude = findViewById(R.id.latitude_var);
                EditText longitude = findViewById(R.id.longitude_var);
                Spinner condition_spinner = (Spinner) findViewById(R.id.condition_spinner);
                String condition = condition_spinner.getSelectedItem().toString();

                if (date.getText().toString().equals("")) {
                    Toast.makeText(ReportBridgeActivity.this, "Fill in all values", Toast.LENGTH_SHORT).show();
                    return;
                }

                insertRecord(db, latitude.getText().toString(), longitude.getText().toString(), date.getText().toString(), condition);

                Toast.makeText(ReportBridgeActivity.this, "Bridge added", Toast.LENGTH_SHORT).show();
            }
        });
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

        if(cursor != null) cursor.close();
        if(db != null) db.close();
    }
}
