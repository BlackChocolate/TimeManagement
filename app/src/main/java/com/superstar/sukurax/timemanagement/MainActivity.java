package com.superstar.sukurax.timemanagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;

public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    static DatebaseHelper datebaseHelper;
    TextView task_text1,task_text2,task_text3,task_text4,userName,userEmail;
    private NotificationManager mNotificationManager;
    public static final int DEFAULT_NOTIFICATION_ID = 1;
    public static int undoneTask=0;
    LinearLayout urgent1linearLayout,urgent2linearLayout,urgent3linearLayout,urgent4linearLayout,urgentToplinearLayout,urgentBottomlinearLayout,nav,nav_header_main;
    SharedPreferences sp;
    ImageView userPic;
    private  NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeColor();
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        nav_header_main=(LinearLayout)navigationView.inflateHeaderView(R.layout.nav_header_main);
        nav=(LinearLayout)nav_header_main.findViewById(R.id.nav);

        userPic=(ImageView)nav_header_main.findViewById(R.id.userPic);
        userName=(TextView)nav_header_main.findViewById(R.id.userName);
        userEmail=(TextView)nav_header_main.findViewById(R.id.userEmail);


        sp= getSharedPreferences("TimeManagement", Context.MODE_PRIVATE);
        switch (sp.getInt("skin_num", 1)){
            case 1:
                nav.setBackgroundColor(getResources().getColor(R.color.skinColor1_2));
                break;
            case 2:
                nav.setBackgroundColor(getResources().getColor(R.color.skinColor2_2));
                break;
            case 3:
                nav.setBackgroundColor(getResources().getColor(R.color.skinColor3_2));
                break;
            case 4:
                nav.setBackgroundColor(getResources().getColor(R.color.skinColor4_2));
                break;
            case 5:
                nav.setBackgroundColor(getResources().getColor(R.color.skinColor5_2));
                break;
            case 6:
                nav.setBackgroundColor(getResources().getColor(R.color.skinColor6_2));
                break;
            default:
                break;
        }

//        创建SQLite数据库
        datebaseHelper=new DatebaseHelper(this,"TimeManagement.db3",1);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        urgentToplinearLayout=(LinearLayout)findViewById(R.id.urgentToplinearLayout);
        urgentBottomlinearLayout=(LinearLayout)findViewById(R.id.urgentBottomlinearLayout);
        urgent1linearLayout=(LinearLayout)findViewById(R.id.urgent1linearLayout);
        urgent2linearLayout=(LinearLayout)findViewById(R.id.urgent2linearLayout);
        urgent3linearLayout=(LinearLayout)findViewById(R.id.urgent3linearLayout);
        urgent4linearLayout=(LinearLayout)findViewById(R.id.urgent4linearLayout);
        urgent1linearLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
//                返回值为0，visible；返回值为4，invisible；返回值为8，gone。
                if(urgent2linearLayout.getVisibility()==0&urgent3linearLayout.getVisibility()==0&urgent4linearLayout.getVisibility()==0){
                    urgent2linearLayout.setVisibility(View.GONE);
                    urgentBottomlinearLayout.setVisibility(View.GONE);
                    task_text1.setTextSize(16);
                }else {
                    urgent2linearLayout.setVisibility(View.VISIBLE);
                    urgentBottomlinearLayout.setVisibility(View.VISIBLE);
                    task_text1.setTextSize(14);
                }

            }
        });
        urgent2linearLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
//                返回值为0，visible；返回值为4，invisible；返回值为8，gone。
                if(urgent1linearLayout.getVisibility()==0&urgent3linearLayout.getVisibility()==0&urgent4linearLayout.getVisibility()==0){
                    urgent1linearLayout.setVisibility(View.GONE);
                    urgentBottomlinearLayout.setVisibility(View.GONE);
                    task_text2.setTextSize(16);
                }else {
                    urgent1linearLayout.setVisibility(View.VISIBLE);
                    urgentBottomlinearLayout.setVisibility(View.VISIBLE);
                    task_text2.setTextSize(14);
                }

            }
        });
        urgent3linearLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
//                返回值为0，visible；返回值为4，invisible；返回值为8，gone。
                if(urgent2linearLayout.getVisibility()==0&urgent1linearLayout.getVisibility()==0&urgent4linearLayout.getVisibility()==0){
                    urgentToplinearLayout.setVisibility(View.GONE);
                    urgent4linearLayout.setVisibility(View.GONE);
                    task_text3.setTextSize(16);
                }else {
                    urgentToplinearLayout.setVisibility(View.VISIBLE);
                    urgent4linearLayout.setVisibility(View.VISIBLE);
                    task_text3.setTextSize(14);
                }

            }
        });
        urgent4linearLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
//                返回值为0，visible；返回值为4，invisible；返回值为8，gone。
                if(urgent2linearLayout.getVisibility()==0&urgent3linearLayout.getVisibility()==0&urgent1linearLayout.getVisibility()==0){
                    urgentToplinearLayout.setVisibility(View.GONE);
                    urgent3linearLayout.setVisibility(View.GONE);
                    task_text4.setTextSize(16);
                }else {
                    urgentToplinearLayout.setVisibility(View.VISIBLE);
                    urgent3linearLayout.setVisibility(View.VISIBLE);
                    task_text4.setTextSize(14);
                }

            }
        });
    }

    private  void setNotification() {
        //设置一个Intent,不然点击通知不会自动消失
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_pic)
                .setContentTitle("明日计划通")
                .setContentText("执行中任务:"+undoneTask)
                .setContentIntent(resultPendingIntent);
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_AUTO_CANCEL 表示该通知能被状态栏的清除按钮给清除掉
        //等价于 builder.setAutoCancel(true);
        notification.flags |= Notification.FLAG_NO_CLEAR;
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, notification);
    }

    public void setThemeColor(){
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
        super.onResume();

        if(AVUser.getCurrentUser()!=null){
            String currentUsername = AVUser.getCurrentUser().getUsername();
            userName.setText(currentUsername);
            userEmail.setText("当前用户懒的写~");

        }

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
        undoneTask=0;
        //加载SQLite数据库
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select * from task ",new String[]{}
        );
        String value1 = "",value2="",value3="",value4 = "";
        if (cursor.moveToFirst()) {
            do{
                switch (cursor.getString(cursor.getColumnIndex("type"))) {
                    case "1":
                        value1+=cursor.getString(cursor.getColumnIndex("content"))+"\n";
                        break;
                    case "2":
                        value2+=cursor.getString(cursor.getColumnIndex("content"))+"\n";
                        break;
                    case "3":
                        value3+=cursor.getString(cursor.getColumnIndex("content"))+"\n";
                        break;
                    case "4":
                        value4+=cursor.getString(cursor.getColumnIndex("content"))+"\n";
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "获取星期错误", Toast.LENGTH_SHORT).show();
                        break;
                }
                if(cursor.getString(cursor.getColumnIndex("state")).equals("0")){
                    undoneTask+=1;
                }

            }while (cursor.moveToNext());
        }
        task_text1.setText(value1);
        task_text2.setText(value2);
        task_text3.setText(value3);
        task_text4.setText(value4);

        setNotification();
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
        if (id == R.id.synchronize) {
//      setContentView(R.layout.remind_setting);
            return true;
        }else if(id==R.id.login){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.setting1) {
            Intent intent =  new Intent(getApplication(),CurrentActivity.class);
            startActivity(intent);
        } else if (id == R.id.setting2) {
            Intent intent =  new Intent(getApplication(),TaskHistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.setting3) {
            Intent intent =  new Intent(getApplication(),NoteActivity.class);
            startActivity(intent);
        } else if (id == R.id.setting4) {
            Intent intent =  new Intent(getApplication(),CalendarActivity.class);
            startActivity(intent);
        } else if (id == R.id.setting5) {
            Intent intent =  new Intent(getApplication(),RemindSettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.setting6) {
            Intent intent =  new Intent(getApplication(),SkinChangeActivity.class);
            startActivity(intent);
        }else if (id == R.id.setting7) {
//            Intent intent =  new Intent(getApplication(),RemindSettingActivity.class);
//            startActivity(intent);
        } else if (id == R.id.setting8) {
            Intent intent =  new Intent(getApplication(),LoginActivity.class);
            startActivity(intent);
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
