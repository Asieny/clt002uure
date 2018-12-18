package com.guangxi.culturecloud.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.CultureCrowdFundingInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.views.TimeTextView;
import com.jarvis.mytaobao.home.MapNavigationActivity;
import com.javis.ab.view.AbSlidingPlayView;
import com.loopj.android.http.RequestParams;
import com.onekeyshare.OnekeyShare;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.com.mytest.framework.AbsActivity;
import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cz.msebera.android.httpclient.Header;

/**
 * 文化众筹详情
 */
public class CultureCrowdFundingDetialActivity extends AbsActivity {

    @InjectView(R.id.iv_back)
    ImageView         ivBack;
    @InjectView(R.id.my_title)
    TextView          myTitle;
    //    @InjectView(R.id.tv_create_account)
    //    TextView          tvCreateAccount;
    @InjectView(R.id.viewPager_menu)
    AbSlidingPlayView viewPager;
    @InjectView(R.id.lable1)
    TextView          lable1;
    @InjectView(R.id.lable2)
    TextView          lable2;
    @InjectView(R.id.lable3)
    TextView          lable3;
    @InjectView(R.id.title)
    TextView          title;
    @InjectView(R.id.release_subject)
    TextView          releaseSubject;
    @InjectView(R.id.lay1)
    LinearLayout      lay1;
    @InjectView(R.id.line)
    View              line;
    @InjectView(R.id.percent)
    TextView          percent;
    @InjectView(R.id.number)
    TextView          number;
    @InjectView(R.id.sy_time)
    TimeTextView      syTime;
    @InjectView(R.id.lay2)
    LinearLayout      lay2;
    @InjectView(R.id.line2)
    View              line2;
    @InjectView(R.id.zc_time)
    TextView          zcTime;
    @InjectView(R.id.activity_time)
    TextView          activityTime;
    @InjectView(R.id.zc_contact)
    TextView          zcContact;
    @InjectView(R.id.zc_area)
    TextView          zcArea;
    @InjectView(R.id.zc_address)
    TextView          zcAddress;
    @InjectView(R.id.zc_introduce)
    TextView          zcIntroduce;
    @InjectView(R.id.zc_remark)
    TextView          zcRemark;
    @InjectView(R.id.repay_remark)
    TextView          repayRemark;
    @InjectView(R.id.warning_remark)
    TextView          warningRemark;
    @InjectView(R.id.lay3)
    LinearLayout      lay3;
    @InjectView(R.id.img_dianzan)
    ImageView         imgDianzan;
    @InjectView(R.id.img_shoucang)
    ImageView         imgShoucang;
    @InjectView(R.id.bt_appointment)
    Button            btAppointment;
    @InjectView(R.id.img_talk)
    ImageView         imgTalk;
    @InjectView(R.id.bt_share)
    ImageView         btShare;
    /**
     * 存储首页轮播的界面
     */
    private ArrayList<View> allListView;


    private String id;
    private String state;

    private UserInfo                userInfo;
    private CultureCrowdFundingInfo firstInfo;
    /**
     * 首页轮播的界面的资源
     */
    private ArrayList<String>       imglist;

    private double mLng = 120.99037;//经度
    private double mLat = 32.079313;//纬度

    private String  UserID     = NetConfig.UserID;
    private boolean isDianZan  = false;
    private boolean isShouCang = false;
    private long   mBetween;
    private String mShowId, showName;
    private String url_address;//众筹的服务器地址
    private String get_img_address;
    private String img_address;//单个图片在副服务器地址
    private String ImageUrlOrPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521093612719&di=6e6a175f275d0efc26956315d4b07582&imgtype=0&src=http%3A%2F%2Fnews.sznews.com%2Fimages%2Fattachement%2Fjpg%2Fsite3%2F20171013%2FIMG6c0b840b679945782523231.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture_crowdfunding_detial);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        state = intent.getStringExtra("state");
        url_address = intent.getStringExtra("url_address");
        get_img_address = intent.getStringExtra("get_img_address");
        userInfo = SPref.getObject(this, UserInfo.class, "userinfo");
        initView();
    }

    private void initView() {
        //设置播放方式为顺序播放
        viewPager.setPlayType(1);
        //设置播放间隔时间
        viewPager.setSleepTime(3000);
        getSearchCrowdfundingInfo();
    }

    //点击事件
    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        zcAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MapNavigationActivity.class);
                intent.putExtra("mLng", mLng);
                intent.putExtra("mLat", mLat);
                startActivity(intent);
            }
        });

        imgTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent mIntent = new Intent(getBaseContext(), SendCommentActivity.class);
                    mIntent.putExtra("showId", mShowId);
                    mIntent.putExtra("fromKind", "3");
                    mIntent.putExtra("Activity_name", firstInfo.fname);
                    mIntent.putExtra("tb_name", "");
                    mIntent.putExtra("fmain_company", "");
                    mIntent.putExtra("area_name", firstInfo.activity_area);
                    mIntent.putExtra("url_address", url_address);
                    startActivity(mIntent);
                }
            }
        });
        imgShoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (isShouCang == false) {
                        String insertFavourite = url_address + "insertOtherFavourite";
                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", mShowId);
                        params.put("Activity_name", showName);
                        params.setUseJsonStreamer(true);
                        postToIntnet(insertFavourite, params, "收藏");
                    } else {
                        String cancelZan = url_address + "cancelOtherFavourite";
                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", mShowId);
                        getSendToIntnet(cancelZan, params, "取消收藏");
                    }
                }
            }
        });

        imgDianzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (isDianZan == false) {
                        //点赞
                        String insertZan = url_address + "insertZan";
                        RequestParams params = new RequestParams();
                        params.put("userid", UserID);
                        params.put("Activity_id", mShowId);
                        params.put("tb_name", "z_Crowd_funding");
                        params.put("area_name", firstInfo.area_name);
                        params.put("ftype", "2");
                        params.put("Activity_name", firstInfo.fname);
                        params.put("begin_time", firstInfo.zc_begin_time);
                        String replace = "";
                        if (!firstInfo.newpic.equals("")) {
                            replace = firstInfo.newpic.split(",")[0].replace("\\", "/");
                        }
                        params.put("pic_address", replace);
                        params.setUseJsonStreamer(true);
                        postToIntnet(insertZan, params, "点赞");
                    } else {
                        //取消点赞
                        imgDianzan.setImageResource(R.drawable.wff_zan);
                        String cancelZan = url_address + "cancelZan";
                        RequestParams params = new RequestParams();
                        params.put("member_id", UserID);
                        params.put("Activity_id", mShowId);
                        params.put("tb_name", firstInfo.tb_name);
                        getSendToIntnet(cancelZan, params, "取消点赞");
                    }
                }
            }
        });
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
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
                            imgDianzan.setImageResource(R.drawable.wff_zan);
                            isDianZan = !isDianZan;
                        }
                        if (kind.equals("取消收藏")) {
                            imgShoucang.setImageResource(R.drawable.wff_collection);
                            isShouCang = !isShouCang;
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
                    if (result.equals("2")) {
                        if (kind.equals("点赞")) {
                            imgDianzan.setImageResource(R.drawable.wff_zan_after);
                            isDianZan = !isDianZan;
                        }
                        if (kind.equals("收藏")) {
                            imgShoucang.setImageResource(R.drawable.wff_collection_after);
                            isShouCang = !isShouCang;
                        }
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //分享
    private void showShare() {
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
                    Toast.makeText(CultureCrowdFundingDetialActivity.this, "分享失败" + throwable, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            //关闭sso授权
            oks.disableSSOWhenAuthorize();
            String url = "http://220.248.107.62:8084/whyapi/index.html?id=" + mShowId + "&tb_type=" + showName;
            // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
            oks.setTitle(firstInfo.fname);
            // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
            oks.setTitleUrl(url);
            // text是分享文本，所有平台都需要这个字段
            oks.setText(firstInfo.fname);
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            if (ImageUrlOrPath != null && ImageUrlOrPath.contains("/sdcard/")) {
                //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                oks.setImagePath(ImageUrlOrPath);
            } else if (ImageUrlOrPath != null) {
                oks.setImageUrl(ImageUrlOrPath); //网络地址
            }
            //            //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
            //            oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(url);
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            //            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(url);
            oks.setDialogMode();
            oks.setSilent(false);
            // 启动分享GUI
            oks.show(this);
        }
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

    //初始化数据
    private void setData(final CultureCrowdFundingInfo firstInfo) {
        if (firstInfo.pic.split(",").length > 1) {
            imglist = new ArrayList<String>();
            for (int i = 0; i < firstInfo.pic.split(",").length; i++) {
                imglist.add(get_img_address + firstInfo.pic.split(",")[i].replace("\\", "/"));
            }
        } else {
            imglist = new ArrayList<String>();
            imglist.add(get_img_address + firstInfo.pic.replace("\\", "/"));
        }

        if (allListView != null) {
            allListView.clear();
            allListView = null;
        }
        allListView = new ArrayList<View>();

        for (int i = 0; i < imglist.size(); i++) {
            //导入ViewPager的布局
            View view = LayoutInflater.from(CultureCrowdFundingDetialActivity.this).inflate(R.layout.pic_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
            ImageLoaderUtil.displayImageIcon(imglist.get(i), imageView);
            //                    imageView.setImageResource(resId[i]);
            allListView.add(view);
        }


        viewPager.addViews(allListView);
        //开始轮播
        viewPager.startPlay();


        if (!firstInfo.label1.equals("")) {
            lable1.setVisibility(View.VISIBLE);
            lable1.setText(firstInfo.label1);
        } else {
            lable1.setVisibility(View.GONE);
        }
        if (!firstInfo.label2.equals("")) {
            lable2.setVisibility(View.VISIBLE);
            lable2.setText(firstInfo.label2);
        } else {
            lable2.setVisibility(View.GONE);
        }
        if (!firstInfo.label3.equals("")) {
            lable3.setVisibility(View.VISIBLE);
            lable3.setText(firstInfo.label3);
        } else {
            lable3.setVisibility(View.GONE);
        }
        myTitle.setText(firstInfo.fname);
        title.setText(firstInfo.fname);
        releaseSubject.setText("发布主体:" + firstInfo.release_subject);
        zcTime.setText(firstInfo.zc_begin_time + "至" + firstInfo.zc_end_time);
        activityTime.setText(firstInfo.activity_time);
        zcContact.setText(firstInfo.zc_contact);
        zcArea.setText(firstInfo.activity_area);
        zcAddress.setText(firstInfo.activity_detail_address);
        percent.setText(firstInfo.fpercent);
        number.setText(firstInfo.raised_number + "份");
        //计算距离时间
        String zc_end_time = firstInfo.zc_end_time;//2018-02-22 10:55:24
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long endTime = 0;
        try {
            java.util.Date end = dfs.parse(zc_end_time);
            endTime = end.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        syTime.setTimes(endTime);
        //        long currentTimeMillis = System.currentTimeMillis();
        //        //计算时间差，秒
        //        mBetween = (endTime - currentTimeMillis) / 1000;
        //        if (mBetween > 0) {
        //            int day1 = (int) (mBetween / (24 * 3600));
        //            int hour1 = (int) (mBetween % (24 * 3600) / 3600);
        //            String hourtime = "";
        //            if (hour1 < 9) {
        //                hourtime = "0" + String.valueOf(hour1);
        //            } else {
        //                hourtime = String.valueOf(hour1);
        //            }
        //            int minute1 = (int) (mBetween % (24 * 3600) % 3600 / 60);
        //            String minTime = "";
        //            if (minute1 < 9) {
        //                minTime = "0" + String.valueOf(minute1);
        //            } else {
        //                minTime = String.valueOf(minute1);
        //            }
        //            int second1 = (int) (mBetween % (24 * 3600) % 3600 % 60 % 60);
        //            String sceTime = "";
        //            if (second1 < 9) {
        //                sceTime = "0" + String.valueOf(second1);
        //            } else {
        //                sceTime = String.valueOf(second1);
        //            }
        //            String time = day1 + "天" + hourtime + ":" + minTime + ":" + sceTime;
        //            syTime.setText(time);
        //        } else {
        //            syTime.setText("已经结束");
        //        }

        String zc_introduceString = firstInfo.zc_introduce.replaceAll("<p>", "");
        Spanned zc_introduceSpanned = Html.fromHtml(zc_introduceString);
        zcIntroduce.setText(zc_introduceSpanned);


        String zc_remarkString = firstInfo.zc_remark.replaceAll("<p>", "");
        Spanned zc_remarkSpanned = Html.fromHtml(zc_remarkString);
        zcRemark.setText(zc_remarkSpanned);

        String repay_remarkString = firstInfo.repay_remark.replaceAll("<p>", "");
        Spanned repay_remarkSpanned = Html.fromHtml(repay_remarkString);
        repayRemark.setText(repay_remarkSpanned);

        String warning_remarkString = firstInfo.warning_remark.replaceAll("<p>", "");
        Spanned warning_remarkSpanned = Html.fromHtml(warning_remarkString);
        warningRemark.setText(warning_remarkSpanned);
        showName = firstInfo.fname;
        mShowId = firstInfo.id;
        int isFavourite = firstInfo.isFavourite;
        if (isFavourite == 0) {
            isShouCang = false;
            imgShoucang.setImageResource(R.drawable.wff_collection);
        } else {
            isShouCang = true;
            imgShoucang.setImageResource(R.drawable.wff_collection_after);
        }
        int isZan = firstInfo.isZan;
        if (isZan == 0) {
            isDianZan = false;
            imgDianzan.setImageResource(R.drawable.wff_zan);
        } else {
            isDianZan = true;
            imgDianzan.setImageResource(R.drawable.wff_zan_after);
        }
        int remain_number = firstInfo.remain_number;
        img_address = firstInfo.pic.split(",")[0].replace("\\", "/");
        String state = String.valueOf(syTime.getText());
        if (state.equals("已经结束")) {
            btAppointment.setText("众筹已结束");
        }else {
            if (remain_number == 0) {
                btAppointment.setText("众筹已满");
            } else {
                btAppointment.setText("立即支持");
                btAppointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mintent = new Intent(getBaseContext(), CrowdFundingOrderActivity.class);
                        CrowdFundingOrderActivity.CrowdInfo = firstInfo;
                        mintent.putExtra("url_address", url_address);
                        mintent.putExtra("img_address", img_address);
                        startActivity(mintent);
                    }
                });
            }
        }
        setListener();
    }

    //获取详情
    private void getSearchCrowdfundingInfo() {
        String url = url_address + "searchCrowdfundingInfo?" + "member_id=" + userInfo.member_id + "&id=" + id;
        HttpUtil.get(url, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                JSONObject obj = jsonObject.optJSONObject("arr");
                firstInfo = new CultureCrowdFundingInfo().fromJson(obj.toString());
                setData(firstInfo);
            }
        });
    }
}
