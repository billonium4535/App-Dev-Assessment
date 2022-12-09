package com.example.appdevassessment;

import static com.example.appdevassessment.database_handler.deleteRecord;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SingleActivity extends AppCompatActivity {

    public static final String EXTRA_BRIDGE_ID = "bridge_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        Button deleteRecordButton = (Button) findViewById(R.id.delete_record_button);

        int id = (Integer)getIntent().getExtras().get(EXTRA_BRIDGE_ID);

        SQLiteOpenHelper database_handler = new database_handler(getApplicationContext());
        SQLiteDatabase db = database_handler.getReadableDatabase();

        Cursor cursor = db.query("BRIDGES", new String[]{"_id", "LATITUDE", "LONGITUDE", "DATE", "CONDITION"}, null, null, null, null, null);

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

        TextView textView_ID = (TextView)findViewById(R.id.textView_ID);
        textView_ID.setText(id_text);

        TextView textView_Lat = (TextView)findViewById(R.id.textView_Lat);
        textView_Lat.setText(lat);

        TextView textView_Lon = (TextView)findViewById(R.id.textView_Lon);
        textView_Lon.setText(lon);

        TextView textView_Date = (TextView)findViewById(R.id.textView_Date);
        textView_Date.setText(date);

        TextView textView_Condition = (TextView)findViewById(R.id.textView_Condition);
        textView_Condition.setText(con);

        deleteRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteRecord(db, (long) id);

                Toast.makeText(SingleActivity.this, "Bridge deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
