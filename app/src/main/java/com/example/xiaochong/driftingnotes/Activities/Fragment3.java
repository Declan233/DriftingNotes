package com.example.xiaochong.driftingnotes.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.xiaochong.driftingnotes.Adapter.KeyWordAdapter;
import com.example.xiaochong.driftingnotes.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Fragment3 extends Fragment {

    @Bind(R.id.user_profile)
    ImageButton userProfile;
    @Bind(R.id.filter_kyword)
    LinearLayout filterKyword;
    @Bind(R.id.view_mypost)
    LinearLayout viewMypost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.user_profile, R.id.filter_kyword, R.id.view_mypost})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_profile:
                break;
            case R.id.filter_kyword:
                startActivity(new Intent(this.getContext(), KeyWordActivity.class));
                break;
            case R.id.view_mypost:
                break;
        }
    }
}
