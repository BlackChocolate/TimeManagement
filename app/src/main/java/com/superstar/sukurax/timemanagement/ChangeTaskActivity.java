package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.superstar.sukurax.timemanagement.MainActivity.datebaseHelper;

public class ChangeTaskActivity extends Activity {
    ImageView back;
    TextView back_text,edit_text;
    SharedPreferences sp;
    Toolbar back_toolbar;

    RadioGroup edit_radioG;
    DatePicker edit_date;
    TimePicker edit_time;
    Button edit_cancel,edit_update;
    RadioButton edit_radioBt1,edit_radioBt2,edit_radioBt3,edit_radioBt4;
    private Calendar cl;
    private int year,month,day,hour,minute;
    Switch remindSwitch;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    String content,type,time,state,task_date,task_time;
    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.change_task);

        back=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_text=(TextView)findViewById(R.id.back_toolbar_text);
        back_toolbar=(Toolbar)findViewById(R.id.back_toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edit_text=(TextView)findViewById(R.id.edit_text);
        edit_radioG=(RadioGroup)findViewById(R.id.edit_radioG);
        edit_date=(DatePicker)findViewById(R.id.date_picker);
        edit_time=(TimePicker)findViewById(R.id.time_picker);
        edit_cancel=(Button)findViewById(R.id.edit_cancel);
        edit_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        edit_update=(Button)findViewById(R.id.edit_update);

        edit_radioBt1=(RadioButton)findViewById(R.id.edit_radioBt1);
        edit_radioBt2=(RadioButton)findViewById(R.id.edit_radioBt2);
        edit_radioBt3=(RadioButton)findViewById(R.id.edit_radioBt3);
        edit_radioBt4=(RadioButton)findViewById(R.id.edit_radioBt4);


    }

    @Override
    protected void onResume() {
        super.onResume();
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

        final String task_id=getIntent().getStringExtra("task_id");
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select * from task ",new String[]{}
        );

        if (cursor.moveToFirst()) {
            do{
                if(cursor.getString(cursor.getColumnIndex("_id")).equals(task_id)){
                    content=cursor.getString(cursor.getColumnIndex("content"));
                    type=cursor.getString(cursor.getColumnIndex("type"));
                    time=cursor.getString(cursor.getColumnIndex("time"));
                    state=cursor.getString(cursor.getColumnIndex("state"));
                }
            }while (cursor.moveToNext());
        }
        edit_text.setText(content);
        switch (type){
            case "1":
                edit_radioBt1.setChecked(true);
                break;
            case "2":
                edit_radioBt2.setChecked(true);
                break;
            case "3":
                edit_radioBt3.setChecked(true);
                break;
            case "4":
                edit_radioBt4.setChecked(true);
                break;
            default:
                break;
        }
        year=Integer.parseInt(time.split("-")[0]);
        month=Integer.parseInt(time.split("-")[1])-1;
        day=Integer.parseInt(time.split(" ")[0].split("-")[1]);
        hour=Integer.parseInt(time.split(" ")[1].split(":")[0]);
        minute=Integer.parseInt(time.split(" ")[1].split(":")[1]);
        edit_date.init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                // TODO Auto-generated method stub
                setTitle(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                task_date=(year-2000)+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            }
        });
        edit_time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                setTitle(hourOfDay+":"+minute);
                task_time=hourOfDay+":"+minute;
            }
        });

        if(state.equals("0")){
            remindSwitch.setChecked(false);
        }else if(state.equals("1")){
            remindSwitch.setChecked(true);
        }

        edit_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer type = 0;
                if(edit_text.getText().toString().equals("")){
                    Toast.makeText(ChangeTaskActivity.this,"任务未填写", Toast.LENGTH_SHORT).show();
                }else if(edit_text.getText().toString().length()>=11){
                    Toast.makeText(ChangeTaskActivity.this,"任务过长，请简略叙述", Toast.LENGTH_SHORT).show();
                }else if(edit_radioG.getCheckedRadioButtonId()!=R.id.edit_radioBt1 & edit_radioG.getCheckedRadioButtonId()!=R.id.edit_radioBt2& edit_radioG.getCheckedRadioButtonId()!=R.id.edit_radioBt3 & edit_radioG.getCheckedRadioButtonId()!=R.id.edit_radioBt4){
                    Toast.makeText(ChangeTaskActivity.this,"未选择类型", Toast.LENGTH_SHORT).show();
                }else {
                    switch (edit_radioG.getCheckedRadioButtonId()){
                        case R.id.edit_radioBt1:
                            type=1;
                            break;
                        case R.id.edit_radioBt2:
                            type=2;
                            break;
                        case R.id.edit_radioBt3:
                            type=3;
                            break;
                        case R.id.edit_radioBt4:
                            type=4;
                            break;
                        default:
                            Toast.makeText(ChangeTaskActivity.this, "类型错误", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    //0代表不提醒，1代表提醒
                    String temp="1";
                    if(remindSwitch.isChecked()){
                        temp="1";
                    }else if(!remindSwitch.isChecked()){
                        temp="0";
                    }
                    SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                    db.execSQL("update  task set  content=? and type=? and time=? and state=? where _id=?",new String[]{edit_text.getText().toString(), type.toString(),task_date+" "+task_time,temp,task_id});
                    Cursor cursor = db.rawQuery("select last_insert_rowid() from task",null);

                    //提醒服务

                    Intent intent =  new Intent(getApplication(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}