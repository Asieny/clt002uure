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

import com.guangxi.culturecloud.activitys.OrderDetailActivity;
import com.guangxi.culturecloud.adapter.MyOrderAdapter;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.MyOrderInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.views.XListView;
import com.guangxi.culturecloud.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;


/**
 * 订单列表
 */
public class MyOrderFrgment extends Fragment implements XListView.IXListViewListener {
    private XListView xListView;

    private MyOrderAdapter adapter;

    private ArrayList<MyOrderInfo> data = new ArrayList<MyOrderInfo>();

    private View    view;
    private String  id;
    private String  kind;
    private Handler mHandler;

    private int start = 1;

    private int pageSize = 10;
    private UserInfo userInfo;
    private static int myRequestCode = 10002;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        id = getArguments().getString("id");
        view = inflater.inflate(R.layout.fragment_library, null);
        userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
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
        if (id.equals("待支付")) {
            url = NetConfig.SEARCHORDERLIST + "member_id=" + userInfo.member_id + "&fstatus=0";
        }
        if (id.equals("已支付")) {
            url = NetConfig.SEARCHORDERLIST + "member_id=" + userInfo.member_id + "&fstatus=1";
        }
        if (id.equals("待领票")) {
            url = NetConfig.SEARCHORDERLIST + "member_id=" + userInfo.member_id + "&fstatus=2";
        }
        if (id.equals("已领票")) {
            url = NetConfig.SEARCHORDERLIST + "member_id=" + userInfo.member_id + "&fstatus=3";
        }
        if (id.equals("订单取消")) {
            url = NetConfig.SEARCHORDERLIST + "member_id=" + userInfo.member_id + "&fstatus=4";
        }
        kind = id;
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
                        MyOrderInfo firstInfo = new MyOrderInfo().fromJson(obj.toString());
                        data.add(firstInfo);
                    }
                    adapter.setDataSource(data);
                }
            }
        });
    }

    private void initView() {

        xListView = (XListView) view.findViewById(R.id.xListView);
        xListView.setPullLoadEnable(false);
        adapter = new MyOrderAdapter(getActivity());
        xListView.setXListViewListener(this);
        mHandler = new Handler();
        xListView.setAdapter(adapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ToastUtils.showLong(getActivity(), "点击了第" + position + "个");
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                OrderDetailActivity.myOrderInfo = data.get(position - 1);
                intent.putExtra("kind", kind);
                startActivityForResult(intent, myRequestCode);
                //                startActivity(intent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == myRequestCode) {
            adapter.notifyDataSetChanged();
        }
    }
}
