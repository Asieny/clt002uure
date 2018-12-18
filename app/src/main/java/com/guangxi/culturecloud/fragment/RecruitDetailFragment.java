package com.guangxi.culturecloud.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.LoginActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.RecruitDetailInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.loopj.android.http.RequestParams;
import com.onekeyshare.OnekeyShare;

import org.json.JSONObject;

import java.util.HashMap;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2017/12/27 12:16
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class RecruitDetailFragment extends Fragment {
    private View           mRootView;
    private ImageView      mImg_club;        //社团展示图片
    private TextView       mClubName;        //社团名称
    private TextView       mRecruitmentDetail; //招募详情
    private TextView       mVolunteerRights; //志愿者权益
    private TextView       mClubIntroduce;   //社团介绍
    private Button         mBt_sign_up;      //我要申请
    private View           mIv_back;  //后退图标
    private TextView       mTitel;   //标题
    private RelativeLayout mRelative;
    private ProgressDialog progressDialog;
    private String mURLHeadString = NetConfig.IMG;
    private ImageView mImg_bg_white;
    private String UserID = NetConfig.UserID;
    private String    actID;
    private ImageView mImg_share;//分享
    private String mShowId        = "";
    private String ImageUrlOrPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521093612719&di=6e6a175f275d0efc26956315d4b07582&imgtype=0&src=http%3A%2F%2Fnews.sznews.com%2Fimages%2Fattachement%2Fjpg%2Fsite3%2F20171013%2FIMG6c0b840b679945782523231.jpg";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_reruit_detail, null);
        initView();
        initData();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        mRelative = (RelativeLayout) mRootView.findViewById(R.id.relative);
        mImg_share = (ImageView) mRootView.findViewById(R.id.img_share);
        mIv_back = mRootView.findViewById(R.id.iv_back);
        mTitel = (TextView) mRootView.findViewById(R.id.login);
        mImg_club = (ImageView) mRootView.findViewById(R.id.img_club);
        mClubName = (TextView) mRootView.findViewById(R.id.tv_clubName);
        mRecruitmentDetail = (TextView) mRootView.findViewById(R.id.tv_recruitmentDetail);
        mVolunteerRights = (TextView) mRootView.findViewById(R.id.tv_volunteer_rights);
        mClubIntroduce = (TextView) mRootView.findViewById(R.id.tv_club_introduce);
        mBt_sign_up = (Button) mRootView.findViewById(R.id.bt_sign_up);
        mImg_bg_white = (ImageView) mRootView.findViewById(R.id.img_bg_white);
    }

    private void initData() {
        //设置后退图标的点击事件
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出回退栈最上面的fragment
                getFragmentManager().popBackStackImmediate(null, 0);
            }
        });
        //设置空点击事件，防止此界面有点击事件渗透到前一个fragment上
        mRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        //获取网络数据
        getIntnetData();
    }

    public void setActID(String actId) {
        actID = actId;
    }

    private void getIntnetData() {
        String urlFirst = NetConfig.SEARCH_VOLUNTEERINFO;
        //        String actid = CultureVolunteerActivity.actID;
        RequestParams params = new RequestParams();
        params.put("member_id", UserID);
        params.put("id", actID);
        HttpUtil.get(urlFirst, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(getActivity(), "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mImg_share.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                RecruitDetailInfo recruitDetailInfo = gson.fromJson(response.toString(), RecruitDetailInfo.class);
                final RecruitDetailInfo.ArrBean arr = recruitDetailInfo.getArr();
                String fname = arr.getFname();//招聘名称
                mTitel.setText(fname + "招聘");
                mClubName.setText(fname);
                String volunteer_conditions = arr.getVolunteer_conditions();
                String volunteer_rights = arr.getVolunteer_rights();
                //解析段落文本显示
                Spanned conditionsSpanned = Html.fromHtml(volunteer_conditions);
                mRecruitmentDetail.setText(conditionsSpanned);
                Spanned rightsSpanned = Html.fromHtml(volunteer_rights);
                mVolunteerRights.setText(rightsSpanned);
                String team_profile = arr.getTeam_profile();
                Spanned profileSpanned = Html.fromHtml(team_profile);
                mClubIntroduce.setText(profileSpanned);
                String newpic = mURLHeadString + arr.getNewpic();
                ImageLoaderUtil.displayImageIcon(newpic, mImg_club);
                //TODO:跟狙返回的字段，判断用户是否报名这个招聘
                int isFavourite = arr.getIsFavourite();
                if (isFavourite == 0) {
                    mBt_sign_up.setText("我要报名");
                    //跳转到报名界面
                    mBt_sign_up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
                            if (userInfo == null) {
                                UserID = NetConfig.UserID;
                            } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
                                UserID = NetConfig.UserID;
                            } else {
                                UserID = userInfo.member_id;
                            }
                            if (UserID.equals(NetConfig.UserID)) {
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            } else {
                                //替换fragment//展示报名界面
                                SignUpVolunteerFragment signUpVolunteerFragment = new SignUpVolunteerFragment();
                                signUpVolunteerFragment.setIsFromDetail(true);
                                signUpVolunteerFragment.setActID(arr.getId());
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction mFt = fragmentManager.beginTransaction();
                                //进行fragment操作:
                                //隐藏fragment自己
                                Fragment reruitDetailFragment = fragmentManager.findFragmentByTag("reruitDetailFragment");
                                mFt.hide(reruitDetailFragment);
                                mFt.add(R.id.frame_recruitment, signUpVolunteerFragment, "signUpVolunteerFragment");
                                //添加到回退栈，实现按物理返回键有后退效果，而不是直接finish act
                                mFt.addToBackStack(null);
                                //提交事务
                                mFt.commit();
                            }
                        }
                    });
                } else {
                    mBt_sign_up.setText("我已报名");
                }
                mImg_bg_white.setVisibility(View.GONE);
                mImg_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mShowId = arr.getId();
                        getOrSetUserID();
                        if (UserID.equals(NetConfig.UserID)) {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        } else {
                            showShare();
                        }
                    }
                });
            }
        });
    }

    //分享
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //分享完成后提交分享积分
                sendIntnetShare();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(getActivity(), "分享失败" + throwable, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        String url = "http://220.248.107.62:8084/whyapi/index.html?id=" + "322" + "&tb_type=" + "2";
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("志愿者招聘");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("志愿者招聘信息");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (ImageUrlOrPath != null && ImageUrlOrPath.contains("/sdcard/")) {
            //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath(ImageUrlOrPath);
        } else if (ImageUrlOrPath != null) {
            oks.setImageUrl(ImageUrlOrPath); //网络地址
        }
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        oks.setDialogMode();
        oks.setSilent(false);
        // 启动分享GUI
        oks.show(getActivity());
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
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getIntnetData();
    }

    //判断是游客id还是用户id
    public void getOrSetUserID() {
        UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
    }
}
