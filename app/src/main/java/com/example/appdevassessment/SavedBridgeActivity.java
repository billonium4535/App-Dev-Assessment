// SavedBridgeActivity.java is the file that displays all records
// and allows users to delete all of their records

// Imports
package com.example.appdevassessment;

import static com.example.appdevassessment.database_handler.deleteAllRecords;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


// The main class that handles the saved screen
public class SavedBridgeActivity extends AppCompatActivity {

    // Initialises variables
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter listAdapter;

    // Function that runs when the screen is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_bridge);

        // Initialises the buttons
        Button deleteAllRecords = findViewById(R.id.delete_all_records);

        // Initialises the database
        SQLiteOpenHelper database_handler = new database_handler(getApplicationContext());
        SQLiteDatabase db = database_handler.getReadableDatabase();

        // Initialises the list view
        ListView listView = findViewById(R.id.listView_bridges);

        // Initialises the database cursor
        cursor = db.query("BRIDGES", new String[]{"_id", "LATITUDE", "LONGITUDE", "DATE", "CONDITION"}, null, null, null, null, null);

        // Sets the variables to be read into the list adapter
        String[] fromFieldNames = new String[] {"_id", "LATITUDE"};
        int[] toViewIDs = new int[] { android.R.id.text1, android.R.id.text2 };

        // Initialises the list adapter with the data above
        listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, fromFieldNames, toViewIDs, 0);

        // Sets the data behind the list view
        listView.setAdapter(listAdapter);

        // Function to set what happens when a record is clicked
        AdapterView.OnItemClickListener itemClickListener =
                (listView1, itemView, position, id) -> {
                    Intent intent = new Intent(SavedBridgeActivity.this, SingleActivity.class);
                    intent.putExtra(SingleActivity.EXTRA_BRIDGE_ID, (int) id);
                    startActivity(intent);
                };

        // Adds the listener to the list view
        listView.setOnItemClickListener(itemClickListener);

        // Function to set what happens when the delete button is clicked
        deleteAllRecords.setOnClickListener(view -> {

            // Deletes all the records in the database
            deleteAllRecords(db);

            // Sends the user a conformation message
            Toast.makeText(SavedBridgeActivity.this, "All bridges deleted", Toast.LENGTH_SHORT).show();
        });
    }

    // Stops updating the location on close
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(cursor != null) cursor.close();
        if(db != null) db.close();
    }
}
