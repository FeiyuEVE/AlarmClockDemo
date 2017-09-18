package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.AlarmClock;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{

    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> alarmList = new ArrayList<String>();
    private TimePicker timePicker;
    private ListView listView;
    private String city;
    private Calendar mCalendar = Calendar.getInstance();
    private AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_addAlarm = (Button) findViewById(R.id.bt_addAlarm);
        Button bt_weather = (Button) findViewById(R.id.bt_addWeather);
        Button bt_stopWatch = (Button) findViewById(R.id.bt_stopWatch);
        listView = (ListView) findViewById(R.id.listView);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        sharedPreferences = getSharedPreferences("alarmTime",MODE_PRIVATE);
        Intent alarmClockIntent = new Intent(MainActivity.this, AlarmClock.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,alarmClockIntent,0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        resetAlarmList();

        adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_multiple_choice,alarmList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
               {
                   alarmList.remove(i);
                   adapter.notifyDataSetChanged();
                   return true;
               }
        });

        //打开添加闹钟
        bt_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddAlarmActivity.class);
                startActivity(intent);
            }
        });

        //打开秒表
        bt_stopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,StopwatchActivity.class);
                startActivity(intent);
            }
        });

        //打开添加城市天气
        bt_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Addweather.class);
                startActivity(intent);
            }
        });
    }

    //重置AlarmList
    public void resetAlarmList(){
        for(int i=0;i<10;i++)
        {
            alarmList.add(sharedPreferences.getInt("alarmTime_Hour"+i,-1)+":"+sharedPreferences.getInt("alarmTime_Minute"+i,-1));
        }
    }

    public void checkTime(){
        for(int i=0;i<10;i++){
            Calendar newCalendar = Calendar.getInstance();
            newCalendar.setTimeInMillis(System.currentTimeMillis());
            newCalendar.set(Calendar.HOUR,sharedPreferences.getInt("alarmTime_Hour"+i,-1));
            newCalendar.set(Calendar.MINUTE,sharedPreferences.getInt("alarmTime_Minute"+i,-1));
            if(newCalendar.compareTo(mCalendar)==1){
                AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                manager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
            }
        }
    }
}

