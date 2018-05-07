package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class NoteAddActivity extends Activity {
    ImageView back_toolbar_pic;
    TextView back_toolbar_text,note_add_cancel,note_add_confirm;
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
        setContentView(R.layout.note_add);
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
        note_add_cancel=(TextView)findViewById(R.id.note_add_cancel);
        note_add_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        note_add_confirm=(TextView)findViewById(R.id.note_add_confirm);
        note_add_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(note_edittext.getText().toString().isEmpty()){
                    Toast.makeText(NoteAddActivity.this, "便签不能为空", Toast.LENGTH_SHORT).show();
                }else {

                    SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                    db.execSQL("insert into note (note_time,note_content) values(?,?)",new String[]{date+" "+time, note_edittext.getText().toString()});

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
        back_toolbar_text.setText("新建便签");
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

        Calendar calendar = Calendar.getInstance();
        date=(calendar.get(Calendar.YEAR))+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        time=calendar.get(Calendar.HOUR_OF_DAY)+"："+calendar.get(Calendar.MINUTE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
