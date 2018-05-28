package com.superstar.sukurax.timemanagement;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;

public class RingActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //显示通知栏
        NotificationCompat.Builder no=new NotificationCompat.Builder(this);
        //设置参数
        no.setDefaults(NotificationCompat.DEFAULT_ALL);
        no.setContentTitle("闹钟提醒");
        no.setContentText("傻逼懒猪快起来啦！！");
        no.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
        Notification notification=no.build();

        //通知管理器
        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //发送通知
        notificationManager.notify(0x101,notification);

    }

    public void close(View view){
        mediaPlayer.stop();
        finish();
    }

}