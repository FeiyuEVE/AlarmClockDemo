package com.demo.feiyueve.alarmclockdemo;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;

import java.util.ArrayList;

public class StopwatchActivity extends AppCompatActivity{

    private ListView listView;
    private Chronometer chronometer;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayAdapter<String> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        stringArrayAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_list_item_1,arrayList);
        listView = (ListView) findViewById(R.id.listView1);
        Button  bt_stop = (Button) findViewById(R.id.bt_stop);
        Button  bt_pause = (Button) findViewById(R.id.bt_pause);
        Button  bt_start = (Button) findViewById(R.id.bt_start);
        Button  bt_reset = (Button) findViewById(R.id.bt_reset);
        chronometer = (Chronometer) findViewById(R.id.chronometer2);

        resetArray();

        listView.setAdapter(stringArrayAdapter);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }
        });

        bt_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.add(0,chronometer.getText().toString());
                arrayList.remove(7);
                stringArrayAdapter.notifyDataSetChanged();
            }
        });

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                resetArray();
                chronometer.setBase(SystemClock.elapsedRealtime());
                stringArrayAdapter.notifyDataSetChanged();
            }
        });

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.add(0,chronometer.getText().toString());
                arrayList.remove(7);
                stringArrayAdapter.notifyDataSetChanged();
                chronometer.stop();
            }
        });
    }

    public void resetArray(){
        for(int i=0;i<=6;i++){
            arrayList.add("00:00");
        }
    }
}
