package com.example.xiaochong.driftingnotes.Activities;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.xiaochong.driftingnotes.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFragmentActivity extends FragmentActivity {

    @Bind(R.id.fl)
    FrameLayout fl;
    @Bind(android.R.id.tabs)
    RadioGroup tabs;
    @Bind(android.R.id.tabhost)
    FragmentTabHost ft;
    @Bind(R.id.bt1)
    Button bt1;
    @Bind(R.id.bt2)
    Button bt2;
    @Bind(R.id.bt3)
    Button bt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //设置页面的显示
        //android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        ft.setup(this, getSupportFragmentManager(), R.id.fl);//设置
        //设置我们的的指示器
        TabHost.TabSpec ts1 = ft.newTabSpec("0").setIndicator("");
        TabHost.TabSpec ts2 = ft.newTabSpec("1").setIndicator("");
        TabHost.TabSpec ts3 = ft.newTabSpec("2").setIndicator("");
        //加入我们刚才定义的选项卡
        ft.addTab(ts1, Fragment1.class, null);
        ft.addTab(ts2, Fragment2.class, null);
        ft.addTab(ts3, Fragment3.class, null);
        //挨着给每个RadioButton加入drawable限制边距以控制显示大小

        Drawable[] drs1 = bt1.getCompoundDrawables();//获取drawables
        Drawable[] drs2 = bt2.getCompoundDrawables();//获取drawables
        Drawable[] drs3 = bt3.getCompoundDrawables();//获取drawables
        Rect r = new Rect(0, 8, drs1[1].getMinimumWidth()*1/4, drs1[1].getMinimumHeight()*1/4+10);//定义一个Rect边界
        drs1[1].setBounds(r);//给drawable设置边界
        drs2[1].setBounds(r);//给drawable设置边界
        drs3[1].setBounds(r);//给drawable设置边界

        bt1.setCompoundDrawables(null,drs1[1],null,null);
        bt2.setCompoundDrawables(null,drs2[1],null,null);
        bt3.setCompoundDrawables(null,drs3[1],null,null);
    }


    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                ft.setCurrentTab(0);
                break;
            case R.id.bt2:
                ft.setCurrentTab(1);
                break;
            case R.id.bt3:
                ft.setCurrentTab(2);
                break;
        }
    }


}
