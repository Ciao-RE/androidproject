package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

public class Mycitys extends DataSupport {

    private String cityName;

    private String weatherId;

    private String temperature;

    private String responsetext;

    public String getResponsetext() {
        return responsetext;
    }

    public void setResponsetext(String responsetext) {
        this.responsetext = responsetext;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
