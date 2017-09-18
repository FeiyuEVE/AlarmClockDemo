package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
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

import static android.R.attr.format;

public class MainActivity extends AppCompatActivity{

    private ArrayAdapter<String> adapter;
    private SharedPreferences.Editor timeEditor;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> alarmList = new ArrayList<String>();
    private ListView listView;
    private Calendar mCalendar,newCalendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private long firstTime,systemTime,alarmClock,time;
    private Intent alarmClockIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTime = SystemClock.elapsedRealtime();
        systemTime = System.currentTimeMillis();


        Button bt_addAlarm = (Button) findViewById(R.id.bt_addAlarm);
        Button bt_weather = (Button) findViewById(R.id.bt_addWeather);
        Button bt_stopWatch = (Button) findViewById(R.id.bt_stopWatch);
        listView = (ListView) findViewById(R.id.listView);

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        timeEditor = getSharedPreferences("alarmTime",MODE_PRIVATE).edit();

        sharedPreferences = getSharedPreferences("alarmTime",MODE_PRIVATE);

        alarmClockIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,alarmClockIntent,0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        checkTime();
        resetAlarmList();


        adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
               {
                   alarmList.remove(i);
                   adapter.notifyDataSetChanged();
                   for(int j=0;j<10;j++) {
                       newCalendar = Calendar.getInstance();
                       newCalendar.setTimeInMillis(System.currentTimeMillis());
                       mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                       newCalendar.set(Calendar.HOUR_OF_DAY, sharedPreferences.getInt("alarmTime_Hour" + j, -1));
                       newCalendar.set(Calendar.MINUTE, sharedPreferences.getInt("alarmTime_Minute" + j, -1));
                       alarmClock = newCalendar.getTimeInMillis();
                       if (alarmClock < systemTime) {
                           timeEditor.putInt("alarmTime_Hour"+i,-1);
                           timeEditor.putInt("alarmTime_Minute"+i,-1);
                           timeEditor.apply();
                       }
                   }
                   Toast.makeText(MainActivity.this,"该闹钟已删除",Toast.LENGTH_LONG);
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
            alarmList.add(String.valueOf(newCalendar));
        }
    }

    public void checkTime(){
        for(int i=0;i<10;i++){
            newCalendar = Calendar.getInstance();
            newCalendar.setTimeInMillis(System.currentTimeMillis());
            mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            newCalendar.set(Calendar.HOUR_OF_DAY,sharedPreferences.getInt("alarmTime_Hour"+i,-1));
            newCalendar.set(Calendar.MINUTE,sharedPreferences.getInt("alarmTime_Minute"+i,-1));
            alarmClock = newCalendar.getTimeInMillis();
            if(alarmClock<systemTime){
                newCalendar.add(Calendar.DAY_OF_MONTH, 1);
                alarmClock = newCalendar.getTimeInMillis();
            }
            time = alarmClock - systemTime;
            firstTime += time;

            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,AlarmManager.INTERVAL_DAY,pendingIntent);
        }
    }
}

