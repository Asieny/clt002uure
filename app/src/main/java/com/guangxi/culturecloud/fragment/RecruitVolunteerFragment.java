package com.guangxi.culturecloud.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.CultureVolunteerActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.RecruitVolunteerInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/27 11:23
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class RecruitVolunteerFragment extends Fragment {

    private View                       mRootView;
    private ListView                   mRecruitment;//招聘club条目
    private View                       mIv_back; //后退图标
    private ImageView                  mToBeVolunteer; //成为志愿者
    private TextView                   mTitel;
    private ProgressDialog             progressDialog;
    private List<RecruitVolunteerInfo> mList; //存储社团招聘信息
    private String RecruitVolunteerUrl = NetConfig.VOLUNTEER_ACTIVITYLIST;
    private String mURLHeadString      = NetConfig.IMG;
    private String             ItemID;
    private ImageView          mImg_share;
    private SmartRefreshLayout mSwipe_refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recruit_tatol, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mSwipe_refresh = (SmartRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        mRecruitment = (ListView) mRootView.findViewById(R.id.lv_recruitment);
        mImg_share = (ImageView) mRootView.findViewById(R.id.img_share);
        mIv_back = mRootView.findViewById(R.id.iv_back);
        mToBeVolunteer = (ImageView) mRootView.findViewById(R.id.img_toBe_Volunteer);
        mTitel = (TextView) mRootView.findViewById(R.id.login);
    }

    private void initData() {
        mTitel.setText("文化志愿者");

        //获取网络数据展示
        getIntnetData();

        //点击图片跳转申请志愿者界面
        mToBeVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //替换fragment//展示申请界面
                SignUpVolunteerFragment signUpVolunteerFragment = new SignUpVolunteerFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction mFt = fragmentManager.beginTransaction();
                //进行fragment操作:
                mFt.add(R.id.frame_recruitment, signUpVolunteerFragment, "signUpVolunteerFragment");
                mFt.addToBackStack(null);
                //提交事务
                mFt.commit();
            }
        });
        mSwipe_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //获取网络数据展示
                getIntnetData();
            }
        });
    }

    private void getIntnetData() {
        HttpUtil.get(RecruitVolunteerUrl, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(getActivity(), "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
                mSwipe_refresh.finishRefresh();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                if (null == mList) {
                    mList = new ArrayList<>();
                } else {
                    mList.clear();
                }
                Gson gson = new Gson();
                RecruitVolunteerInfo recruitVolunteerInfo;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("arr");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject recruitVolunteerInfoData = jsonArray.getJSONObject(i);
                        recruitVolunteerInfo = gson.fromJson(recruitVolunteerInfoData.toString(), RecruitVolunteerInfo.class);
                        //设置完数据，放入list集合
                        mList.add(recruitVolunteerInfo);
                        CultureVolunteerActivity.IDList.add(recruitVolunteerInfo.getId());
                        CultureVolunteerActivity.NameList.add(recruitVolunteerInfo.getFname());
                        //请求完设置数据
                        setIntnetData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setIntnetData() {
        mImg_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        MyAdapter myAdapter = new MyAdapter();
        mRecruitment.setAdapter(myAdapter);
        //点击club招聘信息，查看招聘详情
        mRecruitment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //记录点击的item的id
                RecruitVolunteerInfo recruitVolunteerInfo = mList.get(position);

                //替换fragment//展示单个club招聘详情
                RecruitDetailFragment reruitDetailFragment = new RecruitDetailFragment();
                reruitDetailFragment.setActID(recruitVolunteerInfo.getId());
                FragmentTransaction mFt = getFragmentManager().beginTransaction();
                mFt.add(R.id.frame_recruitment, reruitDetailFragment, "reruitDetailFragment");
                mFt.addToBackStack("reruitDetailFragment");
                //提交事务
                mFt.commit();
            }
        });

    }

    class MyAdapter extends BaseAdapter {
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
                convertView = View.inflate(getActivity(), R.layout.list_item_recruitment, null);
                viewHolder = new MyViewHolder();
                viewHolder.img_club = (ImageView) convertView.findViewById(R.id.img_club);
                viewHolder.tv_recruitmentTitel = (TextView) convertView.findViewById(R.id.tv_recruitmentTitel);
                viewHolder.tv_limit_age = (TextView) convertView.findViewById(R.id.tv_limit_age);
                viewHolder.tv_limit_education = (TextView) convertView.findViewById(R.id.tv_limit_education);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            //TODO:等待网络信息展示（需判断招聘要求有几个（最多两个），可只显示一个）
            RecruitVolunteerInfo recruitVolunteerInfo = mList.get(position);
            String fname = recruitVolunteerInfo.getFname();
            String age_label = recruitVolunteerInfo.getAge_label();
            String xueli_label = recruitVolunteerInfo.getXueli_label();
            String newpic = mURLHeadString + recruitVolunteerInfo.getNewpic();

            //获得跳转页面请求的id
            String clubId = recruitVolunteerInfo.getId();
            viewHolder.tv_recruitmentTitel.setText(fname);
            if (age_label == null) {
                viewHolder.tv_limit_age.setVisibility(View.GONE);
            } else if (age_label.trim().equals("")) {
                viewHolder.tv_limit_age.setVisibility(View.GONE);
            } else {
                viewHolder.tv_limit_age.setText(age_label);
            }
            if (xueli_label == null) {
                viewHolder.tv_limit_education.setVisibility(View.GONE);
            } else if (xueli_label.trim().equals("")) {
                viewHolder.tv_limit_education.setVisibility(View.GONE);
            } else {
                viewHolder.tv_limit_education.setText(xueli_label);
            }
            ImageLoaderUtil.displayImageIcon(newpic, viewHolder.img_club);

            return convertView;
        }
    }

    class MyViewHolder {
        ImageView img_club;//社团招聘底图
        TextView  tv_recruitmentTitel; //招聘titel
        TextView  tv_limit_age; //招聘要求年龄
        TextView  tv_limit_education; //招聘要求学历
    }

    @Override
    public void onResume() {
        super.onResume();
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
}
