package com.example.clothesfortheweather101.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.clothesfortheweather101.data.ClothesContract.LookEntry;


public class ClothesContentProvider extends ContentProvider {

    ClothesDbOpenHelper dbOpenHelper;

    private static final int LOOKS = 111;
    private static final int LOOKS_ID = 222;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(ClothesContract.AUTHORITY,LookEntry.TABLE_NAME,LOOKS);
        uriMatcher.addURI(ClothesContract.AUTHORITY,LookEntry.TABLE_NAME + "/#",LOOKS_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new ClothesDbOpenHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match){
            case LOOKS:
                cursor = db.query(ClothesContract.LookEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case LOOKS_ID:
                selection = LookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(LookEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Can't query incorrect URI " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Integer comfort = values.getAsInteger(LookEntry.COLUMN_COMFORT);
        if(comfort == null || !(comfort == LookEntry.COMFORT_NORMAL ||
                comfort == LookEntry.COMFORT_COOL || comfort == LookEntry.COMFORT_WARM
                || comfort == LookEntry.COMFORT_COLD || comfort == LookEntry.COMFORT_HOT)){
            throw new IllegalArgumentException("You have to input correct comfort");
        }

        Integer temperature = values.getAsInteger(LookEntry.COLUMN_TEMPERATURE);
        if(temperature == null) {
            throw new IllegalArgumentException("You have to put correct temperature");
        }

        Integer clothesType = values.getAsInteger(LookEntry.COLUMN_CLOTHES_TYPE);
        if(clothesType == null || !(clothesType == LookEntry.CLOTHES_TOP ||
                clothesType == LookEntry.CLOTHES_HEAD || clothesType == LookEntry.CLOTHES_LEGS
                || clothesType == LookEntry.CLOTHES_SHOES || clothesType == LookEntry.CLOTHES_HANDS)){
            throw new IllegalArgumentException("You have to input correct clothes");
        }

        String clothes = values.getAsString(LookEntry.COLUMN_CLOTHE);
        if(clothes == null) {
            throw new IllegalArgumentException("You have to put clothes");
        }


        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        switch (match) {
            case LOOKS:
                long id = db.insert(LookEntry.TABLE_NAME,
                        null, values);
                if (id == -1) {
                    Log.e("insertMethod",
                            "Insertion of data in the table failed for "
                                    + uri);
                    return null;
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Insertion of data in " +
                        "the table failed for " + uri);

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsDeleted;

        switch (match){
            case LOOKS:
                rowsDeleted =  db.delete(LookEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case LOOKS_ID:
                selection = LookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted =  db.delete(LookEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Can't delete this URI " + uri);

        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(LookEntry.COLUMN_COMFORT)) {
            Integer comfort = values.getAsInteger(LookEntry.COLUMN_COMFORT);
            if(comfort == null || !(comfort == LookEntry.COMFORT_NORMAL ||
                    comfort == LookEntry.COMFORT_COOL || comfort == LookEntry.COMFORT_WARM
                    || comfort == LookEntry.COMFORT_COLD || comfort == LookEntry.COMFORT_HOT)){
                throw new IllegalArgumentException("You have to input correct comfort");
            }
        }
        if (values.containsKey(LookEntry.COLUMN_TEMPERATURE)) {
            Integer temperature = values.getAsInteger(LookEntry.COLUMN_TEMPERATURE);
            if(temperature == null) {
                throw new IllegalArgumentException("You have to put correct temperature");
            }
        }

        if (values.containsKey(LookEntry.COLUMN_CLOTHES_TYPE)) {
            Integer clothesType = values.getAsInteger(LookEntry.COLUMN_CLOTHES_TYPE);
            if(clothesType == null || !(clothesType == LookEntry.CLOTHES_TOP ||
                    clothesType == LookEntry.CLOTHES_HEAD || clothesType == LookEntry.CLOTHES_LEGS
                    || clothesType == LookEntry.CLOTHES_SHOES || clothesType == LookEntry.CLOTHES_HANDS)){
                throw new IllegalArgumentException("You have to input correct clothes");
            }
        }
        if (values.containsKey(LookEntry.COLUMN_CLOTHE)) {
            String clothes = values.getAsString(LookEntry.COLUMN_CLOTHE);
            if(clothes == null) {
                throw new IllegalArgumentException("You have to put clothes");
            }
        }

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int rowsUpdated;

        switch (match) {
            case LOOKS:
                rowsUpdated = db.update(LookEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LOOKS_ID:
                selection = LookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(LookEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Can't update this URI " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match){
            case LOOKS:
                return LookEntry.CONTENT_MULTIPLE_ITEMS;

            case LOOKS_ID:

                return LookEntry.CONTENT_SINGLE_ITEM;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
    }


}
