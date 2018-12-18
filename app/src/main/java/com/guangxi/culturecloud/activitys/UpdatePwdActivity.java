package com.guangxi.culturecloud.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.guangxi.culturecloud.views.CleanEditText;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;


/**
 * <修改密码>
 *
 * @author 张伟伟
 * @see [相关类/方法]
 * @since V1.00
 */
public class UpdatePwdActivity extends BaseActivity {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.login)
    TextView login;
    @InjectView(R.id.iv_refresh)
    ImageView ivRefresh;
    @InjectView(R.id.title)
    LinearLayout title;
    @InjectView(R.id.oldpwd)
    CleanEditText oldpwd;
    @InjectView(R.id.pwd)
    CleanEditText pwd;
    @InjectView(R.id.btn)
    Button btn;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.inject(this);
        userInfo = SPref.getObject(this, UserInfo.class, "userinfo");
        setListen();
    }

    private void setListen() {
        ivBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                modifyPsw();
            }
        });
    }

    /**
     * 保存密码
     *
     * @param
     * @param
     */
    private void modifyPsw() {
        String url = NetConfig.MODIFYPSW + "id=" + userInfo.member_id + "&old_psw=" + oldpwd.getText().toString()+"&new_psw="+pwd.getText().toString();

        HttpUtil.get(url, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.optString("result");
                String message = response.optString("message");
                if (result.equals("2")) {
                    userInfo.psw = pwd.getText().toString();
                    SPref.setObject(UpdatePwdActivity.this, UserInfo.class, "userinfo", userInfo);
                    ToastUtils.showLong(UpdatePwdActivity.this,message);
                    onBackPressed();
                } else {
                    ToastUtils.showLong(UpdatePwdActivity.this,message);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

}
