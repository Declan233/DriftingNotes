package com.example.xiaochong.driftingnotes.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.example.xiaochong.driftingnotes.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


public class Fragment1 extends Fragment implements LocationSource,AMapLocationListener {


    private MyLocationStyle myLocationStyle;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private AMap aMap;

    private static final String TAG = "Fragment1";

    @Bind(R.id.map1)
    MapView map1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        ButterKnife.bind(this, view);
        map1.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = map1.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        return view;
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener=listener;
        if(locationClient==null){
            locationClient=new AMapLocationClient(getActivity());
            clientOption=new AMapLocationClientOption();
            locationClient.setLocationListener(this);
            clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
            clientOption.setOnceLocationLatest(true);//设置单次精确定位
            clientOption.setInterval(15000);
            locationClient.setLocationOption(clientOption);
            locationClient.startLocation();
            Log.d(TAG, "activate: ");
        }

    }
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener=null;
        if(locationClient!=null){
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient=null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null&&aMapLocation != null) {
            if (aMapLocation != null
                    &&aMapLocation.getErrorCode() == 0) {
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
                Log.d(TAG, "onLocationChanged: "+str);
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                Log.d(TAG, "onLocationChanged: 定位失败"+aMapLocation.getErrorCode()+": "+aMapLocation.getErrorInfo());
            }
        }
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        Log.i("sys", "mf onResume");
        map1.onResume();
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        map1.onDestroy();
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        Log.i("sys", "mf onPause");
        map1.onPause();
        super.onPause();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("sys", "mf onSaveInstanceState");
        map1.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {
        Log.i("sys", "mf onDestroy");
        map1.onDestroy();
        super.onDestroy();
    }
}
