package com.demo.feiyueve.alarmclockdemo;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity{

    Button bt_addAlarm = (Button) findViewById(R.id.bt_addAlarm);
    Button bt_weather = (Button) findViewById(R.id.bt_addWeather);
    Button bt_delAlarm = (Button) findViewById(R.id.bt_delAlarm);
    Button bt_stopWatch = (Button) findViewById(R.id.bt_stopWatch);
    ListView listView = (ListView) findViewById(R.id.listView);
    TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
    ArrayList<String> alarmList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetAlarmList();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
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

        //删除闹钟
        bt_delAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //重置AlarmList
    public void resetAlarmList(){
        for(int i=0;i<5;i++)
        {
            alarmList.add("00");
        }
    }
}

