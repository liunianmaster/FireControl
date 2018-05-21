package com.example.zz_xa.firecontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EasePreferenceManager;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.net.InterfaceAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZZ-XA on 2018/4/11.
 */

//public class ChatInfoFragment extends AppCompatActivity implements Fragment,  EaseBaseActivity{
public class ChatInfoFragment extends Fragment {
    private Button[] mTabs;
    private EaseConversationListFragment conversationListFragment;
   // private EaseContactListFragment contactListFragment;

    private EaseChatFragment chatFragment;
    private EaseBaseFragment baseFragment;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View v = inflater.inflate(R.layout.layout_chatinfo,container,false);
        View v = inflater.inflate(R.layout.activity_chatlist, container, false);


        conversationListFragment = new EaseConversationListFragment();



     //   contactListFragment = new EaseContactListFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,conversationListFragment)
       //         .add(R.id.fragment_container,contactListFragment)
       //         .hide(contactListFragment)
                .show(conversationListFragment)
                .commit();
        //conversationListFragment.hideTitleBar();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(getActivity(),ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID,
                        conversation.conversationId()));
            }
        });

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
        startActivity(intent);


       // getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment).
        //FragmentTransaction trx = getSupportFragmentManager().beginTransaction();

        return v;
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);


        //View v = inflater.inflate(R.layout.layout_chatinfo,container,false);
        View v = inflater.inflate(R.layout.main_tab_weixin,container,false);

        RelativeLayout relativeLayout = (RelativeLayout)v.findViewById(R.id.relative_chat);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent (getActivity(),ChatActivity.class);
                startActivity(intent);
                return false;
            }
        });


        relativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent (getActivity(),ChatActivity.class);
                Intent intent = new Intent (getActivity(),VideoLive.class);
                startActivity(intent);
            }
        });
        */
        /*
        Button button = (Button)v.findViewById(R.id.relative_chat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(),ChatActivity.class);
                startActivity(intent);
            }
        });

        return v;

    }
*/

}
