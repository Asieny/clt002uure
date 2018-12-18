package com.guangxi.culturecloud.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.adapter.BigEventAdapter;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.BigEventInfo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.anno.ViewId;
import cn.com.mytest.anno.ViewLayoutId;
import cz.msebera.android.httpclient.Header;

/**
 * 大活动
 * Created by wangcw on 2018/1/11.
 */
@ViewLayoutId(R.layout.activity_active)
public class BigActiveActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @ViewId(R.id.lv_big)
    private ListView           listView;
    @ViewId(R.id.swipe_refresh)
    private SmartRefreshLayout swipe_refresh;
    @ViewId(R.id.iv_back)
    private ImageView          iv_back;
    @ViewId(R.id.login)
    private TextView           login;
    private BigEventAdapter    adapter;
    private String actUrl = "";
    private int                modelId;
    private String             title;
    private List<BigEventInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String bigUrl = intent.getStringExtra("bigUrl");
        int mModelId = intent.getIntExtra("mModelId", 0);
        String actName = intent.getStringExtra("actName");
        actUrl = bigUrl;
        modelId = mModelId;
        title = actName;
        initData();
        initView();
    }

    private void initView() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        login.setText(title);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        swipe_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    private void initData() {
        if (null == data) {
            data = new ArrayList<>();
        } else {
            data.clear();
        }
        adapter = new BigEventAdapter(this);
        HttpUtil.get(actUrl, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipe_refresh.finishRefresh();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                JSONArray array = jsonObject.optJSONArray("arr");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.optJSONObject(i);
                        BigEventInfo firstInfo = new BigEventInfo().fromJson(obj.toString());
                        data.add(firstInfo);
                    }
                    adapter.setDataSource(data);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        String type = data.get(position).tb_type;
        Intent intent = new Intent(this, ShowsDetailActivityNew.class);
        intent.putExtra("modelId", modelId);
        intent.putExtra("type", type);
        intent.putExtra("filmRoom", adapter.getItem(position).id);
        intent.putExtra("activity_name", adapter.getItem(position).tb_name);
        startActivity(intent);
    }
}
