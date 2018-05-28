package com.superstar.sukurax.timemanagement;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;

public class MyService extends Service {
    private final static String ACTION_NOTIFICATION = "ACTION_NOTIFICATION";
    Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        initAlarm(ACTION_NOTIFICATION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyService.this, "test!", Toast.LENGTH_SHORT).show();
            }
        });


        if (intent != null) {
            if (ACTION_NOTIFICATION.equals(intent.getAction())) {
                Intent acIntent = new Intent(this, MainActivity.class);
                NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent pi = PendingIntent.getActivity(this, 0, acIntent, 0);
                Notification notify = new Notification.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("TickerText:" + "提醒中！")
                        .setContentTitle("提醒设置")
                        .setContentText("点击我关闭")
                        .setContentIntent(pi).build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
                nm.notify(5, notify);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initAlarm(String action) {
//        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent serviceIntent = new Intent(this, MyService.class);
//        serviceIntent.setAction(action);
//        PendingIntent pi = PendingIntent.getService(this, 0, serviceIntent, 0);
//        mAlarmManager.cancel(pi);
//
//        Calendar checkInCal = Calendar.getInstance();
//        checkInCal.setTimeInMillis(System.currentTimeMillis());
//        checkInCal.add(Calendar.SECOND, 5);
//        long repeat = 10000;
//        mAlarmManager.setRepeating(AlarmManager.RTC, checkInCal.getTimeInMillis(), repeat, pi);
    }
}
