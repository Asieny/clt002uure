package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/9 20:07
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class AboutCultureActivity extends BaseActivity {

    private ImageView      mImg_back;
    private EditText       mEdit_name;
    private EditText       mEdit_company;
    private EditText       mEdit_contacts;
    private Button         mBt_submit;
    private ProgressDialog progressDialog;
    private String UserID = NetConfig.UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_culture);
        initView();
        initData();
    }

    private void initView() {
        mImg_back = (ImageView) findViewById(R.id.img_back);
        mEdit_name = (EditText) findViewById(R.id.edit_name);
        mEdit_company = (EditText) findViewById(R.id.edit_company);
        mEdit_contacts = (EditText) findViewById(R.id.edit_contacts);
        mBt_submit = (Button) findViewById(R.id.bt_submit);
    }

    private void initData() {
        mImg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEdit_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String str = String.valueOf(mEdit_name.getText()).trim();
                if (str.equals("请输入您的称呼")) {
                    mEdit_name.setText("");
                    return true;
                } else {
                    return false;
                }
            }
        });
        mEdit_company.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String str = String.valueOf(mEdit_company.getText()).trim();
                if (str.equals("请输入您公司名称")) {
                    mEdit_company.setText("");
                    return true;
                } else {
                    return false;
                }
            }
        });
        mEdit_contacts.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String str = String.valueOf(mEdit_contacts.getText()).trim();
                if (str.equals("请输入您的联系方式")) {
                    mEdit_contacts.setText("");
                    return true;
                } else {
                    return false;
                }
            }
        });
        mBt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交信息到服务器
                String name = String.valueOf(mEdit_name.getText()).trim();
                String company = String.valueOf(mEdit_company.getText()).trim();
                String contacts = String.valueOf(mEdit_contacts.getText()).trim();
                if (name.equals("") || name.equals("请输入您的称呼")) {
                    ToastUtils.makeShortText("名称不能为空", getBaseContext());
                } else if (company.equals("") || company.equals("请输入您公司名称")) {
                    ToastUtils.makeShortText("公司名称不能为空", getBaseContext());
                } else if (contacts.equals("") || contacts.equals("请输入您的联系方式")) {
                    ToastUtils.makeShortText("联系方式不能为空", getBaseContext());
                } else {
                    String UrlInsertHelp = NetConfig.INSERT_JOIN;
                    UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
                    if (userInfo == null) {
                        UserID = NetConfig.UserID;
                    } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals("2")) {
                        UserID = NetConfig.UserID;
                    } else {
                        UserID = userInfo.member_id;
                    }
                    if (UserID.equals(NetConfig.UserID)) {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //TODO:意见提交网络
                        RequestParams params = new RequestParams();
                        params.put("userid", UserID);
                        params.put("fname", name);
                        params.put("fcompany", company);
                        params.put("fmobile", contacts);
                        params.setUseJsonStreamer(true);
                        HttpUtil.post(UrlInsertHelp, params, new HttpUtil.JsonHttpResponseUtil() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                ProgressDialogUtil.startShow(AboutCultureActivity.this,"正在提交，请稍后");
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                ProgressDialogUtil.hideDialog();
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (statusCode == 200) {
                                    ClubDetailInfo clubDetailInfo;
                                    Gson gson = new Gson();
                                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                                    String result = clubDetailInfo.getResult();
                                    String message = clubDetailInfo.getMessage();
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                                    if (result.equals("2")) {
                                        finish();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
