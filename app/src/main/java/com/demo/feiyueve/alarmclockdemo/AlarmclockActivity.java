package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class AlarmclockActivity extends AppCompatActivity {

    private String cityData;
    private WebView webView;
    private Button bt_alarmDelay,bt_alarmStop;
    private SharedPreferences city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmclock);

        city = getSharedPreferences("city",MODE_PRIVATE);
        cityData = city.getString("city","");

        webView = (WebView) findViewById(R.id.webView);
        bt_alarmDelay = (Button) findViewById(R.id.bt_alarmDelay);
        bt_alarmStop = (Button) findViewById(R.id.bt_alarmStop);



        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://m.tianqi.com"+cityData+"");

    }
}
