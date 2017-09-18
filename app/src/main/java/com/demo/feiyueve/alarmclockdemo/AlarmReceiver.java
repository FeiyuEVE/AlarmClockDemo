package com.demo.feiyueve.alarmclockdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

/**
 * Created by FeiyuEVE on 2017/9/18.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setAction("android.intent.action.alarm");
    }

}
