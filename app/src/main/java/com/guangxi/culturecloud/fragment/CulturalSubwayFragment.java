package com.guangxi.culturecloud.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guangxi.culturecloud.R;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/30 15:03
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CulturalSubwayFragment extends Fragment {
    private View           mRootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_space_museum, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {

    }

    private void initData() {

    }
}
