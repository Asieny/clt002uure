package com.guangxi.culturecloud.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.model.upDataVidioInfo;
import com.guangxi.culturecloud.utils.GetPathFromUri;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/12/18 14:20
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class YanXueActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private WebView   web_yanxue;
    private Button    bt_tup;
    private String UserID                             = NetConfig.UserID;
    private int    IMAGE                              = 10086;//调用相册requestcode
    private int    MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 1001;//相册权限申请码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yanxue_web);
        initView();
        initData();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        web_yanxue = (WebView) findViewById(R.id.web_yanxue);
        bt_tup = (Button) findViewById(R.id.bt_tup);
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        bt_tup.setOnClickListener(this);

        //获取用户id
        getUserId();
        ProgressDialogUtil.startShow(this, "正在加载...");

        //设置webview
        initWebView();

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        web_yanxue.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js") || uri.getAuthority().equals("")) {
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) {
                        // 执行JS所需要调用的逻辑
                        // 可以在协议上带有参数并传递到Android上

                        //js调用视频
                        if (uri.toString().contains("yanxueshipin")) {//shipin
                            //打开文件管理器
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");//无类型限制
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            startActivityForResult(intent, REQUEST_FILE_CODE);
                        }
                        //js调用图片
                        if (uri.toString().contains("yanxuetupian")) {
                            //打开相册
                            //第二个参数是需要申请的权限
                            if (ContextCompat.checkSelfPermission(YanXueActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                //权限还没有授予，需要在这里写申请权限的代码
                                ActivityCompat.requestPermissions(YanXueActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                            } else {
                                //权限已经被授予，在这里直接写要执行的相应方法即可
                                //调用相册
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, IMAGE);
                            }
                        }
                        //js调用跳转个人中心
                        if (uri.toString().contains("yanxuepersonal")){
                            setResult(10086);
                            finish();
                        }
                    }
                    return true;
                }
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        web_yanxue.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    ProgressDialogUtil.hideDialog();
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(YanXueActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
        //自适应屏幕
        web_yanxue.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_yanxue.getSettings().setLoadWithOverviewMode(true);
    }

    private int REQUEST_FILE_CODE = 10058;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (web_yanxue.canGoBack()) {
                    web_yanxue.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.bt_tup:
                // 注意调用的JS方法名要对应上
                // 调用javascript的callJS()方法
                ToastUtils.makeShortText("调JS", this);
                web_yanxue.loadUrl("javascript:callJS(\"108\")");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        web_yanxue.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        web_yanxue.onPause();
    }

    @Override
    protected void onDestroy() {
        if (web_yanxue != null) {
            web_yanxue.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            web_yanxue.clearHistory();

            ((ViewGroup) web_yanxue.getParent()).removeView(web_yanxue);
            web_yanxue.destroy();
            web_yanxue = null;
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web_yanxue.canGoBack()) {
            web_yanxue.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (!uri.getPath().contains("video")) {
                    ToastUtils.showShort(this, "您选择的不是视频文件，请重新选择");
                    return;
                }
                String videoPath = GetPathFromUri.getPath(this, data.getData());
                File file = new File(videoPath);
                //上传视频
                upDateVideo(file);
            }
        }
        //相册返回，获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    private void showImage(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        String strByBase64 = Bitmap2StrByBase64(bm);

        //        String imgStr = getImgStr(imagePath);
        web_yanxue.loadUrl("javascript:callJS(\"" + strByBase64 + "\")");
    }

    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void upDateVideo(File file) {
        if (file != null && file.exists() && file.isFile()) {
            long length = file.length();
            int size = (int) (length / 1024 / 1024);
            if (size >= 15) {
                ToastUtils.showShort(this, "上传的视频文件不能超过15M");
                return;
            }
        }else {
            ToastUtils.showShort(this, "视频文件查找出错");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("userid", UserID);
        params.put("title", "");
        try {
            params.put("file", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ToastUtils.showShort(this, "视频文件获取失败");
            return;
        }
        HttpUtil.post(NetConfig.UPLOADVIDEO, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(YanXueActivity.this, "正在提交内容，请稍后");
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
                    Gson gson = new Gson();
                    upDataVidioInfo upDataInfo = gson.fromJson(response.toString(), upDataVidioInfo.class);
                    if ("true".equals(upDataInfo.getIsOK())) {
                        Toast.makeText(YanXueActivity.this, "视频上传成功", Toast.LENGTH_SHORT).show();
                        web_yanxue.reload();
                    } else {
                        Toast.makeText(YanXueActivity.this, "视频上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initWebView() {
        //启用支持javascript
        WebSettings settings = web_yanxue.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置webview
        web_yanxue.loadUrl("http://222.216.241.136:8081/dist/#/" + "?userid=" + UserID);
        web_yanxue.canGoBack();
    }

    private void getUserId() {
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID) || userInfo.member_id.trim().equals("null")) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        if (UserID.equals(NetConfig.UserID)) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }

    /**
     * * 将图片转换成Base64编码
     * * @param imgFile 待处理图片
     * * @return
     */
    public static String getImgStr(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(data));
    }
}
