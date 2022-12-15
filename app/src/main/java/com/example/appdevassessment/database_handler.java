// database_handler.java interacts with the database and handles the data

// Imports
package com.example.appdevassessment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// A class that can be called to interact with SQL files
public class database_handler extends SQLiteOpenHelper {

    // Sets the database version and name
    private static final String DB_NAME = "bridges.db";
    private static final int DB_VERSION = 1;

    public database_handler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Function to create a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BRIDGES ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "LATITUDE TEXT, "
                + "LONGITUDE TEXT, "
                + "DATE TEXT, "
                + "CONDITION TEXT);"
        );
    }

    // Function to upgrade records for a new version of the app
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if((i == 1) && (i1 == 2)) {
            insertRecord(db, "46.0054312", "-7.2259329", "01/12/2022", "Moderate");
        }
    }

    // Function to insert new records into the database
    public static long insertRecord(SQLiteDatabase db , String latitude, String longitude, String date, String condition) {

        // Puts all of the values in one set
        ContentValues recordValues = new ContentValues();
        recordValues.put("LATITUDE", latitude);
        recordValues.put("LONGITUDE", longitude);
        recordValues.put("DATE", date);
        recordValues.put("CONDITION", condition);

        // Adds it to the database
        return db.insert("BRIDGES", null, recordValues);
    }

    // Function to update records in the database
    public static void updateRecord(SQLiteDatabase db, Long id, String latitude, String longitude, String date, String condition) {

        // Puts all of the values in one set
        ContentValues recordValues = new ContentValues();
        recordValues.put("LATITUDE", latitude);
        recordValues.put("LONGITUDE", longitude);
        recordValues.put("DATE", date);
        recordValues.put("CONDITION", condition);

        // Updates the record with the new data
        db.update("BRIDGES", recordValues, "_id = ?", new String[] {Long.toString(id)});
    }

    // Function to delete single records from the database
    public static void deleteRecord(SQLiteDatabase db, Long id) {
        db.delete("BRIDGES", "_id=?", new String[] {Long.toString(id)});
    }

    // Function to delete all records from the database
    public static void deleteAllRecords(SQLiteDatabase db) {
        db.delete("BRIDGES", null, null);
        db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{"BRIDGES"});
    }
}
