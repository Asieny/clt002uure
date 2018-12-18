package com.guangxi.culturecloud.activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.utils.RegexUtils;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.guangxi.culturecloud.utils.VerifyCodeManager;
import com.guangxi.culturecloud.views.CleanEditText;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cn.com.mytest.anno.ViewId;
import cn.com.mytest.anno.ViewLayoutId;
import cz.msebera.android.httpclient.Header;


/**
 * @desc 注册界面
 * 功能描述：一般会使用手机登录，通过获取手机验证码，跟服务器交互完成注册
 *
 */
@ViewLayoutId(R.layout.activity_signup)
public class SignUpActivity extends BaseActivity implements OnClickListener{
    private static final String TAG = "SignupActivity";
    // 界面控件
    private CleanEditText phoneEdit;
    private CleanEditText passwordEdit;
    private CleanEditText verifyCodeEdit;
    private CleanEditText nickName;
    @ViewId(R.id.tv_hide_validate_code)
    private TextView mTextHideValidateCode;
    private Button getVerifiCodeButton,register;

    private VerifyCodeManager codeManager;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        codeManager = new VerifyCodeManager(this,mTextHideValidateCode, phoneEdit, getVerifiCodeButton);

    }

    /**
     * 通用findViewById,减少重复的类型转换
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e(TAG, "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }
    

    private void initViews() {
        back = (ImageView) this.findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        nickName = getView(R.id.et_nickname);
        register = getView(R.id.btn_create_account);
        register.setOnClickListener(this);
        getVerifiCodeButton = getView(R.id.btn_send_verifi_code);
        getVerifiCodeButton.setOnClickListener(this);
        phoneEdit = getView(R.id.et_phone);
        phoneEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        verifyCodeEdit = getView(R.id.et_verifiCode);
        verifyCodeEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);// 下一步
        passwordEdit = getView(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // 点击虚拟键盘的done
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    commit();
                }
                return false;
            }
        });
    }

    private void commit() {
        String phone = phoneEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String name = nickName.getText().toString().trim();

        if (checkInput(phone, password,name)) {
            register();
        }
    }
    // 上传数据
    private void register() {
        RequestParams rp = new RequestParams();
        rp.put("telephone", phoneEdit.getText().toString());
        rp.put("username", nickName.getText().toString());
        rp.put("password", passwordEdit.getText().toString());
        rp.setUseJsonStreamer(true);
        HttpUtil.post(NetConfig.REGISTER, rp, new HttpUtil.JsonHttpResponseUtil() {

            @Override
            public void onStart() {
                showProgress();
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String result = response.optString("result");
                String message = response.optString("message");
                if (result.equals("2")){
                    ToastUtils.showLong(SignUpActivity.this,"注册成功");
                    onBackPressed();
                }
                else {
                    ToastUtils.showLong(SignUpActivity.this,message);
                }

            }

            @Override
            public void onFinish() {
                closeProgress();
                super.onFinish();
            }
        });
    }

    private boolean checkInput(String phone, String password,String nickName) {
        if (TextUtils.isEmpty(phone)) { // 电话号码为空
            ToastUtils.showShort(this, R.string.tip_phone_can_not_be_empty);
            return false;
        }
        if (TextUtils.isEmpty(verifyCodeEdit.getText())) { //验证码
            ToastUtils.showShort(this,"验证码不能为空");
            return false;
        }
        if (!verifyCodeEdit.getText().toString().equals(mTextHideValidateCode.getText()+"")) { //验证码
            ToastUtils.showShort(this,"验证码不对");
            return false;
        }

        if (!RegexUtils.checkMobile(phone)) { // 电话号码格式有误
            ToastUtils.showShort(this, R.string.tip_phone_regex_not_right);
        } else if (password.length() < 6 || password.length() > 32
                || TextUtils.isEmpty(password)) { // 密码格式
            ToastUtils.showShort(this,
                    R.string.tip_please_input_6_32_password);
        }else if (TextUtils.isEmpty(nickName)){
            ToastUtils.showShort(this, R.string.tip_please_input_nickname);
        }else {
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_account:
                commit();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_send_verifi_code:
                codeManager.getVerifyCode();
                break;
            default:
                break;
        }
    }
}
