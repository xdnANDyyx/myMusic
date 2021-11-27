package com.example.myweatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppCompatSpinner spinner;
    private ArrayAdapter<String> mSpAdapter;
    private String [] mCities;
    private TextView tvWeather,tvTem,tvCol,tvAir,tvC;
    private ImageView ivWeather;
    private RecyclerView rvc;
    private FutureWeather mFuture;
    private DayWeatherBean todayWeather;
    private Handler myHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 6){
                String weather = (String) msg.obj;
                Log.d("xie","--主线程收到了handler天气消息--"+ weather);
                Gson gson = new Gson();
                WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
                Log.d("xie","--解析后的数据--"+ weatherBean.toString());
                updateUiOfWeather(weatherBean);
                Toast.makeText(MainActivity.this,"最新天气更新成功！",Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void updateUiOfWeather(WeatherBean weatherBean) {
        if(weatherBean == null){
            return;
        }
        List<DayWeatherBean> dayWeathers = weatherBean.getDayWeathers();
       todayWeather = dayWeathers.get(0);
        if (todayWeather==null){
           return;
        }
        tvC.setText(todayWeather.getTem());
        tvWeather.setText(todayWeather.getWea()+"("+todayWeather.getDate()+todayWeather.getWeek()+")");
        tvTem.setText(todayWeather.getTem2()+"~"+todayWeather.getTem1());
        tvCol.setText(todayWeather.getWin()[0]+todayWeather.getWinSpeed());
        tvAir.setText("空气质量:"+todayWeather.getAir()+todayWeather.getAirLevel()+"\n"+todayWeather.getAirTips());
        ivWeather.setImageResource(getImageOfWeather(todayWeather.getWeaImg()));
        //未来天气的展示
        dayWeathers.remove(0);//去掉当天天气！
        mFuture = new FutureWeather(this,dayWeathers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rvc.setAdapter(mFuture);
        rvc.setLayoutManager(linearLayoutManager);


    }
    private int getImageOfWeather(String weaStr) {
        //xue、lei、shachen、wu、bingbao、yun、yu、yin、qing
        int result = 0;
        switch (weaStr) {
            case "qing":
                result = R.drawable.qing;
                break;
            case "yin":
                result = R.drawable.ying;
                break;
            case "yu":
                result = R.drawable.yu;
                break;
            case "yun":
                result = R.drawable.yun;
                break;
            case "bingbao":
                result = R.drawable.bingbao;
                break;
            case "wu":
                result = R.drawable.wu;
                break;
            case "shachen":
                result = R.drawable.shachen;
                break;
            case "lei":
                result = R.drawable.lei;
                break;
            case "xue":
                result = R.drawable.xue;
                break;
                default:
                    result = R.drawable.wuw;
                    break;
        }
     return result;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
    }

    private void initView() {
        spinner = findViewById(R.id.city);
        mCities = getResources().getStringArray(R.array.cities);
        mSpAdapter = new ArrayAdapter<>(this,R.layout.sp_item,mCities);
        spinner.setAdapter(mSpAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectCity = mCities[position];
            getWeatherOfCity(selectCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvWeather = findViewById(R.id.tv_desc);
        tvTem = findViewById(R.id.tv_tem);
        tvCol = findViewById(R.id.tv_clo);
        tvAir = findViewById(R.id.tv_air);
        tvC = findViewById(R.id.tv_content);
        ivWeather = findViewById(R.id.iv_weather);
        rvc = findViewById(R.id.rwr_fur);
        tvAir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据
                Intent intent = new Intent(MainActivity.this,TipsActivity.class);
                intent.putExtra("tips",todayWeather);

                startActivity(intent);
            }
        });
    }

    private void getWeatherOfCity(String selectCity) {
        //开启子线程，请求网络。
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求网络
                String weatherOfCity = Utils.getOfCity(selectCity);
                //使用handler将数据传递给主线程
                Message message = Message.obtain();
                message.what = 6;
                message.obj = weatherOfCity;
                myHandler.sendMessage(message);
            }
        }).start();
    }

}
