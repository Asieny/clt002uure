package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.AdmireInfo;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.CommentInfo;
import com.guangxi.culturecloud.model.ShowDetailInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.jarvis.MyView.MyListView;
import com.jarvis.mytaobao.home.MapNavigationActivity;
import com.loopj.android.http.RequestParams;
import com.onekeyshare.OnekeyShare;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cz.msebera.android.httpclient.Header;


/**
 * @创建者 AndyYan
 * @创建时间 2018/1/2 21:06
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ShowsDetailActivityNew extends BaseActivity {

    private WebView        mWeb_detail;
    private ImageView      mIv_back;
    private TextView       mTv_place;
    private Button         mBt_appointment;
    private ProgressDialog progressDialog;
    private String mShowsDetailUrl = NetConfig.SEARCH_THEATER_INFO;
    private String mShowImgUrl     = NetConfig.IMG;
    private String mshowUrlAddress = NetConfig.URL_HEAD_ADDRESS;
    private ShowDetailInfo.ArrBean neddAarr;
    private ImageView              mImg_show_picture;
    private TextView               mTv_label_1;
    private TextView               mTv_label_2;
    private TextView               mTv_label_3;
    private TextView               mTv_ticket;
    private TextView               mTv_show_name;
    private TextView               mTv_time;
    private TextView               mTv_phone;
    private TextView               mTv_warning;
    private TextView               mTv_organisers;
    //    private TextView  mTv_undertake;
    //    private TextView  mTv_show_unit;
    private TextView               mTv_number_zan;
    private TextView               mTv_comment;
    private TextView               mTv_pay_kind;
    private String mURLHeadString = NetConfig.IMG;
    private ImageView mTv_talk;
    private String    mShowId;
    private ImageView mImg_dianzan;
    private boolean isDianZan  = false;
    private boolean isShouCang = false;
    private ImageView mImg_shoucang;
    private String    showName;
    private String UserID = NetConfig.UserID;
    private       LinearLayoutManager    mLayoutManager;
    private       RecyclerView           mRecyclerView;
    private       MyRecAdapter           mMyAdapter;   // RecyclerView的适配器
    private       List                   mImgUrlList;//点赞头像url储存
    private       RelativeLayout         mRelative_to_mapview;
    public static ShowDetailInfo.ArrBean mInfo;
    private       MyListView             mLv_talk;
    private       ImageView              mImg_no_intnet;
    private double mLng = 120.99037;//经度
    private double mLat = 32.079313;//纬度
    private TextView      mTv_time_detail;
    private MyRecAdapter  myRecAdapter;//头像适配器
    private MyTalkAdapter mMyTalkAdapter;//评论适配器
    private ArrayList     mShowList;//用户评论集合
    private TextView      mTv_act_detail;
    private TextView      mTv_avt_org;
    private ScrollView    mScrollView_act_detail;
    private TextView      mTv_org_titel;
    private ImageView     mImageShare;
    private TextView      mTv_time_beizhu;
    private long          mBetween;
    private int    mRequestCode   = 10069;//启动评论页
    private String ImageUrlOrPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521093612719&di=6e6a175f275d0efc26956315d4b07582&imgtype=0&src=http%3A%2F%2Fnews.sznews.com%2Fimages%2Fattachement%2Fjpg%2Fsite3%2F20171013%2FIMG6c0b840b679945782523231.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows_detail_new);
        Intent intent = getIntent();
        mShowId = intent.getStringExtra("filmRoom");
        mShowImgUrl = NetConfig.IMG;
        initDetailUrl();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mImgUrlList == null) {
            mImgUrlList = new ArrayList();
        } else {
            mImgUrlList.clear();
        }
        if (mShowList == null) {
            mShowList = new ArrayList();
        } else {
            mShowList.clear();
        }
        initData();
    }

    private void initDetailUrl() {
        int modelId = getIntent().getIntExtra("modelId", 0);
        switch (modelId) {
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
            case 8:
                mShowsDetailUrl = NetConfig.COMMUNITY_DETAIL;
                break;
            case 10:
                mShowsDetailUrl = getIntent().getStringExtra("collectActUrl");
                mShowImgUrl = getIntent().getStringExtra("showImgUrl");
                mshowUrlAddress = getIntent().getStringExtra("url_address");
                break;
        }
    }

    private void initView() {
        mScrollView_act_detail = (ScrollView) findViewById(R.id.scrollView_act_detail);
        mImg_no_intnet = (ImageView) findViewById(R.id.img_no_intnet);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mImg_show_picture = (ImageView) findViewById(R.id.img_show_picture);
        mTv_label_1 = (TextView) findViewById(R.id.tv_label_1);
        mTv_label_2 = (TextView) findViewById(R.id.tv_label_2);
        mTv_label_3 = (TextView) findViewById(R.id.tv_label_3);
        mTv_pay_kind = (TextView) findViewById(R.id.tv_pay_kind);
        mTv_ticket = (TextView) findViewById(R.id.tv_ticket);
        mTv_show_name = (TextView) findViewById(R.id.tv_show_name);
        mTv_place = (TextView) findViewById(R.id.tv_place);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mTv_time_beizhu = (TextView) findViewById(R.id.tv_time_beizhu);
        mTv_time_detail = (TextView) findViewById(R.id.tv_time_detail);
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
        mTv_act_detail = (TextView) findViewById(R.id.tv_act_detail);//活动详情标题
        mTv_avt_org = (TextView) findViewById(R.id.tv_avt_org);//活动单位
        mWeb_detail = (WebView) findViewById(R.id.web_detail);
        mTv_warning = (TextView) findViewById(R.id.tv_warning);
        mTv_org_titel = (TextView) findViewById(R.id.tv_org_titel);
        mTv_organisers = (TextView) findViewById(R.id.tv_organisers);
        //        mTv_undertake = (TextView) findViewById(R.id.tv_undertake);
        //        mTv_show_unit = (TextView) findViewById(R.id.tv_show_unit);
        mTv_number_zan = (TextView) findViewById(R.id.tv_number_zan);
        mTv_comment = (TextView) findViewById(R.id.tv_comment);
        mImageShare = (ImageView) findViewById(R.id.img_share);
        //还有一些待找。
        mTv_talk = (ImageView) findViewById(R.id.img_talk);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLv_talk = (MyListView) findViewById(R.id.lv_talk);

        mImg_dianzan = (ImageView) findViewById(R.id.img_dianzan);
        mImg_shoucang = (ImageView) findViewById(R.id.img_shoucang);
        mBt_appointment = (Button) findViewById(R.id.bt_appointment);
        mRelative_to_mapview = (RelativeLayout) findViewById(R.id.relative_to_mapview);
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
        getOrSetUserID();
        String showId = mShowId;
        RequestParams params = new RequestParams();
        params.put("member_id", UserID);
        params.put("id", showId);

        if (getIntent().getIntExtra("modelId", 0) == 0) {
            params.put("activity_id", getIntent().getStringExtra("filmRoom"));
            params.put("activity_name", getIntent().getStringExtra("activity_name"));
        }

        HttpUtil.get(mShowsDetailUrl, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ShowsDetailActivityNew.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mImg_no_intnet.setVisibility(View.GONE);
                Gson gson = new Gson();
                ShowDetailInfo showDetailInfo = gson.fromJson(response.toString(), ShowDetailInfo.class);
                final ShowDetailInfo.ArrBean arr = showDetailInfo.getArr();
                mInfo = arr;
                neddAarr = arr;
                //获取点赞信息和评论列表
                getZanAndTalkData();
                // TODO:填充数据
                double lng = arr.getLng();
                double lat = arr.getLat();
                if (lng == 0 || lat == 0) {

                } else {
                    mLng = lng;
                    mLat = lat;
                }
                String newpic = mShowImgUrl + arr.getNewpic();
                ImageLoaderUtil.displayImageIcon(newpic, mImg_show_picture);
                String label1 = arr.getLabel1();
                String label2 = arr.getLabel2();
                String label3 = arr.getLabel3();
                if (label1.equals("")) {
                    mTv_label_1.setVisibility(View.GONE);
                } else {
                    mTv_label_1.setText(label1);
                }
                if (label2.equals("")) {
                    mTv_label_2.setVisibility(View.GONE);
                } else {
                    mTv_label_2.setText(label2);
                }
                if (label3.equals("")) {
                    mTv_label_3.setVisibility(View.GONE);
                } else {
                    mTv_label_3.setText(label3);
                }
                float price = arr.getPrice();
                if (price == 0) {
                    mTv_pay_kind.setText("免费");
                } else {
                    mTv_pay_kind.setText("" + price + "元/每人");
                }
                int remain_ticket = arr.getRemain_ticket();
                mTv_ticket.setText("余票" + remain_ticket + "张");
                String fname = arr.getFname();
                mTv_show_name.setText(fname);
                String faddress = arr.getFaddress();
                mTv_place.setText(faddress);
                //活动日期//活动具体时间
                String fdate = arr.getFdate();
                String end_date = arr.getEnd_date();
                //截取日期
                String substringDate1 = "";
                String substringDate2 = "";
                //截取具体时间
                String substringTime1 = "";
                String substringTime2 = "";
                if (fdate != null && !fdate.equals("")) {
                    substringDate1 = fdate.substring(0, 10);
                }
                if (end_date == null || end_date.equals("")) {
                    mTv_time.setText(substringDate1);
                } else {
                    substringDate2 = end_date.substring(0, 10);
                    if (substringDate1.equals(substringDate2)) {
                        mTv_time.setText(substringDate1);
                    } else {
                        mTv_time.setText(substringDate1 + "—" + substringDate2);
                    }
                }
                mTv_time_detail.setVisibility(View.GONE);
                //时间备注
                String time_beizhu = arr.getTime_beizhu();
                if (time_beizhu != null && !"".equals(time_beizhu.trim())) {
                    mTv_time_beizhu.setVisibility(View.VISIBLE);
                    mTv_time_beizhu.setText(time_beizhu);
                } else {
                    mTv_time_detail.setVisibility(View.VISIBLE);
                    String substringDate = "";
                    try {
                        substringDate = end_date.substring(11, 16);
                    } catch (Exception e) {
                    }
                    mTv_time_detail.setText(substringDate);
                }
                //电话
                String fmobile = arr.getFmobile();
                mTv_phone.setText(fmobile);
                //活动详情
                String fdetail = arr.getFdetail();
                mWeb_detail.loadDataWithBaseURL("", getNewContent(fdetail), "text/html", "utf-8", "");
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
                String fprompt = arr.getFprompt();//温馨提示
                //                String clearString = fprompt.replaceAll("<p>", "");
                Spanned conditionsSpanned = Html.fromHtml(fprompt);
                mTv_warning.setText(conditionsSpanned);
                String fmain_company = arr.getFmain_company().trim();
                if (fmain_company.equals("")) {
                    mTv_organisers.setVisibility(View.GONE);
                } else {
                    mTv_organisers.setText("主 办 方 ：" + fmain_company);
                }

                //                mTv_undertake.setText("");
                //                mTv_show_unit.setText("");
                mTv_number_zan.setText("");
                showName = arr.getTb_name();
                int isZan = arr.getIsZan();
                if (isZan == 0) {
                    mImg_dianzan.setImageResource(R.drawable.ic_zan_select1);
                    isDianZan = false;
                } else {
                    mImg_dianzan.setImageResource(R.drawable.ic_zan_select2);
                    isDianZan = true;
                }
                int isFavourite = arr.getIsFavourite();
                if (isFavourite == 0) {
                    mImg_shoucang.setImageResource(R.drawable.ic_star_selected1);
                    isShouCang = false;
                } else {
                    mImg_shoucang.setImageResource(R.drawable.ic_star_selected2);
                    isShouCang = true;
                }
                if ("".equals(arr.getIs_yuyue())) {
                    arr.setIs_yuyue("0");
                }
                int is_yuyue = Integer.valueOf(arr.getIs_yuyue());
                if (is_yuyue == 1) {
                    if (remain_ticket == 0) {
                        mBt_appointment.setText("门票已被抢空");
                    } else {
                        mBt_appointment.setText("立即预约");
                        mBt_appointment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getOrSetUserID();
                                if (UserID.equals(NetConfig.UserID)) {
                                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent mintent = new Intent(getBaseContext(), ReservationShowActivity.class);
                                    mintent.putExtra("picUrl", mShowImgUrl + arr.getNewpic());
                                    if (10 == getIntent().getIntExtra("modelId", 0)) {
                                        mintent.putExtra("collectionUrl", mshowUrlAddress);
                                    } else {
                                        mintent.putExtra("collectionUrl", NetConfig.URL_HEAD_ADDRESS);
                                    }
                                    startActivity(mintent);
                                }
                            }
                        });
                    }
                } else if (is_yuyue == 0) {
                    mTv_ticket.setText("无需预约");
                    mBt_appointment.setText("直接前往");
                    mBt_appointment.setBackgroundColor(Color.parseColor("#9c9c9c"));
                }
                //活动时间结束不可预约
                String zc_end_time = end_date;//2018-02-22 10:55:24
                SimpleDateFormat dfs;
                if (zc_end_time.length() == 16) {//(zc_end_time.length() == 19)
                    dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                } else {
                    dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                long endTime = 0;
                try {
                    java.util.Date end = dfs.parse(zc_end_time);
                    endTime = end.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long currentTimeMillis = System.currentTimeMillis();
                mBetween = (endTime - currentTimeMillis) / 1000;
                if (mBetween <= 0) {
                    mTv_ticket.setVisibility(View.GONE);
                    mBt_appointment.setText("活动已结束");
                    mBt_appointment.setBackgroundColor(Color.parseColor("#9c9c9c"));
                    mBt_appointment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //不做处理
                        }
                    });
                }
                //设置一些监听点击事件
                setListener();
                //点赞用户头像
                LinearLayoutManager LayoutManagerHeadPic = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
                mRecyclerView.setLayoutManager(LayoutManagerHeadPic);
                myRecAdapter = new MyRecAdapter(mImgUrlList);
                mRecyclerView.setAdapter(myRecAdapter);
                //用户评论
                mMyTalkAdapter = new MyTalkAdapter(mShowList);
                mLv_talk.setAdapter(mMyTalkAdapter);
            }
        });
    }

    private void getZanAndTalkData() {
        String businessList = mshowUrlAddress + "zanList";
        RequestParams params = new RequestParams();
        params.put("Activity_id", mShowId);
        HttpUtil.get(businessList, params, new HttpUtil.JsonHttpResponseUtil() {
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
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getBaseContext());
                    return;
                }
                Gson gson = new Gson();
                AdmireInfo venueDetailsInfo = gson.fromJson(response.toString(), AdmireInfo.class);
                int countZan = venueDetailsInfo.getCountZan();
                List<AdmireInfo.ArrBean> arr = venueDetailsInfo.getArr();
                mTv_number_zan.setText("共" + countZan + "人赞过");
                if (mImgUrlList != null) {
                    mImgUrlList.clear();
                }
                for (int i = 0; i < arr.size(); i++) {
                    mImgUrlList.add(arr.get(i).getHeadpic());
                }
                if (mImgUrlList.size() == 0) {
                    mTv_number_zan.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mTv_number_zan.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    myRecAdapter.notifyDataSetChanged();
                }
                //获取评论列表
                getTalkData();
            }
        });
    }

    private void getTalkData() {
        String businessList = mshowUrlAddress + "commentList";
        RequestParams params = new RequestParams();
        params.put("Activity_id", mShowId);
        HttpUtil.get(businessList, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //                hideProgressDialog();
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
                CommentInfo venueDetailsInfo = gson.fromJson(response.toString(), CommentInfo.class);
                List<CommentInfo.ArrBean> arr = venueDetailsInfo.getArr();
                mTv_comment.setText("共" + arr.size() + "条评论");
                if (arr.size() < 20) {
                }
                if (mShowList != null) {
                    mShowList.clear();
                }
                for (int i = 0; i < arr.size(); i++) {
                    mShowList.add(arr.get(i));
                }
                if (mShowList.size() == 0) {
                    mTv_comment.setVisibility(View.GONE);
                    mLv_talk.setVisibility(View.GONE);
                } else {
                    mTv_comment.setVisibility(View.VISIBLE);
                    mLv_talk.setVisibility(View.VISIBLE);
                    mMyTalkAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setListener() {
        mRelative_to_mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapNavigationActivity.class);
                intent.putExtra("mLng", mLng);
                intent.putExtra("mLat", mLat);
                startActivity(intent);
            }
        });
        //点击电话，拨打
        mTv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTv_phone.getText().equals("")) {
                    ToastUtils.makeShortText("该活动没有电话咨询", getBaseContext());
                    return;
                }
                //弹出dialog
                AlertDialog dialog = new AlertDialog.Builder(ShowsDetailActivityNew.this).create();
                dialog.setIcon(R.drawable.icon_phone);      //设置图标
                dialog.setTitle("将为您自动跳转拨号页面");    //设置标题
                dialog.setCancelable(false);        //点击旁边不消失
                dialog.setMessage(mTv_phone.getText());
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mTv_phone.getText()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });//设置确定键
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "复制电话号码", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(mTv_phone.getText());
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//关闭对话框
                    }
                });
                dialog.show();
            }
        });
        //活动详情跳转
        mTv_act_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int top = mWeb_detail.getTop();
                mScrollView_act_detail.scrollTo(0, top);
            }
        });
        //活动单位跳转
        mTv_avt_org.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int top = mTv_org_titel.getTop();
                mScrollView_act_detail.scrollTo(0, top);
            }
        });
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mMyAdapter == null) {
            mMyAdapter = new MyRecAdapter(mImgUrlList);
            mRecyclerView.setAdapter(mMyAdapter);
        }

        mTv_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent mIntent = new Intent(getBaseContext(), SendCommentActivity.class);
                    mIntent.putExtra("showId", mShowId);
                    mIntent.putExtra("fromKind", "1");
                    mIntent.putExtra("Activity_name", neddAarr.getFname());
                    mIntent.putExtra("tb_name", neddAarr.getTb_name());
                    mIntent.putExtra("fmain_company", neddAarr.getFmain_company());
                    mIntent.putExtra("area_name", neddAarr.getArea_name());
                    mIntent.putExtra("url_address", mshowUrlAddress);
                    startActivityForResult(mIntent, mRequestCode);
                    //                    startActivity(mIntent);
                }
            }
        });
        mImg_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (isShouCang == false) {
                        String insertFavourite = mshowUrlAddress + "insertFavourite";
                        String fdate = neddAarr.getFdate();
                        String end_date = neddAarr.getEnd_date();
                        String subFdate = fdate;
                        String subEnd_date = end_date;
                        if (fdate.length() >= 10) {
                            subFdate = fdate.substring(0, 11);
                        }
                        if (end_date.length() >= 10) {
                            subEnd_date = end_date.substring(0, 11);
                        }
                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", neddAarr.getId());
                        params.put("Activity_name", neddAarr.getFname());
                        params.put("area_code", "");
                        params.put("area_name", neddAarr.getArea_name());
                        params.put("pic_address", neddAarr.getNewpic());
                        params.put("tb_name", neddAarr.getTb_name());
                        params.put("faddress", neddAarr.getFaddress());
                        params.put("begin_time", subFdate);
                        params.put("end_time", subEnd_date);
                        params.put("favourite_type", "1");
                        params.setUseJsonStreamer(true);
                        postToIntnet(insertFavourite, params, "收藏");
                    } else {
                        String cancelFavourite = mshowUrlAddress + "cancelFavourite";
                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", mShowId);
                        params.put("area_name", neddAarr.getArea_name());
                        params.setUseJsonStreamer(true);
                        //getSendToIntnet(cancelFavourite, params, "取消收藏");
                        postcancelFavToIntnet(cancelFavourite, params);
                    }
                }
            }
        });

        mImg_dianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (isDianZan == false) {
                        //点赞
                        String insertZan = mshowUrlAddress + "insertZan";
                        RequestParams params = new RequestParams();
                        params.put("userid", UserID);
                        params.put("Activity_id", mShowId);
                        params.put("tb_name", showName);
                        params.put("area_name", neddAarr.getArea_name());
                        params.put("ftype", "0");
                        params.put("Activity_name", neddAarr.getFname());
                        params.put("begin_time", neddAarr.getFdate());
                        params.put("pic_address", neddAarr.getNewpic());
                        params.setUseJsonStreamer(true);
                        postToIntnet(insertZan, params, "点赞");
                    } else {
                        //取消点赞
                        String cancelZan = mshowUrlAddress + "cancelZan";
                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", mShowId);
                        getSendToIntnet(cancelZan, params, "取消点赞");
                    }
                }
            }
        });
        mImageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    OnekeyShare oks = new OnekeyShare();
                    oks.setCallback(new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                            //分享完成后提交分享积分
                            sendIntnetShare();
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {
                            Toast.makeText(ShowsDetailActivityNew.this, "分享失败" + throwable, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {
                        }
                    });
                    //关闭sso授权
                    oks.disableSSOWhenAuthorize();
                    String url = "http://220.248.107.62:8084/whyapi/index.html?id=" + mShowId + "&tb_type=" + showName;
                    // title标题，微信、QQ和QQ空间等平台使用
                    oks.setTitle(mTv_show_name.getText() + "");
                    // titleUrl QQ和QQ空间跳转链接
                    oks.setTitleUrl(url);
                    // text是分享文本，所有平台都需要这个字段
                    oks.setText(mTv_show_name.getText() + "");
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    //                    oks.setImagePath("/sdcard/test.jpg");
                    if (ImageUrlOrPath != null && ImageUrlOrPath.contains("/sdcard/")) {
                        //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        oks.setImagePath(ImageUrlOrPath);
                    } else if (ImageUrlOrPath != null) {
                        oks.setImageUrl(ImageUrlOrPath); //网络地址
                    }
                    // url在微信、微博，Facebook等平台中使用
                    oks.setUrl(url);
                    // comment是我对这条分享的评论，仅在人人网使用
                    // oks.setComment("我是测试评论文本");
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite(getString(R.string.app_name));
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setSiteUrl(url);
                    oks.setDialogMode();
                    oks.setSilent(false);
                    // 启动分享GUI
                    oks.show(ShowsDetailActivityNew.this);
                }
            }
        });
    }

    //分享完后，
    private void sendIntnetShare() {
        RequestParams params = new RequestParams();
        params.put("user_id", UserID);
        params.put("activity_id", mShowId);
        HttpUtil.get(NetConfig.INSERT_SHARE, params, new HttpUtil.JsonHttpResponseUtil() {
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
                Gson gson = new Gson();
                ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                String message = clubDetailInfo.getMessage();
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postcancelFavToIntnet(String url, RequestParams params) {
        HttpUtil.post(url, params, new HttpUtil.JsonHttpResponseUtil() {
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
                    if (result.equals("2")) {
                        mImg_shoucang.setImageResource(R.drawable.ic_star_selected1);
                        isShouCang = false;
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getSendToIntnet(String url, RequestParams params, final String kind) {
        HttpUtil.get(url, params, new HttpUtil.JsonHttpResponseUtil() {
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
                        if (kind.equals("取消点赞")) {
                            mImg_dianzan.setImageResource(R.drawable.ic_zan_select1);
                            isDianZan = false;
                            getZanAndTalkData();
                        }
                        if (kind.equals("取消收藏")) {
                            mImg_shoucang.setImageResource(R.drawable.ic_star_selected1);
                            isShouCang = false;
                        }
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void postToIntnet(String url, RequestParams params, final String kind) {
        HttpUtil.post(url, params, new HttpUtil.JsonHttpResponseUtil() {
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
                    if (result.equals("2")||result.equals("3")) {
                        if (kind.equals("点赞")) {
                            mImg_dianzan.setImageResource(R.drawable.ic_zan_select2);
                            isDianZan = true;
                            getZanAndTalkData();
                        }
                        if (kind.equals("收藏")) {
                            mImg_shoucang.setImageResource(R.drawable.ic_star_selected2);
                            isShouCang = true;
                        }
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }

        Log.d("VACK", doc.toString());
        return doc.toString();
    }

    //场馆评论条目设配器
    class MyTalkAdapter extends BaseAdapter {
        List mList;

        MyTalkAdapter(ArrayList arrayList) {
            this.mList = arrayList;
        }

        @Override
        public int getCount() {
            return mList.size() > 20 ? 20 : mList.size();
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
            MyShowHolder myShowHolder;
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(), R.layout.list_item_talk, null);
                myShowHolder = new MyShowHolder();
                myShowHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
                myShowHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                myShowHolder.tv_speek_time = (TextView) convertView.findViewById(R.id.tv_speek_time);
                myShowHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
                myShowHolder.img_comment = (ImageView) convertView.findViewById(R.id.img_comment);
                convertView.setTag(myShowHolder);
            } else {
                myShowHolder = (MyShowHolder) convertView.getTag();
            }
            CommentInfo.ArrBean arrBean = (CommentInfo.ArrBean) mList.get(position);
            String headpic = arrBean.getHeadpic();
            String username = arrBean.getUsername();
            String comment_date = arrBean.getComment_date().substring(0, 19);
            String comment_content = arrBean.getComment_content();
            Spanned spanned = Html.fromHtml(comment_content);
            ImageLoaderUtil.displayImageRoundIcon(NetConfig.HEAD_IMG + headpic, myShowHolder.img_head);
            String comment_pic = arrBean.getComment_pic();
            if (comment_pic.equals("")) {
                myShowHolder.img_comment.setVisibility(View.GONE);
            } else {
                myShowHolder.img_comment.setVisibility(View.VISIBLE);
                ImageLoaderUtil.displayImageIcon(mShowImgUrl + "photo/" + comment_pic, myShowHolder.img_comment);
            }
            myShowHolder.tv_name.setText(username);
            myShowHolder.tv_speek_time.setText(comment_date);
            myShowHolder.tv_comment.setText(spanned);
            return convertView;
        }

        class MyShowHolder {
            ImageView img_head;//评论用户头像
            TextView  tv_name;//评论用户名
            TextView  tv_speek_time;//评论时间
            TextView  tv_comment;//评论内容
            ImageView img_comment;//评论图片
        }
    }

    public class MyRecAdapter extends RecyclerView.Adapter<MyRecAdapter.ViewHolder> {

        private List mData;

        public MyRecAdapter(List data) {
            this.mData = data;
        }

        public void updateData(ArrayList<String> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_zan_img, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            //TODO:
            if (mData != null && mData.size() > 0) {
                String imgurl = (String) mData.get(position);
                String newpic = NetConfig.HEAD_IMG + imgurl;
                ImageLoaderUtil.displayImageRoundIcon(newpic, holder.mImg);
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mImg; //显示头像图片

            public ViewHolder(View itemView) {
                super(itemView);
                mImg = (ImageView) itemView.findViewById(R.id.img_zanlog);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            getZanAndTalkData();
        }
    }
}
