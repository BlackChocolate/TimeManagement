package com.superstar.sukurax.timemanagement;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class MyLeanCloudApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"Tf6k5Qp7GwlDCIYcADtlaoeJ-gzGzoHsz","JDhRMdlHxTPvr4se5Ts9nhCR");
        AVOSCloud.setDebugLogEnabled(true);
    }
}