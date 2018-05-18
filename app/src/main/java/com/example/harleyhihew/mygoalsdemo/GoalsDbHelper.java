package com.example.harleyhihew.mygoalsdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Harleyhihew on 5/29/2016.
 */
public class GoalsDbHelper extends SQLiteOpenHelper {

    public static int TABLE_VERSION = 1;
    public static String TABLE_NAME = "goals";
    public static String TABLE_KEY_ID = "id";
    public static String TABLE_KEY_GOALS = "myGoals";
    public static String TABLE_KEY_MUSTDO = "mustDo";
    public static String TABLE_KEY_PERCENT = "percent";
    public static String TABLE_KEY_CHECK = "checkMustDo";
    public static String TABLE_CREATE_SQL = "CREATE TABLE " + TABLE_NAME + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "myGoals TEXT(100), " +
            "percent DECIMAL(3,1) DEFAULT 0, " +
            "mustDo TEXT(100), " +
            "checkMustDo INTEGER DEFAULT 0" +
            ")";

    //Constructor
    public GoalsDbHelper(Context context) {
        super(context, TABLE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(db);
        Log.i("DB Helper", "TABLE UPGARADE " + oldVersion + " to " + newVersion);
    }
}
