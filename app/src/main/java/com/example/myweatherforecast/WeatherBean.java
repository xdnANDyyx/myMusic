package com.example.myweatherforecast;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WeatherBean implements Serializable {
    /**
     * cityid	"101170101"
     * city	"银川"
     * cityEn	"yinchuan"
     * country	"中国"
     * countryEn	"China"
     * update_time	"2021-08-07 12:44:00"
     */
    @SerializedName("cityid")
    private String cityId;
    @SerializedName("city")
    private String city;
    @SerializedName("update_time")
    private String updateTime;

    @Override
    public String toString() {
        return "WeatherBean{" +
                "cityId='" + cityId + '\'' +
                ", city='" + city + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", dayWeathers=" + dayWeathers +
                '}';
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<DayWeatherBean> getDayWeathers() {
        return dayWeathers;
    }

    public void setDayWeathers(List<DayWeatherBean> dayWeathers) {
        this.dayWeathers = dayWeathers;
    }

    @SerializedName("data")
    private List<DayWeatherBean> dayWeathers;
}
