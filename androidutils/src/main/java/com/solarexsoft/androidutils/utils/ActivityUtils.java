package com.solarexsoft.androidutils.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 04/05/2017
 *    Desc:
 * </pre>
 */

public class ActivityUtils {

    private ActivityUtils() {
        throw new UnsupportedOperationException("Can't instantiate directly");
    }

    public static boolean isActivityExists(String pkgName, String clsName) {
        Intent intent = new Intent();
        intent.setClassName(pkgName, clsName);
        boolean notExists = Utils.getContext().getPackageManager().resolveActivity(intent, 0) ==
                null || intent.resolveActivity(Utils.getContext().getPackageManager()) == null ||
                Utils.getContext().getPackageManager().queryIntentActivities(intent, 0).size() == 0;
        return !notExists;
    }

    public static void launchActivity(String pkgName, String clsName) {
        launchActivity(pkgName, clsName, null);
    }

    private static void launchActivity(String pkgName, String clsName, Bundle bundle) {
        Utils.getContext().startActivity(getComponentIntent(pkgName, clsName, bundle));
    }

    private static Intent getComponentIntent(String pkgName, String clsName, Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ComponentName cn = new ComponentName(pkgName, clsName);
        intent.setComponent(cn);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static String getLaunchActivity(String pkgName) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager pm = Utils.getContext().getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        for (int i = 0; i < infos.size(); i++) {
            ResolveInfo resolveInfo = infos.get(i);
            if (resolveInfo.activityInfo.packageName.equals(pkgName)) {
                return resolveInfo.activityInfo.name;
            }
        }
        return null;
    }

    public static Activity getTopActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke
                    (null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                activities = (HashMap) activitiesField.get(activityThread);
            } else {
                activities = (ArrayMap) activitiesField.get(activityThread);
            }
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pauseField = activityRecordClass.getDeclaredField("paused");
                pauseField.setAccessible(true);
                if (!pauseField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
