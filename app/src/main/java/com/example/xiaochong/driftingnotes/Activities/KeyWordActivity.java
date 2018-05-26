package com.example.xiaochong.driftingnotes.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xiaochong.driftingnotes.Adapter.KeyWordAdapter;
import com.example.xiaochong.driftingnotes.Adapter.SearchAdapter;
import com.example.xiaochong.driftingnotes.R;
import com.example.xiaochong.driftingnotes.Utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KeyWordActivity extends Activity {


    @Bind(R.id.et_keyword)
    EditText etKeyword;
    @Bind(R.id.addword)
    Button addword;
    @Bind(R.id.keyword_back)
    ImageView keywordBack;
    @Bind(R.id.rcv3)
    RecyclerView rcv3;


    private List<String> datas = new ArrayList<>();

    private static final String TAG = "KeyWordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_word);
        ButterKnife.bind(this);
        initView();//需要放在butterknife后面
    }

    /**
     * 初始化视图
     */
    private void initView() {
        Set<String> mm = SharedPreferencesUtil.getStringSet(this, "FILTERKEYWORD", null);
        if (mm == null)
            return;
        datas.addAll(mm);

        loadKeyWord();
    }

    @OnClick({R.id.keyword_back, R.id.addword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.keyword_back:
                finish();
                break;
            case R.id.addword:
                addKeyWord();
                break;
        }
    }

    /**
     * 增加关键字
     */
    private void addKeyWord() {
        String akw = etKeyword.getText().toString();
        if (!akw.equals("") && !datas.contains(akw)) {
            datas.add(akw);//加入
            //调用加载关键字的函数
            loadKeyWord();
            etKeyword.setText("");
        }else if(datas.contains(akw)){
            Toast.makeText(KeyWordActivity.this, "过滤关键字已经存在", Toast.LENGTH_SHORT).show();
            etKeyword.setText("");
        }else
            Toast.makeText(KeyWordActivity.this, "输入为空", Toast.LENGTH_SHORT).show();

    }

    /**
     * 加载过滤关键词
     */
    private void loadKeyWord() {

        //转换成set存储
        Set result = new HashSet(datas);
        SharedPreferencesUtil.saveStringSet(this, "FILTERKEYWORD", result);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv3.setLayoutManager(llm);
        KeyWordAdapter keyWordAdapter = new KeyWordAdapter(datas);
        rcv3.setAdapter(keyWordAdapter);
        keyWordAdapter.setItemClickListener(new KeyWordAdapter.MyItemClickListener() {
            /**
             * 关键词单个项中的删除按钮的监听事件设置
             * @param view
             * @param position
             */
            @Override
            public void onItemClick(View view, int position) {
                deleteKeyWordItem(position);
            }
        });
    }

    /**
     * 删除过滤关键词
     * @param position
     */
    private void deleteKeyWordItem(int position) {
        String deleted =  datas.remove(position);
        Toast.makeText(KeyWordActivity.this, "删除了关键字：" + deleted, Toast.LENGTH_SHORT).show();
        loadKeyWord();
    }


}
