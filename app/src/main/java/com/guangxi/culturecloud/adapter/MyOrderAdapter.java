package com.guangxi.culturecloud.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.AppConstants;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.MyOrderInfo;
import com.guangxi.culturecloud.model.PayResult;
import com.guangxi.culturecloud.utils.OrderInfoUtil2_0;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Map;

import cn.com.mytest.adapter.AbsAdapter;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zww on 2018/1/5.
 * 活动众筹adapter
 */

public class MyOrderAdapter extends AbsAdapter<MyOrderInfo> {
    private Context context;

    public MyOrderAdapter(Context context) {
        super(context);
        this.context = context;
    }

    private int position2;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.item_list_myorder, parent, false);
        }
        TextView goodsName = (TextView) convertView.findViewById(R.id.tvgoodsname);
        TextView OrderId = (TextView) convertView.findViewById(R.id.OrderId);
        TextView OrderState = (TextView) convertView.findViewById(R.id.randow_num);
        TextView goodsMoney = (TextView) convertView.findViewById(R.id.money);
        TextView count = (TextView) convertView.findViewById(R.id.count);
        TextView btnPay = (TextView) convertView.findViewById(R.id.btn_pay);
        TextView btnCancel = (TextView) convertView.findViewById(R.id.btn_cancel_pay);
        if ("0".equals(getItem(position).fstatus)) {
            btnPay.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        }
        final String title = getItem(position).fname;
        final String amount = getItem(position).amount;
        final String ticketCount = getItem(position).ticket_count;
        final String orderId = getItem(position).order_num;


        goodsName.setText(title);
        goodsMoney.setText("总金额：¥" + amount);
        count.setText("票数：×" + ticketCount);
        OrderId.setText("订单号：" + orderId);
        OrderState.setText("随机码：" + getItem(position).random_number);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position2 = position;
                //                EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                orderStr = getItem(position).id;
                pay(getItem(position).activity_name, Double.parseDouble(amount));
            }
        });
        final String tableName = getItem(position).table_name;
        final String activity_id = getItem(position).activity_id;
        final String id = getItem(position).order_num;
        final String area_name = getItem(position).area_name;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPay(id, tableName, ticketCount, activity_id, area_name);
            }
        });


        return convertView;
    }

    /**
     * 支付
     */
    private void pay(String name, double price) {
        testSend(name, price);
    }

    /**
     * 取消支付
     */
    private void cancelPay(String id, String tableName, String ticketCount, String activityId, String area_name) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("table_name", tableName);
        params.put("ticket_count", ticketCount);
        params.put("activity_id", activityId);
        params.put("area_name", area_name);
        params.setUseJsonStreamer(true);
        HttpUtil.post(NetConfig.CANCEL_WHYORDER, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog("正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    ToastUtils.makeShortText("取消成功", context);
                    getData().remove(position2);
                    notifyDataSetChanged();
                }
            }

        });
    }

    private String  orderStr = "";
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
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        String tradeNo = resultInfo.split("trade_no\":\"")[1];
                        tradeNo = tradeNo.substring(0, tradeNo.indexOf("\""));
                        orderOverOK(orderStr, tradeNo, "zhifubao");
                    } else {
                        Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void testSend(String name, double price) {
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AppConstants.ALI_PLAY_APPID, name, price);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String sign = OrderInfoUtil2_0.getSign(params, AppConstants.ALI_PLAY_RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) context);
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

    private static final int SDK_PAY_FLAG = 1001;

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
                showProgressDialog("正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    ToastUtils.makeShortText("下单成功", context);
                    getData().remove(position2);
                    notifyDataSetChanged();
                }
            }

        });
    }

    private ProgressDialog progressDialog;

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setInverseBackgroundForced(false);//对话框后面的窗体不获得焦点
        progressDialog.setCanceledOnTouchOutside(false);//旁击不消失
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }


}
