package com.demo.feiyueve.alarmclockdemo;

import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Addweather extends AppCompatActivity {
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addweather);

        editText = (EditText) findViewById(R.id.editText);
        Button bt_addWeather = (Button) findViewById(R.id.bt_addWeather);

        bt_addWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("com.addWeather.city",editText.getText().toString());
                intent.setClass(Addweather.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
