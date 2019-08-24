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
    public static final int DB_VERSION = 8;
    public static final String COL_HOUR = "msg_hour";
    public static final String COL_MINUTE = "msg_minute";
    public static final String COL_WISH = "wish_text";
    public static final String COL_MSG_STATE = "auto_msg_state";
    public static final String COL_NOTI_STATE = "auto_noti_state";

    private String create_query = "CREATE TABLE "+DB_NAME+" ( "+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_NAME+" text not null,"
            +COL_CONTACT+" text,"
            +COL_DAY+" integer not null,"
            +COL_MONTH+" integer not null,"
            +COL_YEAR+" integer not null,"
            +COL_HOUR+" integer,"
            +COL_MINUTE+" integer,"
            +COL_WISH+" text,"
            +COL_MSG_STATE+" text,"
            +COL_NOTI_STATE+" text"
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
