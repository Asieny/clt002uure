package com.jarvis.mytaobao.user;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.MyApplication;
import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;
import com.guangxi.culturecloud.activitys.ChooseCityActivity;
import com.guangxi.culturecloud.fragment.PersonalQualificationFragment;
import com.guangxi.culturecloud.fragment.ProvideCompanyFragment;
import com.guangxi.culturecloud.fragment.ProvideSocietyFragment;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.Society;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/15 12:51
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ProvideQualificationsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView                     mIv_back;
    private TextView                      mTv_personal;
    private TextView                      mTv_society;
    private TextView                      mTv_company;
    private FrameLayout                   mFrame;
    private PersonalQualificationFragment mPersonalQualificationFragment;
    private ProvideSocietyFragment        mProvideSocietyFragment;
    private TextView                      mIv_shao;//选择提交地区
    private String                        city;
    private final int    REQUEST_CODE_CITY = 1001;
    private       String mUrl_address      = NetConfig.URL_HEAD_ADDRESS;//定位的服务器地址
    private       String mGet_img_address  = NetConfig.IMG;   //定位的服务器图片地址
    private       String mPost_img_address = NetConfig.URL_CHANGE_UPLOAD_BASE64;//定位的提交服务器地址
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_qualifications);
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mIv_shao = (TextView) findViewById(R.id.iv_shao);
        mTv_personal = (TextView) findViewById(R.id.tv_personal);
        mTv_society = (TextView) findViewById(R.id.tv_society);
        mTv_company = (TextView) findViewById(R.id.tv_company);
        mFrame = (FrameLayout) findViewById(R.id.frame);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        mIv_shao.setText(MyApplication.cityName);
        mTv_personal.setOnClickListener(this);
        mTv_society.setOnClickListener(this);
        mTv_company.setOnClickListener(this);
        //首先展示个人资质界面
        mPersonalQualificationFragment = new PersonalQualificationFragment();
        FragmentTransaction mFt = getFragmentManager().beginTransaction();
        //进行fragment操作:
        mFt.add(R.id.frame, mPersonalQualificationFragment, "personalQualificationFragment");
        //提交事务
        mFt.commit();

        mIv_shao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ProvideQualificationsActivity.this, ChooseCityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CITY);
            }
        });
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction mFt = fragmentManager.beginTransaction();
        Fragment mPersonalQualificationFragment = fragmentManager.findFragmentByTag("personalQualificationFragment");
        Fragment mProvideSocietyFragment = fragmentManager.findFragmentByTag("provideSocietyFragment");
        Fragment mProvideCompanyFragment = fragmentManager.findFragmentByTag("provideCompanyFragment");
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_personal:
                mTv_personal.setTextColor(Color.argb(255, 114, 121, 160));
                mTv_society.setTextColor(Color.BLACK);
                mTv_company.setTextColor(Color.BLACK);
                //展示个人资质界面
                if (mProvideSocietyFragment != null) {
                    if (!mProvideSocietyFragment.isHidden()) {
                        mFt.hide(mProvideSocietyFragment);
                    }
                }
                if (mProvideCompanyFragment != null) {
                    if (!mProvideCompanyFragment.isHidden()) {
                        mFt.hide(mProvideCompanyFragment);
                    }
                }
                if (mPersonalQualificationFragment == null) {
                    mPersonalQualificationFragment = new PersonalQualificationFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame, mPersonalQualificationFragment, "personalQualificationFragment");
                } else {
                    mFt.show(mPersonalQualificationFragment);
                }
                //提交事务
                mFt.commit();
                break;
            case R.id.tv_society:
                mTv_personal.setTextColor(Color.BLACK);
                mTv_society.setTextColor(Color.argb(255, 114, 121, 160));
                mTv_company.setTextColor(Color.BLACK);
                //展示社团资质界面
                if (mPersonalQualificationFragment != null) {
                    if (!mPersonalQualificationFragment.isHidden()) {
                        mFt.hide(mPersonalQualificationFragment);
                    }
                }
                if (mProvideCompanyFragment != null) {
                    if (!mProvideCompanyFragment.isHidden()) {
                        mFt.hide(mProvideCompanyFragment);
                    }
                }
                if (mProvideSocietyFragment == null) {
                    mProvideSocietyFragment = new ProvideSocietyFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame, mProvideSocietyFragment, "provideSocietyFragment");
                } else {
                    mFt.show(mProvideSocietyFragment);
                }
                //提交事务
                mFt.commit();
                break;
            case R.id.tv_company:
                mTv_personal.setTextColor(Color.BLACK);
                mTv_society.setTextColor(Color.BLACK);
                mTv_company.setTextColor(Color.argb(255, 114, 121, 160));
                if (mPersonalQualificationFragment != null) {
                    if (!mPersonalQualificationFragment.isHidden()) {
                        mFt.hide(mPersonalQualificationFragment);
                    }
                }
                if (mProvideSocietyFragment != null) {
                    if (!mProvideSocietyFragment.isHidden()) {
                        mFt.hide(mProvideSocietyFragment);
                    }
                }
                if (mProvideCompanyFragment == null) {
                    mProvideCompanyFragment = new ProvideCompanyFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame, mProvideCompanyFragment, "provideCompanyFragment");
                } else {
                    mFt.show(mProvideCompanyFragment);
                }
                //提交事务
                mFt.commit();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取选择城市地址
        if (requestCode == REQUEST_CODE_CITY) {
            if (data != null) {
                city = data.getStringExtra("city") + "市";
                mIv_shao.setText(city);
                //根据修改地址，修改所需提交的服务器；
                //获取地址请求头
                getUrlHead(city);
            }
        }
    }

    private void getUrlHead(String cityName) {
        RequestParams params = new RequestParams();
        params.put("area", cityName);
        HttpUtil.get(NetConfig.FIRSTURL, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(ProvideQualificationsActivity.this, "正在修改提交地址，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    String ress = response.toString();
                    if (null != ress && !ress.equals("") && !ress.equals("{}")) {
                        Gson gson = new Gson();
                        Society urlAddressInfo = gson.fromJson(response.toString(), Society.class);
                        try {
                            Society.ApiAddressBean apiAddress = urlAddressInfo.getApiAddress();
                            String url_address = apiAddress.getUrl_address();
                            String get_img_address = apiAddress.getGet_img_address();
                            //修改所有链接
                            changeAllLinks(url_address, get_img_address);
                        } catch (Exception e) {
                            ToastUtils.makeShortText("跳转省会城市资讯", getBaseContext());
                        }
                    }
                }
            }
        });
    }

    private void changeAllLinks(String url_address, String get_img_address) {
        mUrl_address = url_address;
        mGet_img_address = get_img_address;
        mPost_img_address = url_address + "uploadBase64";
    }

    public String getUrl_address() {
        return mUrl_address;
    }

    public String getPost_img_address() {
        return mPost_img_address;
    }
}
