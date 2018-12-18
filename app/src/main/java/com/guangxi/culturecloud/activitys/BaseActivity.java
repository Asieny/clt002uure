package com.guangxi.culturecloud.activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.UserInfo;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.com.mytest.framework.AbsActivity;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/30 16:19
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class BaseActivity extends AbsActivity {
    private String UserID = NetConfig.UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrSetUserID();
        if (!UserID.equals(NetConfig.UserID)) {
            //访问服务器，记录每日登陆，加积分
            goToInsertLoginCount();
        }
    }

    private void goToInsertLoginCount() {
        RequestParams params = new RequestParams();
        params.put("user_id", UserID);
        HttpUtil.get(NetConfig.INSERT_LOGINCOUNT, params, new HttpUtil.JsonHttpResponseUtil() {
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
