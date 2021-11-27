package com.example.myweatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FutureWeather extends RecyclerView.Adapter<FutureWeather.WeatherViewHolder> {
    private Context mContent;
    private List<DayWeatherBean> mWeather;

    public FutureWeather(Context mContent,List<DayWeatherBean> mWeather) {
        this.mContent = mContent;
        this.mWeather = mWeather;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.wea_item, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);

        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DayWeatherBean weatherBean = mWeather.get(position);
        holder.tvWeather.setText(weatherBean.getWea());
        holder.tvC.setText(weatherBean.getTem());
        holder.tvDate.setText(weatherBean.getDate()+"\n"+weatherBean.getWeek());
        holder.tvTem.setText(weatherBean.getTem2()+"~"+weatherBean.getTem1());
        holder.tvCol.setText(weatherBean.getWin()[0]+weatherBean.getWinSpeed());
        holder.tvAir.setText("空气:"+weatherBean.getAir()+weatherBean.getAirLevel());
        holder.ivWeather.setImageResource(getImageOfWeather(weatherBean.getWeaImg()));
        // DayWeatherBean weatherBean = mWeather.get(position);
    }

    @Override
    public int getItemCount() {

        return (mWeather == null)?0:mWeather.size();
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

    class WeatherViewHolder extends RecyclerView.ViewHolder{
        TextView tvWeather,tvTem,tvCol,tvAir,tvC,tvDate;
        ImageView ivWeather;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWeather = itemView.findViewById(R.id.tv_desc);
            tvTem = itemView.findViewById(R.id.tv_tem);
            tvCol = itemView.findViewById(R.id.tv_clo);
            tvAir = itemView.findViewById(R.id.tv_air);
            tvC = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);
            ivWeather = itemView.findViewById(R.id.iv_weather);
        }
    }
}
