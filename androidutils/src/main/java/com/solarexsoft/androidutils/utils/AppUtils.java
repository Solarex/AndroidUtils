package com.solarexsoft.androidutils.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            PackageManager pm = context.getPackageManager();
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

    public static boolean isAppDebug(Context context){
        return isAppDebug(context, context.getPackageName());
    }

    public static boolean isAppDebug(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return false;
        }
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(pkgName, 0);
            return ai!=null && (ai.flags&ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public static Signature[] getAppSignature(Context context){
        return getAppSignature(context, context.getPackageName());
    }

    public static Signature[] getAppSignature(Context context, String pkgName){
        if (isEmpty(pkgName)){
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppSignatureSHA1(Context context){
        return getAppSignatureSHA1(context, context.getPackageName());
    }

    public static String getAppSignatureSHA1(Context context, String pkgName){
        Signature[] signature = getAppSignature(context, pkgName);
        if (signature == null) return null;
        return EncryptUtils.encryptSHA1ToString(signature[0].toByteArray()).
                replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    public static boolean isAppForeground(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> infos = activityManager.getRunningAppProcesses();
        if (infos == null || infos.size() == 0){
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : infos){
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                return info.processName.equals(context.getPackageName());
            }
        }
        return false;
    }

    public static boolean isAppForeground(Context context, String pkgName){
        boolean result = !isEmpty(pkgName) && pkgName.equals(Processutils.getForegroundProcessName());
        return result;
    }

    public static class AppInfo{
        private String name;
        private Drawable icon;
        private String packageName;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public void setPackagePath(String packagePath) {
            this.packagePath = packagePath;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public boolean isSystem() {
            return isSystem;
        }

        public void setSystem(boolean system) {
            isSystem = system;
        }

        public AppInfo(String name, Drawable icon, String packageName, String packagePath, String
                versionName, int versionCode, boolean isSystem) {
            this.name = name;
            this.icon = icon;
            this.packageName = packageName;
            this.packagePath = packagePath;
            this.versionName = versionName;
            this.versionCode = versionCode;
            this.isSystem = isSystem;
        }

        @Override
        public String toString() {
            return "Packagename: " + getPackageName()
                    + "\nName: " + getName()
                    + "\nIcon: " + getIcon()
                    + "\nPath: " + getPackagePath()
                    + "\nVersionname: " + getVersionName()
                    + "\nVersioncode: " + getVersionCode()
                    + "\nIsSystem: " + isSystem();
        }
    }

    public static AppInfo getAppInfo(Context context){
        return getAppInfo(context, context.getPackageName());
    }

    public static AppInfo getAppInfo(Context context, String pkgName){
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(pkgName, 0);
            return getBean(pm, pi);
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    private static AppInfo getBean(PackageManager pm, PackageInfo pi){
        if (pm == null || pi == null){
            return null;
        }
        ApplicationInfo ai = pi.applicationInfo;
        String pkgName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String pkgPath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(name,icon,pkgName,pkgPath,versionName,versionCode,isSystem);
    }

    public static List<AppInfo> getAppsInfo(Context context){
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> pis = pm.getInstalledPackages(0);
        for (PackageInfo pi : pis){
            AppInfo ai = getBean(pm, pi);
            if (ai==null){
                continue;
            }
            list.add(ai);
        }
        return list;
    }

    public static boolean cleanAppData(String... dirPaths){
        File[] dirs = new File[dirPaths.length];
        int i = 0;
        for (String dirPath: dirPaths){
            dirs[i++] = new File(dirPath);
        }
        return cleanAppData(dirs);
    }

    public static boolean cleanAppData(File... dirs){
        boolean isSuccess = CleanUtils.cleanInternalCache();
        isSuccess &= CleanUtils.cleanInternalDbs();
        isSuccess &= CleanUtils.cleanInternalSP();
        isSuccess &= CleanUtils.cleanInternalFiles();
        isSuccess &= CleanUtils.cleanExternalCache();
        for (File dir : dirs){
            isSuccess &= CleanUtils.cleanCustomCache(dir);
        }
        return isSuccess;
    }
































































}
