package com.biubiu.www.learnutils.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by Young on 2016-08-15.
 * 设备信息
 */

public class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("I can't find ");
    }

    /**
     * 设备的Mac地址
     * <p>添加权限 ACCESS_WIFI_STATE</p>
     *
     * @param context 上下文
     * @return MAC地址
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAddress = info.getMacAddress().replace(":", "");
        return macAddress == null ? "" : macAddress;
    }

    /**
     * 获取设备MAC地址
     * <p>需添加权限 android.permission.ACCESS_WIFI_STATE</p>
     * @return
     */
    public static String getMacAddress() {
        String macAddress = null;
        LineNumberReader reader = null;
        try {
            Process process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            reader = new LineNumberReader(ir);
            macAddress = reader.readLine().replace(":", "");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return  macAddress == null ?"":macAddress;
    }

    /**
     * 获取厂商
     * @return 设备厂商
     */

    public static String getManufacturer(){
        String MANUFACTURER = Build.MANUFACTURER;
        return MANUFACTURER;
    }

    public static String getModel(){
        String model = Build.MODEL;
        if (model != null){
            model = model.trim().replaceAll("\\s*","");
        }else {
            model = "";
        }
        return  model;
    }
}
