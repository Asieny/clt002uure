package com.jarvis.mytaobao.user;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;
import com.guangxi.culturecloud.fragment.CollectionActFragment;
import com.guangxi.culturecloud.fragment.CollectionMusFragment;
import com.jarvis.MyView.MyFixedViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/20 13:24
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class PersonalCommentActivity extends BaseActivity implements View.OnClickListener {
    private ImageView             mIv_back;
    private TabLayout             mTablayout;
    private CollectionActFragment mCollectionActFragment;
    private CollectionMusFragment mCollectionMusFragment;
    private MyFixedViewpager      mView_pager_space;
    private String                UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_comment);
        UserID=getIntent().getStringExtra("userid");
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mView_pager_space = (MyFixedViewpager) findViewById(R.id.view_pager_space);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        // 创建一个集合,装填Fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        mCollectionActFragment = new CollectionActFragment();
        mCollectionActFragment.setKind("Comment");
        mCollectionActFragment.setUserID(UserID);
        fragments.add(mCollectionActFragment);
        mCollectionMusFragment = new CollectionMusFragment();
        mCollectionMusFragment.setKind("Comment");
        mCollectionMusFragment.setUserID(UserID);
        fragments.add(mCollectionMusFragment);
        // 创建ViewPager适配器
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.setFragments(fragments);
        mView_pager_space.setAdapter(myPagerAdapter);
        mView_pager_space.setCanScroll(false);
        mTablayout.setupWithViewPager(mView_pager_space);
        mTablayout.getTabAt(0).setText("活动").setIcon(R.drawable.icon_data);
        mTablayout.getTabAt(1).setText("场馆").setIcon(R.drawable.icon_data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setFragments(ArrayList<Fragment> fragments) {
            mFragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = mFragmentList.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
