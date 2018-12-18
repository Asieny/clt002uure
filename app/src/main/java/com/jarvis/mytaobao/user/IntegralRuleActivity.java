package com.jarvis.mytaobao.user;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.IntegralRuleInfo;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/4/2 8:47
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class IntegralRuleActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIv_back;
    private TextView  mTv_become;
    private TextView  mTv_rights;
    private WebView   mWeb_integral_rule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_rule);
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mTv_become = (TextView) findViewById(R.id.tv_become);
        mTv_rights = (TextView) findViewById(R.id.tv_rights);
        mWeb_integral_rule = (WebView) findViewById(R.id.web_integral_rule);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        mTv_become.setOnClickListener(this);
        mTv_rights.setOnClickListener(this);

        //首先获取成为会员规则
        getrule("0");
    }

    private void getrule(final String type) {
        RequestParams params = new RequestParams();
        params.put("id", type);
        HttpUtil.get(NetConfig.POINTS_RULE, params, new HttpUtil.JsonHttpResponseUtil() {
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
                IntegralRuleInfo mRuleInfo = gson.fromJson(response.toString(), IntegralRuleInfo.class);
                IntegralRuleInfo.ArrBean arr = mRuleInfo.getArr();
                //把积分规则填写给webview
                setWebView(arr, type);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_become:
                mTv_become.setTextColor(Color.argb(255, 114, 121, 160));
                mTv_rights.setTextColor(Color.BLACK);
                getrule("0");
                break;
            case R.id.tv_rights:
                mTv_become.setTextColor(Color.BLACK);
                mTv_rights.setTextColor(Color.argb(255, 114, 121, 160));
                getrule("1");
                break;
        }
    }

    private void setWebView(IntegralRuleInfo.ArrBean arr, String type) {
        //积分详情
        String rule = arr.getRule();
        mWeb_integral_rule.loadDataWithBaseURL("", getNewContent(rule), "text/html", "utf-8", "");
        //启用支持javascript
        WebSettings settings = mWeb_integral_rule.getSettings();

//        if (type.equals("2")) {
////            settings.setUseWideViewPort(true);//关键点
//            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//            settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
//            settings.setSupportZoom(true); // 支持缩放
//        }
        settings.setJavaScriptEnabled(true);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWeb_integral_rule.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    private String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "50%").attr("height", "auto");
        }
        return doc.toString();
    }
}
