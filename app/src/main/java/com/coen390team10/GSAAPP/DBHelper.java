package com.coen390team10.GSAAPP;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {
        super(context, "DATABASETESTVERSION000000000", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        onCreate(sqLiteDatabase);
    }

    public boolean registerUser(String username, String password){ // sign up
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);

        long result = sqLiteDatabase.insert("users", null, contentValues);
        return result == -1 ? false : true;


    }

    public boolean checkUser(String username, String password){ // sign in
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String[] columns = {"ID"};
        String s = "username =? and password =?";
        String[] sInfo = {username,password};
        Cursor c = sqLiteDatabase.query("users",columns,s,sInfo,null,null,null);
        int count = c.getCount();
        sqLiteDatabase.close();
        c.close();
        return count > 0 ? true : false;
    }

    public boolean isUsernameExists(String username){ // check if user already exists in database
        String query = "SELECT * FROM users WHERE username =? ";
        String[] sA = {username};
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery(query,sA);
        int count = c.getCount();
        c.close();
        return count>=1;
    }
}
