package com.guangxi.culturecloud.activitys;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.fragment.MyPitureWallFragment;
import com.guangxi.culturecloud.fragment.MyVideoWallFragment;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/25 15:40
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ClubDetailsActivity extends BaseActivity {
    private ImageView mFlower;      //花朵的图片
    private ImageView mImg_remind; //提醒今日已浇花
    private ImageView mToShowMore; //显示更多的社团信息
    private boolean isWatered  = false;//记录是否已浇水
    private boolean isShowMore = false;//记录信息介绍是否展开
    private boolean isFollow   = false;//记录是否关注
    private TextView             mIntroduce;    //介绍社团的信息
    private Button               mShowPicture; //照片墙
    private Button               mShowVideo;  //视频展示
    private MyPitureWallFragment mMyPitureWallFragment;
    private MyVideoWallFragment  mMyVideoWallFragment;
    private ImageView            mIv_back;
    private Button               mFollow;
    private ImageView            mImg_club;
    private ImageView            mImg_record;
    private TextView             mTv_clubName;
    private TextView             mTv_label_1;
    private TextView             mTv_label_2;
    private TextView             mTv_label_3;
    private TextView             mName_authentication;
    private TextView             mCredentials_authentication;
    private TextView             mTv_club_member;
    private TextView             mTv_fans;
    private TextView             mTv_water_num;
    private ProgressDialog       progressDialog;
    private List<ClubDetailInfo> mList; //存储社团信息对象
    private String UserID         = NetConfig.UserID;
    private String mURLHeadString = NetConfig.IMG;
    private       List<ClubDetailInfo.ArrBean.ListPicBean> mListPic;
    private       ImageView                                mImg_nonet;
    public static ArrayList                                mSavePicUrlList;
    private       String                                   mClubId; //传递过来的club详情的id
    private       int                                      mJiaohua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_details);

        Intent intent = getIntent();
        mClubId = intent.getStringExtra("clubId");
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        mImg_nonet = (ImageView) findViewById(R.id.img_nonet);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        //社团图片
        mImg_club = (ImageView) findViewById(R.id.img_club);
        //社团logo
        mImg_record = (ImageView) findViewById(R.id.img_record);
        //社团名称
        mTv_clubName = (TextView) findViewById(R.id.tv_clubName);
        //社团标签1
        mTv_label_1 = (TextView) findViewById(R.id.tv_label_1);
        //社团标签2
        mTv_label_2 = (TextView) findViewById(R.id.tv_label_2);
        mTv_label_3 = (TextView) findViewById(R.id.tv_label_3);
        //实名认证
        mName_authentication = (TextView) findViewById(R.id.name_authentication);
        //资质认证
        mCredentials_authentication = (TextView) findViewById(R.id.credentials_authentication);
        mFollow = (Button) findViewById(R.id.bt_follow);        //是否关注
        mIntroduce = (TextView) findViewById(R.id.tv_introduce); //社团介绍信息
        mToShowMore = (ImageView) findViewById(R.id.img_up_down); //显示更多箭头
        //社团成员数
        mTv_club_member = (TextView) findViewById(R.id.tv_club_member);
        //社团粉丝
        mTv_fans = (TextView) findViewById(R.id.tv_fans);
        mFlower = (ImageView) findViewById(R.id.flower);    //浇花的图片
        //浇花数
        mTv_water_num = (TextView) findViewById(R.id.tv_water_num);
        mImg_remind = (ImageView) findViewById(R.id.img_remind);    //提示是否浇花

        mShowPicture = (Button) findViewById(R.id.tv_show_picture);//社团图片墙按钮
        mShowVideo = (Button) findViewById(R.id.tv_show_video); //社团视频按钮

    }

    private void initData() {
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }

        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取网络数据展示
        getIntnetData();
    }

    private void getIntnetData() {
        String url = NetConfig.LOVE_SOCIETY_INFO;
        String ClubId = mClubId;
        String UserId = UserID;
        RequestParams params = new RequestParams();
        params.put("id", ClubId);
        params.put("member_id", UserId);
        HttpUtil.get(url, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ClubDetailsActivity.this, "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                ClubDetailInfo clubDetailInfo;
                Gson gson = new Gson();
                clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                ClubDetailInfo.ArrBean arr = clubDetailInfo.getArr();
                String first_img = arr.getNewfirst_img(); //社团展示照地址
                String finalfirst = mURLHeadString + first_img; //拼接url地址
                String logo = arr.getNewlogo();
                String finallogo = mURLHeadString + logo;
                String title = arr.getFname();//社团名称
                String label1 = arr.getLabel1();
                String label2 = arr.getLabel2();
                String label3 = arr.getLabel3();
                int smrz = arr.getSmrz();
                int zzrz = arr.getZzrz();
                int isFavourite = arr.getIsFavourite();
                String detail_title_sx = arr.getSociety_detail();
                int members = arr.getMembers();
                int sumFavourite = arr.getSumFavourite();
                int isJiaohua = arr.getIsJiaohua();
                mJiaohua = arr.getSumJiaohua();
                //TODO：获取照片墙数据
                mListPic = arr.getListPic();

                FragmentTransaction mFt = getFragmentManager().beginTransaction();
                mShowPicture.setBackgroundResource(R.drawable.bt_picture_chocie);
                mShowVideo.setBackgroundResource(R.drawable.bt_video_normol);
                if (mMyPitureWallFragment != null) {
                    mFt.remove(mMyPitureWallFragment);
                    mMyPitureWallFragment = null;
                }
                mMyPitureWallFragment = new MyPitureWallFragment();
                mFt.add(R.id.frame_picture, mMyPitureWallFragment, "mMyPitureWallFragment");
                //                mFt.show(mMyPitureWallFragment);
                mFt.commit();

                //填充数据
                ImageLoaderUtil.displayImageIcon(finalfirst, mImg_club);
                ImageLoaderUtil.displayImageIcon(finallogo, mImg_record);
                mTv_clubName.setText(title);

                if (label1.trim().equals("")) {
                    mTv_label_1.setVisibility(View.GONE);
                } else {
                    mTv_label_1.setText(label1);
                }
                if (label2.trim().equals("")) {
                    mTv_label_2.setVisibility(View.GONE);
                } else {
                    mTv_label_2.setText(label2);
                }
                if (label3.trim().equals("")) {
                    mTv_label_3.setVisibility(View.GONE);
                } else {
                    mTv_label_3.setText(label3);
                }

                if (smrz == 0) {
                    mName_authentication.setText("未实名认证");
                } else {
                    mName_authentication.setText("实名认证");
                }
                if (zzrz == 0) {
                    mCredentials_authentication.setText("未资质认证");
                } else {
                    mCredentials_authentication.setText("资质认证");
                }
                if (isFavourite == 0) {
                    mFollow.setText("关注");
                    isFollow = false;
                } else {
                    mFollow.setText("已关注");
                    isFollow = true;
                }
                Spanned spanned = Html.fromHtml(detail_title_sx);
                mIntroduce.setText(spanned);
                mTv_club_member.setText("" + members);
                mTv_fans.setText("" + sumFavourite);
                if (isJiaohua == 0) {
                    mFlower.setImageResource(R.drawable.flower_die);
                    isWatered = false;
                } else {
                    mFlower.setImageResource(R.drawable.flower_save);
                    isWatered = true;
                }
                mTv_water_num.setText("" + mJiaohua);
                //设置完数据，给view设置监听事件
                setListenerTep();
            }
        });
    }

    private void setListenerTep() {
        mImg_nonet.setVisibility(View.GONE);

        mFollow.setOnClickListener(new View.OnClickListener() {
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
                    UserID = userInfo.member_id;
                    if (isFollow == false) {
                        //发送关注请求
                        getCare();
                    } else {
                        //发送取消关注请求
                        sendCancelCare();
                    }
                }
            }
        });

        //点击照片墙，展示图片，最多展示6张，超6张，会在最后一张显示剩余未展示的数
        mShowPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction mFt = fragmentManager.beginTransaction();
                mShowPicture.setBackgroundResource(R.drawable.bt_picture_chocie);
                mShowPicture.setTextColor(Color.WHITE);
                mShowVideo.setBackgroundResource(R.drawable.bt_video_normol);
                mShowVideo.setTextColor(Color.BLACK);
                if (mMyPitureWallFragment == null) {
                    mFt.remove(mMyPitureWallFragment);
                    mMyPitureWallFragment = new MyPitureWallFragment();
                    mFt.add(R.id.frame_picture, mMyPitureWallFragment, "mMyPitureWallFragment");
                } else {
                    Fragment mMyVideoWallFragment = fragmentManager.findFragmentByTag("MyVideoWallFragment");
                    if (mMyVideoWallFragment != null) {
                        hideFragment(mMyVideoWallFragment);
                    }
                    mFt.show(mMyPitureWallFragment);
                }
                mFt.commit();
            }
        });
        //点击社团视频，展示视频布局
        mShowVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFragment(mMyPitureWallFragment);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction mFt = fragmentManager.beginTransaction();
                mShowPicture.setBackgroundResource(R.drawable.bt_picture_normol);
                mShowPicture.setTextColor(Color.BLACK);
                mShowVideo.setBackgroundResource(R.drawable.bt_video_chocie);
                mShowVideo.setTextColor(Color.WHITE);
                if (mMyVideoWallFragment == null) {
                    mMyVideoWallFragment = new MyVideoWallFragment();
                    mFt.add(R.id.frame_picture, mMyVideoWallFragment, "MyVideoWallFragment");
                } else {
                    mFt.show(mMyVideoWallFragment);
                }
                //提交事务
                mFt.commit();
            }
        });
        mToShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断当前是否显示更多
                if (isShowMore == false) {
                    mIntroduce.setMaxLines(100);
                    mToShowMore.setImageResource(R.drawable.arrow_up);
                    isShowMore = !isShowMore;
                } else {
                    mIntroduce.setMaxLines(3);
                    mToShowMore.setImageResource(R.drawable.arrow_down);
                    isShowMore = !isShowMore;
                }
            }
        });

        mFlower.setOnClickListener(new View.OnClickListener() {
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
                    //是否已经浇水
                    if (isWatered == false) {
                        //浇水
                        giveWater();
                    } else {
                        Toast.makeText(getBaseContext(), "今日已浇花", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mImg_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出popupwindow提示用户今日已浇花
                final PopupWindow popupWindow = new PopupWindow(getBaseContext());
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(LayoutInflater.from(getBaseContext()).inflate(R.layout.popup_remind, null));
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                //显示popupwindow
                //                popupWindow.showAsDropDown(mImg_remind, -5, 0);
                //获取点击View的坐标
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                //显示在左方
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - popupWindow.getWidth(), location[1] - v.getHeight());
            }
        });
        //照片墙/视频墙
        //        mMyPitureWallFragment = new MyPitureWallFragment();
        //        mMyVideoWallFragment = new MyVideoWallFragment();
        //        FragmentTransaction mFt = getFragmentManager().beginTransaction();
        //进行fragment操作:
        //        mFt.add(R.id.frame_picture, mMyPitureWallFragment);
        //        //提交事务
        //        mFt.commit();
    }

    private void hideFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (fragment != null && !fragment.isHidden()) {
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
    }

    private void giveWater() {
        String jiaohua = NetConfig.INSERT_JIAOHUA;
        String ClubID = mClubId;
        RequestParams params = new RequestParams();
        params.put("userid", UserID);
        params.put("lovesociety_id", ClubID);
        params.setUseJsonStreamer(true);
        HttpUtil.post(jiaohua, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ClubDetailsActivity.this, "正在浇花，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    ClubDetailInfo clubDetailInfo;
                    Gson gson = new Gson();
                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    String result = clubDetailInfo.getResult();
                    if (result.equals("2")) {
                        mTv_water_num.setText("" + (mJiaohua + 1));
                        mFlower.setImageResource(R.drawable.flower_save);
                        isWatered = true;
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendCancelCare() {
        String cancle = NetConfig.CANCEL_FAVOURITE;
        String ClubID = mClubId;

        RequestParams params = new RequestParams();
        params.put("member_id", UserID);
        params.put("Activity_id", ClubID);
        HttpUtil.get(cancle, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ClubDetailsActivity.this, "正在取消关注，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    ClubDetailInfo clubDetailInfo;
                    Gson gson = new Gson();
                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    String result = clubDetailInfo.getResult();
                    mFollow.setText("关注");
                    isFollow = !isFollow;
                    String text = (String) mTv_fans.getText();
                    int integer = Integer.parseInt(text);
                    if (result.equals("2")) {
                        mTv_fans.setText("" + (integer - 1));
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getCare() {
        String addFav = NetConfig.INSERTOTHERFAVOURITE;
        String ClubID = mClubId;
        String CulbName = String.valueOf(mTv_clubName.getText());
        RequestParams params = new RequestParams();
        params.put("member_id", UserID);
        params.put("Activity_id", ClubID);
        params.put("Activity_name", CulbName);
        params.setUseJsonStreamer(true);
        HttpUtil.post(addFav, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ClubDetailsActivity.this, "正在提交请求，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    Gson gson = new Gson();
                    ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    String result = clubDetailInfo.getResult();
                    String text = (String) mTv_fans.getText();
                    mFollow.setText("已关注");
                    isFollow = !isFollow;
                    int integer = Integer.parseInt(text);
                    if (result.equals("2")) {
                        mTv_fans.setText("" + (integer + 1));
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List getPicList() {
        return mListPic;
    }
}
