package com.example.zz_xa.firecontrol.Plotting;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.zz_xa.firecontrol.R;

/**
 * Created by ZZ-XA of wxb on 2018/4/12.
 * Fix by:
 */

public class MarkerPlotting extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.marker_plotting, container, false);
        /*
        Button button = (Button)v.findViewById(R.id.btn_yangan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(),WwebView.class);
                startActivity(intent);
            }
        });
        */

        ImageButton imgBtnYangan = (ImageButton)v.findViewById(R.id.img_btn_yangan);
        imgBtnYangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("温馨提示");
                builder.setMessage("智能烟感功能，攻城狮在努力开发中！！！");
                builder.setPositiveButton("行，再等等",null);
                builder.show();
            }
        });
        ImageButton imgBtnPenlin = (ImageButton)v.findViewById(R.id.img_btn_penlin);
        imgBtnPenlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("温馨提示");
                builder.setMessage("智能喷淋功能，攻城狮在努力开发中！！！");
                builder.setPositiveButton("行，再等等",null);
                builder.show();
            }
        });
        ImageButton imgBtnMen = (ImageButton)v.findViewById(R.id.img_btn_men);
        imgBtnMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("温馨提示");
                builder.setMessage("智能卷帘门功能，攻城狮在努力开发中！！！");
                builder.setPositiveButton("行，再等等",null);
                builder.show();
            }
        });
        ImageButton imgBtnXiaohuo = (ImageButton)v.findViewById(R.id.img_btn_xiaohuo);
        imgBtnXiaohuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("温馨提示");
                builder.setMessage("智能消火栓功能，攻城狮在努力开发中！！！");
                builder.setPositiveButton("行，再等等",null);
                builder.show();
            }
        });
        ImageButton imgBtnZhongkong = (ImageButton)v.findViewById(R.id.img_btn_zhongkong);
        imgBtnZhongkong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("温馨提示");
                builder.setMessage("中控室功能，攻城狮在努力开发中！！！");
                builder.setPositiveButton("行，再等等",null);
                builder.show();
            }
        });
        ImageButton imgBtnQita = (ImageButton)v.findViewById(R.id.img_btn_qita);
        imgBtnQita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("温馨提示");
                builder.setMessage("其他设备功能，攻城狮在努力开发中！！！");
                builder.setPositiveButton("行，再等等",null);
                builder.show();
            }
        });
        return v;
    }
}
