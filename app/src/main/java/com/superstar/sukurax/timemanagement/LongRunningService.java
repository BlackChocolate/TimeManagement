package com.superstar.sukurax.timemanagement;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Date;

public class LongRunningService extends Service {

    @Override

    public IBinder onBind(Intent intent) {

        return null;

    }

    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {

            @Override

            public void run() {

                Log.d("LongRunningService", "executed at " + new Date().

                        toString());

            }

        }).start();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int anHour = 5000; // 这是一小时的毫秒数

        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;

        Intent i = new Intent(this, MainActivity.class);

        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);


//        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("我是伴有铃声效果的通知")
//                .setContentText("美妙么?安静听~")
//                //调用系统默认响铃,设置此属性后setSound()会无效
//                .setDefaults(Notification.DEFAULT_SOUND)
//        Notification notification = builder.build();
//        //设置 Notification 的 flags = FLAG_NO_CLEAR
//        //FLAG_AUTO_CANCEL 表示该通知能被状态栏的清除按钮给清除掉
//        //等价于 builder.setAutoCancel(true);
//        notification.flags |= Notification.FLAG_NO_CLEAR;
//        manager.notify(2, notification);

        return super.onStartCommand(intent, flags, startId);

    }

}