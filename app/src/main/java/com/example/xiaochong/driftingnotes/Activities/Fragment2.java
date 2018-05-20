package com.example.xiaochong.driftingnotes.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.xiaochong.driftingnotes.Adapter.AroundAdapter;
import com.example.xiaochong.driftingnotes.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Fragment2 extends Fragment {


    @Bind(R.id.rcv2)
    RecyclerView rcv2;

    private static final String TAG = "Fragment2";
    @Bind(R.id.et_search2)
    EditText etSearch2;
    @Bind(R.id.search2)
    ImageView search2;

    private List<String> datas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        initView();
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        datas = new ArrayList<>();
//        ArrayList<PoiItem> pois = poiResult.getPois();
//        for (int i = 0; i < pois.size(); i++) {
//            LocationBean locationBean = new LocationBean(pois.get(i).getTitle(),pois.get(i).getSnippet());
//            datas.add(locationBean);
//        }
//        Log.d(TAG, "onPoiSearched: "+datas.size()+" errcode: "+errcode);


        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv2.setLayoutManager(llm);
        AroundAdapter aroundAdapter = new AroundAdapter(datas);
        rcv2.setAdapter(aroundAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.search2)
    public void onViewClicked() {

    }

}
