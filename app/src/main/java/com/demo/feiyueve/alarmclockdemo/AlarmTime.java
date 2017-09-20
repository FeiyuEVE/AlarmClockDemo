package com.demo.feiyueve.alarmclockdemo;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * Created by FeiyuEVE小妖精 on 2017/9/20.
 */

public class AlarmTime extends DataSupport {
    private Calendar calendar;
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

    public void setMillsTime(Calendar calendar) {
        this.millsTime = calendar.getTimeInMillis();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(Calendar calendar) {
        this.alarmHour = calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getAlamrmMinute() {
        return alamrmMinute;
    }

    public void setAlamrmMinute(Calendar calendar) {
        this.alamrmMinute = calendar.get(Calendar.MINUTE);
    }

}
