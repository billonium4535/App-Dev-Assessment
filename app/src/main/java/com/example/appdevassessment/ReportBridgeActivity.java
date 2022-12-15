// ReportBridgeActivity.java is the file that allows users to report bridges

// Imports
package com.example.appdevassessment;

import static com.example.appdevassessment.database_handler.insertRecord;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;


// The main class that handles the report screen
public class ReportBridgeActivity extends AppCompatActivity {

    // Initialises variables
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter listAdapter;

    // Initialises the location manager
    LocationManager locationManager;
    LocationListener locationListener = new MyReportLocationListener();

    // Sets the database name
    private static final String DB_NAME = "bridges.db";

    // Class for displaying the location
    public class MyReportLocationListener implements LocationListener {

        // Function that runs when the location changes
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(Location location) {

            // Initialises variables
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();

            // Gets the location of the display text
            TextView textLocation = (TextView) findViewById(R.id.location2);
            textLocation.setText("Lat:" + currentLatitude + " Lon:" + currentLongitude);

            // Sets the text to the current location and stores the data in variables
            TextView latLocation = (TextView) findViewById(R.id.latitude_var);
            latLocation.setText(Double.toString(currentLatitude));
            TextView lonLocation = (TextView) findViewById(R.id.longitude_var);
            lonLocation.setText(Double.toString(currentLongitude));
        }
    }

    // Function that runs when the screen is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_bridge);

        // Initialises the buttons
        Button reportBridgeButton = (Button) findViewById(R.id.report_bridge_button);

        // Initialises the database
        SQLiteOpenHelper database_handler = new database_handler(getApplicationContext());
        SQLiteDatabase db = database_handler.getReadableDatabase();

        // Initialises the database cursor
        cursor = db.query("BRIDGES", new String[]{"_id", "LATITUDE", "LONGITUDE", "DATE", "CONDITION"}, null, null, null, null, null);

        // Sets the location manager to the system gps
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Gets the gps location
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        } catch (SecurityException e) {
            System.out.println(e.toString());
        }

        // Function to set what happens when the report button is clicked
        reportBridgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gets the location of the users inputs
                EditText date = findViewById(R.id.selected_date_text);
                EditText latitude = findViewById(R.id.latitude_var);
                EditText longitude = findViewById(R.id.longitude_var);
                Spinner condition_spinner = (Spinner) findViewById(R.id.condition_spinner);
                String condition = condition_spinner.getSelectedItem().toString();

                // Checks if all values are filled out
                if (date.getText().toString().equals("")) {
                    Toast.makeText(ReportBridgeActivity.this, "Fill in all values", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Inserts the data into the database
                insertRecord(db, latitude.getText().toString(), longitude.getText().toString(), date.getText().toString(), condition);

                // Sends the user a conformation message
                Toast.makeText(ReportBridgeActivity.this, "Bridge added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to set the date
    public void onCLick_setDate(View view) {

        // Initialises the calender
        Calendar c = Calendar.getInstance();

        @SuppressLint("SetTextI18n") DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, day) -> {

            // Adds 1 to the month as Jan is month 0
            month = month + 1;

            // Gets the location of the inputted date
            EditText selectedDateText = (EditText)findViewById(R.id.selected_date_text);

            // Puts the selected date on the screen
            selectedDateText.setText(day + "/" + (month) + "/" + year);

            // Gets the current year, month and day
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        // Shows the popup on screen
        dialog.show();
    }

    // Stops updating the location on close
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);

        if(cursor != null) cursor.close();
        if(db != null) db.close();
    }
}
