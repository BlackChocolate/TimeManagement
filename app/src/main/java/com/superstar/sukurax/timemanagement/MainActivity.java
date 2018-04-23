package com.superstar.sukurax.timemanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    SQLiteDatabase db;
    static DatebaseHelper datebaseHelper;
    TextView task_text1,task_text2,task_text3,task_text4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplication(),EditActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        task_text1=(TextView)findViewById(R.id.task_text1);
        task_text2=(TextView)findViewById(R.id.task_text2);
        task_text3=(TextView)findViewById(R.id.task_text3);
        task_text4=(TextView)findViewById(R.id.task_text4);

//        创建SQLite数据库
        datebaseHelper=new DatebaseHelper(this,"TimeManagement.db3",1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取系统的日期,设置标题
        Calendar calendar = Calendar.getInstance();
        String weekDay;
        switch (String.valueOf(calendar.get(Calendar.DAY_OF_WEEK))) {
            case "1":
                weekDay="星期日";
                break;
            case "2":
                weekDay="星期一";
                break;
            case "3":
                weekDay="星期二";
                break;
            case "4":
                weekDay="星期三";
                break;
            case "5":
                weekDay="星期四";
                break;
            case "6":
                weekDay="星期五";
                break;
            case "7":
                weekDay="星期六";
                break;
            default:
                weekDay="获取星期错误"+String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
                break;
        }
        toolbar.setTitle(" "+(calendar.get(Calendar.YEAR))+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+"     "+weekDay);

        //加载SQLite数据库
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select * from task ",new String[]{}
        );
        String value1 = "既定\n",value2="随机\n",value3="无定\n",value4 = "随意\n";
        if (cursor.moveToFirst()) {
            do{
                switch (cursor.getString(cursor.getColumnIndex("type"))) {
                    case "1":
                        value1+=cursor.getString(cursor.getColumnIndex("content"))+" "+cursor.getString(cursor.getColumnIndex("time"))+"\n";
                        break;
                    case "2":
                        value2+=cursor.getString(cursor.getColumnIndex("content"))+" "+cursor.getString(cursor.getColumnIndex("time"))+"\n";
                        break;
                    case "3":
                        value3+=cursor.getString(cursor.getColumnIndex("content"))+" "+cursor.getString(cursor.getColumnIndex("time"))+"\n";
                        break;
                    case "4":
                        value4+=cursor.getString(cursor.getColumnIndex("content"))+" "+cursor.getString(cursor.getColumnIndex("time"))+"\n";
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "获取星期错误", Toast.LENGTH_SHORT).show();
                        break;
                }

            }while (cursor.moveToNext());
        }
        task_text1.setText(value1);
        task_text2.setText(value2);
        task_text3.setText(value3);
        task_text4.setText(value4);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setContentView(R.layout.remind_setting);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(datebaseHelper!=null){
            datebaseHelper.close();
        }
    }
}
