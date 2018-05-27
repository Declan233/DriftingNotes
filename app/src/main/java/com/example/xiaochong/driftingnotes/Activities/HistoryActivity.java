package com.example.xiaochong.driftingnotes.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.DPoint;
import com.example.xiaochong.driftingnotes.Adapter.AroundAdapter;
import com.example.xiaochong.driftingnotes.Adapter.HistoryAdapter;
import com.example.xiaochong.driftingnotes.Adapter.KeyWordAdapter;
import com.example.xiaochong.driftingnotes.Entity.AroundData;
import com.example.xiaochong.driftingnotes.R;
import com.example.xiaochong.driftingnotes.Utils.DialogUtil;
import com.example.xiaochong.driftingnotes.Utils.SharedPreferencesUtil;
import com.example.xiaochong.driftingnotes.mApp.MyApplication;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.amap.api.location.CoordinateConverter.calculateLineDistance;

public class HistoryActivity extends Activity {

    @Bind(R.id.rcv4)
    RecyclerView rcv4;

    private String uuid;
    private String token;


    private List<AroundData> datas;

    private static final String TAG = "HistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        uuid = SharedPreferencesUtil.getString(this,"UUID","");
        token = SharedPreferencesUtil.getString(this,"TOKEN","");
        getDatasFromCloud();

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


        JSONArray where = new JSONArray();
        JSONArray cond1 = new JSONArray();
        cond1.put("uuid");
        cond1.put("=");
        cond1.put(uuid);
        where.put(cond1);

        Log.e(TAG, "getDatasFromCloud: onSuccess "+where.toString());

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
        datas = new ArrayList<>();
        try {
            for (int i=0;i<jsonArray.length();i++){
                String title = jsonArray.getJSONObject(i).getString("title");
                String content = jsonArray.getJSONObject(i).getString("content");
                double lat = jsonArray.getJSONObject(i).getDouble("latitude");
                double lon = jsonArray.getJSONObject(i).getDouble("longitude");


                datas.add(new AroundData(title, content,
                        jsonArray.getJSONObject(i).getString("url"),lon, lat));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "processData: "+datas.size());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv4.setLayoutManager(llm);
        HistoryAdapter historyAdapter = new HistoryAdapter(datas);
        rcv4.setAdapter(historyAdapter);
        historyAdapter.setItemClickListener(new HistoryAdapter.MyItemClickListener() {
            /**
             * 关键词单个项中的删除按钮的监听事件设置
             * @param view
             * @param position
             */
            @Override
            public void onItemClick(View view, int position) {
                deleteItem(position);
            }
        });
    }

    private void deleteItem(int position) {
        DialogUtil dialogUtil = new DialogUtil();
        dialogUtil.show("确定删除", "标题："+datas.get(position).getTitle()+"\n内容："+datas.get(position).getContent(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HistoryActivity.this, "点击了确定 " + which, Toast.LENGTH_SHORT).show();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HistoryActivity.this, "点击了取消 " + which, Toast.LENGTH_SHORT).show();
            }
        }, getFragmentManager());
    }


}
