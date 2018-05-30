package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.superstar.sukurax.timemanagement.MainActivity.datebaseHelper;

public class TaskActivity extends Activity {
    ImageView back_toolbar_pic;
    TextView back_toolbar_text;
    SharedPreferences sp;
    android.support.v7.widget.Toolbar back_toolbar;

    TextView taskContent;
    Button task_confirm,task_delay;
    Integer alarmId;
    NumberPicker delay_time_picker;
    Integer timeTemp;
    String alarmContent;
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

    }

    @Override
    protected void onResume() {
        setContentView(R.layout.task_layout);
        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
        back_toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.back_toolbar);
        back_toolbar_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        super.onResume();
        back_toolbar_text.setText("提醒任务");
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

        taskContent=(TextView)findViewById(R.id.taskContent);
        task_confirm=(Button)findViewById(R.id.task_confirm);
        task_delay=(Button)findViewById(R.id.task_delay);

        alarmId=getIntent().getIntExtra("alarmId",0);
        alarmContent=getIntent().getStringExtra("alarmContent");
        Log.d("test",alarmId+"获取alarmId");
        //加载SQLite数据库
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select * from task where _id=?",new String[]{alarmId+""}
        );
        if (cursor.moveToFirst()) {
            Log.d("test","提醒内容:"+cursor.getString(cursor.getColumnIndex("content"))+" 优先级"+cursor.getString(cursor.getColumnIndex("type")));
            taskContent.setText(cursor.getString(cursor.getColumnIndex("content")));

        }
        switch (cursor.getString(cursor.getColumnIndex("type"))){
            case "1":
                taskContent.setBackgroundColor(getResources().getColor(R.color.urgent1));
                break;
            case "2":
                taskContent.setBackgroundColor(getResources().getColor(R.color.urgent2));
                break;
            case "3":
                taskContent.setBackgroundColor(getResources().getColor(R.color.urgent3));
                break;
            case "4":
                taskContent.setBackgroundColor(getResources().getColor(R.color.urgent4));
                break;
            default:
                Toast.makeText(TaskActivity.this, "获取类型失败", Toast.LENGTH_SHORT).show();
                break;
        }
        task_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1更改本地数据为不提醒
                SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                db.execSQL("update  task set state=? where _id=?",new String[]{0+"",alarmId+""});
                //2取消该广播
                alarmCancel(alarmId);
                //3取消该通知

                Intent intent =  new Intent(getApplication(),MainActivity.class);
                startActivity(intent);

            }
        });
        task_delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1弹出时间选择器，选择1-60分钟延后提醒
//                AlertDialog.Builder builder7 = new AlertDialog.Builder(
//                        TaskActivity.this);
//                builder7.setTitle("标题");
//                builder7.setIcon(R.mipmap.ic_launcher);
//                View numPickerView = LayoutInflater.from(TaskActivity.this).inflate(
//                        R.layout.delay_time_picker_layout, null);
//                delay_time_picker=(NumberPicker)numPickerView.findViewById(R.id.delay_time_picker);
//                delay_time_picker.setValue(5);
//                delay_time_picker.setMinValue(1);
//                delay_time_picker.setMaxValue(60);
//                delay_time_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                    @Override
//                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
//                        timeTemp=i1;
//                        Toast.makeText(TaskActivity.this, "选中了"+timeTemp, Toast.LENGTH_SHORT).show();
//                    }
//                });
//                delay_time_picker.setOnScrollListener(new NumberPicker.OnScrollListener() {
//                    @Override
//                    public void onScrollStateChange(NumberPicker numberPicker, int i) {
//
//                    }
//                });

                AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                //Intent设置要启动的组件，这里启动广播
                Intent myIntent = new Intent();
                myIntent.setAction(GlobalValues.TIMER_ACTION);

                myIntent.putExtra("alarmId",alarmId);
                myIntent.putExtra("alarmContent",alarmContent);
                //PendingIntent对象设置动作,启动的是Activity还是Service,或广播!
                //更新内容
                PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), alarmId, myIntent, FLAG_UPDATE_CURRENT);
                //注册闹钟
                alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5*60*1000, sender);
                //2更新该广播
                //3更新该通知
                notificationCancel(alarmId);

                Intent intent =  new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void alarmCancel(Integer alarmId){
        Intent myIntent = new Intent();
        myIntent.setAction(GlobalValues.TIMER_ACTION);

        //应获取点击的alarm_id
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), alarmId, myIntent,0);
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);

        notificationCancel(alarmId);

    }
    void notificationCancel(Integer alarmId){
        //取消通知
        NotificationManager m_NotificationManager=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        m_NotificationManager.cancel(alarmId);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
