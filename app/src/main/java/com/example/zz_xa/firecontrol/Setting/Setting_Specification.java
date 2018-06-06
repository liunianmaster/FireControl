package com.example.zz_xa.firecontrol.Setting;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zz_xa.firecontrol.R;
import com.example.zz_xa.firecontrol.VideoLive.ShowLive;

import static com.example.zz_xa.firecontrol.WwebView.setSpec;

/**
 * Created by Adminstrator of wxb on 2018/5/31.
 * Fix by:
 */

public class Setting_Specification extends Activity {

    private EditText editText;
    private String value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_specification);
        setSpec(this);

        editText = (EditText)findViewById(R.id.set_text_ip);

        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        String value2 = read.getString("code","");
        if(value2 != null){
            //editText.setText(value);
            editText.setHint(value2);
        }
    }

    public String getEditIp(){
        return value;
    }

    public void setIp(View view){
        String code = editText.getText().toString().trim();
        SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_WORLD_WRITEABLE).edit();
        editor.putString("code",code);

        editor.commit();
        Toast.makeText(this,"设置成功", Toast.LENGTH_SHORT).show();

    }

    public void getIp(View view){
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        value = read.getString("code","");
        Toast.makeText(this,"当前服务器地址: "+value, Toast.LENGTH_SHORT).show();

    }

    public void showLive(View view){
        startActivity(new Intent(Setting_Specification.this, ShowLive.class));
    }

}
