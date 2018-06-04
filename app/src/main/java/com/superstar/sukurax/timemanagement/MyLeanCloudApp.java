package com.superstar.sukurax.timemanagement;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
//import com.facebook.stetho.Stetho;
//import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

public class MyLeanCloudApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"Tf6k5Qp7GwlDCIYcADtlaoeJ-gzGzoHsz","JDhRMdlHxTPvr4se5Ts9nhCR");
        AVOSCloud.setDebugLogEnabled(true);

//        Stetho.initializeWithDefaults(this);
//        new OkHttpClient.Builder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();
    }
}