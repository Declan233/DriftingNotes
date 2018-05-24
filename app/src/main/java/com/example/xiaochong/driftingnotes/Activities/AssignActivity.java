package com.example.xiaochong.driftingnotes.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaochong.driftingnotes.mApp.MyApplication;
import com.example.xiaochong.driftingnotes.R;
import com.example.xiaochong.driftingnotes.Utils.MD5Util;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AssignActivity extends Activity {

    @Bind(R.id.a_username)
    EditText aUsername;
    @Bind(R.id.a_password)
    EditText aPassword;
    @Bind(R.id.ar_password)
    EditText arPassword;
    @Bind(R.id.bt_confirm)
    Button btConfirm;
    @Bind(R.id.bt_return)
    Button btReturn;

    private static final String TAG = "AssignActivity";

    private static String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.bt_confirm, R.id.bt_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                register();
                break;
            case R.id.bt_return:
                finish();
                break;
        }
    }

    private boolean register() {
        String username = aUsername.getText().toString();
        String userpassword = aPassword.getText().toString();
        String userrpassword = arPassword.getText().toString();
        if (username.length()<5){
            Toast.makeText(this,"用户名长度不能低于5个字符",Toast.LENGTH_SHORT).show();
            aUsername.setText("");aPassword.setText("");arPassword.setText("");
            return false;
        }else if(userpassword.length()<6){
            Toast.makeText(this,"密码长度不能低于6个字符",Toast.LENGTH_SHORT).show();
            aPassword.setText("");;arPassword.setText("");
            return false;
        }else if(!userpassword.equals(userrpassword)){
            Toast.makeText(this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
            aPassword.setText("");arPassword.setText("");
            return false;
        }
        //密码加密处理
        userpassword = MD5Util.encrypt(userpassword);
        try {
            userpassword = java.net.URLEncoder.encode(userpassword, "utf-8");
            username = java.net.URLEncoder.encode(username, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //发起请求api
        String apiUrl = "http://hn2.api.okayapi.com/";

        Map<String, String> params = new HashMap<>();
        params.put("s", "App.User.Register");
        params.put("username", username);
        params.put("password",userpassword);
        params.put("app_key","8534042F6FF26B8292E70B1348D443DE");

        MyApplication.getmInstance().getMyOkHttp().post()
                .url(apiUrl)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(TAG, "doPost onSuccess JSONObject:" + response);
                        int code = response.optJSONObject("data").optInt("err_code");
                        RegisterResult(code,response);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d(TAG, "doPost onFailure:" + error_msg);
                    }
                });
        return true;
    }

    private void RegisterResult(int code,JSONObject response) {
        if (code==0){//注册成功
            uuid = response.optJSONObject("data").optString("uuid");
            Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
            //2秒钟后回到登陆界面
            TimerTask task = new TimerTask(){
                public void run(){
                    finish();
                }};
            Timer timer = new Timer();
            timer.schedule(task, 2000);
        }else {//注册失败
            String msg = response.optJSONObject("data").optString("err_msg");
            aUsername.setText("");aPassword.setText("");arPassword.setText("");
            Toast.makeText(this,"注册失败,"+msg,Toast.LENGTH_SHORT).show();
        }


    }


}
