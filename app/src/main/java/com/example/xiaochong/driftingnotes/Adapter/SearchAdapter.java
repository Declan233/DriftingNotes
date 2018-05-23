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

        private MyItemClickListener mItemClickListener;

        public SearchAdapter(List<LocationBean> list){
            this.list = list;
            Log.d(TAG, "SearchAdapter: "+this.list.size());
        }


        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent,false);
            MyHolder mh = new MyHolder(view,mItemClickListener);
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

        static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView title;
            TextView snippet;
            private MyItemClickListener mListener;

            public MyHolder(View itemView, MyItemClickListener myItemClickListener) {
                super(itemView);
                //将全局的监听赋值给接口
                this.mListener = myItemClickListener;
                itemView.setOnClickListener(this);
                title = itemView.findViewById(R.id.title);
                snippet = itemView.findViewById(R.id.snippet);
            }

            /**
             * 实现OnClickListener接口重写的方法
             * @param v
             */
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, getPosition());
                }

            }
        }
    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

}

