package com.example.myweatherforecast;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    public static final String URL = "https://tianqiapi.com/api?version=v1&appid=14773547&appsecret=oYG6C2Id";
    public static String doGet(String urlStr){
        String result = "";
        //连接网络
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(urlStr);
           connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.setReadTimeout(5000);
           //从链接中读取数据(二进制数据)
          InputStream inputStream = connection.getInputStream();
          inputStreamReader = new InputStreamReader(inputStream);
          //二进制流送入缓存区
            bufferedReader = new BufferedReader(inputStreamReader);
            String Line = "";
            //从缓冲区一行一行读取字符串
            StringBuilder stringBuilder = new StringBuilder();
            while ((Line = bufferedReader.readLine())!= null){
                stringBuilder.append(Line);
            }
            result = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(connection!= null){
                connection.disconnect();
            }
            if(inputStreamReader!=null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static String getOfCity(String city){
//        String result= "";
        //拼接出获取天气数据的url
        String weatherUrl = URL + "&city=" + city;
        Log.d("fan","---weatherURL---"+weatherUrl);
        String weatherResult =doGet(weatherUrl);
        Log.d("fan","---weather---"+weatherResult);
        return weatherResult;
    }
    //************************************

}
