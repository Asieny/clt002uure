package com.jarvis.mytaobao.discover;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.fragment.SpaceMuseumFragment;
import com.jarvis.MyView.MyFixedViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现主界面
 * 空间界面
 *
 * @author http://yecaoly.taobao.com
 */
public class Discover_F extends Fragment {
    //	private GridView my_gridView_life;
    //	private GridView my_gridView_app;
    //	private Adapter_GridView adapter_GridView_life;
    //	private Adapter_GridView adapter_GridView_app;
    //	// 资源文件
    //	private int[] pic_path_life = { R.drawable.find_g_1, R.drawable.find_g_2,
    //			R.drawable.find_g_3, R.drawable.find_g_4 };
    //	private String[] pic_path_life_name = { "", "", "", "", "" };
    //	private int[] pic_path_app = { R.drawable.find_g_5, R.drawable.find_g_6,
    //			R.drawable.find_g_7, R.drawable.find_g_8 };
    //	private LinearLayout ll_ShaoYiShao;
    //	private LinearLayout ll_game;

    private View      mRootView;
    private TabLayout mTablayout;
    private String[] mStrings = {"博物馆", "美术馆", "图书馆", "文化馆", "体育场馆", "民族文化", "影剧院"};
    private MyFixedViewpager mView_pager_space;
    private ImageView        mImg_no_intnet;
    private RelativeLayout   mTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dicover_f, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mTitle = (RelativeLayout) mRootView.findViewById(R.id.title);
        mTablayout = (TabLayout) mRootView.findViewById(R.id.tablayout);
        mImg_no_intnet = (ImageView) mRootView.findViewById(R.id.img_no_intnet);
        mView_pager_space = (MyFixedViewpager) mRootView.findViewById(R.id.view_pager_space);
    }

    private void initData() {
        // 创建一个集合,装填Fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        // 装填
        //博物馆
        SpaceMuseumFragment museumFragment = new SpaceMuseumFragment();
        museumFragment.setBusiness_kind("03");
        museumFragment.getImg_no_intnet(mImg_no_intnet);
        museumFragment.getTitleView(mTitle);
        fragments.add(museumFragment);
        //美术馆
        SpaceMuseumFragment artsFragment = new SpaceMuseumFragment();
        artsFragment.setBusiness_kind("02");
        artsFragment.getImg_no_intnet(mImg_no_intnet);
        artsFragment.getTitleView(mTitle);
        fragments.add(artsFragment);
        //图书馆
        SpaceMuseumFragment libraryFragment = new SpaceMuseumFragment();
        libraryFragment.setBusiness_kind("01");
        libraryFragment.getImg_no_intnet(mImg_no_intnet);
        libraryFragment.getTitleView(mTitle);
        fragments.add(libraryFragment);
        //文化馆
        SpaceMuseumFragment culturalFragment = new SpaceMuseumFragment();
        culturalFragment.setBusiness_kind("07");
        culturalFragment.getImg_no_intnet(mImg_no_intnet);
        culturalFragment.getTitleView(mTitle);
        fragments.add(culturalFragment);
        //体育馆
        SpaceMuseumFragment gymnasiumFragment = new SpaceMuseumFragment();
        gymnasiumFragment.setBusiness_kind("04");
        gymnasiumFragment.getImg_no_intnet(mImg_no_intnet);
        gymnasiumFragment.getTitleView(mTitle);
        fragments.add(gymnasiumFragment);
        //民族文化
        SpaceMuseumFragment nationalFragment = new SpaceMuseumFragment();
        nationalFragment.setBusiness_kind("05");
        nationalFragment.getImg_no_intnet(mImg_no_intnet);
        nationalFragment.getTitleView(mTitle);
        fragments.add(nationalFragment);
        //影剧院
        SpaceMuseumFragment filmFragment = new SpaceMuseumFragment();
        filmFragment.setBusiness_kind("06");
        filmFragment.getImg_no_intnet(mImg_no_intnet);
        filmFragment.getTitleView(mTitle);
        fragments.add(filmFragment);
        //        fragments.add(new CulturalAttractionsFragment());//文化景点
        //        fragments.add(new SpaceIntangibleheritageFragment());//非遗
        //        fragments.add(new SpaceIntangibleheritageFragment());//微社区
        //        fragments.add(new SpaceIntangibleheritageFragment());//文化地铁
        // 创建ViewPager适配器
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        myPagerAdapter.setFragments(fragments);
        // 给ViewPager设置适配器
        mView_pager_space.setAdapter(myPagerAdapter);
        //设置viewpager不可滑动
        //		mView_pager_space.setCanScroll(false);

        //        mTablayout.addTab(mTablayout.newTab());
        //        mTablayout.addTab(mTablayout.newTab());
        //        mTablayout.addTab(mTablayout.newTab());
        //        mTablayout.addTab(mTablayout.newTab());
        //关联tablayout和viewpager实现联动
        mTablayout.setupWithViewPager(mView_pager_space);

        for (int i = 0; i < mStrings.length; i++) {
            mTablayout.getTabAt(i).setText(mStrings[i]);
        }
    }

    public void reCreatView() {
        initData();
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList;

        public void setFragments(ArrayList<Fragment> fragments) {
            mFragmentList = fragments;
        }

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
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
    //	private void initView(View view) {
    //		ll_ShaoYiShao = (LinearLayout) view.findViewById(R.id.ll_dicover_shao);
    //		ll_ShaoYiShao.setOnClickListener(new OnClickListener() {
    //			public void onClick(View arg0) {
    //				Intent intent = new Intent(getActivity(), CaptureActivity.class);
    //				startActivity(intent);
    //			}
    //		});
    //		ll_game = (LinearLayout) view.findViewById(R.id.ll_dicover_game);
    //		ll_game.setOnClickListener(new OnClickListener() {
    //			public void onClick(View arg0) {
    //				Toast.makeText(getActivity(), "此功能暂未开放", Toast.LENGTH_SHORT)
    //						.show();
    //			}
    //		});
    //		my_gridView_life = (GridView) view
    //				.findViewById(R.id.gridView_find_life);
    //		my_gridView_life.setSelector(new ColorDrawable(Color.TRANSPARENT));
    //		adapter_GridView_life = new Adapter_GridView(getActivity(),
    //				pic_path_life, pic_path_life_name);
    //		my_gridView_life.setAdapter(adapter_GridView_life);
    //		my_gridView_life.setOnItemClickListener(new OnItemClickListener() {
    //			@Override
    //			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    //					long arg3) {
    //				Intent intent = new Intent(getActivity(), WareActivity.class);
    //				startActivity(intent);
    //			}
    //		});
    //
    //		my_gridView_app = (GridView) view.findViewById(R.id.gridView_find_app);
    //		my_gridView_app.setSelector(new ColorDrawable(Color.TRANSPARENT));
    //		adapter_GridView_app = new Adapter_GridView(getActivity(),
    //				pic_path_app, pic_path_life_name);
    //		my_gridView_app.setAdapter(adapter_GridView_app);
    //		my_gridView_app.setOnItemClickListener(new OnItemClickListener() {
    //			@Override
    //			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    //					long arg3) {
    //				Intent intent = new Intent(getActivity(), WareActivity.class);
    //				startActivity(intent);
    //			}
    //		});
    //	}

}
