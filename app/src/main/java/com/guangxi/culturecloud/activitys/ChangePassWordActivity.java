package com.guangxi.culturecloud.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.fragment.SendVerificationFragment;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/1 16:37
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ChangePassWordActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        initViews();
        initData();
    }

    private void initViews() {

    }

    private void initData() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fmt = fm.beginTransaction();
        SendVerificationFragment sendVerificationFragment = new SendVerificationFragment();
        fmt.add(R.id.frame_change_pass,sendVerificationFragment,"sendVerificationFragment");
        fmt.commit();
    }
}
