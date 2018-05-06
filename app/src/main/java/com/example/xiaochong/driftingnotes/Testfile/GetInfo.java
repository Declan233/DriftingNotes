package com.example.xiaochong.driftingnotes.Testfile;

import android.util.Log;

import com.amap.api.location.AMapLocation;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaochong on 2018/5/6.
 */

public class GetInfo {
    private static final String TAG = "GetInfo";

    static public void getLocInfo(AMapLocation aMapLocation){
        String str="";
        str += aMapLocation.getLocationType()+"\n";//获取当前定位结果来源，如网络定位结果，详见定位类型表
        str += aMapLocation.getLatitude()+"  ";//获取纬度
        str += aMapLocation.getLongitude()+"  ";//获取经度
        str += aMapLocation.getAccuracy()+"\n";//获取精度信息
        str += aMapLocation.getAddress()+"\n";//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        str += aMapLocation.getCountry()+"  ";//国家信息
        str += aMapLocation.getProvince()+"  ";//省信息
        str += aMapLocation.getCity()+"\n";//城市信息
        str += aMapLocation.getDistrict()+"  ";//城区信息
        str += aMapLocation.getStreet()+"  ";//街道信息
        str += aMapLocation.getStreetNum()+"\n";//街道门牌号信息
        str += aMapLocation.getCityCode()+"  ";//城市编码
        str += aMapLocation.getAdCode()+"\n";//地区编码
        str += aMapLocation.getAoiName()+"\n";//获取当前定位点的AOI信息
        str += aMapLocation.getGpsAccuracyStatus()+"\n";//获取GPS的当前状态
        //获取定位时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(aMapLocation.getTime());
        df.format(date);
        str += date;
        Log.d(TAG, " getLocInfo: "+str);
    }
}
