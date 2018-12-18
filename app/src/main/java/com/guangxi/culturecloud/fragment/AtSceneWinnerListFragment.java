package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guangxi.culturecloud.activitys.AtSceneActivity;
import com.guangxi.culturecloud.R;
import com.javis.ab.view.AbOnItemClickListener;
import com.javis.ab.view.AbSlidingPlayView;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/17 15:22
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class AtSceneWinnerListFragment extends Fragment implements View.OnClickListener {
    private View              mRootView;
    private AbSlidingPlayView mViewPager;
    private ArrayList<View>   allListView;
    private int[] resId = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner1, R.drawable.banner2};
    private ListView   mListview_winner;
    private ScrollView mScrollView;
    private TextView   mTv_online;
    private TextView   mTv_winner_list;
    private ArrayList  mAdapterArrayList;
    private ArrayList  mArrayListOnline;//在线福利的数据
    private ArrayList  mArrayListWinner;//中奖名单的数据
    private MyAdapter  mMyAdapter;
    private AtSceneRuleFragment atSceneWebFragment;
    private String markFromInfo = "";
    private Button mBt_join_red;
    private LinearLayout mLinear_ding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_at_scene_winner_list, null);
        initView();
        initData();
        return mRootView;

    }

    private void initView() {
        mViewPager = (AbSlidingPlayView) mRootView.findViewById(R.id.viewPager_menu);
        mScrollView = (ScrollView) mRootView.findViewById(R.id.scrollView);
        mTv_online = (TextView) mRootView.findViewById(R.id.tv_online);
        mTv_winner_list = (TextView) mRootView.findViewById(R.id.tv_winner_list);
        mListview_winner = (ListView) mRootView.findViewById(R.id.listview_winner);
        mLinear_ding = (LinearLayout) mRootView.findViewById(R.id.linear_ding);
        mAdapterArrayList =new ArrayList();

        mArrayListOnline = new ArrayList();
        mArrayListOnline.add("你好");
        mArrayListOnline.add("你好");
        mArrayListOnline.add("你好");
        mArrayListOnline.add("你好");

        mArrayListWinner=new ArrayList();
        mArrayListWinner.add("我好");
        mArrayListWinner.add("我好");
        mArrayListWinner.add("我好");
        mArrayListWinner.add("我好");
        mArrayListWinner.add("我好");
        mArrayListWinner.add("我好");

        for (int i=0;i<mArrayListOnline.size();i++){
            mAdapterArrayList.add(mArrayListOnline.get(i));
        }
    }

    private void initData() {
        mScrollView.smoothScrollTo(0, 0);
        mTv_online.setOnClickListener(this);
        mTv_winner_list.setOnClickListener(this);
        mLinear_ding.setOnClickListener(this);
        //设置播放方式为顺序播放
        mViewPager.setPlayType(1);
        //设置播放间隔时间
        mViewPager.setSleepTime(3000);
        initViewPager();

        mMyAdapter = new MyAdapter(mAdapterArrayList);
        mListview_winner.setAdapter(mMyAdapter);
        mListview_winner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapterArrayList.get(position);
                FragmentTransaction mFt = getFragmentManager().beginTransaction();
                //展示web详情榜界面
                if (atSceneWebFragment == null) {
                    atSceneWebFragment = new AtSceneRuleFragment();
                }
                //进行fragment操作:
                mFt.add(R.id.frame_initial, atSceneWebFragment, "atSceneWebFragment");
                mFt.addToBackStack(null);
                //提交事务
                mFt.commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        mAdapterArrayList.clear();
        switch (v.getId()){
            case R.id.tv_online:
                //改变字体颜色
                changeTextColor(mTv_online);
                //TODO:获取网络数据并展示
                for (int i=0;i<mArrayListOnline.size();i++){
                    mAdapterArrayList.add(mArrayListOnline.get(i));
                }
                mMyAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_winner_list:
                //TODO:获取网络数据并展示
                changeTextColor(mTv_winner_list);
                for (int i=0;i<mArrayListWinner.size();i++){
                    mAdapterArrayList.add(mArrayListWinner.get(i));
                }
                mMyAdapter.notifyDataSetChanged();
                break;
            case R.id.linear_ding:
                break;
            default:
                break;
        }
    }

    private void initViewPager() {
        if (allListView != null) {
            allListView.clear();
            allListView = null;
        }
        allListView = new ArrayList<View>();

        for (int i = 0; i < resId.length; i++) {
            //导入ViewPager的布局
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pic_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
            imageView.setImageResource(resId[i]);
            allListView.add(view);
        }
        mViewPager.addViews(allListView);
        //开始轮播
        mViewPager.startPlay();
        mViewPager.setOnItemClickListener(new AbOnItemClickListener() {
            @Override
            public void onClick(int position) {
                //跳转到轮播图网页详情界面
                mAdapterArrayList.get(position);
                FragmentTransaction mFt = getFragmentManager().beginTransaction();
                //展示排行榜界面
                if (atSceneWebFragment == null) {
                    atSceneWebFragment = new AtSceneRuleFragment();
                }
                //进行fragment操作:
                mFt.add(R.id.frame_initial, atSceneWebFragment, "atSceneWebFragment");
                mFt.addToBackStack(null);
                //提交事务
                mFt.commit();
            }
        });
    }

    private void changeTextColor(TextView tv) {
        if (tv.getId() == R.id.tv_online){
            mTv_online.setTextColor(getResources().getColor(R.color.vm_green_87));
            mTv_winner_list.setTextColor(getResources().getColor(R.color.vm_black_87));
        }
        if (tv.getId() == R.id.tv_winner_list){
            mTv_online.setTextColor(getResources().getColor(R.color.vm_black_87));
            mTv_winner_list.setTextColor(getResources().getColor(R.color.vm_green_87));
        }
    }

    class MyAdapter extends BaseAdapter {
        private List mList;

        public MyAdapter(List arrayList) {
            this.mList = arrayList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.list_winner_data_item, null);
                viewHolder = new MyViewHolder();
                viewHolder.img_winner = (ImageView) convertView.findViewById(R.id.img_winner);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_second_title = (TextView) convertView.findViewById(R.id.tv_second_title);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }

            viewHolder.tv_second_title.setText("未实名认证");
            return convertView;
        }
    }

    class MyViewHolder {
        ImageView img_winner;//图片
        TextView  tv_title;   //主标题
        TextView  tv_second_title;    //副标题
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
            Button bt_to_top = (Button) getActivity().findViewById(R.id.bt_to_top);
            bt_to_top.setVisibility(View.VISIBLE);
            if (markFromInfo.equals("AtSceneHomeFragment")){
                if (!mBt_join_red.isShown()) {
                    mBt_join_red.setVisibility(View.VISIBLE);
                }
            }
            markFromInfo="";
        }
    }
}
