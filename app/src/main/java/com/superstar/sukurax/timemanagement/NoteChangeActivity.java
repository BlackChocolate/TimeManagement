package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.superstar.sukurax.timemanagement.MainActivity.datebaseHelper;

public class NoteChangeActivity extends Activity{
    ImageView back_toolbar_pic;
    TextView back_toolbar_text,note_change_cancel,note_change_delete,note_change_send;
    EditText note_edittext;
    String date,time;
    SharedPreferences sp;
    Toolbar back_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp= getSharedPreferences("TimeManagement", Context.MODE_PRIVATE);
        switch (sp.getInt("skin_num", 1)){
            case 1:
                setTheme(R.style.AppTheme);
                break;
            case 2:
                setTheme(R.style.AppTheme2);
                break;
            case 3:
                setTheme(R.style.AppTheme3);
                break;
            case 4:
                setTheme(R.style.AppTheme4);
                break;
            case 5:
                setTheme(R.style.AppTheme5);
                break;
            case 6:
                setTheme(R.style.AppTheme6);
                break;
            default:
                break;
        }
        setContentView(R.layout.note_change);

        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
        back_toolbar=(Toolbar)findViewById(R.id.back_toolbar);
        back_toolbar_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        note_edittext=(EditText)findViewById(R.id.note_edittext);
        note_change_cancel=(TextView)findViewById(R.id.note_change_cancel);
        note_change_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        note_change_delete=(TextView)findViewById(R.id.note_change_delete);
        note_change_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(NoteChangeActivity.this);
                normalDialog.setTitle("确认");
                normalDialog.setMessage("确定要删除该便签吗");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //加载SQLite数据库
                                Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                                        "select * from note where _id="+getIntent().getStringExtra("_id"),new String[]{}
                                );
                                if (cursor.moveToFirst()) {
                                    do{
                                        if(!cursor.getString(cursor.getColumnIndex("syncState")).equals("1")){
                                            SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                                            db.execSQL("UPDATE note SET syncState=? WHERE _id = ? ",new String[]{"7",getIntent().getStringExtra("_id")});
                                            Intent intent =  new Intent(getApplication(),NoteActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }else {
                                            SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                                            db.execSQL("delete from  note WHERE _id = ? ",new String[]{getIntent().getStringExtra("_id")});
                                            Intent intent =  new Intent(getApplication(),NoteActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    }while (cursor.moveToNext());
                                }

                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                            }
                        });
                // 显示
                normalDialog.show();
            }
        });

        note_change_send=(TextView)findViewById(R.id.note_change_send);
        note_change_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(note_edittext.getText().toString().isEmpty()){
                    Toast.makeText(NoteChangeActivity.this, "便签不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "share");
                    intent.putExtra(Intent.EXTRA_TEXT, note_edittext.getText().toString());
                    startActivity(Intent.createChooser(intent, "分享"));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        back_toolbar_text.setText("查看便签");
        switch (sp.getInt("skin_num", 1)){
            case 1:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor1_2));
                break;
            case 2:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor2_2));
                break;
            case 3:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor3_2));
                break;
            case 4:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor4_2));
                break;
            case 5:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor5_2));
                break;
            case 6:
                back_toolbar.setBackgroundColor(getResources().getColor(R.color.skinColor6_2));
                break;
            default:
                break;
        }

        //加载SQLite数据库
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select note_content from note where _id = ?",new String[]{getIntent().getStringExtra("_id")}
        );
        if (cursor.moveToFirst()) {
            do{
                note_edittext.setText(cursor.getString(cursor.getColumnIndex("note_content")));
            }while (cursor.moveToNext());
        }


        Calendar calendar = Calendar.getInstance();
        date=(calendar.get(Calendar.YEAR))+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        time=calendar.get(Calendar.HOUR_OF_DAY)+"："+calendar.get(Calendar.MINUTE);

    }

    @Override
    public void onBackPressed() {
        String str=note_edittext.getText().toString();
        String _id=getIntent().getStringExtra("_id");
        if(str.isEmpty()){
            Toast.makeText(NoteChangeActivity.this, "便签不能为空", Toast.LENGTH_SHORT).show();
        }else {
            //加载SQLite数据库
            Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                    "select * from note where _id="+_id,new String[]{}
            );
            SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
            if (cursor.moveToFirst()) {
                do{
                   if(!cursor.getString(cursor.getColumnIndex("syncState")).equals("1")){
                       db.execSQL("UPDATE note SET note_content = ? WHERE _id = ? ",new String[]{str,_id});
                       db.execSQL("UPDATE note SET syncState = ? WHERE _id = ? ",new String[]{"4",_id});
                   }else {
                       db.execSQL("UPDATE note SET note_content = ? WHERE _id = ? ",new String[]{str,_id});
                   }
                }while (cursor.moveToNext());
            }

        }
        super.onBackPressed();
    }
}
