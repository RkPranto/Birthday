package com.prantology.birthdayhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class BirthDatabase {
    SQLiteDatabase sqLiteDatabase;
    DBOpenHelper sqLiteOpenHelper;

    public static final String CONTACT_DATABASE = "Contact Database";

    public BirthDatabase(Context context) {
        sqLiteOpenHelper = new DBOpenHelper(context);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteOpenHelper.close();
    }

    public boolean insertContact(ContactModel contactModel) {
        this.open();
        ContentValues contentValues = new ContentValues();
        //Log.d(CONTACT_DATABASE, "insert staret");
        contentValues.put(DBOpenHelper.COL_NAME, contactModel.getName());
        contentValues.put(DBOpenHelper.COL_CONTACT, contactModel.getContact());
        contentValues.put(DBOpenHelper.COL_DAY, contactModel.getDay());
        contentValues.put(DBOpenHelper.COL_MONTH, contactModel.getMonth());
        contentValues.put(DBOpenHelper.COL_YEAR, contactModel.getYear());
        contentValues.put(DBOpenHelper.COL_HOUR, contactModel.getHour());
        contentValues.put(DBOpenHelper.COL_MINUTE, contactModel.getMinute());
        contentValues.put(DBOpenHelper.COL_WISH, contactModel.getWish());
        contentValues.put(DBOpenHelper.COL_MSG_STATE, contactModel.getMsgState());
        contentValues.put(DBOpenHelper.COL_NOTI_STATE, contactModel.getNotificationState());

        long inserted = sqLiteDatabase.insert(sqLiteOpenHelper.DB_NAME,null, contentValues);
        this.close();
        //Log.d(CONTACT_DATABASE, "insert end");
        if (inserted > 0) {
            return true;
        }
        return false;
    }


    public ArrayList<ContactModel> getAllContact() {
        this.open();
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        //Log.d(CONTACT_DATABASE, "get all start");

        Cursor cursor = sqLiteDatabase.query(DBOpenHelper.DB_NAME, null, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id_idx = cursor.getColumnIndex(DBOpenHelper.COL_ID);
                int name_idx = cursor.getColumnIndex(DBOpenHelper.COL_NAME);
                int contact_idx = cursor.getColumnIndex(DBOpenHelper.COL_CONTACT);
                int day_idx = cursor.getColumnIndex(DBOpenHelper.COL_DAY);
                int month_idx = cursor.getColumnIndex(DBOpenHelper.COL_MONTH);
                int year_idx = cursor.getColumnIndex(DBOpenHelper.COL_YEAR);
                int hour_idx = cursor.getColumnIndex(DBOpenHelper.COL_HOUR);
                int min_idx = cursor.getColumnIndex(DBOpenHelper.COL_MINUTE);
                int wish_idx = cursor.getColumnIndex(DBOpenHelper.COL_WISH);
                int msgState_idx = cursor.getColumnIndex(DBOpenHelper.COL_MSG_STATE);
                int notiState_idx = cursor.getColumnIndex(DBOpenHelper.COL_NOTI_STATE);
                arrayList.add(new ContactModel(
                        cursor.getInt(id_idx),
                        cursor.getString(name_idx),
                        cursor.getString(contact_idx),
                        cursor.getInt(day_idx),
                        cursor.getInt(month_idx),
                        cursor.getInt(year_idx),
                        cursor.getInt(hour_idx),
                        cursor.getInt(min_idx),
                        cursor.getString(wish_idx),
                        cursor.getString(msgState_idx),
                        cursor.getString(notiState_idx)
                    )
                );
            } while (cursor.moveToNext());
        }
        this.close();
        cursor.close();
        //Log.d(CONTACT_DATABASE, "get all end");
        return arrayList;
    }

    public boolean deleteItem(ContactModel contactModel) {
        this.open();
        int deleted = sqLiteDatabase.delete(DBOpenHelper.DB_NAME,
                sqLiteOpenHelper.COL_ID + "=?",
                new String[]{String.valueOf(contactModel.getId())});
        this.close();
        if (deleted > 0) {
            return true;
        } else {
            return false;
        }

    }


    public boolean updateItem(ContactModel contactModel){
        this.open();

        ContentValues contentValues = new ContentValues();
        Log.d(CONTACT_DATABASE, "update start");
        contentValues.put(DBOpenHelper.COL_NAME, contactModel.getName());
        contentValues.put(DBOpenHelper.COL_CONTACT, contactModel.getContact());
        contentValues.put(DBOpenHelper.COL_DAY, contactModel.getDay());
        contentValues.put(DBOpenHelper.COL_MONTH, contactModel.getMonth());
        contentValues.put(DBOpenHelper.COL_YEAR, contactModel.getYear());
        contentValues.put(DBOpenHelper.COL_HOUR, contactModel.getHour());
        contentValues.put(DBOpenHelper.COL_MINUTE, contactModel.getMinute());
        contentValues.put(DBOpenHelper.COL_WISH, contactModel.getWish());
        contentValues.put(DBOpenHelper.COL_MSG_STATE, contactModel.getMsgState());
        contentValues.put(DBOpenHelper.COL_NOTI_STATE, contactModel.getNotificationState());

        int updated =   sqLiteDatabase.update(DBOpenHelper.DB_NAME,contentValues,DBOpenHelper.COL_ID + "= ?",new String[]{String.valueOf(contactModel.getId())});
        this.close();
        Log.d(CONTACT_DATABASE, "update end "+ updated);
        if (updated > 0) {
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<ContactModel> getTodayBirth() {
        this.open();
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        //Log.d(CONTACT_DATABASE, "get all start");

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        Cursor cursor = sqLiteDatabase.query(DBOpenHelper.DB_NAME,
                null,
                DBOpenHelper.COL_DAY+"=? AND "+ DBOpenHelper.COL_MONTH+"= ?",
                new String[]{String.valueOf(day), String.valueOf(month)},
                null,null,null,null
                );
        if (cursor.moveToFirst()) {
            do {
                int id_idx = cursor.getColumnIndex(DBOpenHelper.COL_ID);
                int name_idx = cursor.getColumnIndex(DBOpenHelper.COL_NAME);
                int contact_idx = cursor.getColumnIndex(DBOpenHelper.COL_CONTACT);
                int day_idx = cursor.getColumnIndex(DBOpenHelper.COL_DAY);
                int month_idx = cursor.getColumnIndex(DBOpenHelper.COL_MONTH);
                int year_idx = cursor.getColumnIndex(DBOpenHelper.COL_YEAR);
                int hour_idx = cursor.getColumnIndex(DBOpenHelper.COL_HOUR);
                int min_idx = cursor.getColumnIndex(DBOpenHelper.COL_MINUTE);
                int wish_idx = cursor.getColumnIndex(DBOpenHelper.COL_WISH);
                int msgState_idx = cursor.getColumnIndex(DBOpenHelper.COL_MSG_STATE);
                int notiState_idx = cursor.getColumnIndex(DBOpenHelper.COL_NOTI_STATE);
                arrayList.add(new ContactModel(
                                cursor.getInt(id_idx),
                                cursor.getString(name_idx),
                                cursor.getString(contact_idx),
                                cursor.getInt(day_idx),
                                cursor.getInt(month_idx),
                                cursor.getInt(year_idx),
                                cursor.getInt(hour_idx),
                                cursor.getInt(min_idx),
                                cursor.getString(wish_idx),
                                cursor.getString(msgState_idx),
                                cursor.getString(notiState_idx)
                        )
                );
            } while (cursor.moveToNext());
        }
        this.close();
        cursor.close();
        //Log.d(CONTACT_DATABASE, "get all end");
        return arrayList;
    }


    public ArrayList<ContactModel> getTommorowBirth() {
        this.open();
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        //Log.d(CONTACT_DATABASE, "get all start");

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 1);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);


        Cursor cursor = sqLiteDatabase.query(DBOpenHelper.DB_NAME,
                null,
                DBOpenHelper.COL_DAY+"=? AND "+ DBOpenHelper.COL_MONTH+"= ?",
                new String[]{String.valueOf(day), String.valueOf(month)},
                null,null,null,null
        );
        if (cursor.moveToFirst()) {
            do {
                int id_idx = cursor.getColumnIndex(DBOpenHelper.COL_ID);
                int name_idx = cursor.getColumnIndex(DBOpenHelper.COL_NAME);
                int contact_idx = cursor.getColumnIndex(DBOpenHelper.COL_CONTACT);
                int day_idx = cursor.getColumnIndex(DBOpenHelper.COL_DAY);
                int month_idx = cursor.getColumnIndex(DBOpenHelper.COL_MONTH);
                int year_idx = cursor.getColumnIndex(DBOpenHelper.COL_YEAR);
                int hour_idx = cursor.getColumnIndex(DBOpenHelper.COL_HOUR);
                int min_idx = cursor.getColumnIndex(DBOpenHelper.COL_MINUTE);
                int wish_idx = cursor.getColumnIndex(DBOpenHelper.COL_WISH);
                int msgState_idx = cursor.getColumnIndex(DBOpenHelper.COL_MSG_STATE);
                int notiState_idx = cursor.getColumnIndex(DBOpenHelper.COL_NOTI_STATE);
                arrayList.add(new ContactModel(
                                cursor.getInt(id_idx),
                                cursor.getString(name_idx),
                                cursor.getString(contact_idx),
                                cursor.getInt(day_idx),
                                cursor.getInt(month_idx),
                                cursor.getInt(year_idx),
                                cursor.getInt(hour_idx),
                                cursor.getInt(min_idx),
                                cursor.getString(wish_idx),
                                cursor.getString(msgState_idx),
                                cursor.getString(notiState_idx)
                        )
                );
            } while (cursor.moveToNext());
        }
        this.close();
        cursor.close();
        //Log.d(CONTACT_DATABASE, "get all end");
        return arrayList;
    }

    public ArrayList<ContactModel> getMonthBirth() {
        this.open();
        ArrayList<ContactModel> arrayList = new ArrayList<>();
        //Log.d(CONTACT_DATABASE, "get all start");

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);

        Cursor cursor = sqLiteDatabase.query(DBOpenHelper.DB_NAME,
                null,
                DBOpenHelper.COL_MONTH+"= ?",
                new String[]{String.valueOf(month)},
                null,null,null,null
        );
        if (cursor.moveToFirst()) {
            do {
                int id_idx = cursor.getColumnIndex(DBOpenHelper.COL_ID);
                int name_idx = cursor.getColumnIndex(DBOpenHelper.COL_NAME);
                int contact_idx = cursor.getColumnIndex(DBOpenHelper.COL_CONTACT);
                int day_idx = cursor.getColumnIndex(DBOpenHelper.COL_DAY);
                int month_idx = cursor.getColumnIndex(DBOpenHelper.COL_MONTH);
                int year_idx = cursor.getColumnIndex(DBOpenHelper.COL_YEAR);
                int hour_idx = cursor.getColumnIndex(DBOpenHelper.COL_HOUR);
                int min_idx = cursor.getColumnIndex(DBOpenHelper.COL_MINUTE);
                int wish_idx = cursor.getColumnIndex(DBOpenHelper.COL_WISH);
                int msgState_idx = cursor.getColumnIndex(DBOpenHelper.COL_MSG_STATE);
                int notiState_idx = cursor.getColumnIndex(DBOpenHelper.COL_NOTI_STATE);
                arrayList.add(new ContactModel(
                                cursor.getInt(id_idx),
                                cursor.getString(name_idx),
                                cursor.getString(contact_idx),
                                cursor.getInt(day_idx),
                                cursor.getInt(month_idx),
                                cursor.getInt(year_idx),
                                cursor.getInt(hour_idx),
                                cursor.getInt(min_idx),
                                cursor.getString(wish_idx),
                                cursor.getString(msgState_idx),
                                cursor.getString(notiState_idx)
                        )
                );
            } while (cursor.moveToNext());
        }
        this.close();
        cursor.close();
        //Log.d(CONTACT_DATABASE, "get all end");
        return arrayList;
    }

    public boolean deleteAll(){
        this.open();
        int dd = sqLiteDatabase.delete(DBOpenHelper.DB_NAME,null,null);
        this.close();
        if(dd>0)return true;
        else return false;
    }
}
