package com.example.xiaochong.driftingnotes.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.xiaochong.driftingnotes.Entity.LocationBean;
import com.example.xiaochong.driftingnotes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaochong on 2018/5/13.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder>{

        private List<LocationBean> list = new ArrayList<>();

        private static final String TAG = "SearchAdapter";

        public SearchAdapter(List<LocationBean> list){
            this.list = list;
            Log.d(TAG, "SearchAdapter: "+this.list.size());
        }


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
            MyHolder mh = new MyHolder(view);
            return mh;
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            String title = list.get(position).getTitle();
            String snippet = list.get(position).getSnippet();
            holder.title.setText(title);
            holder.snippet.setText(snippet);
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
                title = itemView.findViewById(R.id.title);
                snippet = itemView.findViewById(R.id.snippet);
            }
        }
    }

