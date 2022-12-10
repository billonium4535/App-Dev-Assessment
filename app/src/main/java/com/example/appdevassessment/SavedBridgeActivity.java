package com.example.appdevassessment;

import static com.example.appdevassessment.database_handler.deleteAllRecords;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class SavedBridgeActivity extends AppCompatActivity {


    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleCursorAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_bridge);

        Button deleteAllRecords = findViewById(R.id.delete_all_records);

        SQLiteOpenHelper database_handler = new database_handler(getApplicationContext());
        SQLiteDatabase db = database_handler.getReadableDatabase();

        ListView listView = findViewById(R.id.listView_bridges);

        cursor = db.query("BRIDGES", new String[]{"_id", "LATITUDE", "LONGITUDE", "DATE", "CONDITION"}, null, null, null, null, null);

        String[] fromFieldNames = new String[] {"_id", "LATITUDE"};
        int[] toViewIDs = new int[] { android.R.id.text1, android.R.id.text2 };

        listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, fromFieldNames, toViewIDs, 0);

        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener itemClickListener =
                (listView1, itemView, position, id) -> {
                    Intent intent = new Intent(SavedBridgeActivity.this, SingleActivity.class);
                    intent.putExtra(SingleActivity.EXTRA_BRIDGE_ID, (int) id);
                    startActivity(intent);
                };

        listView.setOnItemClickListener(itemClickListener);

        deleteAllRecords.setOnClickListener(view -> {

            deleteAllRecords(db);

            Toast.makeText(SavedBridgeActivity.this, "All bridges deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(cursor != null) cursor.close();
        if(db != null) db.close();
    }
}
