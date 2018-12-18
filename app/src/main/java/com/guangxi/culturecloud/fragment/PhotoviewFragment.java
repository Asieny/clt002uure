package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangxi.culturecloud.activitys.AtSceneActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.views.ViewPagerFixed;
import com.guangxi.culturecloud.R;

import java.util.ArrayList;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.zoom.PhotoView;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/18 10:57
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class PhotoviewFragment extends Fragment {
    private View              mRootView;
    private ViewPager         mView_pager;
    private TextView          mTv_which;
    private ArrayList<String> mImgUrlList; //图片地址信息
    private Button            mBt_join_red;
    private Button            mBt_to_top;
    private String markFromInfo   = "";
    private String mURLHeadString = "http://220.248.107.62:8084/upFiles/";
    private int currentPosition;//记录之前页面点击的是哪个图片

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_photoview_fragment, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mView_pager = (ViewPagerFixed) mRootView.findViewById(R.id.view_pager);
        mTv_which = (TextView) mRootView.findViewById(R.id.tv_which);
        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            mBt_join_red.setVisibility(View.GONE);
        }
    }

    public void setImgUrlData(ArrayList imgUrlList) {
        mImgUrlList = imgUrlList;
    }

    public void setViewPagerCurrentItem(int position) {
        currentPosition = position;
    }

    private void initData() {
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(mImgUrlList);
        mView_pager.setAdapter(myViewPagerAdapter);
        //定位到点击的图片
        mView_pager.setCurrentItem(currentPosition);
        mTv_which.setText((1 + currentPosition) + "/" + mImgUrlList.size());
        mView_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTv_which.setText((position + 1) + "/" + mImgUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 自定义类实现PagerAdapter，填充显示数据
     */
    class MyViewPagerAdapter extends PagerAdapter {
        private ArrayList<String> mArrayList;

        public MyViewPagerAdapter(ArrayList<String> arrayList) {
            this.mArrayList = arrayList;
        }

        // 显示多少个页面
        @Override
        public int getCount() {
            return mArrayList == null ? 0 : mArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化显示的条目对象
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.photoview_only, null);
            view.setLayoutParams(lp);
            //设置viewpager中子view显示的数据
            PhotoView img_show = (PhotoView) view.findViewById(R.id.photoview);
            ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + mArrayList.get(position), img_show);

            // 添加到ViewPager容器
            container.addView(view);
            // 返回填充的View对象
            return view;
        }

        // 销毁条目对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public void setMarkFromInfo(String markFromInfo) {
        this.markFromInfo = markFromInfo;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            mBt_to_top = (Button) getActivity().findViewById(R.id.bt_to_top);
            if (markFromInfo.equals("AtSceneHomeFragment")) {
                if (!mBt_join_red.isShown()) {
                    mBt_join_red.setVisibility(View.VISIBLE);
                }
            }
            mBt_to_top.setVisibility(View.VISIBLE);
            markFromInfo = "";
        }
    }
}
