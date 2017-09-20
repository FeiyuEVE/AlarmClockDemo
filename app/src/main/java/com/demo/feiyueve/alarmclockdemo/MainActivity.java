package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{

    private ArrayAdapter<String> adapter;
    private List<String> alarmList = new ArrayList<String>();
    private ListView listView;
    private Calendar mCalendar,sysCalendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent alarmClockIntent;

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

        resetAlarmList();

        adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,alarmList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
               {
                   List<AlarmTime> alarmTimes = DataSupport.select("millsTime","id").find(AlarmTime.class);
                   int j=0;
                   for(AlarmTime alarmTime:alarmTimes){
                       String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(alarmTime.getCalendar().getTime());
                       if(date.equals(alarmList.get(j))){
                           DataSupport.delete(AlarmTime.class,alarmTime.getId());
                       }
                       j++;
                   }
                   alarmList.remove(i);
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
                DataSupport.deleteAll(AlarmTime.class);
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
        List<AlarmTime> alarmTimes = DataSupport.findAll(AlarmTime.class);
        for(AlarmTime alarmTime:alarmTimes){
            String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(alarmTime.getCalendar().getTime());
            alarmList.add(date);
        }
    }

    //筛选距当前最近的一个闹钟
    public long minAlarmClock() {
        List<AlarmTime> alarmTimes = DataSupport.order("millsTime desc").find(AlarmTime.class);
        return alarmTimes.get(0).getMillsTime();
    }

    public void alarmClock() {
            Toast.makeText(MainActivity.this, "闹钟启动", Toast.LENGTH_SHORT).show();
            alarmClockIntent.setAction("android.intent.action.alarm");
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmClockIntent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, minAlarmClock(), pendingIntent);
    }
}

