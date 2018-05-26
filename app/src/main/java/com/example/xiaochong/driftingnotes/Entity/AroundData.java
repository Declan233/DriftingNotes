package com.example.xiaochong.driftingnotes.Entity;

import android.graphics.Bitmap;

/**
 * Created by xiaochong on 2018/5/25.
 */

public class AroundData {
    String title;
    String content;
    String picurl;
    double lon;
    double lat;
    double distance;

    public AroundData(String title, String content, String picurl,double lon,double lat,double distance){
        this.title = title;
        this.content = content;
        this.picurl = picurl;
        this.lon = lon;
        this.lat = lat;
        this.distance = distance;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPicurl() {
        return picurl;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public double getDistance() {
        return distance;
    }

}
