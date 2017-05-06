package com.solarexsoft.androidutils.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;

import static android.text.TextUtils.isEmpty;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 05/05/2017
 *    Desc:
 * </pre>
 */

public final class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("Can't instantiate directly");
    }

    public static boolean isAppInstalled(Context context, String pkgName) {
        return !isEmpty(pkgName) && (IntentUtils.getLaunchIntent(pkgName) != null);
    }

    public static void installApp(Context contxt, String filePath){
        installApp(contxt, FileUtils.getFileByPath(filePath));
    }

    public static void installApp(Context context, File file){
        if (!FileUtils.isFileExists(file)){
            return;
        }
        context.startActivity(IntentUtils.getInstallIntent(file));
    }

    public static void installApp(Activity activity, String filePath, int requsetCode){
        installApp(activity, FileUtils.getFileByPath(filePath), requsetCode);
    }

    public static void installApp(Activity activity, File file, int requestCode){
        if (FileUtils.isFileExists(file)){
            activity.startActivityForResult(IntentUtils.getInstallIntent(file), requestCode);
        }
    }

    public static boolean installAppSilent(String filePath){
        File file = FileUtils.getFileByPath(filePath);
        if (!FileUtils.isFileExists(file)){
            return false;
        }
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath;
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, !isSystemApp(Utils.getContext()), true);
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
    }

    public static void uninstallApp(Context context, String pkgName){
        if (!isEmpty(pkgName)){
            context.startActivity(IntentUtils.getUninstallIntent(pkgName));
        }
    }

    public static void uninstallApp(Activity activity, String pkgName, int reqCode){
        if (!isEmpty(pkgName)){
            activity.startActivityForResult(IntentUtils.getUninstallIntent(pkgName), reqCode);
        }
    }

    public static boolean uninstallAppSilent(Context context, String pkgName, boolean isKeepData){
        if (isEmpty(pkgName)){
            return false;
        }
        String command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (isKeepData?"-k ":"") + pkgName;
        ShellUtils.CommandResult commandResult = ShellUtils.execCmd(command, !isSystemApp(context), true);
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success");
    }

    public static boolean isAppRoot(){
        ShellUtils.CommandResult result = ShellUtils.execCmd("echo root", true);
        if (result.result == 0){
            return true;
        }
        if (result.errorMsg != null){
            LogUtils.d("isAppRoot", result.errorMsg);
        }
        return false;
    }

    public static void launchApp(String pkgName){
        if (isEmpty(pkgName)){
            return;
        }
        Utils.getContext().startActivity(IntentUtils.getLaunchIntent(pkgName));
    }

    public static void launchApp(Activity activity, String pkgName, int reqCode){
        if (isEmpty(pkgName)){
            return;
        }
        activity.startActivityForResult(IntentUtils.getLaunchIntent(pkgName), reqCode);
    }

    public static String getAppPackageName(Context context){
        return context.getPackageName();
    }

    public static void getAppDetailsSettings(Context context){
        getAppDetailsSettings(context, context.getPackageName());
    }

    public static void getAppDetailsSettings(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return;
        }
        context.startActivity(IntentUtils.getAppDetailsSettingsIntent(pkgName));
    }

    public static String getAppName(Context context){
        return getAppName(context, context.getPackageName());
    }

    public static String getAppName(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return null;
        }
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkgName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable getAppIcon(Context context){
        return getAppIcon(context, context.getPackageName());
    }

    public static Drawable getAppIcon(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return null;
        }
        try{
            PackageManage pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkgName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppPath(Context context){
        return getAppPath(context, context.getPackageName());
    }

    public static String getAppPath(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return null;
        }
        try{
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkgName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppVersionName(Context context){
        return getAppVersionName(context, context.getPackageName());
    }

    public static String getAppVersionName(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkgName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static int getAppVersionCode(Context context){
        return getAppVersionCode(context, context.getPackageName());
    }

    public static int getAppVersionCode(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return -1;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkgName, 0);
            return pi == null ? null : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isSystemApp(Context context){
        return isSystemApp(context, context.getPackageName());
    }

    public static boolean isSystemApp(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return false;
        }
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkgName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }




































































}
