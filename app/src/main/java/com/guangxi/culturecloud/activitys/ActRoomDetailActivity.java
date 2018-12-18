package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ActRoomInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/26 14:34
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ActRoomDetailActivity extends BaseActivity {
    private ImageView    mIv_back;
    private TextView     mAct_room_name;        //活动室名称
    private ImageView    mImg_act_room_picture; //活动室展示图
    private TextView     mTv_label_1;           //标签1
    private TextView     mTv_label_2;           //标签2
    private TextView     mTv_phone;             //电话
    private TextView     mTv_area;              //面积
    private TextView     mTv_member;            //容纳人数
    private TextView     mTv_remarks;           //备注
    private RecyclerView mRecyclerView_table;   //预订表
    private Button       mBt_submit;            //预约
    private int mSelectedColor = 0;//记录点击时的条目
    private MyTitleRecAdapter mTitleRecAdapter;
    private String UserID = NetConfig.UserID;
    private ScrollView     mScrollView_act_detail;
    private String         mActRoomID;//活动室ID
    private String         url_address;//活动室连接地址
    private String         get_img_address;//活动室图片链接地址
    private ProgressDialog progressDialog;
    private int mRequestCode = 10086;
    private TextView  mTv_none;
    private ImageView mImg_no_intnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_room_detail);
        Intent intent = getIntent();
        mActRoomID = intent.getStringExtra("actRoomID");
        url_address = intent.getStringExtra("url_address");
        get_img_address = intent.getStringExtra("get_img_address");
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mImg_no_intnet = (ImageView) findViewById(R.id.img_no_intnet);
        mScrollView_act_detail = (ScrollView) findViewById(R.id.scrollView_act_detail);
        mAct_room_name = (TextView) findViewById(R.id.act_room_name);
        mImg_act_room_picture = (ImageView) findViewById(R.id.img_act_room_picture);
        mTv_label_1 = (TextView) findViewById(R.id.tv_label_1);
        mTv_label_2 = (TextView) findViewById(R.id.tv_label_2);
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
        mTv_area = (TextView) findViewById(R.id.tv_area);
        mTv_member = (TextView) findViewById(R.id.tv_member);
        mTv_remarks = (TextView) findViewById(R.id.tv_remarks);
        mTv_none = (TextView) findViewById(R.id.tv_none);
        mRecyclerView_table = (RecyclerView) findViewById(R.id.recyclerView_table);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void initData() {
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取网络数据
        getIntnetData();
    }

    private void getIntnetData() {
        getOrSetUserID();
        RequestParams params = new RequestParams();
        params.put("id", mActRoomID);
        params.put("user_id", UserID);
        HttpUtil.get(url_address + "businessplayroom", params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ActRoomDetailActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getBaseContext());
                    return;
                }
                mImg_no_intnet.setVisibility(View.GONE);
                Gson gson = new Gson();
                ActRoomInfo actRoomInfo = gson.fromJson(response.toString(), ActRoomInfo.class);
                ActRoomInfo.ArrBean arr = actRoomInfo.getArr();
                setData(arr);
            }
        });
    }

    private void setData(final ActRoomInfo.ArrBean arr) {
        String newpic = get_img_address + arr.getNewpic();
        ImageLoaderUtil.displayImageIcon(newpic, mImg_act_room_picture);
        mTv_label_1.setText(arr.getLabel1());
        mTv_label_2.setText(arr.getLabel2());
        mTv_phone.setText(arr.getPlayroom_phone());
        mTv_area.setText(arr.getPlayroom_area());
        mTv_member.setText(arr.getPlayroom_iaccommodate());
        mTv_remarks.setText(arr.getPlayroom_remark());

        final int hang = Integer.parseInt(arr.getHang()) + 1;
        final List<ActRoomInfo.ArrBean.ListBean> timeList = arr.getList();
        final List<MyDataAndTimeInfo> addAllData = new ArrayList<MyDataAndTimeInfo>();//存入所有时间信息
        int size = timeList.size();
        for (int i = 0; i < size; i++) {
            ActRoomInfo.ArrBean.ListBean listBean = timeList.get(i);
            String date = listBean.getDate();
            MyDataAndTimeInfo myDataInfo = new MyDataAndTimeInfo();
            myDataInfo.setMData(date);
            myDataInfo.setMDataId(listBean.getId());
            myDataInfo.setMWeek(listBean.getWeek());
            addAllData.add(myDataInfo);//存入日期
            List<ActRoomInfo.ArrBean.ListBean.TimeBean> time = listBean.getTime();
            for (int n = 0; n < (hang - 1); n++) {
                ActRoomInfo.ArrBean.ListBean.TimeBean timeBean;
                String begin_time;
                String end_time;
                String timeId;
                int isyuding;
                try {
                    timeBean = time.get(n);
                    begin_time = timeBean.getBegin_time();
                    end_time = timeBean.getEnd_time();
                    timeId = timeBean.getId();
                    isyuding = timeBean.getIsyuding();
                } catch (Exception e) {
                    begin_time = "";
                    end_time = "";
                    timeId = "";
                    isyuding = 0;
                }
                MyDataAndTimeInfo myTimeInfo = new MyDataAndTimeInfo();
                myTimeInfo.setMBegin_time(begin_time);
                myTimeInfo.setMEnd_time(end_time);
                myTimeInfo.setMTimeId(timeId);
                myTimeInfo.setMIsyuding(isyuding);
                addAllData.add(myTimeInfo);//存入日期对应的时间段
            }
        }
        StaggeredGridLayoutManager staggeredManager = new StaggeredGridLayoutManager(hang, StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView_table.setLayoutManager(staggeredManager);
        mTitleRecAdapter = new MyTitleRecAdapter(addAllData, hang);
        mRecyclerView_table.setAdapter(mTitleRecAdapter);
        if (addAllData.size() > 0) {
            mTv_none.setVisibility(View.GONE);
            mBt_submit.setVisibility(View.VISIBLE);
            mBt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOrSetUserID();
                    if (UserID.equals(NetConfig.UserID)) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (mSelectedColor == 0) {
                            ToastUtils.makeShortText("请选择日期", ActRoomDetailActivity.this);
                            return;
                        }
                        ReserveActroomActiviy.mReserveArrBean = arr;
                        Intent mIntent = new Intent(getBaseContext(), ReserveActroomActiviy.class);
                        mIntent.putExtra("url_address", url_address);
                        mIntent.putExtra("get_img_address", get_img_address);
                        mIntent.putExtra("data", timeList.get((mSelectedColor / hang)).getDate());
                        mIntent.putExtra("time", addAllData.get(mSelectedColor).getMBegin_time() + "-" + addAllData.get(mSelectedColor).getMEnd_time());
                        mIntent.putExtra("data_id", timeList.get((mSelectedColor / hang)).getId());
                        mIntent.putExtra("time_id", addAllData.get(mSelectedColor).getMTimeId());
                        startActivityForResult(mIntent, mRequestCode);
                    }
                }
            });
        }

        mScrollView_act_detail.scrollTo(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            //获取网络数据
            getIntnetData();
        }
    }

    public class MyTitleRecAdapter extends RecyclerView.Adapter<MyTitleRecAdapter.ViewHolder> {

        private List<MyDataAndTimeInfo> mData;
        private int                     mColumn;
        public static final  int firstLine = 1;
        private static final int otherLine = 2;

        public MyTitleRecAdapter(List data, int column) {
            this.mData = data;
            this.mColumn = column;
        }

        @Override
        public int getItemViewType(int position) {
            if (position % mColumn == 0) {
                return firstLine;//填充日期和星期
            } else {
                return otherLine;//填充时间和是否可预约
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            // 实例化展示的view
            switch (viewType) {
                case firstLine:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_appointment_top_item, parent, false);
                    break;
                case otherLine:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_appointment_item, parent, false);
                    break;
            }
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据

            if (position % mColumn == 0) {
                String mData = this.mData.get(position).getMData();
                String substring = mData;
                if (mData.length() >= 10) {
                    substring = mData.substring(5, 10);
                }
                holder.tv_data.setText(substring);
                holder.tv_week.setText(this.mData.get(position).getMWeek());
            } else {
                String mBegin_time = mData.get(position).getMBegin_time();
                String mEnd_time = mData.get(position).getMEnd_time();
                holder.tv_time.setText(mBegin_time + "-" + mEnd_time);
                int mIsyuding = mData.get(position).getMIsyuding();
                if (mIsyuding == 0) {
                    holder.tv_reservations.setText("不可预订");
                }
                if (mIsyuding == 1) {
                    holder.tv_reservations.setText("可预订");
                }
                if (mIsyuding == 2) {
                    holder.tv_reservations.setText("已预订");
                }
            }
            //TODO:
            if (position % mColumn != 0) {
                holder.relative.setBackgroundColor(getResources().getColor(R.color.vm_blue_38));
                if (position == mSelectedColor) {//选中
                    holder.relative.setBackgroundColor(getResources().getColor(R.color.vm_blue_100));
                }
                MyDataAndTimeInfo myDataAndTimeInfo = mData.get(position);
                int mIsyuding = myDataAndTimeInfo.getMIsyuding();
                if (mIsyuding == 2) {//已预订
                    holder.relative.setBackgroundColor(getResources().getColor(R.color.vm_blue_100));
                }

                holder.relative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSelectedColor == position) {
                            mSelectedColor = 0;
                        } else {
                            mSelectedColor = position;
                        }
                        int mIsyuding1 = mData.get(mSelectedColor).getMIsyuding();
                        if (mIsyuding1 == 2) {
                            mSelectedColor = 0;
                        }
                        if (mIsyuding1 == 0) {
                            mSelectedColor = 0;
                        }
                        mTitleRecAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView       tv_data;  //日期
            TextView       tv_week;  //星期
            TextView       tv_time;  //时间
            TextView       tv_reservations;  //是否可预约
            RelativeLayout relative;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_data = (TextView) itemView.findViewById(R.id.tv_data);
                tv_week = (TextView) itemView.findViewById(R.id.tv_week);
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                tv_reservations = (TextView) itemView.findViewById(R.id.tv_reservations);
                relative = (RelativeLayout) itemView.findViewById(R.id.relative);
            }
        }
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

    class MyDataAndTimeInfo {
        String mData;
        String mDataId;
        String mWeek;
        int    mIsyuding;
        String mEnd_time;
        String mBegin_time;
        String mTimeId;

        public String getMData() {
            return mData;
        }

        public void setMData(String mData) {
            this.mData = mData;
        }

        public String getMDataId() {
            return mDataId;
        }

        public void setMDataId(String mDataId) {
            this.mDataId = mDataId;
        }

        public String getMWeek() {
            return mWeek;
        }

        public void setMWeek(String mWeek) {
            this.mWeek = mWeek;
        }

        public int getMIsyuding() {
            return mIsyuding;
        }

        public void setMIsyuding(int mIsyuding) {
            this.mIsyuding = mIsyuding;
        }

        public String getMEnd_time() {
            return mEnd_time;
        }

        public void setMEnd_time(String mEnd_time) {
            this.mEnd_time = mEnd_time;
        }

        public String getMBegin_time() {
            return mBegin_time;
        }

        public void setMBegin_time(String mBegin_time) {
            this.mBegin_time = mBegin_time;
        }

        public String getMTimeId() {
            return mTimeId;
        }

        public void setMTimeId(String mTimeId) {
            this.mTimeId = mTimeId;
        }
    }
}
