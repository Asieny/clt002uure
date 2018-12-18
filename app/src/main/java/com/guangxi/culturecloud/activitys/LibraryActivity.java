package com.guangxi.culturecloud.activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.adapter.PagerAdapter;
import com.guangxi.culturecloud.fragment.LibraryFragment;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zww on 2017/12/19.
 */

public class LibraryActivity extends BaseActivity {
    @InjectView(R.id.tv_title)
    TextView mTitle;

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.img_library)
    ImageView imgLibrary;
    @InjectView(R.id.tabs)
    TabLayout mTabLayout;
    @InjectView(R.id.view_pager)
    ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    //    private String[] mTitles = {"tab0", "tab1", "tab2", "tab3", "tab4"};
    private ArrayList<String> mTitles = new ArrayList<String>();
    private ArrayList<String> mTypecode = new ArrayList<String>();
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.inject(this);
        type = getIntent().getIntExtra("type", 0);
        initView();
    }

    private void initView() {
        libraryTypeList();
        mPagerAdapter = new PagerAdapter(LibraryActivity.this);
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
    }

    private void addPages(String title) {
        Bundle Bundle = new Bundle();
        Bundle.putString("id", title);
        Bundle.putInt("type", type); //0 图书馆  1美术馆 2博物馆 3体育馆 4民族文化 5文化艺术馆
        mPagerAdapter.addPage(LibraryFragment.class, Bundle);
    }

    private void addTabs() {
        int defaultID;

        for (int i = 0; i < mTitles.size(); i++) {
            addTab(mTitles.get(i), i);
            addPages(mTitles.get(i));
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

    private String url = "";

    private void libraryTypeList() {
        if (type == 0) {
            url = NetConfig.LIBRARYTYPELIST;
            mTitle.setText("图书馆");
            imgLibrary.setImageResource(R.drawable.book);
        }
        if (type == 1) {
            url = NetConfig.ARTSTYPELIST;
            mTitle.setText("美术馆");
            imgLibrary.setImageResource(R.drawable.book_1);
        }
        if (type == 2) {
            url = NetConfig.MUSEUMTYPELIST;
            mTitle.setText("博物馆");
            imgLibrary.setImageResource(R.drawable.book_2);
        }
        if (type == 3) {
            url = NetConfig.SPORT_TYPE_LIST;
            mTitle.setText("体育场馆");
            imgLibrary.setImageResource(R.drawable.book_3);
        }
        if (type == 4) {
            url = NetConfig.NATION_TYPE_LIST;
            mTitle.setText("民族文化");
            imgLibrary.setImageResource(R.drawable.book_4);
        }
        if (type == 5) {
            url = NetConfig.CULTURETYPELIST;
            mTitle.setText("文化艺术馆");
            imgLibrary.setImageResource(R.drawable.book_5);
        }

        HttpUtil.get(url, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                JSONArray arry = jsonObject.optJSONArray("arr");
                for (int i = 0; i < arry.length(); i++) {
                    JSONObject typeObj = arry.optJSONObject(i);
                    String typename = typeObj.optString("typename");
                    mTypecode.add(typeObj.optString("typecode"));
                    mTitles.add(typename);
                }
                addTabs();
            }
        });
    }

}
