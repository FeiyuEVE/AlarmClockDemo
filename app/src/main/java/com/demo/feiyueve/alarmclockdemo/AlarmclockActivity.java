package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */


import android.Manifest;
import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

public class AlarmclockActivity extends AppCompatActivity {

    private String cityData;
    private WebView webView;
    private Button bt_alarmStop;
    private SharedPreferences city;
    private MediaPlayer mediaPlayer;
    private Vibrator mVibrator;
    public LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmclock);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        city = getSharedPreferences("city",MODE_PRIVATE);
        cityData = city.getString("city","");

        webView = (WebView) findViewById(R.id.webView);
        bt_alarmStop = (Button) findViewById(R.id.bt_alarmStop);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://m.tianqi.com/"+cityData+"");

        initMediaPlayer();
        mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
        mVibrator.vibrate(new long[]{400,400,400,400},3);

        bt_alarmStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                onStop();

                Intent intent2 = new Intent();
                intent2.setAction("android.intent.action.cancel");
                localBroadcastManager.sendBroadcast(intent2);

                Intent intent = new Intent();
                intent.setClass(AlarmclockActivity.this,MainActivity.class);
                AlarmclockActivity.this.finish();
                startActivity(intent);
            }
        });
    }

    private void initMediaPlayer(){
        mediaPlayer = MediaPlayer.create(AlarmclockActivity.this,R.raw.music);
        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVibrator.cancel();
    }
}
