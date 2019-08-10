package com.example.birthday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String CONTACT_HELPER = "Birthday Helper";
    public static final String DB_NAME = "Birthday_Database";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_CONTACT = "contact";
    public static final String COL_DAY = "day";
    public static final String COL_MONTH = "month";
    public static final String COL_YEAR = "year";
    public static final int DB_VERSION = 1;

    private String create_query = "CREATE TABLE "+DB_NAME+" ( "+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_NAME+" text ,"
            +COL_CONTACT+" text,"
            +COL_DAY+" integer,"
            +COL_MONTH+" integer,"
            +COL_YEAR+" integer"
            +");";
    private String updrade_query = "DROP TABLE IF EXISTS "+DB_NAME;

    Context context;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(updrade_query);
        this.onCreate(db);
    }
}
