package com.example.zz_xa.firecontrol;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.ui.EaseBaseActivity;

/**
 * Created by ZZ-XA on 2018/4/13.
 */

public class LoginActivity extends EaseBaseActivity {
    private EditText usernameView;
    private EditText pwdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EMClient.getInstance().isLoggedInBefore()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);
        TextView textJlx = (TextView)findViewById(R.id.text_jlinxin);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"jlinxin.TTF");
        textJlx.setTypeface(typeface);

        usernameView = (EditText) findViewById(R.id.username);
        pwdView = (EditText) findViewById(R.id.password);
        Button loginBtn = (Button) findViewById(R.id.btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(usernameView.getText())){
                    Toast.makeText(getApplicationContext(), "请输入用户名", 0).show();
                    return;
                }
                if(TextUtils.isEmpty(pwdView.getText())){
                    Toast.makeText(getApplicationContext(), "请输入密码", 0).show();
                    return;
                }

                EMClient.getInstance().login(usernameView.getText().toString()
                        , pwdView.getText().toString()
                        , new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();

                            }

                            @Override
                            public void onError(int i, String s) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "登录失败", 0).show();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
            }
        });
    }

}
