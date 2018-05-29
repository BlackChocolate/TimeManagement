package com.superstar.sukurax.timemanagement;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;


public class WidgetProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION = "com.seewo.appwidgettest.action.CLICK"; // 点击事件的广播ACTION

    //每次窗口小部件被更新都调用一次该方法
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.desktop_layout);
        Intent intent = new Intent(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, R.id.app_widget_linearLayout, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.app_widget_linearLayout, pendingIntent);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }
    @Override
        public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
     if (CLICK_ACTION.equals(intent.getAction())) {
         context.startActivity(new Intent(context,NoteActivity.class));
    }
}

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
    @Override public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
}





