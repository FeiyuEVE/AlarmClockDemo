package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

public class AddAlarmActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button bt_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        bt_ok = (Button) findViewById(R.id.bt_ok);

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
                Intent intent = new Intent();
                intent.setClass(AddAlarmActivity.this,MainActivity.class);
                startActivity(intent);
                AddAlarmActivity.this.finish();
            }
        });
    }
    public void addAlarm(){
        List<AlarmTime> alarmTimes = DataSupport.order("id desc").find(AlarmTime.class);
        Calendar calendar = Calendar.getInstance();
        Calendar sysCal = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, getHourTime());
        calendar.set(Calendar.MINUTE, getMinuteTime());
        if(calendar.getTimeInMillis()<sysCal.getTimeInMillis()){
            calendar.add(Calendar.DATE,1);
        }
        if(alarmTimes.get(0).getId()<7) {
            AlarmTime alarmTime = new AlarmTime();
            alarmTime.setCalendar(calendar);
            alarmTime.setAlamrmMinute(calendar);
            alarmTime.setAlarmHour(calendar);
            alarmTime.setMillsTime(calendar);
            alarmTime.save();
        }else{
            Toast.makeText(AddAlarmActivity.this,"闹钟已添加满\n请先删除其它闹钟",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent = new Intent();
            intent.setClass(AddAlarmActivity.this,MainActivity.class);
            startActivity(intent);
            AddAlarmActivity.this.finish();

        }
        return true;
    }
}
