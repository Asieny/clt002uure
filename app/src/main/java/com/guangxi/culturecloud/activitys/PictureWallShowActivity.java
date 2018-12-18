package com.guangxi.culturecloud.activitys;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangxi.culturecloud.R;

import java.util.ArrayList;

import cn.com.mytest.util.ImageLoaderUtil;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/26 15:44
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class PictureWallShowActivity extends BaseActivity {

    private RecyclerView               mRecPhotoShow;
    //    LinearLayoutManager mLayoutManager;
    private StaggeredGridLayoutManager mLayoutManager;
    private ViewPager                  mViewPager;
    private LinearLayout               mLinearPictureDetail;
    private MyViewPagerAdapter         myViewPagerAdapter;
    private TextView                   mWhichOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_show);
        initView();
        initData();
    }

    private void initView() {
        mRecPhotoShow = (RecyclerView) findViewById(R.id.rec_photo_show);
        mLinearPictureDetail = (LinearLayout) findViewById(R.id.linear_picture_detail);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_picture);
    }

    private void initData() {
        //      mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //瀑布流显示
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        MyAdapter myAdapter = new MyAdapter(ClubDetailsActivity.mSavePicUrlList);
        // 设置布局管理器
        mRecPhotoShow.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecPhotoShow.setAdapter(myAdapter);
        // 设置Item添加和移除的动画
        //        mRecPhotoShow.setItemAnimator(new DefaultItemAnimator());
        // 设置Item之间间隔样式
        //      mRecPhotoShow.addItemDecoration(mDividerItemDecoration);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList mData;

        public MyAdapter() {
        }

        public MyAdapter(ArrayList<String> data) {
            this.mData = data;
        }

        public void updateData(ArrayList<String> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_recycler_item, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            String url = (String) mData.get(position);
            ImageLoaderUtil.displayImageIcon((String) mData.get(position), holder.mImg);

            holder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击小图片之后可见大图片墙，并给其设置数据
                    mLinearPictureDetail.setVisibility(View.VISIBLE);
                    myViewPagerAdapter = new MyViewPagerAdapter(mData);
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

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
            //            return 10;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mImg;

            public ViewHolder(View itemView) {
                super(itemView);
                mImg = (ImageView) itemView.findViewById(R.id.img_picture_show);
            }
        }
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
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClubDetailsActivity.mSavePicUrlList = null;
    }
}
