package com.superstar.sukurax.timemanagement;

import android.app.Activity;
import android.content.Intent;

public class ThemeUtils{

    private static int sTheme;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity
     * of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
//        onActivityCreateSetTheme(activity);
//        activity.startActivity(new Intent(activity, activity.getClass()));
        Intent intent =  new Intent(activity,SkinChangeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    /** Set the theme of the activity, according to the configuration. */
    private static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            case 1:
                activity.setTheme(R.style.AppTheme);
                break;
            case 2:
                activity.setTheme(R.style.AppTheme2);
                break;
            case 3:
                activity.setTheme(R.style.AppTheme3);
                break;
            case 4:
                activity.setTheme(R.style.AppTheme4);
                break;
            case 5:
                activity.setTheme(R.style.AppTheme5);
                break;
            case 6:
                activity.setTheme(R.style.AppTheme6);
                break;
            default:
                break;
        }
    }
}
