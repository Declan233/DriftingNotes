package com.example.xiaochong.driftingnotes.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.example.xiaochong.driftingnotes.Adapter.AroundAdapter;
import com.example.xiaochong.driftingnotes.Adapter.SearchAdapter;
import com.example.xiaochong.driftingnotes.Entity.LocationBean;
import com.example.xiaochong.driftingnotes.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Fragment2 extends Fragment implements LocationSource, AMapLocationListener {


    @Bind(R.id.map2)
    MapView map2;
    @Bind(R.id.et_search2)
    LinearLayout etSearch2;
    @Bind(R.id.rcv2)
    RecyclerView rcv2;

    private MyLocationStyle myLocationStyle;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private AMap aMap;

    private static final String TAG = "Fragment2";

    private List<String> datas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        ButterKnife.bind(this, view);
        initViews(savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        datas = new ArrayList<>();
//        ArrayList<PoiItem> pois = poiResult.getPois();
//        for (int i = 0; i < pois.size(); i++) {
//            LocationBean locationBean = new LocationBean(pois.get(i).getTitle(),pois.get(i).getSnippet());
//            datas.add(locationBean);
//        }
//        Log.d(TAG, "onPoiSearched: "+datas.size()+" errcode: "+errcode);


        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv2.setLayoutManager(llm);
        AroundAdapter aroundAdapter = new AroundAdapter(datas);
        rcv2.setAdapter(aroundAdapter);
    }


    private void initViews(Bundle savedInstanceState) {
        map2.onCreate(savedInstanceState);
        aMap = map2.getMap();
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setAllGesturesEnabled(true);//所有手势有效

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setAllGesturesEnabled(true);//所有手势有效

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }



    @OnClick(R.id.et_search2)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), PoiActivity.class));
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                Log.d(TAG, "onLocationChanged: 定位失败" + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo());
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
        map2.onResume();
        super.onResume();
    }


    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("sys", "mf onSaveInstanceState");
        map2.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onPause() {
        Log.i("sys", "mf onPause");
        map2.onPause();
        super.onPause();
    }

    /**
     * 方法必须重写
     * map的生命周期方法
     */
    @Override
    public void onDestroy() {
        Log.i("sys", "mf onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        map2.onDestroy();
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(getActivity());
            clientOption = new AMapLocationClientOption();
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
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }
}
