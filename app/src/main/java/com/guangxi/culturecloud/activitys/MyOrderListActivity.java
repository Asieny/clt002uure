package com.guangxi.culturecloud.activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.adapter.PagerAdapter;
import com.guangxi.culturecloud.fragment.MyOrderFrgment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *我的订单
 */
public class MyOrderListActivity extends BaseActivity {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tabs)
    TabLayout mTabLayout;
    @InjectView(R.id.view_pager)
    ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private String[] mTitles = {"待支付", "已支付", "待领票", "已领票","订单取消"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        ButterKnife.inject(this);
        initView();

    }

    private void initView() {
        mPagerAdapter = new PagerAdapter(MyOrderListActivity.this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addTabs();
    }

    private void addPages(String title) {
        Bundle Bundle = new Bundle();
        Bundle.putString("id", title);
        mPagerAdapter.addPage(MyOrderFrgment.class, Bundle);
    }

    private void addTabs() {
        int defaultID;

        for (int i = 0; i < mTitles.length; i++) {
            addTab(mTitles[i], i);
            addPages(mTitles[i]);

        }
        mViewPager.setCurrentItem(0);
    }

    private void addTab(String title, int position) {
        TabLayout.Tab tab = mTabLayout.newTab();
//        LayoutInflater inflater = LayoutInflater.from(getActivity());
//        TextView view = (TextView) inflater.inflate(R.layout.top_tab_layout, null);
//        view.setText(textID);
//
//        tab.setCustomView(view);
        tab.setText(title);

        mTabLayout.addTab(tab, position);
    }

}
