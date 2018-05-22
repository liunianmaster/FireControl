package com.example.zz_xa.firecontrol.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zz_xa.firecontrol.R;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconScrollTabBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZZ-XA of wxb on 2018/4/13.
 * Fix by:
 */

public class ChatAddressList extends Fragment {
    private EaseContactListFragment contactListFragment;
    private EaseEmojiconScrollTabBar tabBar;
  //  private EaseConversationListFragment conversationListFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View v = inflater.inflate(R.layout.layout_chatinfo,container,false);
        View v = inflater.inflate(R.layout.activity_address_list, container, false);
        contactListFragment = new EaseContactListFragment();
      //  conversationListFragment = new EaseConversationListFragment();
        //contactListFragment.hideTitleBar();

        contactListFragment.setContactsMap(getContacts());
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container2, contactListFragment)
          //      .add(R.id.fragment_container,conversationListFragment)
           //     .hide(conversationListFragment)
                .show(contactListFragment)
                .commit();

        return v;
    }

    private Map<String, EaseUser> getContacts(){
        Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
        /*
        for(int i = 1; i <= 10; i++){
            EaseUser user = new EaseUser("easeuitest" + i);
            contacts.put("easeuitest" + i, user);
        }
        */
        EaseUser uu = new EaseUser("aamaster");
        contacts.put("aamaster",uu);
        uu = new EaseUser("zzmaster");
        contacts.put("zzmaster",uu);
       // uu = new EaseUser("小明");
      //  contacts.put("xiaoming",uu);
        return contacts;
    }

}
