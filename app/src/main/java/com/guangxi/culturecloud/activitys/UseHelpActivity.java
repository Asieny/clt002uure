package com.guangxi.culturecloud.activitys;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.fragment.HelpItemFragment;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/9 15:28
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class UseHelpActivity extends BaseActivity {

    private ImageView mImg_back;
    private FrameLayout mFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_help);

        initView();
        initData();
    }
    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
    }
    private void initData() {

        //首先展示全部帮助条目
        HelpItemFragment helpFragment = new HelpItemFragment();
        FragmentTransaction mFt = getFragmentManager().beginTransaction();
        //进行fragment操作:
        mFt.add(R.id.frame, helpFragment,"helpFragment");
        //提交事务
        mFt.commit();
    }


}
