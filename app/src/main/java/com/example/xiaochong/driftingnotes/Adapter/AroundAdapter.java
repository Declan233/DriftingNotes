package com.example.xiaochong.driftingnotes.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.xiaochong.driftingnotes.R;

import java.util.ArrayList;
import java.util.List;

public class AroundAdapter extends RecyclerView.Adapter<AroundAdapter.MyHolder> {

    private List<String> list;

    private static final String TAG = "AroundAdapter";

    public AroundAdapter(List<String> list){
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent,false);
        MyHolder mh = new MyHolder(view);
        return mh;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView snippet;

        public MyHolder(View itemView) {
            super(itemView);
//            title = itemView.findViewById(R.id.title);
//            snippet = itemView.findViewById(R.id.snippet);
        }
    }

}
