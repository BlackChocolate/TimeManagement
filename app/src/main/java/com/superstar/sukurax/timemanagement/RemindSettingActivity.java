package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    Integer todayTrueNum=0,todayFalseNum=0,weekTrueNum=0,weekFalseNum=0,monthTrueNum=0,monthFalseNum=0;
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

        TabHost.TabSpec spec = host.newTabSpec("today");
        spec.setContent(R.id.tab1);
        spec.setIndicator("今日");
        host.addTab(spec);

        spec = host.newTabSpec("week");
        spec.setContent(R.id.tab2);
        spec.setIndicator("本周");
        host.addTab(spec);

        spec = host.newTabSpec("month");
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if(dayWeek==1){
            dayWeek = 8;
        }
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date mondayDate = cal.getTime();
        String weekBegin = sdf.format(mondayDate);
        System.out.println("所在周星期一的日期：" + weekBegin);

        cal.add(Calendar.DATE, 4 +cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        String weekEnd = sdf.format(sundayDate);
        System.out.println("所在周星期日的日期：" + weekEnd);

        Integer weekBeginDay=Integer.parseInt(weekBegin.split("-")[2]);
        Integer weekEndDay=Integer.parseInt(weekEnd.split("-")[2]);
        today.setText(monthC+"月"+day+"日");
        week.setText(monthC+"月"+weekBeginDay+"-"+""+weekEndDay+"日");
        month.setText(monthC+"月");

        //加载SQLite数据库
        Cursor cursor=datebaseHelper.getReadableDatabase().rawQuery(
                "select * from task ",new String[]{}
        );

        if (cursor.moveToFirst()) {
            do{
                //逻辑
                //"2018-6-5 15:46"
                String timeCursor=cursor.getString(cursor.getColumnIndex("time"));
                Integer timeCursorYear=Integer.parseInt(timeCursor.split("-")[0]);
                Integer timeCursorMonth=Integer.parseInt(timeCursor.split("-")[1]);
                Integer timeCursorDay=Integer.parseInt(timeCursor.split(" ")[0].split("-")[2]);
                //当天判断
                if(timeCursorYear==year&timeCursorMonth==monthC&timeCursorDay==day){
                    if(cursor.getString(cursor.getColumnIndex("state")).equals("0")){
                        todayTrueNum++;
                    }else{
                        todayFalseNum++;
                    }
                }
                //临近一周判断
                SimpleDateFormat todayTime = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdffrom= new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfto = new SimpleDateFormat("yyyy-MM-dd");
                Date today=null,to = null,from=null;
                try {
                    today=todayTime.parse(timeCursor+":00");
                    from= sdffrom.parse(weekBegin);
                    to= sdfto.parse(weekEnd);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(belongCalendar(today,from,to)){
                    if(cursor.getString(cursor.getColumnIndex("state")).equals("0")){
                        weekTrueNum++;
                    }else{
                        weekFalseNum++;
                    }

                }
                //当月判断
                if(timeCursorYear==year&timeCursorMonth==monthC){
                    if(cursor.getString(cursor.getColumnIndex("state")).equals("0")){
                        monthTrueNum++;
                    }else{
                        monthFalseNum++;
                    }
                }
            }while (cursor.moveToNext());
        }
        todayTrue.setText(String.valueOf(todayTrueNum));
        todayFalse.setText(String.valueOf(todayFalseNum));
        todayRatingBar.setNumStars(5);
        if((todayTrueNum-todayFalseNum)<=0){
            todayRatingBar.setRating(0);
        }else if(todayTrueNum-todayFalseNum>5){
            todayRatingBar.setRating(5);
        }else {
            todayRatingBar.setRating(todayTrueNum-todayFalseNum);
        }
        weekTrue.setText(String.valueOf(weekTrueNum));
        weekFalse.setText(String.valueOf(weekFalseNum));
        weekRatingBar.setNumStars(5);
        if((weekTrueNum-weekFalseNum)<=0){
            weekRatingBar.setRating(0);
        }else if(weekTrueNum-weekFalseNum>5){
            weekRatingBar.setRating(5);
        }else {
            weekRatingBar.setRating(weekTrueNum-weekFalseNum);
        }
        monthTrue.setText(String.valueOf(monthTrueNum));
        monthFalse.setText(String.valueOf(monthFalseNum));
        monthRatingBar.setNumStars(5);
        if((monthTrueNum-monthFalseNum)<=0){
            monthRatingBar.setRating(0);
        }else if(monthTrueNum-monthFalseNum>5){
            monthRatingBar.setRating(5);
        }else {
            monthRatingBar.setRating(monthTrueNum-monthFalseNum);
        }

    }
    public static boolean belongCalendar(Date time, Date from, Date to) {
        Calendar date = Calendar.getInstance();
        date.setTime(time);

        Calendar after = Calendar.getInstance();
        after.setTime(from);

        Calendar before = Calendar.getInstance();
        before.setTime(to);

        if (date.after(after) && date.before(before)) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
