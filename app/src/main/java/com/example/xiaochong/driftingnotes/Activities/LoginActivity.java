package com.example.xiaochong.driftingnotes.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xiaochong.driftingnotes.R;
import com.example.xiaochong.driftingnotes.Utils.MD5Util;
import com.example.xiaochong.driftingnotes.Utils.SharedPreferencesUtil;
import com.example.xiaochong.driftingnotes.mApp.MyApplication;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @Bind(R.id.ed_username)
    EditText edUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_login)
    Button btLogin;
    @Bind(R.id.usr_image)
    ImageView usrImage;
    @Bind(R.id.bt_assign)
    Button btAssign;

    private String mphnum;
    private String mpsw;

    private static String uuid;
    private static String token;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //判断登陆状态
        determin();
    }

    /**
     * 发起登陆状态请求
     */
    private void determin() {
        if (SharedPreferencesUtil.islogin(this)) {
            String Token = SharedPreferencesUtil.getString(this,"TOKEN","");
            String Uuid = SharedPreferencesUtil.getString(this,"UUID","");

            String apiUrl = "http://hn2.api.okayapi.com/";
            Map<String, String> params = new HashMap<>();
            params.put("s", "App.User.Check");
            params.put("uuid", Uuid);
            params.put("token",Token);
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
                            confirmLoginState(code);
                        }

                        @Override
                        public void onFailure(int statusCode, String error_msg) {
                            Log.d(TAG, "doPost onFailure:" + error_msg);
                        }
                    });

        }
    }

    /**
     * 根据登陆状态作出反应
     * @param code
     */
    private void confirmLoginState(int code) {
        if (code==0){
            startActivity(new Intent(this,MainFragmentActivity.class));
            finish();
        }else
            Toast.makeText(this,"登陆状态失效，请重新登陆",Toast.LENGTH_SHORT).show();
    }

    /**
     * 保存登陆凭证
     */
    private void saveUserInfo() {
//        SharedPreferencesUtil.saveString(this, "USERNAME", mphnum);
//        SharedPreferencesUtil.saveString(this, "USERPSW", mpsw);
        SharedPreferencesUtil.saveString(this, "UUID", uuid);
        SharedPreferencesUtil.saveString(this, "TOKEN", token);
        SharedPreferencesUtil.setState(this);
    }

    @OnClick({R.id.bt_login, R.id.bt_assign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                if (validate()) {
                    Login(edUsername.getText().toString(),etPassword.getText().toString());
                }
                break;
            case R.id.bt_assign:
                startActivity(new Intent(this, AssignActivity.class));
                break;
        }
    }


    /**
     * 检查用户输入的信息
     * @return
     */
    private boolean validate() {
        mphnum = edUsername.getText().toString();
        mpsw = etPassword.getText().toString();
        if (mphnum.equals("")) {
            Toast.makeText(getApplicationContext(), "请输入您的用户名", Toast.LENGTH_SHORT).show();
            return false;
        }if (mphnum.length() < 5) {
            Toast.makeText(getApplicationContext(), "用户名长度不低于5位", Toast.LENGTH_SHORT).show();
            edUsername.setText("");
            return false;
        }
        if (mpsw.equals("")) {
            Toast.makeText(getApplicationContext(), "请输入您的密码", Toast.LENGTH_SHORT).show();
            return false;
        }if (mpsw.length() < 6) {
            Toast.makeText(getApplicationContext(), "密码长度不低于6位", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            return false;
        }
        return true;
    }

    /**
     *  发起登陆请求
     * @param username
     * @param password
     */
    private void Login(String username,String password){
        //发起请求api
        String apiUrl = "http://hn2.api.okayapi.com/";

        //加密
        password = MD5Util.encrypt(password);

        Map<String, String> params = new HashMap<>();
        params.put("s", "App.User.Login");
        params.put("username", username);
        params.put("password",password);
        params.put("app_key","8534042F6FF26B8292E70B1348D443DE");

        MyApplication.getmInstance().getMyOkHttp().post()
                .url(apiUrl)
                .params(params)
                .tag(this)
                .enqueue(new JsonResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        Log.d(TAG, "doPost onSuccess JSONObject:" + response);
                        int code = response.optInt("ret");
                        LoginResult(code,response);
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        Log.d(TAG, "doPost onFailure:" + error_msg);
                    }
                });
    }

    /**
     * 根据服务器返回确定是否登陆成功
     * @param ret
     * @param response
     * @return
     */
    private boolean LoginResult(int ret,JSONObject response) {
        if (ret==200){//登陆成功
            if(response.optJSONObject("data").optInt("code")==0) {
                uuid = response.optJSONObject("data").optString("uuid");
                token = response.optJSONObject("data").optString("token");
                saveUserInfo();
                Intent intent = new Intent(this, MainFragmentActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        }else {//登陆失败
            String msg = response.optJSONObject("data").optString("err_msg");
            edUsername.setText("");etPassword.setText("");
            Toast.makeText(this,"注册失败,"+msg,Toast.LENGTH_SHORT).show();
        }
        return false;

    }


}
