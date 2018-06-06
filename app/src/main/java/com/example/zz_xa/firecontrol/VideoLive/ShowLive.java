package com.example.zz_xa.firecontrol.VideoLive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.zz_xa.firecontrol.R;

/**
 * Created by Adminstrator of wxb on 2018/6/1.
 * Fix by:
 */

public class ShowLive extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_show_live);

        webView = (WebView)findViewById(R.id.video_show);

        initWeb();

    }
    public void initWeb(){
        String url = "https://www.baidu.com/";
        //url = "https://baike.baidu.com/item/%E8%8A%9C%E6%B9%96%E9%93%B6%E6%B3%B0%E5%9F%8E";
        url = "http://192.168.1.128/live/agora/Agora_Web_SDK_FULL/index.html";
        url = "http://t1966652k0.imwork.net:25024/Android/lmt/";
        url = "http://t1966652k0.imwork.net:25024/Android/fire/zhibo/";
        //String url ="http://t1966652k0.imwork.net:25024/FireManagerSystem/login.html?cc=0551";
        //String url ="http://221.13.129.65:5384/firemanagersystem/login.html?cc=0551";
        //此方法可以在webview中打开链接而不会跳转到外部浏览器
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);//支持播放网页视频
        webView.setWebChromeClient(new WebChromeClient());//重写
        webView.setWebViewClient(new WebViewClient());//不写的话，调到默认浏览器了

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == event.ACTION_DOWN){
                    if(keyCode == event.KEYCODE_BACK){
                        if(webView.canGoBack()){
                            webView.goBack();
                            return true;

                        }
                    }
                }
                return false;
            }
        });
    }
}
