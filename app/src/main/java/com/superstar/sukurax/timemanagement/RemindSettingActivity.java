package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.superstar.sukurax.timemanagement.MainActivity.datebaseHelper;

public class RemindSettingActivity extends Activity {
    ImageView back_toolbar_pic;
    TextView back_toolbar_text;
    SharedPreferences sp;
    Toolbar back_toolbar;

    TextView userName,today,todayTrue,todayFalse,week,weekTrue,weekFalse,month,monthTrue,monthFalse;
    Integer todayTrueNum,todayFalseNum,weekTrueNum,weekFalseNum,monthTrueNum,monthFalseNum;
    RatingBar todayRatingBar,weekRatingBar,monthRatingBar;
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

        setContentView(R.layout.task_count);
        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
        back_toolbar=(Toolbar)findViewById(R.id.back_toolbar);
        back_toolbar_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userName=(TextView)findViewById(R.id.userName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        back_toolbar_text.setText("任务统计");

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

        if(AVUser.getCurrentUser()!=null){
            String currentUsername = AVUser.getCurrentUser().getUsername();
            userName.setText(currentUsername);
        }
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("今日");
        spec.setContent(R.id.tab1);
        spec.setIndicator("今日");
        host.addTab(spec);

        spec = host.newTabSpec("本周");
        spec.setContent(R.id.tab2);
        spec.setIndicator("本周");
        host.addTab(spec);

        spec = host.newTabSpec("本月");
        spec.setContent(R.id.tab3);
        spec.setIndicator("本月");
        host.addTab(spec);

        today=(TextView)findViewById(R.id.today);
        todayTrue=(TextView)findViewById(R.id.todayTrue);
        todayFalse=(TextView)findViewById(R.id.todayFalse);
        week=(TextView)findViewById(R.id.week);
        weekTrue=(TextView)findViewById(R.id.weekTrue);
        weekFalse=(TextView)findViewById(R.id.weekFalse);
        month=(TextView)findViewById(R.id.month);
        monthTrue=(TextView)findViewById(R.id.monthTrue);
        monthFalse=(TextView)findViewById(R.id.monthFalse);

        todayRatingBar=(RatingBar)findViewById(R.id.todayRatingBar);
        weekRatingBar=(RatingBar)findViewById(R.id.weekRatingBar);
        monthRatingBar=(RatingBar)findViewById(R.id.monthRatingBar);

        Calendar cl = Calendar.getInstance();
        int year = cl.get(Calendar.YEAR);
        int monthC = cl.get(Calendar.MONTH) + 1;
        int day = cl.get(Calendar.DAY_OF_MONTH);
        int hour = cl.get(Calendar.HOUR_OF_DAY);
        int minute = cl.get(Calendar.MINUTE);

        today.setText(monthC+"月"+day+"日");
        week.setText(monthC+"月"+day+"-"+(day+7)+"日");
        month.setText(monthC+"月1-30日");

        //加载SQLite数据库
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select * from task ",new String[]{}
        );
        if (cursor.moveToFirst()) {
            do{
                //逻辑
            }while (cursor.moveToNext());
        }
        todayTrue.setText("1");
        todayFalse.setText("1");
        todayRatingBar.setNumStars(5);
        todayRatingBar.setRating(1);
        weekTrue.setText("3");
        weekFalse.setText("2");
        weekRatingBar.setNumStars(5);
        weekRatingBar.setRating(2);
        monthTrue.setText("3");
        monthFalse.setText("3");
        monthRatingBar.setNumStars(5);
        monthRatingBar.setRating(3);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    String week1to7(){
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
//        Calendar cal = Calendar.getInstance();
//        Date time= null;
//        try {
//            time = sdf.parse("2015-9-4 14:22:47");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        cal.setTime(time);
//        System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期
//
//        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
//        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
//        if(1 == dayWeek) {
//            cal.add(Calendar.DAY_OF_MONTH, -1);
//        }
//
//        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
//
//        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
//        cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
//        System.out.println("所在周星期一的日期："+sdf.format(cal.getTime()));
//        System.out.println(cal.getFirstDayOfWeek()+"-"+day+"+6="+(cal.getFirstDayOfWeek()-day+6));
//
//        cal.add(Calendar.DATE, 6);
//        System.out.println("所在周星期日的日期："+sdf.format(cal.getTime()));
//        return sdf.format(cal.getTime())+sdf.format(cal.getTime());
//    }
}
