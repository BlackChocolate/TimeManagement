package com.superstar.sukurax.timemanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatebaseHelper extends SQLiteOpenHelper {
    //syncState
    //待添加1、添加中2、已添加3、待修该4、修改中5、已修改6、待删除7、删除中8、已删除9、待更新10、更新中11、已更新12
    final String CREATE_TABLE_TASK=
            "create table task(_id integer primary "+
                    "key autoincrement,content,type,time,state,syncState default 1)";
    final String CREATE_TABLE_NOTE=
            "create table note(_id integer primary "+
                    "key autoincrement,note_time,note_content,syncState default 1)";
    public DatebaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TASK);
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        System.out.print("--onUpdate Called--"+
            oldVersion+">"+newVersion);

    }
}
