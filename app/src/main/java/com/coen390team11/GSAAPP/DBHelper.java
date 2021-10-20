package com.coen390team11.GSAAPP;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHelper {/*extends SQLiteOpenHelper {

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public DBHelper(@Nullable Context context) {
        super(context, "GROCERYDATABASE1.3", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(sqLiteDatabase);
    }

    public boolean registerUser(String username, String password){ // sign up
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME,username);
        contentValues.put(COLUMN_PASSWORD,password);

        long result = sqLiteDatabase.insert(TABLE_USERS, null, contentValues);
        return result != -1;


    }

    public boolean checkUser(String username, String password){ // sign in
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String[] columns = {"ID"};
        String s = COLUMN_USERNAME + " =? and " + COLUMN_PASSWORD + " =?";
        String[] sInfo = {username,password};
        Cursor c = sqLiteDatabase.query(TABLE_USERS,columns,s,sInfo,null,null,null);
        int count = c.getCount();
        sqLiteDatabase.close();
        c.close();
        return count > 0;
    }

    public boolean isUsernameExists(String username){ // check if user already exists in database
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " =? ";
        String[] sA = {username};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(query,sA);
        int count = c.getCount();
        c.close();
        return count>=1;
    }*/
}
