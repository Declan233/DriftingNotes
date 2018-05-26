package com.example.xiaochong.driftingnotes.Adapter;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.amap.api.services.geocoder.GeocodeSearch;
import com.example.xiaochong.driftingnotes.Entity.AroundData;
import com.example.xiaochong.driftingnotes.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class AroundAdapter extends RecyclerView.Adapter<AroundAdapter.MyHolder> {

    private List<AroundData> list;

    private static final String TAG = "AroundAdapter";

    private ContentResolver cr;

    public AroundAdapter(List<AroundData> list){
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent,false);
        cr = parent.getContext().getContentResolver();
        MyHolder mh = new MyHolder(view);
        return mh;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String title = list.get(position).getTitle();
        String snippet = list.get(position).getContent();
        String picurl = list.get(position).getPicurl();
        String loc_info = " 经度：" + list.get(position).getLon() + "  纬度：" + list.get(position).getLat();
        String distance = " 距离：" + list.get(position).getDistance();
        if (!picurl.equals(""))
            holder.text_image.setImageBitmap(getBitmap(list.get(position).getPicurl()));
        holder.title.setText(title);
        holder.snippet.setText(snippet);
        holder.loc_info.setText(loc_info);
        holder.distance.setText(distance);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView snippet;
        ImageView text_image;
        TextView loc_info;
        TextView distance;


        public MyHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            snippet = itemView.findViewById(R.id.snippet);
            text_image = itemView.findViewById(R.id.text_image);
            loc_info = itemView.findViewById(R.id.loc_info);
            distance = itemView.findViewById(R.id.distance);
        }
    }

    public static Bitmap getBitmap(String path){

        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
