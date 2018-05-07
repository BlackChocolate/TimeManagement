package com.superstar.sukurax.timemanagement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.weiget.CalendarView;

import java.util.Arrays;
import java.util.Calendar;

public class CalendarActivity extends Activity {
    ImageView back_toolbar_pic,calendar_left,calendar_right;
    TextView back_toolbar_text;
    TextView calendar_date;
    int currentYear,currentMonth;
    final int lastYear=2012,lastMonth=1,nextYear=2024,nextMonth=12;
    SharedPreferences sp;
    Toolbar back_toolbar;
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
        setContentView(R.layout.calendar_view);
        back_toolbar_pic=(ImageView)findViewById(R.id.back_toolbar_pic);
        back_toolbar_text=(TextView) findViewById(R.id.back_toolbar_text);
        back_toolbar=(Toolbar)findViewById(R.id.back_toolbar);
        calendar_date=(TextView)findViewById(R.id.calendar_date);
        calendar_left=(ImageView)findViewById(R.id.calendar_left);
        calendar_right=(ImageView)findViewById(R.id.calendar_right);
        back_toolbar_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


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



        Calendar calendar = Calendar.getInstance();
        back_toolbar_text.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
        currentYear=calendar.get(Calendar.YEAR);
        currentMonth=(calendar.get(Calendar.MONTH)+1);
        calendar_date.setText(currentYear+"年"+currentMonth+"月");


        final CalendarView calendarView = (CalendarView) findViewById(R.id.calendar);
//日历init，年月日之间用点号隔开
        calendarView
                .setStartEndDate(lastYear+"."+lastMonth, nextYear+"."+nextMonth)
                .setInitDate(calendar.get(Calendar.YEAR)+"."+(calendar.get(Calendar.MONTH)+1))
                .setSingleDate(calendar.get(Calendar.YEAR)+"."+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.DAY_OF_MONTH))
                .init();

//月份切换回调
        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
//                Toast.makeText(CalendarActivity.this, Arrays.toString(date), Toast.LENGTH_LONG).show();
                calendar_date.setText(date[0]+"年"+date[1]+"月");

            }
        });

        calendar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.lastMonth();
            }
        });
        calendar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.nextMonth();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
