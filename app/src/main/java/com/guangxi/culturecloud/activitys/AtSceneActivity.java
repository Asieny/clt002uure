package com.guangxi.culturecloud.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.fragment.VoteForWorksFragment;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.Square;
import com.guangxi.culturecloud.model.UserInfo;
import com.loopj.android.http.RequestParams;
import com.onekeyshare.OnekeyShare;

import org.json.JSONObject;

import java.util.HashMap;

import cn.com.mytest.util.SPref;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/16 16:26
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class AtSceneActivity extends BaseActivity {

    private       ImageView            mIv_back;
    private       VoteForWorksFragment mVoteForWorksFragment;
    public static Square               mSquare;
    private       ImageView            mImg_share;
    private boolean needShowDetails = false;
    private ImageView mImg_no_intnet;
    private String UserID         = NetConfig.UserID;
    private String ImageUrlOrPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521093612719&di=6e6a175f275d0efc26956315d4b07582&imgtype=0&src=http%3A%2F%2Fnews.sznews.com%2Fimages%2Fattachement%2Fjpg%2Fsite3%2F20171013%2FIMG6c0b840b679945782523231.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_scene);
        Intent intent = getIntent();
        needShowDetails = intent.getBooleanExtra("ShowDetails", false);
        initView();
        initData();
    }

    private void initView() {
        mImg_no_intnet = (ImageView) findViewById(R.id.img_no_intnet);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mImg_share = (ImageView) findViewById(R.id.img_share);
    }

    private void initData() {
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                if (backStackEntryCount > 1) {
                    fragmentManager.popBackStack();
                } else {
                    finish();
                }
            }
        });

        //首先展示总体招募信息
        mVoteForWorksFragment = new VoteForWorksFragment();
        if (needShowDetails) {
            mVoteForWorksFragment.setNeedShowDetailsFragment(true);
        }
        FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();
        //进行fragment操作:
        mFt.add(R.id.frame_initial, mVoteForWorksFragment, "voteForWorksFragment");
        if (!needShowDetails) {
            mFt.addToBackStack(null);
        }
        //提交事务
        mFt.commit();

        mImg_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            Toast.makeText(AtSceneActivity.this, "分享失败" + throwable, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {

                        }
                    });
                    //关闭sso授权
                    oks.disableSSOWhenAuthorize();
                    String url = "http://220.248.107.62:8084/whyapi/index.html?id=" + "40288a60609c3d2e01609c589c520018" + "&tb_type=" + "泥城影剧院排片-帕丁顿熊22";
                    // title标题，微信、QQ和QQ空间等平台使用
                    oks.setTitle("文化云·我在现场");
                    // titleUrl QQ和QQ空间跳转链接
                    oks.setTitleUrl(url);
                    // text是分享文本，所有平台都需要这个字段
                    oks.setText("文化云·我在现场");
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    if (ImageUrlOrPath != null && ImageUrlOrPath.contains("/sdcard/")) {
                        //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        oks.setImagePath(ImageUrlOrPath);
                    } else if (ImageUrlOrPath != null) {
                        oks.setImageUrl(ImageUrlOrPath); //网络地址
                    }
                    //                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    //                    oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                    // url在微信、微博，Facebook等平台中使用
                    oks.setUrl(url);
                    // comment是我对这条分享的评论，仅在人人网使用
                    //                oks.setComment("我是测试评论文本");
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite(getString(R.string.app_name));
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setSiteUrl(url);
                    oks.setDialogMode();
                    oks.setSilent(false);
                    // 启动分享GUI
                    oks.show(AtSceneActivity.this);
                }
            }
        });
        mImg_no_intnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断当前按键是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 1) {
                fragmentManager.popBackStack();
            } else {
                finish();
            }
        }
        return true;
    }

    //分享完后，
    private void sendIntnetShare() {
        RequestParams params = new RequestParams();
        params.put("user_id", UserID);
        params.put("activity_id", "0");
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
}
