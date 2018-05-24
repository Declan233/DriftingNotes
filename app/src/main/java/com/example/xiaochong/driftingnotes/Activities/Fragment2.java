package com.example.xiaochong.driftingnotes.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xiaochong.driftingnotes.Adapter.AroundAdapter;
import com.example.xiaochong.driftingnotes.Entity.LocationBean;
import com.example.xiaochong.driftingnotes.R;
import com.example.xiaochong.driftingnotes.Utils.SharedPreferencesUtil;
import com.example.xiaochong.driftingnotes.mApp.MyApplication;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Fragment2 extends Fragment {


    @Bind(R.id.rcv2)
    RecyclerView rcv2;

    private static final String TAG = "Fragment2";
    @Bind(R.id.et_search2)
    EditText etSearch2;
    @Bind(R.id.search2)
    ImageView search2;

    private List<String> datas;

    private String uuid;
    private String token;

    private LocationBean mylb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        initView();
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        datas = new ArrayList<>();

        mylb = new LocationBean(SharedPreferencesUtil.getFloat(getContext(),"LONTITUDE",0),
                SharedPreferencesUtil.getFloat(getContext(),"LATITUDE",0),
                SharedPreferencesUtil.getString(getContext(),"TITLE",""),
                SharedPreferencesUtil.getString(getContext(),"SNIPPET",""));

        uuid = SharedPreferencesUtil.getString(this.getContext(),"UUID","");
        token = SharedPreferencesUtil.getString(this.getContext(),"TOKEN","");
        getDatasFromCloud();

//        getOneTest();

//        ArrayList<PoiItem> pois = poiResult.getPois();
//        for (int i = 0; i < pois.size(); i++) {
//            LocationBean locationBean = new LocationBean(pois.get(i).getTitle(),pois.get(i).getSnippet());
//            datas.add(locationBean);
//        }
//        Log.d(TAG, "onPoiSearched: "+datas.size()+" errcode: "+errcode);


//        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        rcv2.setLayoutManager(llm);
//        AroundAdapter aroundAdapter = new AroundAdapter(datas);
//        rcv2.setAdapter(aroundAdapter);
    }

    private void getOneTest() {

        String apiUrl = "http://hn2.api.okayapi.com/";

        Map<String, String> params = new HashMap<>();
        params.put("s", "App.Table.Get");
        params.put("uuid", uuid);
        params.put("token",token);
        params.put("app_key","8534042F6FF26B8292E70B1348D443DE");
        params.put("model_name","DN_Notes");
        params.put("id","1");


        MyApplication.getmInstance().getMyOkHttp().post()
                .url(apiUrl)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(TAG, "doDelete onSuccess:" + response);
                        int code = response.optJSONObject("data").optInt("err_code");
                        if (code==0){
                            Toast.makeText(Fragment2.this.getContext(), "查询成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d(TAG, "doUpload onFailure:" + error_msg);
                    }

                });
    }

    private void getDatasFromCloud() {
        //发起请求api
        String apiUrl = "http://hn2.api.okayapi.com/";

        Map<String, String> params = new HashMap<>();
        params.put("s", "App.Table.FreeQuery");
        params.put("uuid", uuid);
        params.put("token",token);
        params.put("app_key","8534042F6FF26B8292E70B1348D443DE");
        params.put("model_name","DN_Notes");
        params.put("select","titel,content,longitude,latitude,url,uuid");


        mylb.getLat();
        mylb.getLon();

        MyApplication.getmInstance().getMyOkHttp().post()
                .url(apiUrl)
                .params(params)
                .addParam("select","*")
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(TAG, "doDelete onSuccess:" + response);
                        int code = response.optJSONObject("data").optInt("err_code");
                        if (code==0){
                            Toast.makeText(Fragment2.this.getContext(), "查询成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d(TAG, "doUpload onFailure:" + error_msg);
                    }

                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.search2)
    public void onViewClicked() {

    }

}
