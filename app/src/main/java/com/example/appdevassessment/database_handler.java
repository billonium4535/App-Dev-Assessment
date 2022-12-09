package com.example.appdevassessment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class database_handler extends SQLiteOpenHelper {
    private static final String DB_NAME = "bridges.db";
    private static final int DB_VERSION = 1;

    public database_handler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BRIDGES ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "LATITUDE TEXT, "
                + "LONGITUDE TEXT, "
                + "DATE TEXT, "
                + "CONDITION TEXT);"
        );

//        insertRecord(db, "53.0029512", "-2.2721329", "09/12/2022", "Good");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if((i == 1) && (i1 == 2)) {

//            insertRecord(db, "46.0054312", "-7.2259329", "01/12/2022", "Moderate");

        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if((i == 2) && (i1 == 1)) {

            sqLiteDatabase.delete("BRIDGES", "_id=?", new String[] {"Lucy"});

        }

        Log.i(">>> DatabaseHelper", "Database downgraded");
    }

    public static long insertRecord(SQLiteDatabase db , String latitude, String longitude, String date, String condition) {

        ContentValues recordValues = new ContentValues();
        recordValues.put("LATITUDE", latitude);
        recordValues.put("LONGITUDE", longitude);
        recordValues.put("DATE", date);
        recordValues.put("CONDITION", condition);

        long newRecordID = db.insert("BRIDGES", null, recordValues);

        return newRecordID;
    }

    public static String getDatabaseContentsAsString(SQLiteDatabase db) {

        Cursor cursor = db.query("BRIDGES",
                new String[]{"_id", "LATITUDE", "LONGITUDE", "DATE", "CONDITION"},
                null, null, null, null, "_id ASC");

        String databaseAsString = System.getProperty("line.separator");

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                for (int i=0; i < cursor.getColumnCount() - 1; i++) {
                    databaseAsString += cursor.getString(i) + "     ";
                }
                databaseAsString += System.getProperty("line.separator");
                cursor.moveToNext();
            }

            if(cursor != null) cursor.close();
        }

        return databaseAsString;
    }

    public static void updateRecord(SQLiteDatabase db, Long id, String latitude, String longitude, String date, String condition) {

        ContentValues recordValues = new ContentValues();

        recordValues.put("LATITUDE", latitude);
        recordValues.put("LONGITUDE", longitude);
        recordValues.put("DATE", date);
        recordValues.put("CONDITION", condition);

        db.update("BRIDGES", recordValues, "_id = ?", new String[] {Long.toString(id)});
    }

    public static void deleteRecord(SQLiteDatabase db, Long id) {
        db.delete("BRIDGES", "_id=?", new String[] {Long.toString(id)});
    }

    public static void deleteAllRecords(SQLiteDatabase db) {
        db.delete("BRIDGES", null, null);
        db.delete("SQLITE_SEQUENCE","NAME = ?",new String[]{"BRIDGES"});
    }
}
