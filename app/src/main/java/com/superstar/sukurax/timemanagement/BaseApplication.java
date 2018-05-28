package com.superstar.sukurax.timemanagement;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyService.class));
        Log.d("Test","application oncreate");
    }
}
