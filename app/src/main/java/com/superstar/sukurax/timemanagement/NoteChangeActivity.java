package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
    TextView back_toolbar_text,note_change_cancel,note_change_delete,note_change_confirm;
    EditText note_edittext;
    String date,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_change);

        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
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
                                SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                                db.execSQL("DELETE FROM note WHERE _id = ? ",new String[]{getIntent().getStringExtra("_id")});

                                Intent intent =  new Intent(getApplication(),NoteActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
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

        note_change_confirm=(TextView)findViewById(R.id.note_change_confirm);
        note_change_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(note_edittext.getText().toString().isEmpty()){
                    Toast.makeText(NoteChangeActivity.this, "便签不能为空", Toast.LENGTH_SHORT).show();
                }else {

                    SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                    db.execSQL("UPDATE note SET note_content = ? WHERE _id = ? ",new String[]{note_edittext.getText().toString(),getIntent().getStringExtra("_id")});

                    Intent intent =  new Intent(getApplication(),NoteActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        back_toolbar_text.setText("查看便签");

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
        super.onBackPressed();
    }
}