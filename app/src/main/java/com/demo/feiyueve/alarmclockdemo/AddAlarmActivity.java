package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddAlarmActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button bt_ok;
    private SharedPreferences.Editor timeEditor;
    private SharedPreferences alarmTime;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        timeEditor = getSharedPreferences("alarmTime",MODE_PRIVATE).edit();
        alarmTime = getSharedPreferences("alarmTime",MODE_PRIVATE);

        timeEditor.putInt("alarmTime_Hour"+0,-1);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minuteOfHour) {
                    for(int i=0;i<10;i++){
                        if(alarmTime.getInt("alarmTime_Hour"+i,-1)!=-1&&alarmTime.getInt("alarmTime_Minute"+i,-1)!=-1){
                            continue;
                        }
                        if(alarmTime.getInt("alarmTime_Hour"+i,-1)==-1||alarmTime.getInt("alarmTime_Minute"+i,-1)==-1) {
                            timeEditor.putInt("alarmTime_Hour"+i,hourOfDay);
                            timeEditor.putInt("alarmTime_Minute"+i,minuteOfHour);
                            timeEditor.apply();
                            Toast.makeText(AddAlarmActivity.this,"添加闹钟成功",Toast.LENGTH_LONG);
                            break;
                        }
                    }
                }
            });
                Intent intent = new Intent();
                intent.setClass(AddAlarmActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
