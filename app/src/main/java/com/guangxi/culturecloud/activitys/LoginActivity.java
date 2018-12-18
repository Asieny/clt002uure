package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.RegexUtils;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.guangxi.culturecloud.views.CleanEditText;
import com.jarvis.mytaobao.user.MessageEvent;
import com.mob.tools.utils.UIHandler;
import com.onekeyshare.OnekeyShare;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cn.com.mytest.util.SPref;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.action;
import static android.view.View.OnClickListener;

/**
 * @desc 登录界面
 */
public class LoginActivity extends BaseActivity implements PlatformActionListener, Handler.Callback, OnClickListener {

    private static final String TAG                      = "loginActivity";
    private static final int    REQUEST_CODE_TO_REGISTER = 0x001;

    // 界面控件
    private CleanEditText accountEdit;
    private CleanEditText passwordEdit;

    // 第三方平台获取的访问token，有效时间，uid
    private String    accessToken;
    private String    expires_in;
    private String    uid;
    private String    sns;
    private ImageView back;

    private TextView register;
    private static final int MSG_ACTION_CCALLBACK = 0;
    private ProgressDialog progressDialog;

    private UserInfo userinfo;
    private TextView mTv_forget;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

    }

    /**
     * 初始化视图
     */
    private void initViews() {
        back = (ImageView) this.findViewById(R.id.iv_back);
        back.setOnClickListener(this);
        register = (TextView) this.findViewById(R.id.tv_create_account);
        register.setOnClickListener(this);
        accountEdit = (CleanEditText) this.findViewById(R.id.et_email_phone);
        accountEdit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        accountEdit.setTransformationMethod(HideReturnsTransformationMethod
                .getInstance());
        passwordEdit = (CleanEditText) this.findViewById(R.id.et_password);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        passwordEdit.setImeOptions(EditorInfo.IME_ACTION_GO);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    clickLogin();
                }
                return false;
            }
        });
        mTv_forget = (TextView) findViewById(R.id.tv_forget);
        mTv_forget.setOnClickListener(this);
    }

    private void clickLogin() {
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (checkInput(account, password)) {
            login();
        }
    }

    private void login() {
        String url = NetConfig.LOGIN + "telephone=" + accountEdit.getText().toString() + "&psw=" + passwordEdit.getText().toString();
        HttpUtil.get(url, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                showProgress();
                super.onStart();
            }

            @Override
            public void onFinish() {
                closeProgress();
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                String result = jsonObject.optString("result");
                String message = jsonObject.optString("message");
                if (result.equals("2")) {
                    userinfo = new UserInfo();
                    userinfo.telephone = accountEdit.getText().toString();
                    userinfo.psw = passwordEdit.getText().toString();
                    userinfo.username = jsonObject.optString("username");
                    userinfo.member_id = jsonObject.optString("member_id");
                    final String userHeadUrl = jsonObject.optString("headpic");
                    //下载保存头像图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loadHeadImg(NetConfig.HEAD_IMG + userHeadUrl);
                        }
                    }).start();
                    SPref.setObject(LoginActivity.this, UserInfo.class, "userinfo", userinfo);
                    ToastUtils.showLong(LoginActivity.this, "登录成功");
                    EventBus.getDefault().post(new MessageEvent(NetConfig.HEAD_IMG+userHeadUrl));
                    onBackPressed();
                } else {
                    ToastUtils.showLong(LoginActivity.this, message);
                }

            }
        });
    }

    //下载保存头像图片
    private void loadHeadImg(String userHeadUrl) {
        Bitmap bitmap = decodeUriAsBitmapFromNet(userHeadUrl);
        try {
            saveFile(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     *
     * @param bm
     * @throws IOException
     */
    public File saveFile(Bitmap bm) throws IOException {
        mPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/icon_bitmap/";
        File dirFile = new File(mPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File myIconFile = new File(mPath + "UserHeadIcon.jpg");
//        if (myIconFile.exists()) {
//            myIconFile.delete();
//        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myIconFile));
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        }catch (NullPointerException n){

        }
        bos.flush();
        bos.close();
        return myIconFile;
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    private Bitmap decodeUriAsBitmapFromNet(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 检查输入
     *
     * @param account
     * @param password
     * @return
     */
    public boolean checkInput(String account, String password) {
        // 账号为空时提示
        if (account == null || account.trim().equals("")) {
            Toast.makeText(this, R.string.tip_account_empty, Toast.LENGTH_LONG)
                    .show();
        } else {
            // 账号不匹配手机号格式（11位数字且以1开头）
            if (!RegexUtils.checkMobile(account)) {
                Toast.makeText(this, R.string.tip_account_regex_not_right,
                        Toast.LENGTH_LONG).show();
            } else if (password == null || password.trim().equals("")) {
                Toast.makeText(this, R.string.tip_password_can_not_be_empty,
                        Toast.LENGTH_LONG).show();
            } else {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_login:
                clickLogin();
                break;
            case R.id.iv_wechat:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.SSOSetting(false);
                authorize(wechat, 1);
                break;
            case R.id.iv_qq:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(this);
                qq.SSOSetting(false);
                authorize(qq, 2);
                break;
            case R.id.iv_sina:
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                sina.setPlatformActionListener(this);
                sina.SSOSetting(false);
                authorize(sina, 3);
                break;
            case R.id.tv_create_account:
                enterRegister();
                break;
            //            case R.id.tv_forget_password:
            //                enterForgetPwd();
            //                break;
            case R.id.tv_forget:
                Intent intent1 = new Intent(getBaseContext(), ChangePassWordActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    //分享
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 跳转到注册页面
     */
    private void enterRegister() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, REQUEST_CODE_TO_REGISTER);
    }


    //授权
    private void authorize(Platform plat, int type) {
        switch (type) {
            case 1:
                showProgressDialog(getString(R.string.opening_wechat));
                break;
            case 2:
                showProgressDialog(getString(R.string.opening_qq));
                break;
            case 3:
                showProgressDialog(getString(R.string.opening_blog));
                break;
        }
        if (plat.isValid()) { //如果授权就删除授权资料
            plat.removeAccount();
        }
        plat.showUser(null);//授权并获取用户信息
    }

    //登陆授权成功的回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息

    }

    //登陆授权错误的回调
    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    //登陆授权取消的回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    //登陆发送的handle消息在这里处理
    @Override
    public boolean handleMessage(Message message) {
        hideProgressDialog();
        switch (message.arg1) {
            case 1: { // 成功
                Toast.makeText(LoginActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();

                //获取用户资料
                Platform platform = (Platform) message.obj;
                String userId = platform.getDb().getUserId();//获取用户账号
                String userName = platform.getDb().getUserName();//获取用户名字
                String userIcon = platform.getDb().getUserIcon();//获取用户头像
                String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                Toast.makeText(LoginActivity.this, "用户信息为--用户名：" + userName + "  性别：" + userGender, Toast.LENGTH_SHORT).show();

                //下面就可以利用获取的用户信息登录自己的服务器或者做自己想做的事啦!
                //。。。

            }
            break;
            case 2: { // 失败
                Toast.makeText(LoginActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
            }
            break;
            case 3: { // 取消
                Toast.makeText(LoginActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
