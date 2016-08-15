package com.biubiu.www.learnutils.bean;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Young on 2016-08-15.
 *
 * App相关
 */

public class AppInfo {
    private String name;
    private Drawable icon;
    private String packageName;
    private String versionName;
    private int versionCode;
    private boolean isSD;
    private boolean isUser;

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

    public boolean isSD() {
        return isSD;
    }

    public void setSD(boolean SD) {
        isSD = SD;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public AppInfo(String name, Drawable icon, String packageName, String versionName, int versionCode, boolean isSD, boolean isUser) {
        this.setName(name);
        this.setIcon(icon);
        this.setPackageName(packageName);
        this.setVersionName(versionName);
        this.setVersionCode(versionCode);
        this.setSD(isSD);
        this.setUser(isUser);
    }

    /**
     * 获取当前App信息
     * <p>AppInfo (名称,图标,包名,版本号,版本Code,是否安装SD卡,是否是用户程序)</p>
     *
     * @param context
     * @return
     */
    public static AppInfo getAppInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi != null ? getBean(pm, pi) : null;

    }

    /**
     * 得到AppInfo的Bean
     *
     * @param pm 包的管理
     * @param pi 包的信息
     * @return AppInfo类
     */
    private static AppInfo getBean(PackageManager pm, PackageInfo pi) {
        ApplicationInfo ai = pi.applicationInfo;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packageName = pi.packageName;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSD = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        boolean isUser = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        return new AppInfo(name, icon, packageName, versionName, versionCode, isSD, isUser);
    }

    /**
     * 获取所有已安装App信息
     *
     * @param context
     * @return
     */
    public static List<AppInfo> getAllAppsInfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();

        //获取系统中安装的所有软件信息
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            if (pi != null) {
                list.add(getBean(pm, pi));
            }
        }
        return list;
    }

    /**
     * 根据包名获取意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 根据包名判断APP是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return true:已经安装 ;false:未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        return getIntentByPackageName(context, packageName) != null;
    }

    /**
     * 打开指定包名的App
     *
     * @param context     上下文
     * @param packageName 包名
     * @return true:打开成功,false:打开失败
     */
    public static boolean openAppByPackageName(Context context, String packageName) {
        Intent intent = getIntentByPackageName(context, packageName);
        if (intent != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 打开指定包名的App应用信息界面
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void openAppInfo(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    /**
     * 可用来做App信息分享
     *
     * @param context
     * @param info
     */
    public static void shareAppInfo(Context context, String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        context.startActivity(intent);
    }

    /**
     * 判断当前App处于前台还是后台
     * <p>需要添加权限 android.permission.GET_TASKS</p>
     * <P>并且是系统应用该方法才有效</P>
     *
     * @param context 上下文
     * @return true:后台 false:前台
     */
    public static boolean isAppBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()){
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())){
                return true;
            }
        }
        return false;
    }



}
