package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.ShowDetailInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;


/**
 * @创建者 AndyYan
 * @创建时间 2018/1/2 21:06
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ShowsDetailActivity extends BaseActivity {

    private WebView        mWeb_detail;
    private ImageView      mIv_back;
    private TextView       mTv_place;
    private Button         mBt_appointment;
    private ProgressDialog progressDialog;
    private String mShowsDetailUrl = "http://220.248.107.62:8084/whyapi/searchTheaterInfo";
    private ImageView mImg_show_picture;
    private TextView  mTv_label_1;
    private TextView  mTv_label_2;
    private TextView  mTv_label_3;
    private TextView  mTv_ticket;
    private TextView  mTv_show_name;
    private TextView  mTv_time;
    private TextView  mTv_phone;
    private TextView  mTv_warning;
    private TextView  mTv_organisers;
    private TextView  mTv_undertake;
    private TextView  mTv_show_unit;
    private TextView  mTv_number_zan;
    private TextView  mTv_pay_kind;
    private String mURLHeadString = "http://220.248.107.62:8084/upFiles/";
    private TextView  mTv_talk;
    private String    mShowId;
    private ImageView mImg_dianzan;
    private boolean isDianZan  = false;
    private boolean isShouCang = false;
    private ImageView mImg_shoucang;
    private String    showName;
    private String UserID = NetConfig.UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows_detail);
        Intent intent = getIntent();
        mShowId = intent.getStringExtra("filmRoom");
        initDetailUrl();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initDetailUrl() {
        switch (getIntent().getIntExtra("modelId", 0)) {
            case 0:
                mShowsDetailUrl = NetConfig.RECOMMND_INFO;
                break;
            case 1:
                mShowsDetailUrl = NetConfig.LIBRARY_Detail;
                break;
            case 2:
                mShowsDetailUrl = NetConfig.ARTS_Detail;
                break;
            case 3:
                mShowsDetailUrl = NetConfig.MUSEUM_Detail;
                break;
            case 4:
                mShowsDetailUrl = NetConfig.SPORT_Detail;
                break;
            case 5:
                mShowsDetailUrl = NetConfig.NATION_Detail;
                break;
            case 6:
                mShowsDetailUrl = NetConfig.CULTURE_Detail;
                break;
            case 7:
                mShowsDetailUrl = NetConfig.FILM_Detail;
                break;

        }

    }


    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        //        mImg_show_picture = (ImageView) findViewById(R.id.img_show_picture);
        //        mTv_label_1 = (TextView) findViewById(R.id.tv_label_1);
        //        mTv_label_2 = (TextView) findViewById(R.id.tv_label_2);
        //        mTv_label_3 = (TextView) findViewById(R.id.tv_label_3);
        //        mTv_pay_kind = (TextView) findViewById(R.id.tv_pay_kind);
        //        mTv_ticket = (TextView) findViewById(R.id.tv_ticket);
        //        mTv_show_name = (TextView) findViewById(R.id.tv_show_name);
        //        mTv_place = (TextView) findViewById(R.id.tv_place);
        //        mTv_time = (TextView) findViewById(R.id.tv_time);
        //        mTv_phone = (TextView) findViewById(R.id.tv_phone);

        mWeb_detail = (WebView) findViewById(R.id.web_detail);
        //        mTv_warning = (TextView) findViewById(R.id.tv_warning);
        //        mTv_organisers = (TextView) findViewById(R.id.tv_organisers);
        //        mTv_undertake = (TextView) findViewById(R.id.tv_undertake);
        //        mTv_show_unit = (TextView) findViewById(R.id.tv_show_unit);
        //        mTv_number_zan = (TextView) findViewById(R.id.tv_number_zan);

        //还有一些待找。TODO:
        mTv_talk = (TextView) findViewById(R.id.tv_talk);
        mImg_dianzan = (ImageView) findViewById(R.id.img_dianzan);
        mImg_shoucang = (ImageView) findViewById(R.id.img_shoucang);
        mBt_appointment = (Button) findViewById(R.id.bt_appointment);
    }

    private void initData() {
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取网络数据展示
        getIntentData();
    }

    private void getIntentData() {
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        String showId = mShowId;
        RequestParams params = new RequestParams();
        params.put("member_id", UserID);
        params.put("id", showId);

        if (getIntent().getIntExtra("modelId", 0) == 0) {
            params.put("activity_id", getIntent().getStringExtra("activity_id"));
            params.put("activity_name", getIntent().getStringExtra("activity_name"));
        }

        HttpUtil.get(mShowsDetailUrl, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ShowsDetailActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ShowDetailInfo showDetailInfo;
                Gson gson = new Gson();
                String s = response.toString();
                showDetailInfo = gson.fromJson(response.toString(), ShowDetailInfo.class);
                ShowDetailInfo.ArrBean arr = showDetailInfo.getArr();
                String mNWebViewUrl = "http://220.248.107.62:8084/whyapi/index.html?id=";
                //设置webview
                int infoType = getIntent().getIntExtra("modelId", 0);
                if (infoType == 0) {
                    infoType = Integer.parseInt(getIntent().getStringExtra("type"));
                }
                if (infoType == 7) {
                    infoType = 6;
                }
                String url = mNWebViewUrl + arr.getId() + "&member_id=" + UserID + "&tb_type=" + (infoType);
                mWeb_detail.loadUrl(url);
                //启用支持javascript
                WebSettings settings = mWeb_detail.getSettings();
                settings.setJavaScriptEnabled(true);
                //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
                mWeb_detail.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // TODO Auto-generated method stub
                        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                        view.loadUrl(url);
                        return true;
                    }
                });

                // TODO:填充数据
                //                String newpic =mURLHeadString+ arr.getNewpic();
                //                ImageLoaderUtil.displayImageIcon(newpic,mImg_show_picture);
                //                String label1 = arr.getLabel1();
                //                String label2 = arr.getLabel2();
                //                String label3 = arr.getLabel3();
                //                if (label1.equals("")){
                //                    mTv_label_1.setVisibility(View.GONE);
                //                }else {
                //                    mTv_label_1.setText(label1);
                //                }
                //                if (label2.equals("")){
                //                    mTv_label_2.setVisibility(View.GONE);
                //                }else {
                //                    mTv_label_2.setText(label2);
                //                }
                //                if (label3.equals("")){
                //                    mTv_label_3.setVisibility(View.GONE);
                //                }else {
                //                    mTv_label_3.setText(label3);
                //                }
                //                float price = arr.getPrice();
                //                if (price==0){
                //                    mTv_pay_kind.setText("免费");
                //                }else {
                //                    mTv_pay_kind.setText(""+price+"元/每人");
                //                }
                //                int remain_ticket = arr.getRemain_ticket();
                //                mTv_ticket.setText("余票"+remain_ticket+"张");
                //                String fname = arr.getFname();
                //                mTv_show_name.setText(fname);
                //                String faddress = arr.getFaddress();
                //                mTv_place.setText(faddress);
                //                String fdate = arr.getFdate();
                //                mTv_time.setText(fdate);
                //                String fmobile = arr.getFmobile();
                //                mTv_phone.setText(fmobile);

                //                String fprompt = arr.getFprompt();//温馨提示
                //                mTv_warning.setText(fprompt);
                //                String fmain_company = arr.getFmain_company().trim();
                //                if (fmain_company.equals("")){
                //                    mTv_organisers.setVisibility(View.GONE);
                //                }else {
                //                    mTv_organisers.setText(fmain_company);
                //                }

                //                mTv_undertake.setText("");
                //                mTv_show_unit.setText("");
                //                mTv_number_zan.setText("");
                showName = arr.getFname();
                int isZan = arr.getIsZan();
                if (isZan == 0) {
                    mImg_dianzan.setImageResource(R.drawable.wff_zan);
                    isDianZan = false;
                } else {
                    mImg_dianzan.setImageResource(R.drawable.wff_zan_after);
                    isDianZan = true;
                }
                int isFavourite = arr.getIsFavourite();
                if (isFavourite == 0) {
                    mImg_shoucang.setImageResource(R.drawable.wff_collection);
                    isShouCang = false;
                } else {
                    mImg_shoucang.setImageResource(R.drawable.wff_collection_after);
                    isShouCang = true;
                }
                if ("".equals(arr.getIs_yuyue())) {
                    arr.setIs_yuyue("0");
                }
                int is_yuyue = Integer.valueOf(arr.getIs_yuyue());
                if (is_yuyue == 0) {
                    mBt_appointment.setText("立即预约");
                    mBt_appointment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mintent = new Intent(getBaseContext(), ReservationShowActivity.class);
                            startActivity(mintent);
                        }
                    });
                } else {
                    mBt_appointment.setText("已预约");
                }
                //设置一些监听点击事件
                setListener();
            }
        });
    }

    private void setListener() {
        //        mTv_place.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                //TODO:跳转百度地图activity
        //            }
        //        });

        //        //设置webview
        //        mWeb_detail.loadUrl(mWebViewUrl);
        //        //启用支持javascript
        //        WebSettings settings = mWeb_detail.getSettings();
        //        settings.setJavaScriptEnabled(true);
        //        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        //        mWeb_detail.setWebViewClient(new WebViewClient() {
        //            @Override
        //            public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //                // TODO Auto-generated method stub
        //                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        //                view.loadUrl(url);
        //                return true;
        //            }
        //        });
        mTv_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
                if (userInfo == null) {
                    UserID = NetConfig.UserID;
                } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
                    UserID = NetConfig.UserID;
                } else {
                    UserID = userInfo.member_id;
                }
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent mIntent = new Intent(getBaseContext(), SendCommentActivity.class);
                    mIntent.putExtra("showId", mShowId);
                    startActivity(mIntent);
                }
            }
        });
        mImg_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
                if (userInfo == null) {
                    UserID = NetConfig.UserID;
                } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
                    UserID = NetConfig.UserID;
                } else {
                    UserID = userInfo.member_id;
                }
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (isShouCang == false) {
                        String insertFavourite = "http://220.248.107.62:8084/whyapi/insertFavourite";
                        String Activity_id = mShowId;
                        String Activity_name = showName;
                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", Activity_id);
                        params.put("Activity_name", Activity_name);
                        params.setUseJsonStreamer(true);
                        HttpUtil.post(insertFavourite, params, new HttpUtil.JsonHttpResponseUtil() {
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
                                    ClubDetailInfo clubDetailInfo;
                                    Gson gson = new Gson();
                                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                                    String result = clubDetailInfo.getResult();
                                    if (result.equals("2")) {
                                        mImg_shoucang.setImageResource(R.drawable.wff_collection_after);
                                        isShouCang = !isShouCang;
                                    }
                                    String message = clubDetailInfo.getMessage();
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        String cancelZan = "http://220.248.107.62:8084/whyapi/cancelFavourite";
                        String Activity_id = mShowId;
                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", Activity_id);
                        HttpUtil.get(cancelZan, params, new HttpUtil.JsonHttpResponseUtil() {
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
                                    ClubDetailInfo clubDetailInfo;
                                    Gson gson = new Gson();
                                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                                    String result = clubDetailInfo.getResult();
                                    if (result.equals("2")) {
                                        mImg_shoucang.setImageResource(R.drawable.wff_collection);
                                        isDianZan = !isDianZan;
                                    }
                                    String message = clubDetailInfo.getMessage();
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        mImg_dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
                if (userInfo == null) {
                    UserID = NetConfig.UserID;
                } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
                    UserID = NetConfig.UserID;
                } else {
                    UserID = userInfo.member_id;
                }
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (isDianZan == false) {
                        //点赞
                        String insertZan = "http://220.248.107.62:8084/whyapi/insertZan";
                        String Activity_id = mShowId;
                        RequestParams params = new RequestParams();
                        params.put("userid", UserID);
                        params.put("Activity_id", Activity_id);
                        params.setUseJsonStreamer(true);
                        HttpUtil.post(insertZan, params, new HttpUtil.JsonHttpResponseUtil() {
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
                                    ClubDetailInfo clubDetailInfo;
                                    Gson gson = new Gson();
                                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                                    String result = clubDetailInfo.getResult();
                                    if (result.equals("2")) {
                                        mImg_dianzan.setImageResource(R.drawable.wff_zan_after);
                                        isDianZan = !isDianZan;
                                    }
                                    String message = clubDetailInfo.getMessage();
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        //取消点赞
                        mImg_dianzan.setImageResource(R.drawable.wff_zan);
                        String cancelZan = "http://220.248.107.62:8084/whyapi/cancelZan";
                        String Activity_id = mShowId;

                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", Activity_id);
                        HttpUtil.get(cancelZan, params, new HttpUtil.JsonHttpResponseUtil() {
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
                                    ClubDetailInfo clubDetailInfo;
                                    Gson gson = new Gson();
                                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                                    String result = clubDetailInfo.getResult();
                                    if (result.equals("2")) {
                                        mImg_dianzan.setImageResource(R.drawable.wff_zan);
                                        isDianZan = !isDianZan;
                                    }
                                    String message = clubDetailInfo.getMessage();
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        FilmRoomActivity.markShow = null;
    }
}
