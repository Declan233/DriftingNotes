package com.example.xiaochong.driftingnotes.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.xiaochong.driftingnotes.Adapter.SearchAdapter;
import com.example.xiaochong.driftingnotes.Entity.LocationBean;
import com.example.xiaochong.driftingnotes.R;
import com.example.xiaochong.driftingnotes.Utils.InputTipTask;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoiActivity extends Activity implements TextWatcher,PoiSearch.OnPoiSearchListener {

    private static final String TAG = "PoiActivity";

    @Bind(R.id.dialog_search_back)
    ImageView dialogSearchBack;
    @Bind(R.id.et_search)
    AutoCompleteTextView etSearch;
    @Bind(R.id.bt_search)
    ImageView btSearch;
    @Bind(R.id.dialog_search_recyclerview)
    RecyclerView dialogSearchRecyclerview;

    private  PoiSearch.Query query;
    private  PoiSearch poiSearch;
    private ArrayList<LocationBean> datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        ButterKnife.bind(this);

        etSearch.addTextChangedListener(this);
    }

    @OnClick({R.id.dialog_search_back, R.id.bt_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_search_back:
                finish();
                break;
            case R.id.bt_search:
                searchLocationPoi();
                break;
        }
    }

    private void searchLocationPoi() {
        //关闭键盘
//        KeyBoardUtils.closeKeybord(etSearch, BaseApplication.mContext);
        if (TextUtils.isEmpty(etSearch.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "内容为空!", Toast.LENGTH_SHORT).show();
        } else {
            query = new PoiSearch.Query(etSearch.getText().toString().trim(), "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            query.setCityLimit(true);
            query.setPageSize(40);// 设置每页最多返回多少条poiitem
            query.setPageNum(0);// 设置查第一页
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() <= 0) {
            return;
        }else {
            //高德地图的输入的自动提示，代码在后面
            InputTipTask.getInstance(this).setAdapter(etSearch).searchTips(s.toString().trim(), "");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int errcode) {
        if (errcode == 1000) {
            datas = new ArrayList<>();
            ArrayList<PoiItem> pois = poiResult.getPois();
            for (int i = 0; i < pois.size(); i++) {
                LocationBean locationBean = new LocationBean(pois.get(i).getTitle(),pois.get(i).getSnippet());
                datas.add(locationBean);
            }
            Log.d(TAG, "onPoiSearched: "+datas.size()+" errcode: "+errcode);


            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            dialogSearchRecyclerview.setLayoutManager(llm);
            SearchAdapter searchAdapter = new SearchAdapter(datas);
            dialogSearchRecyclerview.setAdapter(searchAdapter);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
