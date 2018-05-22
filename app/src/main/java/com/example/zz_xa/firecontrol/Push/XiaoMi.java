package com.example.zz_xa.firecontrol.Push;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.hyphenate.chat.EMMipushReceiver;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by ZZ-XA of wxb on 2018/5/2.
 * Fix by:
 */

public class XiaoMi extends PushMessageReceiver {
    // 当前账户的 regId
    private String regId = null;

    /**
     * 接收客户端向服务器发送注册命令消息后返回的响应
     *
     * @param context 上下文对象
     * @param miPushCommandMessage 注册结果
     */
    @Override public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
        String command = miPushCommandMessage.getCommand();
        List<String> arguments = miPushCommandMessage.getCommandArguments();
        String cmdArg1 = null;
        String cmdArg2 = null;
        if (arguments != null && arguments.size() > 0) {
            cmdArg1 = arguments.get(0);
        }
        if (arguments != null && arguments.size() > 1) {
            cmdArg2 = arguments.get(1);
        }
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (miPushCommandMessage.getResultCode() == ErrorCode.SUCCESS) {
                // 这里可以获取到当前账户的 regId，可以发送给自己的服务器，用来做一些业务处理
                regId = cmdArg1;
            }
        }
       // VMLog.d("onReceiveRegisterResult regId: %s", regId);
        //Toast.makeText(context, regId,0).show();
    }

    /**
     * 接收服务器推送的透传消息
     *
     * @param context 上下文对象
     * @param miPushMessage 推送消息对象
     */
    @Override public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
    }

    /**
     * 接收服务器推送的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息）
     *
     * @param context 上下文
     * @param miPushMessage 推送消息对象
     */
    @Override public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        miPushMessage.setTitle("这里是客户端设置 title");
        super.onNotificationMessageArrived(context, miPushMessage);
    }

    /**
     * 接收服务器发来的通知栏消息（用户点击通知栏时触发）
     *
     * @param context 上下文对象
     * @param miPushMessage 消息对象
     */
    @Override public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);

        Toast.makeText(context, "Click", 0).show();
        miPushMessage.setTitle("Click");
       // Intent var3 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
       // var3.addFlags(268435456);
        //context.startActivity(var3);
       // context.startActivity(new Intent(context, TabActivity.class));

    }

    /**
     * 接收客户端向服务器发送命令消息后返回的响应
     *
     * @param context 上下文对象
     * @param miPushCommandMessage 服务器响应的命令消息对象
     */
    @Override public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }
}
