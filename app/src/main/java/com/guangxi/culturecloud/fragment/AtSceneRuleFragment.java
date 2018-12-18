package com.guangxi.culturecloud.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.VenueDetailsInfo;
import com.guangxi.culturecloud.utils.ToastUtils;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/17 14:47
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class AtSceneRuleFragment extends Fragment {
    private View           mRootView;
    private WebView        mWebView_rule;
    private RelativeLayout mRelative_rule;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_at_scene_rule, null);
        initView();
        return mRootView;
    }

    private void initView() {
        mRelative_rule = (RelativeLayout) mRootView.findViewById(R.id.relative_rule);
        mWebView_rule = (WebView) mRootView.findViewById(R.id.web_rule);

        mRelative_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止点击渗透
            }
        });
        //请求网络；获取规则详情
        gettoIntnent();
    }

    private void gettoIntnent() {
        String ruleUrl = "http://220.248.107.62:8084/whyapi/serGcActivityRule";
        HttpUtil.get(ruleUrl, new HttpUtil.JsonHttpResponseUtil() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        if (statusCode != 200) {
                            ToastUtils.makeShortText("网络错误", getActivity());
                            return;
                        }
                        Gson gson = new Gson();
                        VenueDetailsInfo personalFavAndComInfo = gson.fromJson(jsonObject.toString(), VenueDetailsInfo.class);
                        String frule = personalFavAndComInfo.getFrule().getFrule();
                        initData(frule);
                    }
                }
        );
    }

    private void initData(String frule) {
        mWebView_rule.loadDataWithBaseURL("", getNewContent(frule), "text/html", "utf-8", "");
        //启用支持javascript
        WebSettings settings = mWebView_rule.getSettings();
        settings.setJavaScriptEnabled(true);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView_rule.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });


        //        String url = "https://www.duanwenxue.com/article/4686498.html";
        //        mWebView_rule.loadUrl(url);
        //        //启用支持javascript
        //        WebSettings settings = mWebView_rule.getSettings();
        //        settings.setJavaScriptEnabled(true);
        //        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        //        mWebView_rule.setWebViewClient(new WebViewClient() {
        //            @Override
        //            public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //                // TODO Auto-generated method stub
        //                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        //                view.loadUrl(url);
        //                return true;
        //            }
        //        });
        //        mWebView_rule.setFocusable(false);
        //        mBt_toTop.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                VoteForWorksFragment voteForWorksFragment = (VoteForWorksFragment) getFragmentManager().findFragmentByTag("voteForWorksFragment");
        //                ScrollView scrollview = (ScrollView) voteForWorksFragment.mRootView.findViewById(R.id.first_scroll);
        //                scrollview.scrollTo(0,0);
        //            }
        //        });
    }

    private String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        Log.d("VACK", doc.toString());
        return doc.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mWebView_rule != null) {
            mWebView_rule.stopLoading();
            mWebView_rule.clearCache(true);
            mWebView_rule.destroy();
        }
    }
}
