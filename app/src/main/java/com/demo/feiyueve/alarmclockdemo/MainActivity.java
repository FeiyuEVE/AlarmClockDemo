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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{

    private ArrayAdapter<String> adapter;
    private List<String> alarmList= new ArrayList<String>(Arrays.asList("无闹钟","无闹钟","无闹钟","无闹钟","无闹钟","无闹钟","无闹钟","可继续添加闹钟"));;
    private ListView listView;
    private Calendar mCalendar=Calendar.getInstance();;
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


        adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,alarmList);
        listView.setAdapter(adapter);
        updateAlarmList();
        adapter.notifyDataSetChanged();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
               {
                   List<AlarmTime> alarmTimes = DataSupport.select("millsTime","id").find(AlarmTime.class);
                   int j=0;
                   for(AlarmTime alarmTime:alarmTimes){
                       mCalendar.setTimeInMillis(alarmTime.getMillsTime());
                       mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                       String date=new SimpleDateFormat("yyyy年MM月dd E HH:mm:ss").format(mCalendar.getTime());
                       if(date.equals(alarmList.get(j))){
                           Toast.makeText(MainActivity.this,"已删除"+alarmTime.getId(),Toast.LENGTH_SHORT).show();
                           DataSupport.delete(AlarmTime.class,alarmTime.getId());
                           break;
                       }
                       j++;
                   }
                   if(i==7) {
                       alarmList.set(i, "可继续添加闹钟");
                   }else{
                       alarmList.set(i, "无闹钟");
                   }
                   adapter.notifyDataSetChanged();
                   return true;
               }
        });


        alarmClock();

        //打开添加闹钟
        bt_addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmNumber()) {
                    Intent intent = new Intent(MainActivity.this, AddAlarmActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                } else
                {Toast.makeText(MainActivity.this, "闹钟已添加满\n请先删除", Toast.LENGTH_SHORT).show();}
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
                DataSupport.deleteAll(AlarmTime.class);
                resetAlarmList();
                adapter.notifyDataSetChanged();
            }
        });
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

    public void updateAlarmList(){
        Calendar sysCal = Calendar.getInstance();
        sysCal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        List<AlarmTime> alarmTimes = DataSupport.order("MillsTime").find(AlarmTime.class);
        if(alarmTimes.isEmpty()){
            Toast.makeText(MainActivity.this,"快添加闹钟吧",Toast.LENGTH_SHORT).show();
        }else{
            for(AlarmTime alarmTime:alarmTimes) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd E HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(alarmTime.getMillsTime());
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                if(calendar.getTimeInMillis()<sysCal.getTimeInMillis()){
                    calendar.add(Calendar.DATE,1);
                    alarmTime.setMillsTime(calendar.getTimeInMillis());
                    alarmTime.save();
                }//如果闹钟时间在系统时间之前则将时间加一天
                String dateS = simpleDateFormat.format(calendar.getTime());
                for(int i=0;i<8;i++) {
                    if (alarmList.get(i).equals("可继续添加闹钟") || alarmList.get(i).equals("无闹钟")) {
                        alarmList.set(i, dateS);
                        break;
                    }
                }
            }
        }
    }

    //重置AlarmList
    public void resetAlarmList(){
        for(int i =0;i<7;i++){
            alarmList.set(i,"无闹钟");
        }
        alarmList.set(7,"可继续添加闹钟");
    }

    //筛选距当前最近的一个闹钟
    public long minAlarmClock() {
        List<AlarmTime> alarmTimes = DataSupport.order("millsTime").find(AlarmTime.class);
        long temp = 10000000000L;
        if(!alarmTimes.isEmpty()&&alarmTimes.get(0).getMillsTime()>temp){
            temp = alarmTimes.get(0).getMillsTime();
        }
        return temp;
    }

    public void alarmClock() {
        if(minAlarmClock() > 10000000000L) {
            Toast.makeText(MainActivity.this, "闹钟启动", Toast.LENGTH_SHORT).show();
            alarmClockIntent.setAction("android.intent.action.alarm");
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmClockIntent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, minAlarmClock(), pendingIntent);
        }
    }

    public boolean confirmNumber(){
        int count = 0;
        for(int i=0;i<7;i++) {
            if (alarmList.get(7).equals("可继续添加闹钟")||alarmList.get(i).equals("无闹钟")) {
                return true;
            } else{
                count++;
            }
        }
        if(count == 7){
            return false;
        }
        return true;
    }
}

