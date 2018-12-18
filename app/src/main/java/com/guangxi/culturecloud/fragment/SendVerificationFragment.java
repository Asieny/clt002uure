package com.guangxi.culturecloud.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.utils.RegexUtils;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.guangxi.culturecloud.R;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/2 8:32
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class SendVerificationFragment extends Fragment implements View.OnClickListener {
    private View      mRootView;
    private ImageView mImg_back;
    private EditText  mEdit_phone_num;//手机号填写栏
    private EditText  mEdit_test_pass;//验证码填写栏
    private Button    mBt_get_test;//发送验证码
    private Button    mBt_next;//下一步
    private String    mPhone_num;//手机号
    private String    mtest_pass;//验证码
    private String  markVerification = "-12345678";
    private int     count            = 60;//验证码可重新点击发送时间间隔
    private Handler handler          = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_send_verification, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mEdit_phone_num = (EditText) mRootView.findViewById(R.id.edit_phone_num);
        mEdit_test_pass = (EditText) mRootView.findViewById(R.id.edit_test_pass);
        mBt_get_test = (Button) mRootView.findViewById(R.id.bt_get_test);
        mBt_next = (Button) mRootView.findViewById(R.id.bt_next);
    }

    private void initData() {
        mImg_back.setOnClickListener(this);
        mBt_get_test.setOnClickListener(this);
        mBt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.bt_get_test:
                //判断一下是否填写手机号
                mPhone_num = String.valueOf(mEdit_phone_num.getText()).trim();
                if (mPhone_num.equals("") || mPhone_num.equals("请输入11位手机号")) {
                    ToastUtils.makeShortText("请输入手机号码", getActivity());
                } else {
                    // 账号不匹配手机号格式（11位数字且以1开头）
                    if (!RegexUtils.checkMobile(mPhone_num)) {
                        Toast.makeText(getActivity(), R.string.tip_account_regex_not_right,
                                Toast.LENGTH_LONG).show();
                    } else {
                        sendTointnet();
                    }
                }
                break;
            case R.id.bt_next:
                mPhone_num = String.valueOf(mEdit_phone_num.getText()).trim();
                mtest_pass = String.valueOf(mEdit_test_pass.getText()).trim();
                if (mPhone_num.equals("") || mPhone_num.equals("请输入11位手机号")) {
                    ToastUtils.makeShortText("请输入手机号码", getActivity());
                    return;
                }
                if (mtest_pass.equals("") || mtest_pass.equals("请输入6位验证码")) {
                    ToastUtils.makeShortText("请输入验证号码", getActivity());
                    return;
                }
                checkVerification();
                break;
        }
    }

    private void checkVerification() {
        if (!mtest_pass.equals(markVerification)) {
            ToastUtils.makeShortText("验证码错误，请从新获取验证码", getActivity());
        } else {
            ChangeNewPassWordFragment newPassWordFragment = new ChangeNewPassWordFragment();
            FragmentTransaction fmt = getFragmentManager().beginTransaction();
            fmt.add(R.id.frame_change_pass, newPassWordFragment, "newPassWordFragment");
            newPassWordFragment.setPhoneNum(mPhone_num);
            fmt.commit();
        }
    }

    private void sendTointnet() {
        String checkMessage = NetConfig.CHECK_MESSAGE;
        RequestParams params = new RequestParams();
        params.put("mobile", mPhone_num);
        HttpUtil.get(checkMessage, params, new HttpUtil.JsonHttpResponseUtil() {
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
                ClubDetailInfo clubDetailInfo;
                Gson gson = new Gson();
                clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                boolean valid = clubDetailInfo.getValid();
                if (valid) {
                    String validateCode = clubDetailInfo.getValidateCode();
                    markVerification = validateCode;
                    ToastUtils.makeShortText("验证码发送成功", getActivity());

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            handler.postDelayed(this, 1000);//递归执行，一秒执行一次
                            if (count > 0) {
                                count--;
                                mBt_get_test.setText(count + "秒后可重新发送");
                                mBt_get_test.setClickable(false);
                            } else {
                                mBt_get_test.setText("发送验证码");
                                mBt_get_test.setClickable(true);
                                handler.removeCallbacks(this);
                            }
                        }
                    }, 1000);    //第一次执行，一秒之后。第一次执行完就没关系了
                } else {
                    ToastUtils.makeShortText("验证码发送失败，请重新请求", getActivity());
                }

            }
        });
    }
}
