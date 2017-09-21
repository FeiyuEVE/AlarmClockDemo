package com.demo.feiyueve.alarmclockdemo;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * Created by FeiyuEVE小妖精 on 2017/9/20.
 */

public class AlarmTime extends DataSupport {
    private int id;
    private long millsTime;
    private int alarmHour;
    private int alamrmMinute;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMillsTime() {
        return millsTime;
    }

    public void setMillsTime(long millsTime) {
        this.millsTime = millsTime;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }

    public int getAlamrmMinute() {
        return alamrmMinute;
    }

    public void setAlamrmMinute(int alamrmMinute) {
        this.alamrmMinute = alamrmMinute;
    }
}
