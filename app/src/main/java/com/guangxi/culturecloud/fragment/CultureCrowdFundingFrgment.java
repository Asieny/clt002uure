package com.guangxi.culturecloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.CultureCrowdFundingDetialActivity;
import com.guangxi.culturecloud.activitys.LoginActivity;
import com.guangxi.culturecloud.adapter.CultureCrowdFundingAdapter;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.CultureCrowdFundingInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.views.XListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;


/**
 * 活动众筹
 */
public class CultureCrowdFundingFrgment extends Fragment implements XListView.IXListViewListener {
    private XListView xListView;

    private CultureCrowdFundingAdapter adapter;

    private CultureCrowdFundingInfo firstInfo;

    private ArrayList<CultureCrowdFundingInfo> data = new ArrayList<CultureCrowdFundingInfo>();

    private View view;
    private String state;

    private Handler mHandler;

    private int start = 1;

    private int pageSize = 10;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        state = getArguments().getString("id");
        view = inflater.inflate(R.layout.fragment_library, null);
        initView();
        getMenu();
        return view;
    }

    private void getMenu() {
        if (data.size() > 0) {
            data.clear();
        }
        getAllData();
    }

    private String url = "";

    private void getAllData() {
        if (state.equals("全部")) {
            url = NetConfig.CROWDFUNDINGLIST + "fstatus=";
        }
        if (state.equals("众筹中")) {
            url = NetConfig.CROWDFUNDINGLIST + "fstatus=0";
        }
        if (state.equals("将要结束")) {
            url = NetConfig.CROWDFUNDINGLIST + "fstatus=1";
        }
        if (state.equals("众筹成功")) {
            url = NetConfig.CROWDFUNDINGLIST + "fstatus=2";
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
                JSONArray array = jsonObject.optJSONArray("arr");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.optJSONObject(i);
                        firstInfo = new CultureCrowdFundingInfo().fromJson(obj.toString());
                        data.add(firstInfo);
                    }
                    adapter.setDataSource(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initView() {

        xListView = (XListView) view.findViewById(R.id.xListView);
        xListView.setPullLoadEnable(false);
        adapter = new CultureCrowdFundingAdapter(getActivity());
        xListView.setXListViewListener(this);
        mHandler = new Handler();
        xListView.setAdapter(adapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CultureCrowdFundingInfo info = (CultureCrowdFundingInfo) parent.getAdapter().getItem(position);
                if(null!= SPref.getObject(getActivity(), UserInfo.class, "userinfo")){
                    Intent intent = new Intent(getActivity(), CultureCrowdFundingDetialActivity.class);
                    intent.putExtra("id", info.id);
                    intent.putExtra("url_address",NetConfig.URL_HEAD_ADDRESS);
                    intent.putExtra("get_img_address",NetConfig.IMG);
                    if (!info.newpic.equals("")) {
                        String picUrl = info.newpic.split(",")[0].replace("\\", "/");
                        intent.putExtra("img_address",picUrl);
                    }
                    intent.putExtra("state",state);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = 1;
                data.clear();
                getAllData();
                xListView.setAdapter(adapter);
                onLoad();
            }
        }, 500);

    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start++;
//                getAllData(start);
                onLoad();
            }
        }, 500);
    }

    // 刷新上面时间设置
    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        xListView.setRefreshTime(df.format(new Date()));
    }
}
