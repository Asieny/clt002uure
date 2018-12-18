package com.jarvis.mytaobao.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.model.UserPointsInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/20 14:51
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class PersonalPointsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView    mIv_back;
    private TextView     mTv_howmuch;   //多少积分
    private LinearLayout mLinear_detail;//查看积分规则
    //    private LinearLayout mLinear_more;  //更多积分明细
    private ListView     mList_view;    //积分条目
    private String UserID = NetConfig.UserID;
    private List               mList;
    private SmartRefreshLayout mSwipe_refresh;
    private ImageView          mImg_no_intnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_points);
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mImg_no_intnet = (ImageView) findViewById(R.id.img_no_intnet);
        mSwipe_refresh = (SmartRefreshLayout) findViewById(R.id.swipe_refresh);
        mTv_howmuch = (TextView) findViewById(R.id.tv_howmuch);
        mLinear_detail = (LinearLayout) findViewById(R.id.linear_detail);
        //        mLinear_more = (LinearLayout) findViewById(R.id.linear_more);
        mList_view = (ListView) findViewById(R.id.list_view);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        mLinear_detail.setOnClickListener(this);
        //        mLinear_more.setOnClickListener(this);

        mList = new ArrayList();
        getOrSetUserID();
        //访问网络获取积分详细
        getIntnetData();
        mSwipe_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //访问网络获取积分详细
                getIntnetData();
            }
        });
    }

    //访问网络获取积分详细
    private void getIntnetData() {
        RequestParams params = new RequestParams();
        params.put("userid", UserID);
        HttpUtil.get(NetConfig.SERUSER_POINTS, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                mImg_no_intnet.setVisibility(View.VISIBLE);
                ProgressDialogUtil.startShow(PersonalPointsActivity.this, "正在加载");
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
                    ToastUtils.makeShortText("网络错误", PersonalPointsActivity.this);
                    return;
                }
                mImg_no_intnet.setVisibility(View.GONE);
                Gson gson = new Gson();
                UserPointsInfo mRuleInfo = gson.fromJson(response.toString(), UserPointsInfo.class);
                UserPointsInfo.ArrBean arr = mRuleInfo.getArr();
                String totoal_points = arr.getTotoal_points();
                mTv_howmuch.setText(totoal_points);
                List<UserPointsInfo.ArrBean.UserpointBean> userpoint = arr.getUserpoint();
                //把积分详细填入list
                mList.clear();
                for (int i = 0; i < userpoint.size(); i++) {
                    mList.add(userpoint.get(i));
                }
                //填写列表数据
                setDetailData();
            }
        });
    }

    private void setDetailData() {
        MyAdapter myAdapter = new MyAdapter(mList);
        mList_view.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.linear_detail://查看积分规则
                Intent intentRule = new Intent(PersonalPointsActivity.this, IntegralRuleActivity.class);
                startActivity(intentRule);
                break;
            case R.id.linear_more://查看更多积分明细
                //                Intent intent = new Intent(PersonalPointsActivity.this, AllPointsActivity.class);
                //                startActivity(intent);
                break;
        }
    }

    class MyAdapter extends BaseAdapter {
        List mList;

        public MyAdapter(List list) {
            this.mList = list;
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
                viewHolder = new MyViewHolder();
                convertView = View.inflate(PersonalPointsActivity.this, R.layout.list_item_point, null);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_plus_point = (TextView) convertView.findViewById(R.id.tv_plus_point);
                viewHolder.tv_explain = (TextView) convertView.findViewById(R.id.tv_explain);
                viewHolder.tv_complete_time = (TextView) convertView.findViewById(R.id.tv_complete_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            UserPointsInfo.ArrBean.UserpointBean mUserpointBean = (UserPointsInfo.ArrBean.UserpointBean) mList.get(position);
            String type = mUserpointBean.getType();
            String fdate = mUserpointBean.getFdate();
            if (type.equals("1")) {
                viewHolder.tv_title.setText("首次注册");
                viewHolder.tv_plus_point.setText("+1200");
                viewHolder.tv_explain.setText("首次注册奖励");
                viewHolder.tv_complete_time.setText(fdate);
            }
            if (type.equals("2")) {
                viewHolder.tv_title.setText("登陆");
                viewHolder.tv_plus_point.setText("+50");
                viewHolder.tv_explain.setText("每日登陆");
                viewHolder.tv_complete_time.setText(fdate);
            }
            if (type.equals("3")) {
                viewHolder.tv_title.setText("发言奖励");
                viewHolder.tv_plus_point.setText("+10");
                viewHolder.tv_explain.setText("成功评论一次");
                viewHolder.tv_complete_time.setText(fdate);
            }
            if (type.equals("4")) {
                viewHolder.tv_title.setText("转发一次");
                viewHolder.tv_plus_point.setText("+50");
                viewHolder.tv_explain.setText("转发奖励");
                viewHolder.tv_complete_time.setText(fdate);
            }
            if (type.equals("5")) {
                viewHolder.tv_title.setText("活跃奖励");
                viewHolder.tv_plus_point.setText("+200");
                viewHolder.tv_explain.setText("自然周登录访问三次及以上");
                viewHolder.tv_complete_time.setText(fdate);
            }
            return convertView;
        }
    }

    class MyViewHolder {
        TextView tv_title;
        TextView tv_plus_point;
        TextView tv_explain;
        TextView tv_complete_time;
    }

    //判断是游客id还是用户id
    public void getOrSetUserID() {
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
    }
}
