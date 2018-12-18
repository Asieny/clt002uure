package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangxi.culturecloud.activitys.ClubDetailsActivity;
import com.guangxi.culturecloud.activitys.PictureWallShowActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.R;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/26 13:44
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class MyPitureWallFragment extends Fragment {

    private View mRootView;
    private Button mBtMore;
    private RecyclerView mRecycler_pic;
    private ArrayList<String> mArrayList;
    private List mPicList;
    private String mURLHeadString = NetConfig.IMG;
    private ViewPager                  mViewPager;
    private LinearLayout               mLinearPictureDetail;
    private MyViewPagerAdapter         myViewPagerAdapter;
    private TextView                   mWhichOne;
    private TextView mTv_no_pic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.picture_wall_new,null);
        initView();
        return mRootView;
    }

    private void initView() {
        mArrayList = new ArrayList<>();

        Activity activity = getActivity();
        if (activity instanceof ClubDetailsActivity){
            mPicList = ((ClubDetailsActivity) activity).getPicList();
        }

        for (int i = 0;i<mPicList.size();i++){
            ClubDetailInfo.ArrBean.ListPicBean mListPicBean = (ClubDetailInfo.ArrBean.ListPicBean) mPicList.get(i);
            String newpicUrl = mURLHeadString+mListPicBean.getNewpic();
            mArrayList.add(newpicUrl);
        }
        mTv_no_pic = (TextView) mRootView.findViewById(R.id.tv_no_pic);
        if (mArrayList.size()==0){
            mTv_no_pic.setVisibility(View.VISIBLE);
        }
        mRecycler_pic = (RecyclerView) mRootView.findViewById(R.id.recycler_pic);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);

        mRecycler_pic.setLayoutManager(gridLayoutManager);
        MyAdapter myAdapter = new MyAdapter(mArrayList);
        mRecycler_pic.setAdapter(myAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<String> mData;

        public MyAdapter(ArrayList<String> data) {
            this.mData = data;
        }

        @Override
        public int getItemCount() {

            return mData.size()>=6?6:mData.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_recycler_newitem, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
//             绑定数据
            if (mData.size()>=1){
                ImageLoaderUtil.displayImageIcon(mArrayList.get(position),holder.mImg);
            }
            if (mData.size()>6){
                if (position == (mData.size()-1) || position == 6){
                    holder.bt_over.setVisibility(View.VISIBLE);
                    holder.bt_over.setText("+"+(mData.size()-6));
                    holder.bt_over.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PictureWallShowActivity.class);
                            startActivity(intent);
                        }
                    });
                    holder.mImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ClubDetailsActivity.mSavePicUrlList == null){
                                ClubDetailsActivity.mSavePicUrlList = mArrayList;
                            }
                            Intent intent = new Intent(getActivity(), PictureWallShowActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
            holder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClubDetailsActivity.mSavePicUrlList == null){
                        ClubDetailsActivity.mSavePicUrlList = mArrayList;
                    }
                    //显示前6个图片展示
                    mLinearPictureDetail = (LinearLayout)getActivity().findViewById(R.id.linear_picture_detail);
                    mViewPager = (ViewPager) getActivity().findViewById(R.id.view_pager_picture);
                    //点击小图片之后可见大图片墙，并给其设置数据
                    mLinearPictureDetail.setVisibility(View.VISIBLE);
                    myViewPagerAdapter = new MyViewPagerAdapter(mArrayList);
                    mViewPager.setAdapter(myViewPagerAdapter);
                    //定位到点击的图片
                    mViewPager.setCurrentItem(position);
                    mWhichOne = (TextView) mLinearPictureDetail.findViewById(R.id.tv_which);
                    //设置显示位置和图片上传时间
                    mWhichOne.setText((position + 1) + "/" + mData.size());
                    //设置viewpager点击消失
                    mLinearPictureDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLinearPictureDetail.setVisibility(View.INVISIBLE);
                        }
                    });
                    mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            mWhichOne.setText((position + 1) + "/" + mData.size());
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mImg;
            Button bt_over;
            public ViewHolder(View itemView) {
                super(itemView);
                mImg = (ImageView) itemView.findViewById(R.id.img_picture_show);
                bt_over = (Button) itemView.findViewById(R.id.bt_over);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClubDetailsActivity.mSavePicUrlList=null;
    }
    /**
     * 自定义类实现PagerAdapter，填充显示数据
     */
    class MyViewPagerAdapter extends PagerAdapter {
        private ArrayList mArrayList;

        public MyViewPagerAdapter(ArrayList arrayList) {
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
            View view = inflater.inflate(R.layout.img_and_date, null);
            view.setLayoutParams(lp);
            //设置viewpager中子view显示的数据
            TextView sendTime = (TextView) view.findViewById(R.id.tv_send_time);
            ImageView img_show = (ImageView) view.findViewById(R.id.img_show);
            ImageLoaderUtil.displayImageIcon((String) mArrayList.get(position), img_show);
            sendTime.setText("2017/08/08");

            //设置照片墙整个点击消失
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLinearPictureDetail.setVisibility(View.INVISIBLE);
                }
            });
            // 添加到ViewPager容器
            container.addView(view);
            // 返回填充的View对象
            return view;
        }

        // 销毁条目对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
