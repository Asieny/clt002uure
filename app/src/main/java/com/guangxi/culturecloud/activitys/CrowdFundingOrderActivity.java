package com.guangxi.culturecloud.activitys;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.AppConstants;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.CultureCrowdFundingInfo;
import com.guangxi.culturecloud.model.PayResult;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.OrderInfoUtil2_0;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.RegexUtils;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/28 9:57
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CrowdFundingOrderActivity extends BaseActivity implements View.OnClickListener {

    private       ImageView               mIv_back;
    private       TextView                mTv_crowd_title;//众筹名称
    private       TextView                mTv_time;       //众筹时间
    private       TextView                mTv_place;      //众筹活动举办地址
    private       TextView                mTv_unit_price;//众筹单价
    private       TextView                mTv_supporter;//支持者数
    private       TextView                mTv_sum_portion;//限额
    private       TextView                mTv_residue_portion;//剩余份数
    private       TextView                mTv_explain;  //支持说明
    private       Button                  mBt_reduce;   //减按键
    private       Button                  mBt_num;      //购买数量
    private       Button                  mBt_plus;     //加按键
    private       EditText                mEdit_phone;  //通知电话
    private       TextView                mTv_sum_price;//总支付额
    private       CheckBox                mCheck;       //接受条款
    private       Button                  mBt_submit;   //提交订单
    public static CultureCrowdFundingInfo CrowdInfo;//众筹详尽信息
    private       String                  mBt_numText;
    private int mBuyNumber = 1;
    private TextView mTv_limit;
    private String UserID = NetConfig.UserID;
    private ProgressDialog progressDialog;
    private String         mUrl_address;//众筹所在的服务器地址
    private String         img_address;//众筹第一张图片在的其服务器地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowdfunding_order);
        Intent intent = getIntent();
        mUrl_address = intent.getStringExtra("url_address");
        img_address = intent.getStringExtra("img_address");
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_crowd_title = (TextView) findViewById(R.id.tv_crowd_title);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mTv_place = (TextView) findViewById(R.id.tv_place);
        mTv_unit_price = (TextView) findViewById(R.id.tv_unit_price);
        mTv_supporter = (TextView) findViewById(R.id.tv_supporter);
        mTv_sum_portion = (TextView) findViewById(R.id.tv_sum_portion);
        mTv_residue_portion = (TextView) findViewById(R.id.tv_residue_portion);
        mTv_explain = (TextView) findViewById(R.id.tv_explain);
        mTv_limit = (TextView) findViewById(R.id.tv_limit);
        mBt_reduce = (Button) findViewById(R.id.bt_reduce);
        mBt_num = (Button) findViewById(R.id.bt_num);
        mBt_plus = (Button) findViewById(R.id.bt_plus);
        mEdit_phone = (EditText) findViewById(R.id.edit_phone);
        mTv_sum_price = (TextView) findViewById(R.id.tv_sum_price);
        mCheck = (CheckBox) findViewById(R.id.check);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        String fname = CrowdInfo.fname;
        String zc_begin_time = CrowdInfo.zc_begin_time;
        String zc_end_time = CrowdInfo.zc_end_time;
        String activity_address = CrowdInfo.activity_address;
        String activity_detail_address = CrowdInfo.activity_detail_address;
        final float price = CrowdInfo.price;
        int target_number = CrowdInfo.target_number;
        int remain_number = CrowdInfo.remain_number;
        String repay_remark = CrowdInfo.repay_remark;
        final int limit_number = CrowdInfo.limit_number;

        mTv_crowd_title.setText(fname);
        mTv_time.setText(zc_begin_time + " 至 " + zc_end_time);
        mTv_place.setText(activity_address + activity_detail_address);
        mTv_unit_price.setText("¥" + price);
        mTv_sum_portion.setText("限额" + target_number + "份 | ");
        mTv_residue_portion.setText("剩余" + remain_number + "份");
        Spanned explain = Html.fromHtml(repay_remark);
        mTv_explain.setText(explain);
        mTv_limit.setText("本次众筹最多可购买" + limit_number + "张");
        mTv_sum_price.setText("¥" + price);
        mBt_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBt_numText = String.valueOf(mBt_num.getText());
                mBuyNumber = Integer.valueOf(mBt_numText);
                if (mBuyNumber <= 1) {
                    mBuyNumber = 1;
                } else {
                    mBuyNumber = mBuyNumber - 1;
                }
                mBt_num.setText("" + mBuyNumber);
                float money = price * mBuyNumber;
                mTv_sum_price.setText("¥" + money);
            }
        });
        mBt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBt_numText = String.valueOf(mBt_num.getText());
                mBuyNumber = Integer.valueOf(mBt_numText);
                if (mBuyNumber >= limit_number) {
                    mBuyNumber = limit_number;
                } else {
                    mBuyNumber = mBuyNumber + 1;
                }
                mBt_num.setText("" + mBuyNumber);
                double money = price * mBuyNumber;
                DecimalFormat df = new DecimalFormat("######0.00");
                mTv_sum_price.setText("¥" + df.format(money));

            }
        });

        mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBt_submit.setBackgroundColor(Color.parseColor("#29CCB1"));
                    mBt_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
                            if (userInfo == null) {
                                UserID = NetConfig.UserID;
                            } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
                                UserID = NetConfig.UserID;
                            } else {
                                UserID = userInfo.member_id;
                            }
                            if (UserID.equals(NetConfig.UserID)) {
                                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                String text = String.valueOf(mEdit_phone.getText()).trim();
                                if (text.equals("") || text.equals("请输入手机号")) {
                                    Toast.makeText(getBaseContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                // 账号不匹配手机号格式（11位数字且以1开头）
                                if (!RegexUtils.checkMobile(text)) {
                                    Toast.makeText(getBaseContext(), R.string.tip_account_regex_not_right,
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                                //提交订单
                                sendDataToIntnet();
                            }
                        }
                    });
                } else {
                    mBt_submit.setBackgroundColor(Color.parseColor("#8D8594"));
                    mBt_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //空点击事件，不做处理
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void sendDataToIntnet() {
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        final double price = CrowdInfo.price * mBuyNumber;
        DecimalFormat df = new DecimalFormat("######0.00");

        //        String insertWhyOrder = NetConfig.INSERT_WHYORDER;
        String insertWhyOrder = mUrl_address + "insertWhyOrder.do";
        String activity_id = CrowdInfo.id;
        String ticket_count = String.valueOf(mBt_num.getText());
        String amount = df.format(price);
        String table_name = CrowdInfo.tb_name;
        String faddress = CrowdInfo.activity_detail_address;
        String fname = CrowdInfo.fname;

        RequestParams params = new RequestParams();
        //        params.put("activity_id", activity_id);
        //        params.put("member_id", UserID);
        //        params.put("ticket_count", ticket_count);
        //        params.put("amount", amount);
        //        params.put("table_name", table_name);
        //        params.put("fname", fname);
        //        params.put("newPic", newPic);
        //        params.put("name", userInfo.username);
        //        params.put("phone", String.valueOf(mEdit_phone.getText()).trim());
        params.put("activity_id", activity_id);
        params.put("member_id", UserID);
        params.put("ticket_count", ticket_count);
        params.put("fstatus", "0");
        params.put("amount", amount);
        params.put("table_name", table_name);
        params.put("name", userInfo.username);
        params.put("phone", String.valueOf(mEdit_phone.getText()).trim());
        params.put("lng", CrowdInfo.lng);
        params.put("lat", CrowdInfo.lat);
        params.put("pic_address", img_address);
        params.put("begin_time", CrowdInfo.zc_begin_time);
        params.put("fname", fname);
        params.put("area_code", "");
        params.put("area_name", CrowdInfo.area_name);
        params.put("address", faddress);
        params.setUseJsonStreamer(true);
        HttpUtil.post(insertWhyOrder, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(CrowdFundingOrderActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            //{"message":"用户下单成功","id":"ba5edf0c-26c0-4fb8-90d9-223dd67a174f","result":"2"}
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    if ("2".equals(response.optString("result"))) {
                        orderStr = response.optString("id");
                        double price = CrowdInfo.price * mBuyNumber;
                        DecimalFormat df = new DecimalFormat("######0.00");
                        double resultPrice = Double.parseDouble(df.format(price));
                        if (resultPrice < 0.01) {
                            orderOverOK(orderStr, "", "zhifubao");
                        } else {
                            if (checkAliPayInstalled(CrowdFundingOrderActivity.this)) {
                                testSend(resultPrice);
                            } else {
                                ToastUtils.makeShortText("您未安装支付宝", CrowdFundingOrderActivity.this);
                            }
                        }
                    } else {
                        String message = response.optString("message");
                        ToastUtils.makeShortText(message, CrowdFundingOrderActivity.this);
                    }
                }
            }
        });
    }

    private              String orderStr     = "";
    private static final int    SDK_PAY_FLAG = 1001;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
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
                        Toast.makeText(CrowdFundingOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        String tradeNo = resultInfo.split("trade_no\":\"")[1];
                        tradeNo = tradeNo.substring(0, tradeNo.indexOf("\""));
                        orderOverOK(orderStr, tradeNo, "zhifubao");
                    } else {
                        Toast.makeText(CrowdFundingOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        //                        sendDataToIntnet();
                    }
                    break;
            }
        }
    };

    private void testSend(double price) {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */

        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AppConstants.ALI_PLAY_APPID, CrowdInfo.fname, price);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String sign = OrderInfoUtil2_0.getSign(params, AppConstants.ALI_PLAY_RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(CrowdFundingOrderActivity.this);
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

    private void orderOverOK(String order_num, String pay_no, String pay_type) {
        RequestParams params = new RequestParams();
        params.put("order_num", order_num);
        params.put("pay_no", pay_no);
        params.put("pay_type", pay_type);
        params.setUseJsonStreamer(true);//NetConfig.INSERT_WHYORDER_OK
        HttpUtil.post(NetConfig.INSERT_WHYORDER_OK, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(CrowdFundingOrderActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    ToastUtils.makeShortText("下单成功", CrowdFundingOrderActivity.this);
                    finish();
                }
            }

        });
    }

    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }
}
