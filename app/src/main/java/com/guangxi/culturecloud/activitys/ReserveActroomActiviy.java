package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ActRoomInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/27 14:28
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ReserveActroomActiviy extends BaseActivity implements View.OnClickListener {

    private       ImageView           mIv_back;
    public static ActRoomInfo.ArrBean mReserveArrBean;
    private       ImageView           mImg_act_room;
    private       TextView            mTv_act_room_name;
    private       TextView            mTv_place;
    private       TextView            mTv_data;
    private       TextView            mTv_time;
    private       TextView            mTv_price;
    private       EditText            mEdit_user_name;
    private       EditText            mEdit_order_name;
    private       EditText            mEdit_phone_num;
    private       EditText            mEdit_use_way;
    private       Button              mBt_submit;
    private       String              mUrl_address;
    private       String              mGet_img_address;
    private       String              mData;
    private       String              mData_id;
    private       String              mTime_id;
    private       String              mTime;
    private       ProgressDialog      progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservate_actroom);
        Intent intent = getIntent();
        mUrl_address = intent.getStringExtra("url_address");
        mGet_img_address = intent.getStringExtra("get_img_address");
        mData = intent.getStringExtra("data");
        mTime = intent.getStringExtra("time");
        mData_id = intent.getStringExtra("data_id");
        mTime_id = intent.getStringExtra("time_id");
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mImg_act_room = (ImageView) findViewById(R.id.img_act_room);
        mTv_act_room_name = (TextView) findViewById(R.id.tv_act_room_name);
        mTv_place = (TextView) findViewById(R.id.tv_place);
        mTv_data = (TextView) findViewById(R.id.tv_data);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mTv_price = (TextView) findViewById(R.id.tv_price);
        mEdit_user_name = (EditText) findViewById(R.id.edit_user_name);
        mEdit_order_name = (EditText) findViewById(R.id.edit_order_name);
        mEdit_phone_num = (EditText) findViewById(R.id.edit_phone_num);
        mEdit_use_way = (EditText) findViewById(R.id.edit_use_way);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);

        String newpic = mGet_img_address + mReserveArrBean.getNewpic();
        String playroom_name = mReserveArrBean.getPlayroom_name();
        String address = mReserveArrBean.getAddress();
        double playroom_price = Double.parseDouble(mReserveArrBean.getPlayroom_price());

        ImageLoaderUtil.displayImageIcon(newpic, mImg_act_room);
        mTv_act_room_name.setText(playroom_name);
        mTv_place.setText(address);
        mTv_data.setText(mData);
        mTv_time.setText(mTime);
        if (playroom_price == 0) {
            mTv_price.setText("免费");
        } else {
            mTv_price.setText("欢迎电话" + mReserveArrBean.getPlayroom_phone() + "预约咨询");
        }

        mBt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_submit:
                String user_name = String.valueOf(mEdit_user_name.getText()).trim();
                String order_name = String.valueOf(mEdit_order_name.getText()).trim();
                String phone_num = String.valueOf(mEdit_phone_num.getText()).trim();
                String use_way = String.valueOf(mEdit_use_way.getText()).trim();
                if (user_name.equals("") || user_name.equals("请填写使用者姓名")) {
                    ToastUtils.makeShortText("请填写使用者姓名", ReserveActroomActiviy.this);
                    return;
                }
                if (order_name.equals("") || order_name.equals("请填写预订联系人")) {
                    ToastUtils.makeShortText("请填写预订联系人", ReserveActroomActiviy.this);
                    return;
                }
                if (phone_num.equals("") || phone_num.equals("请填写手机号")) {
                    ToastUtils.makeShortText("请填写手机号", ReserveActroomActiviy.this);
                    return;
                }
                if (use_way.equals("") || use_way.equals("请填写活动室使用用途")) {
                    ToastUtils.makeShortText("请填写活动室使用用途", ReserveActroomActiviy.this);
                    return;
                }
                sendToIntnet(user_name, order_name, phone_num, use_way);
                break;
        }
    }

    private void sendToIntnet(String user_name, String order_name, String phone_num, String use_way) {
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        RequestParams params = new RequestParams();
        params.put("playroom_id", mReserveArrBean.getId());
        params.put("member_id", userInfo.member_id);
        params.put("user_name", user_name);
        params.put("contact", order_name);
        params.put("phone", phone_num);
        params.put("purpose", use_way);
        params.put("date_id", mData_id);
        params.put("time_id", mTime_id);
        params.put("playroom_name", mReserveArrBean.getPlayroom_name());
        params.setUseJsonStreamer(true);
        HttpUtil.post(mUrl_address + "insertPlayroomOrder.do", params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ReserveActroomActiviy.this, "正在提交，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    ToastUtils.makeShortText("预约成功", ReserveActroomActiviy.this);
                    finish();
                }
            }
        });
    }
}
