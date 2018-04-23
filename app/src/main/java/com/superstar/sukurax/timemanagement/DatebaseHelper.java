package com.superstar.sukurax.timemanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatebaseHelper extends SQLiteOpenHelper {
    final String CREATE_TABLE_SQL=
            "create table task(_id integer primary "+
                    "key autoincrement,content,type,time)";

    public DatebaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        System.out.print("--onUpdate Called--"+
            oldVersion+">"+newVersion);

    }
}
