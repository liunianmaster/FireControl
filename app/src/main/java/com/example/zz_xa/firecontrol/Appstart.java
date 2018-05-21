package com.example.zz_xa.firecontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hyphenate.easeui.ui.EaseBaseActivity;

/**
 * Created by ZZ-XA on 2018/4/24.
 */

public class Appstart extends EaseBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appstart);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Appstart.this, LoginActivity.class);
                startActivity(intent);
                Appstart.this.finish();
            }
        },1000);
    }
}
