package com.example.zz_xa.firecontrol.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.zz_xa.firecontrol.R;

/**
 * Created by Adminstrator of wxb on 2018/5/18.
 * Fix by:
 */

public class SettingAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account_secunity);

        init();
    }

    public void init(){
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        TextView textView = (TextView)findViewById(R.id.title_info);
        textView.setText(title);
    }

    public void setting_info_back(View view){
        finish();
    }
}
