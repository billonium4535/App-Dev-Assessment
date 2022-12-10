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

public class SingleActivity extends AppCompatActivity {

    public static final String EXTRA_BRIDGE_ID = "bridge_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        Button deleteRecordButton = findViewById(R.id.delete_record_button);
        Button editRecordButton = findViewById(R.id.edit_record_button);

        int id = (Integer)getIntent().getExtras().get(EXTRA_BRIDGE_ID);

        SQLiteOpenHelper database_handler = new database_handler(getApplicationContext());
        SQLiteDatabase db = database_handler.getReadableDatabase();

        Cursor cursor = db.query("BRIDGES",
                new String[]{"_id", "LATITUDE", "LONGITUDE", "DATE", "CONDITION"},
                "_id = ?", new String[] {Integer.toString(id)},
                null, null, null);

        String id_text = null;
        String lat = null;
        String lon = null;
        String date = null;
        String con = null;

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            id_text = cursor.getString(0);
            lat = cursor.getString(1);
            lon = cursor.getString(2);
            date = cursor.getString(3);
            con = cursor.getString(4);
        }

        cursor.close();

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

        deleteRecordButton.setOnClickListener(view -> {

            deleteRecord(db, (long) id);

            Toast.makeText(SingleActivity.this, "Bridge deleted", Toast.LENGTH_SHORT).show();
        });

        String finalLat = lat;
        String finalLon = lon;
        editRecordButton.setOnClickListener(view -> {

            EditText date1 = findViewById(R.id.selected_date_text);
            Spinner condition_spinner = findViewById(R.id.condition_spinner3);
            String condition = condition_spinner.getSelectedItem().toString();

            if (date1.getText().toString().equals("")) {
                Toast.makeText(SingleActivity.this, "Fill in all values", Toast.LENGTH_SHORT).show();
                return;
            }

            updateRecord(db, (long) id, finalLat, finalLon, date1.getText().toString(), condition);
            Toast.makeText(SingleActivity.this, "Bridge updated", Toast.LENGTH_SHORT).show();
        });
    }

    public void onCLick_setDate_single(View view) {
        Calendar c = Calendar.getInstance();

        @SuppressLint("SetTextI18n") DatePickerDialog dialog = new DatePickerDialog(this, (view1, year, month, day) -> {
            month = month + 1;
            EditText selectedDateText = findViewById(R.id.selected_date_text);
            selectedDateText.setText(day + "/" + (month) + "/" + year);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dialog.show();
    }
}
