package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

public class Notices extends DataSupport {

    private String cityName;

    private String weatherId;

    private boolean loh;

    private String temperature;

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

    public boolean isLoh() {
        return loh;
    }

    public void setLoh(boolean loh) {
        this.loh = loh;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
