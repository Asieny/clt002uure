package com.guangxi.culturecloud.activitys;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.fragment.RecruitVolunteerFragment;

import java.util.ArrayList;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/27 10:32
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CultureVolunteerActivity extends BaseActivity {

    public static ArrayList<String> IDList   = new ArrayList();
    public static ArrayList<String> NameList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_of_culture);
        initView();
        initData();
    }

    private void initView() {

        //首先展示总体招募信息
        RecruitVolunteerFragment recruitVolunteerFragment = new RecruitVolunteerFragment();
        FragmentTransaction mFt = getFragmentManager().beginTransaction();
        //进行fragment操作:
        mFt.add(R.id.frame_recruitment, recruitVolunteerFragment, "recruitVolunteerFragment");
        //提交事务
        mFt.commit();
    }

    private void initData() {

    }
}
