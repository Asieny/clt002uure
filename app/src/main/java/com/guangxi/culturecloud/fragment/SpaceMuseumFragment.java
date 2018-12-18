package com.guangxi.culturecloud.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.VenueDetailsActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.MuseumDetailInfo;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.jarvis.MyView.MyListView;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.javis.mytools.DropBean;
import com.javis.mytools.DropdownButton;
import com.loopj.android.http.RequestParams;
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
 * @创建时间 2018/1/22 10:24
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class SpaceMuseumFragment extends Fragment {
    private View                   mRootView;
    private DropdownButton         dropdownButton1;
    private DropdownButton         dropdownButton2;
    private DropdownButton         dropdownButton3;
    private List<DropBean>         times;
    private List<DropBean>         types;
    private List<DropBean>         names;
    private MyListView             mListView;//博物馆展示的listview
    private ProgressDialog         progressDialog;
    private List<MuseumDetailInfo> mMuseumDetailInfoList;//记录博物馆条目
    private String business_kind  = "03";
    private String mURLHeadString = NetConfig.IMG;
    private ImageView          mImg_no_intnet;
    private RelativeLayout     mTitle;
    private SmartRefreshLayout mSwipe_refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_space_museum, null);
        initView();
        return mRootView;
    }

    private void initView() {
        dropdownButton1 = (DropdownButton) mRootView.findViewById(R.id.time1);
        dropdownButton2 = (DropdownButton) mRootView.findViewById(R.id.time2);
        dropdownButton3 = (DropdownButton) mRootView.findViewById(R.id.time3);
        mListView = (MyListView) mRootView.findViewById(R.id.listView_tao);
        mSwipe_refresh = (SmartRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        mMuseumDetailInfoList = new ArrayList();
        initSomeData();
        mSwipe_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getIntnetData();
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        downRowY = event.getRawY();
//                        ToastUtils.makeShortText("" + (downRowY - downY), getActivity());
                        if (downRowY - downY >=0) {
                            mTitle.setVisibility(View.VISIBLE);
                        }else {
                            mTitle.setVisibility(View.GONE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }

    float downY;//点击时，手指位置
    float downRowY;//移动时手指位置

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getIntnetData();
        }
    }

    private void getIntnetData() {
        String businessList = NetConfig.BUSINESS_LIST;
        RequestParams params = new RequestParams();
        params.put("business_kind", business_kind);
        HttpUtil.get(businessList, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                if (mImg_no_intnet != null) {
                    mImg_no_intnet.setVisibility(View.VISIBLE);
                }
                ProgressDialogUtil.startShow(getActivity(),"正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
                mSwipe_refresh.finishRefresh();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getActivity());
                    return;
                }
                if (mImg_no_intnet != null) {
                    mImg_no_intnet.setVisibility(View.GONE);
                }
                Gson gson = new Gson();
                MuseumDetailInfo museumInfo;
                try {
                    JSONArray jsonArray = response.getJSONArray("arr");
                    if (mMuseumDetailInfoList != null) {
                        mMuseumDetailInfoList.clear();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject museumInfoData = jsonArray.getJSONObject(i);
                        museumInfo = gson.fromJson(museumInfoData.toString(), MuseumDetailInfo.class);
                        //设置完数据，放入list集合
                        mMuseumDetailInfoList.add(museumInfo);
                        //设置数据
                        setDataAndListener();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setDataAndListener() {
        MyListAdapter myListAdapter = new MyListAdapter(mMuseumDetailInfoList);
        mListView.setAdapter(myListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转馆类详情
                Intent intent = new Intent(getActivity(), VenueDetailsActivity.class);
                MuseumDetailInfo museumDetailInfo = mMuseumDetailInfoList.get(position);
                String businessID = museumDetailInfo.getId();
                String tb_name = museumDetailInfo.getTb_name();
                //传递访问参数
                intent.putExtra("businessID", businessID);
                intent.putExtra("tb_name", tb_name);
                startActivity(intent);
            }
        });
    }

    private void initSomeData() {
        times = new ArrayList<DropBean>();
        types = new ArrayList<DropBean>();
        names = new ArrayList<DropBean>();
        times.add(new DropBean("全部商圈"));
        times.add(new DropBean("商圈A"));
        times.add(new DropBean("商圈B"));

        types.add(new DropBean("智能排序"));
        types.add(new DropBean("顺序"));
        types.add(new DropBean("降序"));

        names.add(new DropBean("筛选"));
        names.add(new DropBean("可预订"));
        names.add(new DropBean("全部"));
        dropdownButton1.setData(times);
        dropdownButton2.setData(types);
        dropdownButton3.setData(names);
        dropdownButton1.setOnDropItemSelectListener(new DropdownButton.OnDropItemSelectListener() {
            @Override
            public void onDropItemSelect(int Postion) {
                ToastUtils.makeShortText("全部商圈", getActivity());
            }
        });
    }

    class MyListAdapter extends BaseAdapter {
        List mList;

        public MyListAdapter(List mList) {
            this.mList = mList;
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
                convertView = View.inflate(getActivity(), R.layout.list_item_space_museum, null);
                viewHolder = new MyViewHolder();
                viewHolder.img_museum_show = (ImageView) convertView.findViewById(R.id.img_museum_show);
                viewHolder.tv_museum_title = (TextView) convertView.findViewById(R.id.tv_museum_title);
                viewHolder.tv_museum_place = (TextView) convertView.findViewById(R.id.tv_museum_place);
                viewHolder.tv_museum_act_num = (TextView) convertView.findViewById(R.id.tv_museum_act_num);
                viewHolder.tv_actroom_num = (TextView) convertView.findViewById(R.id.tv_actroom_num);
                viewHolder.linear_num = (LinearLayout) convertView.findViewById(R.id.linear_num);
                viewHolder.linear_actroom = (LinearLayout) convertView.findViewById(R.id.linear_actroom);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            //TODO:网络数据填充
            MuseumDetailInfo museumDetailInfo = (com.guangxi.culturecloud.model.MuseumDetailInfo) mList.get(position);
            String newpic = museumDetailInfo.getNewpic();
            ImageLoaderUtil.displayImageIcon(mURLHeadString + newpic, viewHolder.img_museum_show);
            viewHolder.tv_museum_title.setText(museumDetailInfo.getBusiness_name());
            viewHolder.tv_museum_place.setText(museumDetailInfo.getBusiness_address());
            int activitynum = museumDetailInfo.getActivitynum();
            if (activitynum == 0) {
                viewHolder.linear_num.setVisibility(View.GONE);
            } else {
                viewHolder.linear_num.setVisibility(View.VISIBLE);
                viewHolder.tv_museum_act_num.setText("" + activitynum);
            }
            int playroomnum = museumDetailInfo.getPlayroomnum();
            if (playroomnum == 0) {
                viewHolder.linear_actroom.setVisibility(View.GONE);
            } else {
                viewHolder.linear_actroom.setVisibility(View.VISIBLE);
                viewHolder.tv_actroom_num.setText("" + playroomnum);
            }
            return convertView;
        }
    }

    public void setBusiness_kind(String kind) {
        business_kind = kind;
    }

    class MyViewHolder {
        ImageView    img_museum_show;//博物馆展示照
        TextView     tv_museum_title;//博物馆名称
        TextView     tv_museum_place;//博物馆地址
        TextView     tv_museum_act_num;//博物馆活动数
        TextView     tv_actroom_num;//博物馆活动室数
        LinearLayout linear_num;//是否有活动条目
        LinearLayout linear_actroom;//是否有活动室条目
    }

    public void getImg_no_intnet(ImageView mImg_no_intnet) {
        this.mImg_no_intnet = mImg_no_intnet;
        mImg_no_intnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getTitleView(RelativeLayout title) {
        this.mTitle = title;
    }
}
