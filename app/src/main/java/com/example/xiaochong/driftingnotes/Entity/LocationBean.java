package com.example.xiaochong.driftingnotes.Entity;

import java.io.Serializable;

/**
 * Created by xiaochong on 2018/5/13.
 */

public class LocationBean implements Serializable {

    private double lon;
    private double lat;
    private String title;
    private String snippet;

    public LocationBean(String title,String snippet){
        this.title = title;
        this.snippet = snippet;
    }

    public LocationBean(double lon,double lat,String title,String snippet){
        this.lon = lon;
        this.lat = lat;
        this.title = title;
        this.snippet = snippet;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public String toString(){
        return title+snippet+lon+" "+lat;
    }
}