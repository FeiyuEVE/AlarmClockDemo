package com.demo.feiyueve.alarmclockdemo;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * Created by FeiyuEVE小妖精 on 2017/9/20.
 */

public class AlarmTime extends DataSupport {
    private int id;
    private Calendar calendar;
    private long millsTime;
    private boolean repeat;
    private int alarmHour;
    private int alamrmMinute;
    private String weekDay;
    private boolean launch;

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

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
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

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isLaunch() {
        return launch;
    }

    public void setLaunch(boolean launch) {
        this.launch = launch;
    }


}
