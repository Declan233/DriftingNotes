package com.example.xiaochong.driftingnotes.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaochong.driftingnotes.Cache.SharedPreferencesUtil;
import com.example.xiaochong.driftingnotes.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.ed_username)
    EditText edUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_login)
    Button btLogin;

    private String mUserName = "18996400896";
    private String mPassWord = "abcd1234";

    private String mphnum;
    private String mpsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        determin();
    }

    private void determin() {
        if(SharedPreferencesUtil.islogin(this)){
            jump();
        }
    }

    @OnClick(R.id.bt_login)
    public void onViewClicked() {
        if (validate()){
            saveUserInfo();
            jump();

        }
    }

    /**
     * 保存用户名和密码
     */
    private void saveUserInfo() {
        SharedPreferencesUtil.saveString(this,"USERNAME",mphnum);
        SharedPreferencesUtil.saveString(this,"USERPSW",mpsw);
        SharedPreferencesUtil.setState(this);
    }


    private void jump() {
        Intent intent = new Intent(this,MainFragmentActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validate() {
        mphnum = edUsername.getText().toString();
        mpsw = etPassword.getText().toString();
        if (mphnum.equals("")) {
            Toast.makeText(getApplicationContext(), "请输入您的手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mpsw.equals("")){
            Toast.makeText(getApplicationContext(), "请输入您的密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mphnum.length()<11||!mphnum.equals(mUserName)) {
            Toast.makeText(getApplicationContext(), "您输入的手机号有误，请重新输入", Toast.LENGTH_SHORT).show();
            edUsername.setText("");
            return false;
        }
        if(!mpsw.equals(mPassWord)) {
            Toast.makeText(getApplicationContext(), "您输入的密码有误，请重新输入", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            return false;
        }
        return true;
    }

}
