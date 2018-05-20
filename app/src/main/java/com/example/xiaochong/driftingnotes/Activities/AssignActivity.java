package com.example.xiaochong.driftingnotes.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaochong.driftingnotes.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AssignActivity extends AppCompatActivity {

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
                Log.d(TAG, "onViewClicked: 注册按钮点下");
                break;
            case R.id.bt_return:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
