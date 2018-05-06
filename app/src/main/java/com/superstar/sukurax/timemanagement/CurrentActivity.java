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
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.superstar.sukurax.timemanagement.MainActivity.datebaseHelper;

public class CurrentActivity extends Activity {
    ImageView back_toolbar_pic;
    TextView back_toolbar_text;
    private SimpleAdapter undoneAdapter,doneAdapter;
    private List<Switch> list_switch;
    private ListView undoneList,doneList;
    private List<Map<String, Object>> undoneData = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> doneData = new ArrayList<Map<String, Object>>();
    SharedPreferences sp;
    android.support.v7.widget.Toolbar back_toolbar;
    String date,time;
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
            default:
                break;
        }

        setContentView(R.layout.current_task);
        undoneList=(ListView)findViewById(R.id.undone_list);
        doneList=(ListView)findViewById(R.id.done_list);

        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
        back_toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.back_toolbar);
        back_toolbar_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    void getData(){
        MainActivity.undoneTask=0;
        undoneData.clear();
        doneData.clear();
        //加载SQLite数据库
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select * from task ",new String[]{}
        );
        if (cursor.moveToFirst()) {
            do{
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
                item.put("time", cursor.getString(cursor.getColumnIndex("time")));
                item.put("content", cursor.getString(cursor.getColumnIndex("content")));
                if(cursor.getString(cursor.getColumnIndex("state")).equals("0")){
                    undoneData.add(item);
                }else {
                    doneData.add(item);
                }
            }while (cursor.moveToNext());
        }

        undoneAdapter = new SimpleAdapter(
                this,
                undoneData,
                R.layout.task_list_item,
                new String[] { "_id","time", "content" },// 与下面数组元素要一一对应
                new int[] {R.id.task_id,R.id.task_time, R.id.task_content });
        undoneList.setAdapter(undoneAdapter);

        doneAdapter = new SimpleAdapter(
                this,
                doneData,
                R.layout.task_list_item,
                new String[] { "_id","time", "content" },// 与下面数组元素要一一对应
                new int[] {R.id.task_id,R.id.task_time, R.id.task_content });
        doneList.setAdapter(doneAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void changeData(){
        undoneAdapter.notifyDataSetChanged();
        doneAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        back_toolbar_text.setText("当前任务提醒");

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
            default:
                break;
        }


        getData();
        if(!undoneData.isEmpty()){
            undoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(CurrentActivity.this);
                    normalDialog.setTitle("改为已完成？");
                    normalDialog.setMessage("该事项将取消提醒");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String,Object> map = undoneData.get(i);
                                    String _id = map.get("_id").toString();

                                    SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                                    db.execSQL("UPDATE task SET state = ? WHERE _id = ? ",new String[]{"1",_id});

                                    getData();
                                    changeData();
                                    onResume();
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

        }
        if(!doneData.isEmpty()){
            doneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(CurrentActivity.this);
                    normalDialog.setTitle("改为执行中？");
                    normalDialog.setMessage("该事项将添加提醒");
                    normalDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String,Object> map = doneData.get(i);
                                    String _id = map.get("_id").toString();

                                    SQLiteDatabase db = MainActivity.datebaseHelper.getWritableDatabase();
                                    db.execSQL("UPDATE task SET state = ? WHERE _id = ? ",new String[]{"0",_id});

                                    getData();
                                    changeData();
                                    onResume();
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
        }



    }
}
