package com.biubiu.www.learnutils.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Young on 2016-08-12.
 */

public class AppUtils {

    /**
     * 安装App
     * 根据路径安装App
     *
     * @param content
     * @param filePath
     */
    public static void installApp(Context content , String filePath){
        installApp(content,new File(filePath));
    }

    /**
     * 安装App
     * <p>根据文件安装App</p>
     *
     * @param context 上下文
     * @param file    文件
     */
    public static void installApp(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 卸载制定包名的App
     *
     * @param context 上下文
     * @param packName  包名
     */
    public static void unistallApp(Context context,String packName){
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:"+packName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }



}
