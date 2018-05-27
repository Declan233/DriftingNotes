package com.example.xiaochong.driftingnotes.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.xiaochong.driftingnotes.R;

import java.util.List;

/**
 * Created by xiaochong on 2018/5/25.
 */

public class KeyWordAdapter extends RecyclerView.Adapter<KeyWordAdapter.MyHolder> {

    private static final String TAG = "KeyWordAdapter";

    private List<String> list;

    private MyItemClickListener mItemClickListener;


    public KeyWordAdapter(List<String> list){
        this.list = list;
        Log.d(TAG, "KeyWordAdapter: "+list.size()+" "+list.toString() );
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3, parent,false);
        MyHolder mh = new KeyWordAdapter.MyHolder(view,mItemClickListener);
        return mh;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String keyword = list.get(position);
        holder.keyword.setText(keyword);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView keyword;
        Button deletebt;
        private MyItemClickListener mListener;

        public MyHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            keyword = itemView.findViewById(R.id.Keyword);
            deletebt = itemView.findViewById(R.id.delete_bt);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            deletebt.setOnClickListener(this);
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
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

}
