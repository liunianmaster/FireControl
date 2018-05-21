package com.example.zz_xa.firecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.model.EaseMember;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.utils.EaseGlobal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZ-XA on 2018/4/26.
 */

public class ChatGroup extends Fragment {
    private Button[] mTabs;
    private EaseConversationListFragment conversationListFragment;
    // private EaseContactListFragment contactListFragment;

    private EaseChatFragment chatFragment;
    private EaseBaseFragment baseFragment;

    public static ChatActivity activityInstance;
    String toChatUsername;
    EMMessageListener messageListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View v = inflater.inflate(R.layout.layout_chatinfo,container,false);
        View v = inflater.inflate(R.layout.activity_chat, container, false);


        conversationListFragment = new EaseConversationListFragment();



        //   contactListFragment = new EaseContactListFragment();


        chatFragment = new EaseChatFragment();
        //EaseChatFragment 聊天页面，最主要的fragment
        //EaseContactListFragment 联系人页面
        //EaseConversationListFragment  会话列表页面

        //chatFragment.hideTitleBar();


        //chatFragment.getActivity().setTitle("111");
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        // it is group chat
        intent.putExtra("chatType", 2);
        //intent.putExtra("userId", groupAdapter.getItem(position - 3).getGroupId());
        String s = "47426898558977";
        intent.putExtra("userId", s);
        //startActivity(intent);

        //user or group id
        //toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        toChatUsername = s;
       // chatFragment = new EaseChatFragment();
        //set arguments
        chatFragment.setArguments(intent.getExtras());
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();


        List<EaseMember> memberList = new ArrayList<>();
        EaseMember em = new EaseMember();
        em.member_hxid = "zzmaster";
        //em.member_headphoto = "http://img2.woyaogexing.com/2018/04/26/5f22905fee12337c!400x400_big.jpg";
        //em.member_headphoto = "http://t1966652k0.imwork.net:25024/Android/img/zzmaster.png";
        em.member_headphoto = "file:///android_asset/zzmaster.png";
        em.member_nickname = "大江中队";
        memberList.add(em);

        EaseMember em2 = new EaseMember();
        em2.member_hxid = "master";
        //em2.member_headphoto = "http://img2.woyaogexing.com/2018/04/26/b51759840962e1d5!400x400_big.jpg";
        //em2.member_headphoto = "http://t1966652k0.imwork.net:25024/Android/img/master.png";
        em2.member_headphoto = "file:///android_asset/master.png";
        em2.member_nickname = "指挥中心";
        memberList.add(em2);

        EaseMember em3 = new EaseMember();
        em3.member_hxid = "aamaster";
        //em3.member_headphoto = "http://img5.duitang.com/uploads/item/201508/30/20150830132007_TjANX.thumb.224_0.jpeg";
        //em3.member_headphoto = "http://t1966652k0.imwork.net:25024/Android/img/aamaster.png";
        em3.member_headphoto = "file:///android_asset/aamaster.png";
        em3.member_nickname = "芜湖中队";
        memberList.add(em3);

        EaseGlobal.memberList = memberList;

        String us = EMClient.getInstance().getCurrentUser();
        String nickname = "";
        if(us.equals("zzmaster")){
            nickname = "大江中队";
        }else if(us.equals("master")){
            nickname = "指挥中心";
        }else if (us.equals("aamaster")){
            nickname = "芜湖中队";
        }
       // Toast.makeText(getContext(), nickname, 0).show();

        // getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment).
        //FragmentTransaction trx = getSupportFragmentManager().beginTransaction();


        //EMMessageListener messageListener = new EMMessageListener() {
        /*
        messageListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for(EMMessage message : list){
                    DemoHelper.getInstance().getNotifier().onNewMsg(message);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
        */


        return v;
    }
/*
    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list){
            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername) || message.conversationId().equals(toChatUsername)) {
             //   messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);

            //    conversation.markMessageAsRead(message.getMsgId());
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }
*/
    /*
    @Override
    public void onResume() {
        super.onResume();

        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(getActivity());

        EMClient.getInstance().chatManager().addMessageListener(messageListener);


    }

    @Override
    public void onStop() {

        EMClient.getInstance().chatManager().removeMessageListener(messageListener);

        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(getActivity());

        super.onStop();

    }
    */
}
