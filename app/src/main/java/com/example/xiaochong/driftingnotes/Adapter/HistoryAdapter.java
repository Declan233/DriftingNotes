package com.example.xiaochong.driftingnotes.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaochong.driftingnotes.Entity.AroundData;
import com.example.xiaochong.driftingnotes.R;

import java.util.List;

import static com.example.xiaochong.driftingnotes.Adapter.AroundAdapter.getBitmap;

/**
 * Created by xiaochong on 2018/5/25.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyHolder> {

    private static final String TAG = "HistoryAdapter";

    private List<AroundData> list;

    private MyItemClickListener mItemClickListener;


    public HistoryAdapter(List<AroundData> list){
        this.list = list;
        Log.d(TAG, "HistoryAdapter: "+list.size()+" "+list.toString() );
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item4, parent,false);
        MyHolder mh = new HistoryAdapter.MyHolder(view,mItemClickListener);
        return mh;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String title = list.get(position).getTitle();
        String snippet = list.get(position).getContent();
        String picurl = list.get(position).getPicurl();
        String loc_lon = " 经度：" + list.get(position).getLon();
        String loc_lat = " 纬度：" + list.get(position).getLat();
        if (!picurl.equals(""))
            holder.history_text_image.setImageBitmap(getBitmap(list.get(position).getPicurl()));
        holder.history_title.setText(title);
        holder.history_snippet.setText(snippet);
        holder.history_loc_lon.setText(loc_lon);
        holder.history_loc_lat.setText(loc_lat);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView history_title;
        TextView history_snippet;
        ImageView history_text_image;
        TextView history_loc_lon;
        TextView history_loc_lat;

        Button deletebt;
        private MyItemClickListener mListener;

        public MyHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            history_title = itemView.findViewById(R.id.history_title);
            history_snippet = itemView.findViewById(R.id.history_snippet);
            history_text_image = itemView.findViewById(R.id.history_text_image);
            history_loc_lon = itemView.findViewById(R.id.history_loc_lon);
            history_loc_lat = itemView.findViewById(R.id.history_loc_lat);
            deletebt = itemView.findViewById(R.id.history_delete_bt);
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
