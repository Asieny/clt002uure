package com.guangxi.culturecloud.activitys;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.AppConstants;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.MyOrderInfo;
import com.guangxi.culturecloud.model.PayResult;
import com.guangxi.culturecloud.utils.OrderInfoUtil2_0;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.jarvis.mytaobao.home.MapNavigationActivity;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Map;

import cn.com.mytest.util.ImageLoaderUtil;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/5 9:45
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private       ImageView      mIv_back;
    private       ImageView      mImg_show;
    private       TextView       mTv_show_name;
    private       TextView       mTv_place;
    private       TextView       mTv_show_data;
    private       TextView       mTv_show_time;
    private       TextView       mTv_number;
    private       TextView       mTv_random_number;
    private       RelativeLayout mRelative_to_mapview;
    private       TextView       mTv_place_tomap;
    private       TextView       mTv_order_number;
    private       TextView       mTv_order_time;
    private       Button         mBt_cancel;
    private       Button         mBt_state;
    private       LinearLayout   mLinear_show;
    public static MyOrderInfo    myOrderInfo;//订单信息
    private double mLng = 120.99037;//经度
    private double mLat = 32.079313;//纬度
    private String         mKind;//订单状态
    private ProgressDialog progressDialog;
    private ImageView      mImg_no_intnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Intent intent = getIntent();
        mKind = intent.getStringExtra("kind");
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mImg_no_intnet = (ImageView) findViewById(R.id.img_no_intnet);
        mLinear_show = (LinearLayout) findViewById(R.id.linear_show);
        mImg_show = (ImageView) findViewById(R.id.img_show);
        mTv_show_name = (TextView) findViewById(R.id.tv_show_name);
        mTv_place = (TextView) findViewById(R.id.tv_place);
        mTv_show_data = (TextView) findViewById(R.id.tv_show_data);
        mTv_show_time = (TextView) findViewById(R.id.tv_show_time);
        mTv_number = (TextView) findViewById(R.id.tv_number);
        mTv_random_number = (TextView) findViewById(R.id.tv_random_number);
        mRelative_to_mapview = (RelativeLayout) findViewById(R.id.relative_to_mapview);
        mTv_place_tomap = (TextView) findViewById(R.id.tv_place_tomap);
        mTv_order_number = (TextView) findViewById(R.id.tv_order_number);
        mTv_order_time = (TextView) findViewById(R.id.tv_order_time);
        mBt_cancel = (Button) findViewById(R.id.bt_cancel);
        mBt_state = (Button) findViewById(R.id.bt_state);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        mImg_no_intnet.setVisibility(View.GONE);
        //获取活动详情数据
        setData();
        // connectIntnet(myOrderInfo.activity_id, myOrderInfo.table_name);
    }

    //    //访问网络获取订单活动详情
    //    private void connectIntnet(String activity_id, String table_name) {
    //        String mShowsDetailUrl = NetConfig.SERACTIVITYINFO;
    //        RequestParams params = new RequestParams();
    //        params.put("activity_id", activity_id);
    //        params.put("table_name", table_name);
    //        HttpUtil.get(mShowsDetailUrl, params, new HttpUtil.JsonHttpResponseUtil() {
    //            @Override
    //            public void onStart() {
    //                showProgressDialog("正在加载，请稍后");
    //                super.onStart();
    //            }
    //
    //            @Override
    //            public void onFinish() {
    //                hideProgressDialog();
    //                super.onFinish();
    //            }
    //
    //            @Override
    //            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
    //                super.onSuccess(statusCode, headers, response);
    //                if (statusCode == 200) {
    //                    mImg_no_intnet.setVisibility(View.GONE);
    //                    Gson gson = new Gson();
    //                    MyActivityInfo showDetailInfo = gson.fromJson(response.toString(), MyActivityInfo.class);
    //                    MyActivityInfo.ActivityInfoBean activityInfo = showDetailInfo.getActivityInfo();
    ////                    setData(activityInfo);
    //                }
    //            }
    //        });
    //    }

    private void setData() {
        String newpic = myOrderInfo.newpic;
        String faddress = myOrderInfo.address;
        String fdate = myOrderInfo.begin_time;
        double lat = Double.parseDouble(myOrderInfo.lat);
        double lng = Double.parseDouble(myOrderInfo.lng);
        if (!newpic.equals("")) {
            String picUrl = newpic.split(",")[0];
            ImageLoaderUtil.displayImageIcon(myOrderInfo.get_img_address + picUrl.replace("\\", "/"), mImg_show);
        }
        //        ImageLoaderUtil.displayImageIcon(NetConfig.IMG + newpic, mImg_show);
        mTv_show_name.setText(myOrderInfo.activity_name);
        mTv_place.setText(faddress);
        String date = fdate.substring(0, 10);
        String time = fdate.substring(11, 16);
        mTv_show_data.setText(date);
        mTv_show_time.setText(time);
        mTv_number.setText(myOrderInfo.ticket_count);
        mTv_random_number.setText(myOrderInfo.random_number);
        mLng = lng;
        mLat = lat;
        mTv_place_tomap.setText(faddress);
        mTv_order_number.setText(myOrderInfo.order_num);
        String order_time = myOrderInfo.order_time;
        String substring = order_time.substring(0, 19);
        mTv_order_time.setText(substring);
        if (mKind.equals("订单取消")) {
            mBt_cancel.setVisibility(View.GONE);
            mBt_state.setText("查看活动详情，重新下单");
            //跳转活动详情
            mBt_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toShowDetail();
                }
            });
        }
        String table_name = myOrderInfo.table_name;
        if (table_name.equals("z_crowd_funding")) {
            mBt_cancel.setVisibility(View.GONE);
            mBt_state.setText("查看众筹详情");
            mBt_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转众筹详情
                    toCrowdFundingDetail();
                }
            });
        }
        if (mKind.equals("待支付")) {
            mBt_state.setText("支付");
            mBt_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    testSend(Double.parseDouble(myOrderInfo.amount));
                }
            });
        }
        if (mKind.equals("已领票") || mKind.equals("待领票")) {
            mBt_cancel.setVisibility(View.GONE);
            mBt_state.setVisibility(View.GONE);
        }
        mLinear_show.setOnClickListener(this);
        mRelative_to_mapview.setOnClickListener(this);
        mBt_cancel.setOnClickListener(this);
    }

    private String orderStr = myOrderInfo.id;

    private void testSend(double price) {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */

        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AppConstants.ALI_PLAY_APPID, myOrderInfo.activity_name, price);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String sign = OrderInfoUtil2_0.getSign(params, AppConstants.ALI_PLAY_RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderDetailActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;

                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);

        payThread.start();
    }

    private static final int     SDK_PAY_FLAG = 1001;
    @SuppressLint("HandlerLeak")
    private              Handler mHandler     = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    //                    Map<String,String> map = (Map<String, String>)msg.obj;
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //同步获取结果
                    String resultInfo = payResult.getResult();
                    Log.i("Pay", "Pay:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(OrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        String tradeNo = resultInfo.split("trade_no\":\"")[1];
                        tradeNo = tradeNo.substring(0, tradeNo.indexOf("\""));
                        orderOverOK(orderStr, tradeNo, "zhifubao");
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void toCrowdFundingDetail() {
        Intent intent = new Intent(OrderDetailActivity.this, CultureCrowdFundingDetialActivity.class);
        intent.putExtra("id", myOrderInfo.activity_id);
        intent.putExtra("url_address", myOrderInfo.url_address);
        intent.putExtra("get_img_address", myOrderInfo.get_img_address);
        if (!myOrderInfo.newpic.equals("")) {
            String picUrl = myOrderInfo.newpic.split(",")[0].replace("\\", "/");
            intent.putExtra("img_address", picUrl);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.linear_show://跳转活动详情
                if (myOrderInfo.table_name.equals("z_crowd_funding")) {
                    toCrowdFundingDetail();
                } else {
                    toShowDetail();
                }
                break;
            case R.id.relative_to_mapview://跳转百度地图
                Intent intentMap = new Intent(getBaseContext(), MapNavigationActivity.class);
                intentMap.putExtra("mLng", mLng);
                intentMap.putExtra("mLat", mLat);
                startActivity(intentMap);
                break;
            case R.id.bt_cancel://取消订单
                String id = myOrderInfo.order_num;
                String table_name1 = myOrderInfo.table_name;
                String ticket_count = myOrderInfo.ticket_count;
                String activity_id1 = myOrderInfo.activity_id;
                sendToIntent(id, table_name1, ticket_count, activity_id1);
                break;
        }
    }

    private void toShowDetail() {
        String activity_id = myOrderInfo.activity_id;
        String table_name = myOrderInfo.table_name;
        Intent intent = new Intent(OrderDetailActivity.this, ShowsDetailActivityNew.class);
        intent.putExtra("filmRoom", activity_id);
        String url_address = myOrderInfo.url_address;
        String actUrl = "";
        if (table_name.equals("z_arts")) {
            actUrl = url_address + "searchArtsInfo";
        } else if (table_name.equals("z_library")) {
            actUrl = url_address + "searchLibraryInfo";
        } else if (table_name.equals("z_museum")) {
            actUrl = url_address + "searchMuseumInfo";
        } else if (table_name.equals("z_Sports")) {
            actUrl = url_address + "searchSportsInfo";
        } else if (table_name.equals("z_Nation")) {
            actUrl = url_address + "nationInfo";
        } else if (table_name.equals("z_Culture")) {
            actUrl = url_address + "searchCultureInfo";
        } else if (table_name.equals("z_Theater")) {
            actUrl = url_address + "searchTheaterInfo";
        } else if (table_name.equals("z_community")) {
            actUrl = url_address + "searchCommunityInfo";
        } else {
            actUrl = url_address + "bigEventInfo";
        }
        intent.putExtra("modelId", 10);
        intent.putExtra("collectActUrl", actUrl);
        intent.putExtra("showImgUrl", myOrderInfo.get_img_address);
        intent.putExtra("filmRoom", activity_id);
        intent.putExtra("url_address", url_address);
        startActivity(intent);
    }

    private void orderOverOK(String order_num, String pay_no, String pay_type) {
        RequestParams params = new RequestParams();
        params.put("order_num", order_num);
        params.put("pay_no", pay_no);
        params.put("pay_type", pay_type);
        params.setUseJsonStreamer(true);
        HttpUtil.post(NetConfig.INSERT_WHYORDER_OK, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(OrderDetailActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    ToastUtils.makeShortText("下单成功", OrderDetailActivity.this);
                    finish();
                }
            }

        });
    }

    private void sendToIntent(String id, String table_name, String ticket_count, String activity_id) {
        String cancleWhyOrder = NetConfig.CANCEL_WHYORDER;
        //        String cancleWhyOrder = myOrderInfo.url_address + "cancleWhyOrder.do";
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("table_name", table_name);
        params.put("ticket_count", ticket_count);
        params.put("activity_id", activity_id);
        params.put("area_name", myOrderInfo.area_name);
        params.setUseJsonStreamer(true);
        HttpUtil.post(cancleWhyOrder, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200) {
                    Gson gson = new Gson();
                    ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    String result = clubDetailInfo.getResult();
                    String message = clubDetailInfo.getMessage();
                    if (result.equals("2")) {
                        finish();
                    }
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
