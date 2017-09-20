package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{

    private long systemTime;
    private ArrayAdapter<String> adapter;
    private SharedPreferences.Editor timeEditor;
    private SharedPreferences sharedPreferences;
    private ArrayList<String> alarmList = new ArrayList<String>();
    private ListView listView;
    private Calendar mCalendar,newCalendar,sysCalendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent alarmClockIntent;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Connector.getDatabase();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmClockIntent = new Intent();

        Button bt_addAlarm = (Button) findViewById(R.id.bt_addAlarm);
        Button bt_weather = (Button) findViewById(R.id.bt_addWeather);
        Button bt_stopWatch = (Button) findViewById(R.id.bt_stopWatch);
        Button bt_empty = (Button) findViewById(R.id.bt_empty);
        listView = (ListView) findViewById(R.id.listView);

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        timeEditor = getSharedPreferences("alarmTime",MODE_PRIVATE).edit();

        sharedPreferences = getSharedPreferences("alarmTime",MODE_PRIVATE);

        resetAlarmList();

        adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,alarmList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
               {
                   for(int j=0;j<7;j++) {
                       String timeTemp = sharedPreferences.getInt("alarmTime_Hour" + j, -1)
                               + ":" +sharedPreferences.getInt("alarmTime_Minute" + j, -1);
                       if (alarmList.get(i).equals(timeTemp)){
                           timeEditor.putInt("alarmTime_Hour"+j,-1);
                           timeEditor.putInt("alarmTime_Minute"+j,-1);
                           timeEditor.apply();
                           Toast.makeText(MainActivity.this,"闹钟删除成功",Toast.LENGTH_LONG).show();
                       }
                   }
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
                MainActivity.this.finish();
            }
        });

        //打开秒表
        bt_stopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,StopwatchActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        //打开添加城市天气
        bt_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Addweather.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        bt_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmList.clear();
                timeEditor.clear();
                timeEditor.commit();
                resetAlarmList();
                adapter.notifyDataSetChanged();
            }
        });

        alarmClock();
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("android.intent.action.cancel".equals(intent.getAction())) {
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.cancel(pendingIntent);
            }
        }
    }
    //重置AlarmList
    public void resetAlarmList(){
        for(int i=0;i<7;i++)
        {
            alarmList.add(0,sharedPreferences.getInt("alarmTime_Hour" +i, -1)
                    +":"+sharedPreferences.getInt("alarmTime_Minute" +i, -1));
        }
    }

    //筛选距当前最近的一个闹钟
    public long minAlarmClock() {
        long temp[]={9000000000000000000L,9000000000000000000L,9000000000000000000L,9000000000000000000L,
                9000000000000000000L,9000000000000000000L,9000000000000000000L,9000000000000000000L},minAlarmClock=9000000000000000000L;
        sysCalendar = Calendar.getInstance();
        sysCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        sysCalendar.setTimeInMillis(System.currentTimeMillis());
        newCalendar = Calendar.getInstance();
        newCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        newCalendar.setTimeInMillis(System.currentTimeMillis());
        for (int i = 0; i < 7; i++) {
            newCalendar.set(Calendar.HOUR_OF_DAY, sharedPreferences.getInt("alarmTime_Hour" + i,-1));
            newCalendar.set(Calendar.MINUTE, sharedPreferences.getInt("alarmTime_Minute" + i,-1));
            if(sharedPreferences.getInt("alarmTime_Hour" + i,-1)!=-1) {
                temp[count] = newCalendar.getTimeInMillis();
                systemTime = sysCalendar.getTimeInMillis();
                if (newCalendar.compareTo(sysCalendar) == -1) {
                    newCalendar.add(Calendar.DATE, 1);
                    temp[count] = newCalendar.getTimeInMillis();
                }
                minAlarmClock = temp[7];
                for (int j = 0; j <= count; j++) {
                    if (j < 6 && minAlarmClock > temp[j]) {
                        minAlarmClock = temp[j];
                    }
                }
                count++;
            }
        }
        return minAlarmClock;
    }

    public void alarmClock() {
        if (minAlarmClock() < 5000000000000000000L){
            Toast.makeText(MainActivity.this, "闹钟启动", Toast.LENGTH_SHORT).show();
            alarmClockIntent.setAction("android.intent.action.alarm");
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmClockIntent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, minAlarmClock(), pendingIntent);
         }
    }
}

