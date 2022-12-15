// SingleActivity.java is the file that displays each record individually

// Imports
package com.example.appdevassessment;

import static com.example.appdevassessment.database_handler.deleteRecord;
import static com.example.appdevassessment.database_handler.updateRecord;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;


// The main class that handles the saved screen
public class SingleActivity extends AppCompatActivity {

    // Initialises the database name
    public static final String EXTRA_BRIDGE_ID = "bridge_id";

    // Function that runs when the screen is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        // Initialises the buttons
        Button deleteRecordButton = findViewById(R.id.delete_record_button);
        Button editRecordButton = findViewById(R.id.edit_record_button);

        // Gets and sets the ID to a variable
        int id = (Integer)getIntent().getExtras().get(EXTRA_BRIDGE_ID);

        // Initialises the database
        SQLiteOpenHelper database_handler = new database_handler(getApplicationContext());
        SQLiteDatabase db = database_handler.getReadableDatabase();

        // Initialises the database cursor
        Cursor cursor = db.query("BRIDGES",
                new String[]{"_id", "LATITUDE", "LONGITUDE", "DATE", "CONDITION"},
                "_id = ?", new String[] {Integer.toString(id)},
                null, null, null);

        // Initialises variables
        String id_text = null;
        String lat = null;
        String lon = null;
        String date = null;
        String con = null;

        // Gets data from the database and sets it to variables
        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            id_text = cursor.getString(0);
            lat = cursor.getString(1);
            lon = cursor.getString(2);
            date = cursor.getString(3);
            con = cursor.getString(4);
        }

        // Closes the cursor
        cursor.close();

        // Sets the variables to text and puts them on screen
        TextView textView_ID = findViewById(R.id.textView_ID);
        textView_ID.setText(id_text);

        TextView textView_Lat = findViewById(R.id.textView_Lat);
        textView_Lat.setText(lat);

        TextView textView_Lon = findViewById(R.id.textView_Lon);
        textView_Lon.setText(lon);

        TextView textView_Date = findViewById(R.id.textView_Date);
        textView_Date.setText(date);

        TextView textView_Condition = findViewById(R.id.textView_Condition);
        textView_Condition.setText(con);


        // Function to set what happens when the delete button is clicked
        deleteRecordButton.setOnClickListener(view -> {

            // Deletes the selected record in the database
            deleteRecord(db, (long) id);

            // Sends the user a conformation message
            Toast.makeText(SingleActivity.this, "Bridge deleted", Toast.LENGTH_SHORT).show();
        });

        // Initialises final variables
        String finalLat = lat;
        String finalLon = lon;

        // Function to set what happens when the edit button is clicked
        editRecordButton.setOnClickListener(view -> {

            // Gets the location of the users inputs
            EditText date1 = findViewById(R.id.selected_date_text);
            Spinner condition_spinner = findViewById(R.id.condition_spinner3);
            String condition = condition_spinner.getSelectedItem().toString();

            // Checks if all values are filled out
            if (date1.getText().toString().equals("")) {
                Toast.makeText(SingleActivity.this, "Fill in all values", Toast.LENGTH_SHORT).show();
                return;
            }

            // Updates the record with the inputted data into the database
            updateRecord(db, (long) id, finalLat, finalLon, date1.getText().toString(), condition);

            // Sends the user a conformation message
            Toast.makeText(SingleActivity.this, "Bridge updated", Toast.LENGTH_SHORT).show();
        });
    }

    // Function to set the date
    public void onCLick_setDate_single(View view) {

        // Initialises the calender
        Calendar c = Calendar.getInstance();

        @SuppressLint("SetTextI18n") DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, day) -> {

            // Adds 1 to the month as Jan is month 0
            month = month + 1;

            // Gets the location of the inputted date
            EditText selectedDateText = findViewById(R.id.selected_date_text);

            // Puts the selected date on the screen
            selectedDateText.setText(day + "/" + (month) + "/" + year);

            // Gets the current year, month and day
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        // Shows the popup on screen
        dialog.show();
    }
}
