package com.example.xiaochong.driftingnotes.Activities;

import android.os.Bundle;
import android.os.StrictMode;
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

import com.amap.api.location.DPoint;
import com.amap.api.maps2d.CoordinateConverter;
import com.example.xiaochong.driftingnotes.Adapter.AroundAdapter;
import com.example.xiaochong.driftingnotes.Entity.AroundData;
import com.example.xiaochong.driftingnotes.Entity.LocationBean;
import com.example.xiaochong.driftingnotes.R;
import com.example.xiaochong.driftingnotes.Utils.SharedPreferencesUtil;
import com.example.xiaochong.driftingnotes.mApp.MyApplication;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.amap.api.location.CoordinateConverter.calculateLineDistance;


public class Fragment2 extends Fragment{


    @Bind(R.id.rcv2)
    RecyclerView rcv2;

    private static final String TAG = "Fragment2";
    @Bind(R.id.et_search2)
    EditText etSearch2;
    @Bind(R.id.search2)
    ImageView search2;

    private List<AroundData> adatas;

    private String uuid;
    private String token;

    private LocationBean mylb;

    private int precision=3;//过滤精确度
    private List<String> fliterKeyWord = new ArrayList<>();
    private String searchWord = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        mylb = new LocationBean(SharedPreferencesUtil.getFloat(getContext(),"LONTITUDE",0),
                SharedPreferencesUtil.getFloat(getContext(),"LATITUDE",0),
                SharedPreferencesUtil.getString(getContext(),"TITLE",""),
                SharedPreferencesUtil.getString(getContext(),"SNIPPET",""));
        searchWord = etSearch2.getText().toString();

        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    private void initView() {

        mylb = new LocationBean(SharedPreferencesUtil.getFloat(getContext(),"LONTITUDE",0),
                SharedPreferencesUtil.getFloat(getContext(),"LATITUDE",0),
                SharedPreferencesUtil.getString(getContext(),"TITLE",""),
                SharedPreferencesUtil.getString(getContext(),"SNIPPET",""));

        uuid = SharedPreferencesUtil.getString(this.getContext(),"UUID","");
        token = SharedPreferencesUtil.getString(this.getContext(),"TOKEN","");
        getDatasFromCloud();
        //获取过滤关键字
        fliterKeyWord.clear();
        fliterKeyWord.addAll(SharedPreferencesUtil.getStringSet(getContext(),"FILTERKEYWORD",null));
        Log.d(TAG, "initView: ");
    }

    /**
     * 请求数据
     */
    private void getDatasFromCloud() {
        //发起请求api
        String apiUrl = "http://hn2.api.okayapi.com/";

        Map<String, String> params = new HashMap<>();
        params.put("s", "App.Table.FreeQuery");
        params.put("uuid", uuid);
        params.put("token",token);
        params.put("app_key","8534042F6FF26B8292E70B1348D443DE");
        params.put("model_name","DN_Notes");
        params.put("select","title,content,longitude,latitude,url,uuid");
        params.put("logic","and");


        JSONArray where = new JSONArray();
        JSONArray cond1 = new JSONArray();
        cond1.put("longitude");
        cond1.put("<");
        cond1.put(((int)(mylb.getLon()+precision))+"");
        JSONArray cond2 = new JSONArray();
        cond2.put("longitude");
        cond2.put(">");
        cond2.put(((int)(mylb.getLon()-precision))+"");
        where.put(cond1);
        where.put(cond2);

        Log.e(TAG, "getDatasFromCloud: onSuccess "+mylb.toString());

        MyApplication.getmInstance().getMyOkHttp().post()
                .url(apiUrl)
                .params(params)
                .addParam("where",where.toString())
                .addParam("perpage","100")
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(TAG, "doDelete onSuccess:" + response);
                        int code = response.optInt("ret");
                        if (code==200){
                            processData(response.optJSONObject("data").optJSONArray("list"));
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d(TAG, "doUpload onFailure:" + error_msg);
                    }

                });
    }


    /**
     * 对请求回的数据进行处理
     * @param jsonArray
     */
    private void processData(JSONArray jsonArray) {
        adatas = new ArrayList<>();
        try {
            for (int i=0;i<jsonArray.length();i++){
                String title = jsonArray.getJSONObject(i).getString("title");
                String content = jsonArray.getJSONObject(i).getString("content");
                double lat = jsonArray.getJSONObject(i).getDouble("latitude");
                double lon = jsonArray.getJSONObject(i).getDouble("longitude");
                if (mylb.getLat()+precision < lat|| mylb.getLat()-precision > lat)
                    continue;
                boolean have = false;
                for (String ky:fliterKeyWord)
                    if (title.contains(ky)||content.contains(ky)){
                        have = true;
                        break;
                    }
                if (have) continue; //存在关键字，跳过该项数据
                if (!searchWord.equals(""))
                    if((title.indexOf(searchWord)==-1)&&(content.indexOf(searchWord)==-1)) continue;
                DPoint startLatlng = new DPoint(mylb.getLat(),mylb.getLon());
                DPoint endLatlng = new DPoint(lat,lon);
                adatas.add(new AroundData(title, content,
                        jsonArray.getJSONObject(i).getString("url"),lon, lat,
                        calculateLineDistance(startLatlng,endLatlng)));
            }
        } catch (JSONException e) {
                e.printStackTrace();
            }
        Log.d(TAG, "processData: "+adatas.size());
        searchWord = "";

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv2.setLayoutManager(llm);
        AroundAdapter aroundAdapter = new AroundAdapter(adatas);
        rcv2.setAdapter(aroundAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.search2)
    public void onViewClicked() {
        searchWord = etSearch2.getText().toString();
        getDatasFromCloud();
    }


}
