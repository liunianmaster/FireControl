package com.example.zz_xa.firecontrol.Erwei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zz_xa.firecontrol.R;
import com.example.zz_xa.firecontrol.WwebView;

/**
 * Created by ZZ-XA of wxb on 2018/4/12.
 * Fix by:
 */

public class ErweiPlan extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.erwei_plan, container, false);
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerAdapter());


        return v;
    }

    public void fun(){
        Intent intent = new Intent (getActivity(),WwebView.class);
        startActivity(intent);
    }


}
