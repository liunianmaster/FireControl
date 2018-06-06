package com.example.zz_xa.firecontrol.Plotting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.zz_xa.firecontrol.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adminstrator of wxb on 2018/5/31.
 * Fix by:
 */

public class MarkerPlotLocation extends Activity implements LocationSource, AMapLocationListener, AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener {

    MapView mapView;
    ListView mapList;

    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_DES = "des";

    private AMapLocationClient mapLocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private LatLng latLng;
    private String city;
    private AMap aMap;
    private String deepType = "";//poi 搜索类型
    private PoiSearch.Query query;// poi 查询条件类
    private PoiSearch poiSearch;
    private PoiResult poiResult; //poi返回的结果
    private PoiOverlay poiOverlay;//poi 图层
    private List<PoiItem> poiItems;//poi 数据

    private SimpleAdapter adapter;
    private RelativeLayout listViewShow;
    private FrameLayout infoShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_plotting_location);

        mapView = (MapView)findViewById(R.id.plot_map);
        mapList = (ListView)findViewById(R.id.plot_list_view);

        listViewShow = (RelativeLayout)findViewById(R.id.plot_list_show);
        infoShow = (FrameLayout)findViewById(R.id.plot_info_show);

        mapView.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        if(aMap == null){
            aMap = mapView.getMap();
            aMap.setOnCameraChangeListener(this);
            setUpMap();
        }
        deepType = "交通地名";

        Intent intent = getIntent();
        String title = intent.getStringExtra("plotTitle");
        TextView textView = (TextView)findViewById(R.id.plotting_title);
        textView.setText(title);
    }

    public void setting_info_back(View view){
        finish();
    }
    public void listBtnClick(View view){
        listViewShow.setVisibility(View.GONE);
        infoShow.setVisibility(View.VISIBLE);
    }
    public void saveInfoBtn(View view){
        Toast.makeText(this, "信息已上传",Toast.LENGTH_SHORT).show();
    }
    public void plotInfoGone(View view){
        listViewShow.setVisibility(View.VISIBLE);
        infoShow.setVisibility(View.GONE);
    }

    private void setUpMap(){
        if(mapLocationClient == null){
            mapLocationClient = new AMapLocationClient(getApplicationContext());
            AMapLocationClientOption locationClientOption = new AMapLocationClientOption();

            //设置定位监听
            mapLocationClient.setLocationListener(this);
            locationClientOption.setOnceLocation(true);
            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mapLocationClient.setLocationOption(locationClientOption);
            mapLocationClient.startLocation();
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        myLocationStyle.strokeColor(Color.BLACK);
        myLocationStyle.radiusFillColor(Color.argb(100,0,0,180));
        myLocationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
    }

    protected void doSearchQuery() {
        aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
        int currentPage = 0;
        query = new PoiSearch.Query("", deepType, city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        LatLonPoint lp = new LatLonPoint(latLng.latitude, latLng.longitude);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));
        // 设置搜索区域为以lp点为圆心，其周围2000米范围
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //显示我的位置
                mListener.onLocationChanged(aMapLocation);
                latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16), 1000, null);
                city = aMapLocation.getProvince();
                doSearchQuery();
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("error: ", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        mapLocationClient.startLocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if(mapLocationClient != null){
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        latLng = cameraPosition.target;
        aMap.clear();
        aMap.addMarker(new MarkerOptions().position(latLng));
        doSearchQuery();
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();

                    if (poiItems != null && poiItems.size() > 0) {


                        List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
                        for (int i=0; i<poiItems.size(); i++){
                            HashMap<String,String> map = new HashMap<String, String>();
                            String n1 = poiItems.get(i).getAdName();//编码
                            String n2 = poiItems.get(i).getAdCode();
                            String n3 = poiItems.get(i).getBusinessArea();
                            String n4 = poiItems.get(i).getCityCode();
                            String n5 = poiItems.get(i).getCityName();
                            String n6 = poiItems.get(i).getDirection();

                            String n7 = poiItems.get(i).getShopID();
                            String n8 = poiItems.get(i).getSnippet();
                            String n9 = poiItems.get(i).getTel();
                            String n0 = poiItems.get(i).getTitle();

                            String d1 = poiItems.get(i).getEmail();
                            String d2 = poiItems.get(i).getParkingType();
                            String d3 = poiItems.get(i).getPoiId();
                            String d4 = poiItems.get(i).getPostcode();
                            String d5 = poiItems.get(i).getProvinceCode();
                            String d6 = poiItems.get(i).getProvinceName();
                            String d7 = poiItems.get(i).getTypeCode();
                            String d8 = poiItems.get(i).getTypeDes();
                            String d9 = poiItems.get(i).getWebsite();
                            int dd = poiItems.get(i).getDistance();
                            String d0 = String.valueOf(dd);


                            //String name = n1+" "+n3+" "+n5+"： "+n8+"： "+n0;
                            //String des = d1+" "+d2+" "+d3+" "+d4+" "+d5+" "+d6+" "+d7+" "+d8+" "+d9+" "+d0;
                            String name = n0;
                            String des = d6+n5+n8;

                            //String des2 =  poiItems.get(i).getPoiId(); //区
                            String des2 =  poiItems.get(i).getTypeDes();

                            double lat = poiItems.get(i).getLatLonPoint().getLatitude();
                            double lng = poiItems.get(i).getLatLonPoint().getLongitude();
                            String la = String.valueOf(lat);
                            String ln = String.valueOf(lng);

                            map.put(KEY_LAT,la);
                            map.put(KEY_LNG, ln);

                            map.put("name",name);
                            //des = "des";
                            map.put(KEY_DES,des);

                            listString.add(map);
                        }
                        adapter = new SimpleAdapter(this, listString, R.layout.item_layout,
                                new String[] {"name", KEY_DES}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
                        mapList.setAdapter(adapter);

                        mapList.setOnItemClickListener(new OnItemClickListener());
                        adapter.notifyDataSetChanged();

  /*                      adapter = new PoiSearch_adapter(this, poiItems);
                        mapList.setAdapter(adapter);
                        mapList.setOnItemClickListener(new mOnItemClickListener());
*/
                    }
                } else {
                    //Log.e("无结果");
                    Toast.makeText(this,"无结果",Toast.LENGTH_SHORT);
                }
            } else {
                //Log.e("无结果");
                Toast.makeText(this,"无结果",Toast.LENGTH_SHORT);
            }
        }else if(rCode ==27) {
            //Logger.e("error_network");
            Toast.makeText(this,"error_network",Toast.LENGTH_SHORT);
        } else if(rCode ==32) {
            //Logger.e("error_key");
            Toast.makeText(this,"error_key",Toast.LENGTH_SHORT);
        } else {
            //Log.e("error_other：" , rCode);
            Toast.makeText(this,"error_other",Toast.LENGTH_SHORT);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    //定位 end

    @Override
    protected void onResume(){
        super.onResume();
        mapLocationClient.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapLocationClient.startLocation();
    }

    @Override
    protected void onDestroy() {
        mapLocationClient.onDestroy();
        super.onDestroy();
    }

    private class OnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            latLng = new LatLng(poiItems.get(position).getLatLonPoint().getLatitude(), poiItems.get(position).getLatLonPoint().getLongitude());
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16), 1000, null);

        /*
        Intent intent = new Intent();
        intent.putExtra(KEY_LAT, poiItems.get(position).getLatLonPoint().getLatitude());
        intent.putExtra(KEY_LNG, poiItems.get(position).getLatLonPoint().getLongitude());
        intent.putExtra(KEY_DES, poiItems.get(position).getTitle());
        setResult(RESULT_OK, intent);
        finish();
        */
        }
    }





}
