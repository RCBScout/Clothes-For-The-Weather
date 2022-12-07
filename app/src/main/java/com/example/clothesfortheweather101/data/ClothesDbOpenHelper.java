package com.example.clothesfortheweather101.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.clothesfortheweather101.data.ClothesContract.LookEntryB;
import com.example.clothesfortheweather101.data.ClothesContract.LookEntry;


public class ClothesDbOpenHelper extends SQLiteOpenHelper {
    public ClothesDbOpenHelper( Context context) {
        super(context, ClothesContract.DATABASE_NAME, null, ClothesContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOOKS_TABLE = "CREATE TABLE " + LookEntry.TABLE_NAME + "("
                + LookEntry._ID + " INTEGER PRIMARY KEY,"
                + LookEntry.COLUMN_COMFORT + " INTEGER NOT NULL,"
                + LookEntry.COLUMN_TEMPERATURE + " INTEGER,"
                + LookEntry.COLUMN_CLOTHES_TYPE + " INTEGER,"
                + LookEntry.COLUMN_CLOTHE + " TEXT" + ")";
        db.execSQL(CREATE_LOOKS_TABLE);

        String CREATE_LOOKS_TABLE_B = "CREATE TABLE " + LookEntryB.TABLE_NAME + "("
                + LookEntryB._ID + " INTEGER PRIMARY KEY,"
                + LookEntryB.COLUMN_COMFORT + " INTEGER NOT NULL,"
                + LookEntryB.COLUMN_TEMPERATURE + " INTEGER,"
                + LookEntryB.COLUMN_CLOTHES_TYPE + " INTEGER,"
                + LookEntryB.COLUMN_CLOTHE + " TEXT" + ")";
        db.execSQL(CREATE_LOOKS_TABLE_B);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ClothesContract.DATABASE_NAME);
        onCreate(db);
    }
}
