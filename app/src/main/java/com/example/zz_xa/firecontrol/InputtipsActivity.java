package com.example.zz_xa.firecontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.zz_xa.firecontrol.Erwei.RecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adminstrator of wxb on 2018/5/17.
 * Fix by:
 */

public class InputtipsActivity extends Activity implements TextWatcher, Inputtips.InputtipsListener {
    private String city = "西安";
    private AutoCompleteTextView mKeywordText;
    private ListView minputlist;
    private static MainActivity mMainActivity = null;
    public static void setMainActivity(MainActivity mainActivity){
        mMainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputtip);

        city = mMainActivity.getMyCity();
        minputlist = (ListView)findViewById(R.id.inputlist);
        mKeywordText = (AutoCompleteTextView)findViewById(R.id.input_edittext);
        mKeywordText.addTextChangedListener(this);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        InputtipsQuery inputtipsQuery = new InputtipsQuery(newText, city);
        inputtipsQuery.setCityLimit(true);
        Inputtips inputtips = new Inputtips(InputtipsActivity.this, inputtipsQuery);
        inputtips.setInputtipsListener(this);
        inputtips.requestInputtipsAsyn();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void search_back(View view){
        finish();
    }

private HashMap<Integer,View> mView;
    private SimpleAdapter adapter;

    @Override
    public void onGetInputtips(final List<Tip> list, int rCode) {
        mView = new HashMap<Integer, View>();
        LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(rCode == AMapException.CODE_AMAP_SUCCESS){
            List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
            for(int i=0; i<list.size(); i++){
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", list.get(i).getName());
                map.put("address",list.get(i).getDistrict()+list.get(i).getAddress());
                //map.put("address",list.get(i).getAddress());
                String add = list.get(i).getAddress();
                String dis = list.get(i).getName();
                String poi = list.get(i).getPoiID();
                String ty = list.get(i).getTypeCode();
                map.put("latlng", list.get(i).getPoint().toString());
                LatLonPoint latLonPoint = list.get(i).getPoint();

                listString.add(map);

                View view = mView.get(i);
                if(view == null){
                    view = layoutInflater.inflate(R.layout.item_layout, null);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                       //     Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(InputtipsActivity.this);
                            builder.setTitle("温馨提示");
                            builder.setMessage("智能烟感功能，攻城狮在努力开发中！！！");
                            builder.setPositiveButton("行，再等等",null);
                            builder.show();
                        }
                    });

                    mView.put(i,view);
                }

            }
            adapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item_layout,
                    new String[] {"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
            minputlist.setAdapter(adapter);

            minputlist.setOnItemClickListener(new MyListener());

            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this.getApplicationContext(), rCode,Toast.LENGTH_SHORT).show();
        }
    }

    class MyListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, String> mMap = (HashMap<String, String>) adapter.getItem(position);
        //    Toast.makeText(getApplicationContext(),mMap.get("latlng").toString(),0).show();
            String latlng = mMap.get("latlng").toString();
            String des = mMap.get("address").toString();
            if(latlng != null && des != null){
                mMainActivity.drawMarkers(latlng, des);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "地点获取失败，请重试",0).show();
            }

        }
    }
}
