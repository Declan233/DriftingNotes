package com.example.xiaochong.driftingnotes.mApp;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.tsy.sdk.myokhttp.MyOkHttp;



/**
 * Created by xiaochong on 2018/5/1.
 */


public class MyApplication extends MultiDexApplication {

    private static MyApplication mInstance;

    private MyOkHttp myOkHttp;

    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        myOkHttp = new MyOkHttp();
    }

    public static MyApplication getmInstance() {
        return mInstance;
    }

    public MyOkHttp getMyOkHttp() {
        return myOkHttp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
