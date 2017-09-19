package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

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

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
                Intent intent = new Intent();
                intent.setClass(AddAlarmActivity.this,MainActivity.class);
                AddAlarmActivity.this.finish();
                startActivity(intent);
            }
        });
    }
    public void addAlarm(){
        if(alarmTime.getInt("count",1)<7){
            timeEditor.putInt("alarmTime_Hour" + alarmTime.getInt("count",0),getHourTime());
            timeEditor.putInt("alarmTime_Minute" + alarmTime.getInt("count",0),getMinuteTime());
            timeEditor.apply();
            String temp = String.valueOf(alarmTime.getInt("alarmTime_Hour" + alarmTime.getInt("count",0),1))
                    +":"+String.valueOf(alarmTime.getInt("alarmTime_Minute"+ alarmTime.getInt("count",0),1));
            timeEditor.putInt("count",alarmTime.getInt("count",0)+1);
            if(timeEditor.commit()) {
                Toast.makeText(AddAlarmActivity.this, "添加闹钟成功:"+temp, Toast.LENGTH_LONG).show();
            }
        }else {
            timeEditor.putInt("count",0);
            timeEditor.putInt("alarmTime_Hour" + alarmTime.getInt("count",0),getHourTime());
            timeEditor.putInt("alarmTime_Minute" + alarmTime.getInt("count",0),getMinuteTime());
            timeEditor.apply();
            String temp = String.valueOf(alarmTime.getInt("alarmTime_Hour" + alarmTime.getInt("count",0),1) )
                    +":"+String.valueOf(alarmTime.getInt("alarmTime_Minute"+ alarmTime.getInt("count",0),1) );
            timeEditor.putInt("count",alarmTime.getInt("count",0)+1);
            if(timeEditor.commit()) {
                Toast.makeText(AddAlarmActivity.this, "添加闹钟成功*:"+temp, Toast.LENGTH_LONG).show();
        }
        }
    }

    public int getHourTime(){
        if (Build.VERSION.SDK_INT >= 23 )
            return timePicker.getHour();
        else
            return timePicker.getCurrentHour();
    }

    public int getMinuteTime(){
        if (Build.VERSION.SDK_INT >= 23 )
            return timePicker.getMinute();
        else
            return timePicker.getCurrentMinute();
    }
}
