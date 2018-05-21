package com.example.zz_xa.firecontrol;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZZ-XA on 2018/4/27.
 */

public class SettingsActivity extends AppCompatActivity {
    private static MainActivity sMainActivity = null;
    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

    }

    public void setting_back(View view){
        finish();
    }
    public void initOnclick(View view){
        //Toast.makeText(getBaseContext(), "点击事件", 0).show();
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("此模块功能，攻城狮在努力开发中！！！");
        builder.setPositiveButton("行，再等等",null);
        builder.show();

        //sendMessage();
        sendNewPushMessage();


    }
    private void sendMessage() {
        //checkContacts();
        EMMessage message = EMMessage.createTxtSendMessage("测试发送消息，主要是为了测试是否在线", "aamaster");
        //设置强制推送
        message.setAttribute("em_force_notification", "true");
        //设置自定义推送提示
        JSONObject extObj = new JSONObject();
        try {
            extObj.put("em_push_title", "老版本推送显示内容");
            extObj.put("extern", "定义推送扩展内容");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.setAttribute("em_apns_ext", extObj);
        sendMessage(message);
    }
    /**
     * 发送新版推送消息
     */
    private void sendNewPushMessage() {
        //checkContacts();
        EMMessage message = EMMessage.createTxtSendMessage("测试发送消息，主要是为了测试是否在线", "aamaster");
        //设置强制推送
        message.setAttribute("em_force_notification", "true");
        //设置自定义推送提示
        JSONObject extObj = new JSONObject();
        try {
            extObj.put("em_push_name", "新版推送标题zz");
            extObj.put("em_push_content", "新版推送显示内容");
            extObj.put("extern", "定义推送扩展内容");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.setAttribute("em_apns_ext", extObj);
        sendMessage(message);
    }

    /**
     * 最终调用发送信息方法
     *
     * @param message 需要发送的消息
     */
    private void sendMessage(final EMMessage message) {
        /**
         *  调用sdk的消息发送方法发送消息，发送消息时要尽早的设置消息监听，防止消息状态已经回调，
         *  但是自己没有注册监听，导致检测不到消息状态的变化
         *  所以这里在发送之前先设置消息的状态回调
         */
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                String str = String.format("消息发送成功 msgId %s, content %s", message.getMsgId(), message
                        .getBody());
                //VMLog.i(str);
                //printInfo(str);
                Toast.makeText(getBaseContext(), str, 0).show();
            }

            @Override
            public void onError(final int i, final String s) {
                String str = String.format("消息发送失败 code: %d, error: %s", i, s);
                //VMLog.i(str);
                //printInfo(str);
                Toast.makeText(getBaseContext(), str, 0).show();
            }

            @Override
            public void onProgress(int i, String s) {
                // TODO 消息发送进度，这里不处理，留给消息Item自己去更新
                //VMLog.i("消息发送中 progress: %d, %s", i, s);
            }
        });
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }
    public void logout(View view){
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
                sMainActivity.finish();
                startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
            }
            @Override
            public void onError(int i, String s) {

            }
            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public void setting_logout(final View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("是否退出登录？");
        dialog.setCancelable(false);

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //System.exit(0);
                logout(view);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SettingsActivity.this, "谢谢支持", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }
}
