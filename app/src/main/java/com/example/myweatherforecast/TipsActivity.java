package com.example.myweatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;

public class TipsActivity extends AppCompatActivity {
    private RecyclerView rlvTips;
    private TipsAdapter mTipsAapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        rlvTips = findViewById(R.id.rlv_tips);
        Intent intent = getIntent();
       // Log.d("xie","信息不为空！"+ intent);
       DayWeatherBean weatherBeanw =(DayWeatherBean) intent.getSerializableExtra("tips");
        if(weatherBeanw == null){
            Log.d("xie","信息为空！");
            return;
        }
        mTipsAapter = new TipsAdapter(this,weatherBeanw.getmTipsBeans());
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rlvTips.setAdapter(mTipsAapter);
        rlvTips.setLayoutManager(new LinearLayoutManager(this));
    }
}
