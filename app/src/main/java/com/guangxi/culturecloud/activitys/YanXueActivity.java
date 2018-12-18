package com.guangxi.culturecloud.activitys;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;

/**
 * @创建者 AndyYan
 * @创建时间 2018/12/18 14:20
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class YanXueActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private WebView   web_yanxue;

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
    }

    private void initData() {
        iv_back.setOnClickListener(this);
        ProgressDialogUtil.startShow(this, "正在加载");
        //设置webview
        web_yanxue.loadUrl("http://222.216.241.136:8081/dist/#/");
        //启用支持javascript
        WebSettings settings = web_yanxue.getSettings();
        settings.setJavaScriptEnabled(true);

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        web_yanxue.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
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
        });
        //自适应屏幕
        web_yanxue.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_yanxue.getSettings().setLoadWithOverviewMode(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
