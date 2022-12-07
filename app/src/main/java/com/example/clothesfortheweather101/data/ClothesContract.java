package com.example.clothesfortheweather101.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ClothesContract {

    private ClothesContract(){

    }
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME ="clothes";
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.clothesfortheweather101";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final class LookEntry implements BaseColumns {


        public static final String TABLE_NAME ="looks";

        public static final String _ID =BaseColumns._ID;
        public static final String COLUMN_COMFORT ="comfort";
        public static final String COLUMN_CLOTHES_TYPE ="clothestype";
        public static final String COLUMN_TEMPERATURE ="temperature";
        public static final String COLUMN_CLOTHE ="clothes";

        public static final int COMFORT_NORMAL = 0;
        public static final int COMFORT_COOL = 1;
        public static final int COMFORT_WARM = 2;
        public static final int COMFORT_COLD = 3;
        public static final int COMFORT_HOT = 4;

        public static final int CLOTHES_TOP = 0;
        public static final int CLOTHES_HEAD = 1;
        public static final int CLOTHES_LEGS = 2;
        public static final int CLOTHES_SHOES = 3;
        public static final int CLOTHES_HANDS = 4;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

    }

    public static final class LookEntryB implements BaseColumns {


        public static final String TABLE_NAME ="looksb";

        public static final String _ID =BaseColumns._ID;
        public static final String COLUMN_COMFORT ="comfort";
        public static final String COLUMN_CLOTHES_TYPE ="clothestype";
        public static final String COLUMN_TEMPERATURE ="temperature";
        public static final String COLUMN_CLOTHE ="clothes";

        public static final int COMFORT_NORMAL = 0;
        public static final int COMFORT_COOL = 1;
        public static final int COMFORT_WARM = 2;
        public static final int COMFORT_COLD = 3;
        public static final int COMFORT_HOT = 4;

        public static final int CLOTHES_TOP = 0;
        public static final int CLOTHES_HEAD = 1;
        public static final int CLOTHES_LEGS = 2;
        public static final int CLOTHES_SHOES = 3;
        public static final int CLOTHES_HANDS = 4;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

        public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + TABLE_NAME;

    }
}
