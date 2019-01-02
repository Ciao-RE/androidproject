package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

public class Now {

    @SerializedName("tmp")
    public String temperature;


    @SerializedName("cond")
    public More more;
    /**
     * fl : -4
     * hum : 19
     * pcpn : 0.0
     * pres : 1023
     * vis : 10
     * wind_deg : 190
     * wind_dir : 南风
     * wind_sc : 2
     * wind_spd : 6
     */

    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String vis;
    private String wind_dir;
    private String wind_sc;

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }

    public String getWind_sc() {
        return wind_sc;
    }

    public void setWind_sc(String wind_sc) {
        this.wind_sc = wind_sc;
    }

    public class More {

        @SerializedName("txt")
        public String info;

    }
    
    

}
