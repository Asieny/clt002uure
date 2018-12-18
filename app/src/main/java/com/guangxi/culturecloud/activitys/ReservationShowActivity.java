package com.guangxi.culturecloud.activitys;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.AppConstants;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.MessageEvent;
import com.guangxi.culturecloud.model.PayResult;
import com.guangxi.culturecloud.model.ShowDetailInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.OrderInfoUtil2_0;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.RegexUtils;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Map;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/3 11:22
 * @描述 下订单
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ReservationShowActivity extends BaseActivity {

    private ImageView mIv_back; //回退键
    private Button    mBt_submit;
    private Button    mBt_num;
    private EditText  mEdit_phone_num;
    private EditText  mEdit_order_name;
    private Button    mBt_reduce;
    private Button    mBt_plus;
    private String    mBt_numText;
    private int mBuyNumber  = 1;
    private int limitNumber = 5;
    private ProgressDialog         progressDialog;
    private ShowDetailInfo.ArrBean mOrder;//活动详情数据
    private ImageView              mImg_show;
    private TextView               mTv_show_name;
    private TextView               mTv_place;
    private TextView               mTv_show_data;
    private TextView               mTv_show_time;
    private String UserID = NetConfig.UserID;
    private TextView mTv_sum_money;
    private TextView mTv_howmuch;
    private String   piUrl;
    private String   mCollectionUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_show);
        piUrl = getIntent().getStringExtra("picUrl");//活动详情的图片详细地址
        mCollectionUrl = getIntent().getStringExtra("collectionUrl");//从服务器的地址
        mOrder = ShowsDetailActivityNew.mInfo;
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mImg_show = (ImageView) findViewById(R.id.img_show);
        mTv_show_name = (TextView) findViewById(R.id.tv_show_name);
        mTv_place = (TextView) findViewById(R.id.tv_place);
        mTv_show_data = (TextView) findViewById(R.id.tv_show_data);
        mTv_show_time = (TextView) findViewById(R.id.tv_show_time);
        mBt_reduce = (Button) findViewById(R.id.bt_reduce);
        mBt_num = (Button) findViewById(R.id.bt_num);
        mBt_plus = (Button) findViewById(R.id.bt_plus);
        mEdit_phone_num = (EditText) findViewById(R.id.edit_phone_num);
        mEdit_order_name = (EditText) findViewById(R.id.edit_order_name);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
        mTv_sum_money = (TextView) findViewById(R.id.tv_sum_money);
        mTv_howmuch = (TextView) findViewById(R.id.tv_howmuch);
    }

    private void initData() {
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageLoaderUtil.displayImageIcon(piUrl, mImg_show);
        mTv_show_name.setText(mOrder.getFname());
        mTv_place.setText(mOrder.getFaddress());
        String fdate = mOrder.getFdate();
        String dataString = fdate.substring(0, 10);
        mTv_show_data.setText(dataString);//日期
        String timeString = "";
        try {
            timeString = fdate.substring(11, 16);
        } catch (Exception e) {

        }
        mTv_show_time.setText(timeString);//具体时间
        mTv_howmuch.setText("¥" + mOrder.getPrice());
        mTv_sum_money.setText("¥" + mOrder.getPrice());
        //获取网络数据展示
        //        getIntnetData();

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
                float money = mOrder.getPrice() * mBuyNumber;
                mTv_sum_money.setText("¥" + money);
            }
        });
        mBt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBt_numText = String.valueOf(mBt_num.getText());
                mBuyNumber = Integer.valueOf(mBt_numText);
                if (mBuyNumber >= limitNumber) {
                    mBuyNumber = limitNumber;
                } else {
                    mBuyNumber = mBuyNumber + 1;
                }
                mBt_num.setText("" + mBuyNumber);
                double money = mOrder.getPrice() * mBuyNumber;
                DecimalFormat df = new DecimalFormat("######0.00");
                mTv_sum_money.setText("¥" + df.format(money));

            }
        });
        mBt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                show1();
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
                    String mEdit_phone_numText = String.valueOf(mEdit_phone_num.getText()).trim();
                    if (mEdit_phone_numText.equals("请输入手机号") || mEdit_phone_numText.equals("")) {
                        Toast.makeText(getBaseContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 账号不匹配手机号格式（11位数字且以1开头）
                    if (!RegexUtils.checkMobile(mEdit_phone_numText)) {
                        Toast.makeText(getBaseContext(), R.string.tip_account_regex_not_right,
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String mEdit_order_nameText = String.valueOf(mEdit_order_name.getText()).trim();
                    if (mEdit_order_nameText.equals("预订人姓名") || mEdit_order_nameText.equals("")) {
                        Toast.makeText(getBaseContext(), "请输姓名", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //TODO:提交预订信息到服务器
                    //                    sendDataToIntnet();
                    //调用支付宝接口成功后提交订单
                    //                    EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                    // testSend();

                    //                    sendDataToIntnet();
                    openPopupWindow(mBt_submit);
                }
            }
        });
    }

    private void sendDataToIntnet() {
        final double price = mOrder.getPrice() * mBuyNumber;

        DecimalFormat df = new DecimalFormat("######0.00");
        String insertWhyOrder = mCollectionUrl + "insertWhyOrder.do";
        String activity_id = mOrder.getId();
        String ticket_count = String.valueOf(mBt_num.getText());
        String amount = df.format(price);
        String table_name = mOrder.getTb_name();
        String faddress = mOrder.getFaddress();
        String fname = mOrder.getFname();

        RequestParams params = new RequestParams();
        params.put("activity_id", activity_id);
        params.put("member_id", UserID);
        params.put("ticket_count", ticket_count);
        if (mOrder.getPrice() * mBuyNumber <= 0.01) {
            params.put("fstatus", "1");//免费
        } else {
            params.put("fstatus", "0");
        }
        params.put("amount", amount);
        params.put("table_name", table_name);
        params.put("name", mEdit_order_name.getText());
        params.put("phone", mEdit_phone_num.getText());
        params.put("lng", mOrder.getLng());
        params.put("lat", mOrder.getLat());
        params.put("pic_address", mOrder.getNewpic());
        params.put("begin_time", mOrder.getFdate());
        params.put("fname", fname);
        params.put("area_code", "");
        params.put("area_name", mOrder.getArea_name());
        params.put("address", faddress);
        params.setUseJsonStreamer(true);
        HttpUtil.post(insertWhyOrder, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ReservationShowActivity.this, "正在加载，请稍后");
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
                        double price = mOrder.getPrice() * mBuyNumber;
                        DecimalFormat df = new DecimalFormat("######0.00");
                        double resultPrice = Double.parseDouble(df.format(price));
                        if (resultPrice < 0.01) {
                            orderOverOK(orderStr, "", "zhifubao");
                        } else {
                            if (checkAliPayInstalled(ReservationShowActivity.this)) {
                                testSend(resultPrice);
                            } else {
                                ToastUtils.makeShortText("您未安装支付宝", ReservationShowActivity.this);
                            }
                        }
                    } else {
                        String message = response.optString("message");
                        ToastUtils.makeShortText(message, ReservationShowActivity.this);
                    }
                }
            }

        });
    }

    private              String  orderStr       = "";
    private static final int     SDK_PAY_FLAG   = 1001;
    private static final int     SDK_WXPAY_FLAG = 1000;
    @SuppressLint("HandlerLeak")
    private              Handler mHandler       = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //同步获取结果
                    String resultInfo = payResult.getResult();
                    Log.i("Pay", "Pay:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(ReservationShowActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        String tradeNo = resultInfo.split("trade_no\":\"")[1];
                        tradeNo = tradeNo.substring(0, tradeNo.indexOf("\""));
                        orderOverOK(orderStr, tradeNo, "zhifubao");
                    } else {
                        Toast.makeText(ReservationShowActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        //                        sendDataToIntnet();
                    }
                    break;
                case SDK_WXPAY_FLAG:
                    //                  String result = String.valueOf(msg.obj);
                    String result = msg.obj.toString();
                    if ("支付成功".equals(result)) {
                        ToastUtils.makeShortText("WX支付成功", ReservationShowActivity.this);
                    } else {
                        ToastUtils.makeShortText("WX支付失败", ReservationShowActivity.this);
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

        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AppConstants.ALI_PLAY_APPID, mOrder.getFname(), price);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String sign = OrderInfoUtil2_0.getSign(params, AppConstants.ALI_PLAY_RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ReservationShowActivity.this);
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
        params.setUseJsonStreamer(true);
        HttpUtil.post(NetConfig.INSERT_WHYORDER_OK, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ReservationShowActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    ToastUtils.makeShortText("下单成功", ReservationShowActivity.this);
                    finish();
                }
            }

        });
    }


    private void show1() {
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    public static boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    private PopupWindow popupWindow;

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(ReservationShowActivity.this).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //设置背景色
                setBackgroundAlpha(1.0f);
            }
        });
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    //设置PopupWindow的View点击事件
    private void setOnPopupViewClick(View view) {
        RelativeLayout rlt_wx, rlt_zfb;
        TextView tv_cancel;
        rlt_wx = (RelativeLayout) view.findViewById(R.id.rlt_wx);//微信
        rlt_zfb = (RelativeLayout) view.findViewById(R.id.rlt_zfb);//支付宝
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);//支付宝
        rlt_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //微信支付
                payWithWeiXin();
            }
        });
        rlt_zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //支付宝支付
                payWithZhiFuBao();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    private void payWithWeiXin() {
        final double price = mOrder.getPrice() * mBuyNumber;
        DecimalFormat df = new DecimalFormat("######0.00");
        String insertWhyOrder = mCollectionUrl + "payOrder";
        String activity_id = mOrder.getId();
        String ticket_count = String.valueOf(mBt_num.getText());
        String amount = df.format(price);
        String table_name = mOrder.getTb_name();
        String faddress = mOrder.getFaddress();
        String fname = mOrder.getFname();

        RequestParams params = new RequestParams();
        params.put("activity_id", activity_id);
        params.put("amount", amount);
        params.put("area_code", "");
        params.put("area_name", mOrder.getArea_name());
        params.put("fname", fname);
        if (mOrder.getPrice() * mBuyNumber <= 0.01) {
            params.put("fstatus", "1");//免费
        } else {
            params.put("fstatus", "0");
        }
        params.put("ip", "222.216.241.136");
        params.put("member_id", UserID);
        params.put("order_time", "20181216");
        params.put("paycode", "2");
        params.put("phone", mEdit_phone_num.getText());
        params.put("table_name", table_name);
        params.put("ticket_count", ticket_count);
        params.put("username", mEdit_order_name.getText());

        //        params.put("lng", mOrder.getLng());
        //        params.put("lat", mOrder.getLat());
        //        params.put("pic_address", mOrder.getNewpic());
        //        params.put("begin_time", mOrder.getFdate());
        //        params.put("address", faddress);
        params.setUseJsonStreamer(true);
        HttpUtil.post(insertWhyOrder, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ReservationShowActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            //{"message": "用户下单成功","orderinfo": "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%221.0%22%2C%22subject%22%3A%22%E5%81%9C%E8%BD%A6%E4%BB%98%E8%B4%B9%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%221216100034-1671%22%7D&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F118.89.109.106%3A8080%2FYKTJK%2FAliPay&app_id=2018020602149335&sign_type=RSA2&version=1.0&timestamp=2016-07-29+16%3A55%3A53&sign=QsLTFEX4JvXDXWpYx0YBlUtc%2FXTEm5h54QszPBOEAD0jq3f3R%2F9PaLMvJMvBAQKGhqr%2FdKXq94rBOA4k7was8Xtg4LMCAIMvqkiEph2ApAx5KLJQk7oQDF586AANi54Ila7nWKutzCAF8SjIPYMlPrMpmx%2BOKily%2FpajxX4kPK7GWe%2B%2Bo3YePZXw4rn%2FkJ28HLepovsPU%2BHPgGVcG%2FeE37C76fV6qUfCnh4jcvSIp3grhA6aVZbDS7%2FpCyl0EPN0VvMuRrA84KqzcpL8swh6tUEjB3jdvfaMhnOrYUkhuMnCLou0uWH35go2s3CQqr6jICMGIKN86%2FXTE0Cm25gTQg%3D%3D","result": 2}
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    if ("2".equals(response.optString("result"))) {
                        String return_code = response.optString("return_code");
                        String return_msg = response.optString("return_msg");
                        if (return_code.equals("SUCCESS")) {
                            String appId = response.optString("appId");
                            String partnerId = response.optString("partnerId");
                            String prepayId = response.optString("prepayId");
                            String nonceStr = response.optString("nonceStr");
                            int timeStamp = response.optInt("timeStamp");
                            String sign = response.optString("sign");
                            toWXPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign);
                        }
                        ToastUtils.makeShortText(return_msg, ReservationShowActivity.this);
                    } else {
                        String message = response.optString("message");
                        ToastUtils.makeShortText(message, ReservationShowActivity.this);
                    }
                }
            }
        });
    }

    private IWXAPI iwxapi; //微信支付api

    private void toWXPay(final String appId, final String partnerId, final String prepayId, final String nonceStr, final int timeStamp, final String sign) {
        iwxapi = WXAPIFactory.createWXAPI(ReservationShowActivity.this, appId); //初始化微信api
        iwxapi.registerApp(appId); //注册appid  appid可以在开发平台获取
        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
            @Override
            public void run() {
                PayReq request = new PayReq(); //调起微信APP的对象
                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
                request.appId = appId;
                request.partnerId = partnerId;//微信支付分配的商户号
                request.prepayId = prepayId;//微信返回的支付 交易会话ID
                request.packageValue = "Sign=WXPay";
                request.nonceStr = nonceStr;//随机字符串，不长于32位。
                request.timeStamp = String.valueOf(timeStamp);//时间戳
                request.sign = sign;//签名
                iwxapi.sendReq(request);//发送调起微信的请求
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void payWithZhiFuBao() {
        final double price = mOrder.getPrice() * mBuyNumber;
        DecimalFormat df = new DecimalFormat("######0.00");
        String insertWhyOrder = mCollectionUrl + "payOrder";
        String activity_id = mOrder.getId();
        String ticket_count = String.valueOf(mBt_num.getText());
        String amount = df.format(price);
        String table_name = mOrder.getTb_name();
        String faddress = mOrder.getFaddress();
        String fname = mOrder.getFname();

        RequestParams params = new RequestParams();
        params.put("activity_id", activity_id);
        params.put("amount", amount);
        params.put("area_code", "");
        params.put("area_name", mOrder.getArea_name());
        params.put("fname", fname);
        if (mOrder.getPrice() * mBuyNumber <= 0.01) {
            params.put("fstatus", "1");//免费
        } else {
            params.put("fstatus", "0");
        }
        params.put("ip", "222.216.241.136");
        params.put("member_id", UserID);
        params.put("order_time", "20181216");
        params.put("paycode", "1");
        params.put("phone", mEdit_phone_num.getText());
        params.put("table_name", table_name);
        params.put("ticket_count", ticket_count);
        params.put("username", mEdit_order_name.getText());

        //        params.put("lng", mOrder.getLng());
        //        params.put("lat", mOrder.getLat());
        //        params.put("pic_address", mOrder.getNewpic());
        //        params.put("begin_time", mOrder.getFdate());
        //        params.put("address", faddress);
        params.setUseJsonStreamer(true);
        HttpUtil.post(insertWhyOrder, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ReservationShowActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            //{"message": "用户下单成功","orderinfo": "charset=utf-8&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%221.0%22%2C%22subject%22%3A%22%E5%81%9C%E8%BD%A6%E4%BB%98%E8%B4%B9%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%221216100034-1671%22%7D&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F118.89.109.106%3A8080%2FYKTJK%2FAliPay&app_id=2018020602149335&sign_type=RSA2&version=1.0&timestamp=2016-07-29+16%3A55%3A53&sign=QsLTFEX4JvXDXWpYx0YBlUtc%2FXTEm5h54QszPBOEAD0jq3f3R%2F9PaLMvJMvBAQKGhqr%2FdKXq94rBOA4k7was8Xtg4LMCAIMvqkiEph2ApAx5KLJQk7oQDF586AANi54Ila7nWKutzCAF8SjIPYMlPrMpmx%2BOKily%2FpajxX4kPK7GWe%2B%2Bo3YePZXw4rn%2FkJ28HLepovsPU%2BHPgGVcG%2FeE37C76fV6qUfCnh4jcvSIp3grhA6aVZbDS7%2FpCyl0EPN0VvMuRrA84KqzcpL8swh6tUEjB3jdvfaMhnOrYUkhuMnCLou0uWH35go2s3CQqr6jICMGIKN86%2FXTE0Cm25gTQg%3D%3D","result": 2}
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    if ("2".equals(response.optString("result"))) {
                        orderStr = response.optString("orderinfo");
                        double price = mOrder.getPrice() * mBuyNumber;
                        DecimalFormat df = new DecimalFormat("######0.00");
                        double resultPrice = Double.parseDouble(df.format(price));
                        if (resultPrice < 0.01) {
                            ToastUtils.makeShortText("免费", ReservationShowActivity.this);
                            orderOverOK(orderStr, "", "zhifubao");
                        } else {
                            if (checkAliPayInstalled(ReservationShowActivity.this)) {
                                testZFBSend(resultPrice, orderStr);
                            } else {
                                ToastUtils.makeShortText("您未安装支付宝", ReservationShowActivity.this);
                            }
                        }
                    } else {
                        String message = response.optString("message");
                        ToastUtils.makeShortText(message, ReservationShowActivity.this);
                    }
                }
            }

        });
    }

    private void testZFBSend(double resultPrice, final String orderStr) {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        //        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AppConstants.ALI_PLAY_APPID, "mOrder.getFname()", price);
        //        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        //        //
        //        String sign = OrderInfoUtil2_0.getSign(params, AppConstants.ALI_PLAY_RSA2_PRIVATE, true);
        //        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ReservationShowActivity.this);
                Map<String, String> result = alipay.payV2(orderStr, true);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wxPaySuccess(MessageEvent messageEvent) {
        if ("WX支付成功".equals(messageEvent.getMessage())) {
            finish();
        }
    }
}
