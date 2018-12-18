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

import com.guangxi.culturecloud.activitys.FilmRoomActivity;
import com.guangxi.culturecloud.activitys.ShowsDetailActivityNew;
import com.guangxi.culturecloud.adapter.LibraryAdapter;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.FilmShowInfo;
import com.guangxi.culturecloud.model.LibraryInfo;
import com.guangxi.culturecloud.views.XListView;
import com.guangxi.culturecloud.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;


/**
 * 图书馆
 */
public class LibraryFragment extends Fragment implements XListView.IXListViewListener {
    private XListView xListView;

    private LibraryAdapter adapter;

    private LibraryInfo firstInfo;

    private ArrayList<LibraryInfo> data = new ArrayList<LibraryInfo>();

    private View view;
    private String id;

    private Handler mHandler;

    private int start = 1;

    private int pageSize = 10;

    private int type;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        id = getArguments().getString("id");
        type = getArguments().getInt("type");
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
        if (type == 0) {
            url = NetConfig.LIBRARYACTIVITYLIST + "type=" + id;
        }
        if (type == 1) {
            url = NetConfig.ARTSACTIVITYLIST + "type=" + id;
        }
        if (type == 2) {
            url = NetConfig.MUSEUMACTIVITYLIST + "type=" + id;
        }
        if (type == 3) {
            url = NetConfig.SPORTLIST + "type=" + id;
        }
        if (type == 4) {
            url = NetConfig.NATIONLIST + "type=" + id;
        }
        if (type == 5) {
            url = NetConfig.CULTUREACTIVITYLIST + "type=" + id;
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
                        firstInfo = new LibraryInfo().fromJson(obj.toString());
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
        adapter = new LibraryAdapter(getActivity());
        xListView.setXListViewListener(this);
        mHandler = new Handler();
        xListView.setAdapter(adapter);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowsDetailActivityNew.class);
                intent.putExtra("filmRoom",adapter.getItem(position-1).id);
                intent.putExtra("modelId",type+1);
                FilmShowInfo filmShowInfo = new FilmShowInfo();
                filmShowInfo.setId(adapter.getItem(position-1).id);
                filmShowInfo.setNewpic(adapter.getItem(position-1).newpic);
                filmShowInfo.setFdate(adapter.getItem(position-1).fdate);
                filmShowInfo.setFaddress(adapter.getItem(position-1).faddress);
                filmShowInfo.setFname(adapter.getItem(position-1).fname);

                FilmRoomActivity.markShow = filmShowInfo;

                startActivity(intent);
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
