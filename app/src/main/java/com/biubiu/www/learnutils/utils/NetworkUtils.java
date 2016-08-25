package com.biubiu.www.learnutils.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.sax.RootElement;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

/**
 * Created by Young on 2016-08-25.
 */

public class NetworkUtils {

    private NetworkUtils() {
        throw new UnsupportedOperationException("此次网络检测失败");
    }

    public static final int NETWORK_WIFI = 1;
    public static final int NETWORK_4G = 4;
    public static final int NETWORK_3G = 3;
    public static final int NETWORK_2G = 2;
    public static final int NETWORK_UNKNOWN = 5;
    public static final int NETWORK_NO = -1;

    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;

    /**
     * 打开网络设置界面
     * <p>适用于Android 3.0</p>
     *
     * @param context
     */
    public static void openWirelessSettings(Context context) {
        if (Build.VERSION.SDK_INT > 10) {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS));
        } else {
            context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * 获取活动网络信息
     *
     * @param context
     * @return
     */
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return true 可用；false 不可用
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        return networkInfo != null && networkInfo.isAvailable();
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return true 可用；false 不可用
     */
    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * 判断网络是否是4G
     * <p><uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/></p>
     *
     * @param context
     * @return
     */
    public static boolean is4G(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断wifi是否连接
     *
     * @param context 上下文
     * @return true 能连；false 不能连
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取移动网络运营商名称
     *
     * @param context
     * @return
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    /**
     * 获取移动终端类型
     *
     * @param context
     * @return 手机制式
     * <p>
     * 0，手机制式未知
     * 1，手机制式GSM
     * 2，手机制式CDMA
     * 3，4g
     */
    public static int getPhoneType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }

    public static int getNetWorkType(Context context) {
        int netType = NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NETWORK_2G;
                        break;
                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NETWORK_4G;
                        break;
                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")){
                            netType = NETWORK_3G;
                        }else {
                            netType = NETWORK_UNKNOWN;
                        }
                        break;
                }
            }else {
                netType = NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    /**
     * 获取当前网络类型（wifi,2G,3G,4G）
     *
     * @param context
     * @return
     *
     *
     */
    public static String getNetWorkTypeName(Context context){
        switch (getNetWorkType(context)){
            case NETWORK_WIFI:
                return "NETWORK_WIFI";
            case NETWORK_4G:
                return "NETWORK_4G";
            case NETWORK_3G:
                return "NETWORK_3G";
            case NETWORK_2G:
                return "NETWORK_2G";
            case NETWORK_NO:
                return "NETWORK_NO";
            default:
                return "NETWORK_UNKNOWN";
        }
    }

}
