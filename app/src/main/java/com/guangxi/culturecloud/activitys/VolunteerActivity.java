package com.guangxi.culturecloud.activitys;

import android.os.Bundle;

import com.guangxi.culturecloud.R;

import cn.com.mytest.anno.ViewLayoutId;
import cn.com.mytest.framework.AbsActivity;

/**
 * 文化志愿者
 * Created by wangcw on 2017/12/21.
 */

@ViewLayoutId(R.layout.activity_volunteer)
public class VolunteerActivity extends AbsActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

    }
}
