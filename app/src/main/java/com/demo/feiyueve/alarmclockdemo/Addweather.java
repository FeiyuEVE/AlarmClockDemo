package com.demo.feiyueve.alarmclockdemo;

/**
 * Created by FeiyuEVE on 2017/9/17.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Addweather extends AppCompatActivity {
    private EditText editText;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addweather);

        editor = getSharedPreferences("city",MODE_PRIVATE).edit();
        editText = (EditText) findViewById(R.id.editText);
        Button bt_addWeather = (Button) findViewById(R.id.bt_addWeather);

        bt_addWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCityData();
                Intent intent = new Intent();
                intent.setClass(Addweather.this, MainActivity.class);
                startActivity(intent);
                Addweather.this.finish();
            }
        });
    }

    public void saveCityData(){
        editor.putString("city",editText.getText().toString());
        if(editor.commit()) {
            Toast.makeText(Addweather.this, "城市添加成功", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            Intent intent = new Intent();
            intent.setClass(Addweather.this,MainActivity.class);
            startActivity(intent);
            Addweather.this.finish();
        }
        return true;
    }
}
