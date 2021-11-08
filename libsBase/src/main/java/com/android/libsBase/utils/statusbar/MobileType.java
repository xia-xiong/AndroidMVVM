package com.android.libsBase.utils.statusbar;

import android.os.Build;

import androidx.annotation.NonNull;

/**
 * Created by qinbaoyuan on 2018/12/3.
 * 判断设备厂商
 */
public class MobileType {
//    华为——Huawei
//    魅族——Meizu
//    小米——Xiaomi
//    索尼——Sony
//    oppo——OPPO
//    LG——LG
//    vivo——vivo
//    三星——samsung
//    乐视——Letv
//    中兴——ZTE
//    酷派——YuLong
//    联想——LENOVO

    public static boolean isXiaomi(){
        return checkManufacturer("Xiaomi");
    }

    public static boolean isMeizu(){
        return checkManufacturer("Meizu");
    }

    public static boolean isOppo(){
        return checkManufacturer("OPPO");
    }


    private static boolean checkManufacturer(@NonNull String manufacturer){
        return manufacturer.equalsIgnoreCase(Build.MANUFACTURER);
    }
}
