package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.superstar.sukurax.timemanagement.MainActivity.datebaseHelper;

public class TaskHistoryActivity extends Activity {
    ImageView back_toolbar_pic;
    TextView back_toolbar_text;
    LinearLayout switch_empty,switch_something;
    private SimpleAdapter simpleAdapter;
    private ListView listView;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
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
        setContentView(R.layout.task_history);
        listView = (ListView) findViewById(R.id.task_history_list);
        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
        back_toolbar=(Toolbar)findViewById(R.id.back_toolbar);
        back_toolbar_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        switch_empty=(LinearLayout)findViewById(R.id.switch_empty);
        switch_something=(LinearLayout)findViewById(R.id.switch_something);

    }

    @Override
    protected void onResume() {
        super.onResume();
        back_toolbar_text.setText("历史任务");
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

        data.clear();

        //加载SQLite数据库
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select * from task ",new String[]{}
        );
        if (cursor.moveToFirst()) {
            do{
                if(!cursor.getString(cursor.getColumnIndex("_id")).equals("7")){
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("_id", cursor.getString(cursor.getColumnIndex("_id")));
                    item.put("time", cursor.getString(cursor.getColumnIndex("time")));
                    item.put("content", cursor.getString(cursor.getColumnIndex("content")));
                    if(cursor.getString(cursor.getColumnIndex("state")).equals("1")){
                        item.put("state", "未完成");
                    }else if(cursor.getString(cursor.getColumnIndex("state")).equals("0")){
                        item.put("state", "已完成");
                    }
                    data.add(item);
                }
            }while (cursor.moveToNext());
        }

        if(data.isEmpty()){
            switch_empty.setVisibility(View.VISIBLE);
            switch_something.setVisibility(View.GONE);
        }else {
            simpleAdapter = new SimpleAdapter(
                    this,
                    data,
                    R.layout.task_history_list_item,
                    new String[] { "_id","time", "content","state" },// 与下面数组元素要一一对应
                    new int[] {R.id.task_history_id,R.id.task_history_time, R.id.task_history_content ,R.id.task_history_state});

            listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Map<String,Object> map = data.get(i);
                    String _id = map.get("_id").toString();

//                    Intent intent =  new Intent(getApplication(),NoteChangeActivity.class);
//                    intent.putExtra("_id",_id);
//                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
