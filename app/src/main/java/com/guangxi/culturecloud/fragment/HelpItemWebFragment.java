package com.guangxi.culturecloud.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.UseHelpActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.HelpeRulerInfo;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/9 16:03
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class HelpItemWebFragment extends Fragment {
    private View      mRootView;
    private ImageView mImg_back;
    private WebView   mWeb_show;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_help_web, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        UseHelpActivity activity = (UseHelpActivity) getActivity();
        mImg_back = (ImageView) mRootView.findViewById(R.id.img_back);
        mWeb_show = (WebView) mRootView.findViewById(R.id.web_show);
    }

    private void initData() {
        mImg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出回退栈最上面的fragment
                getFragmentManager().popBackStackImmediate(null, 0);
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        HelpItemFragment fragment = (HelpItemFragment) fragmentManager.findFragmentByTag("helpFragment");
        String strUrl = fragment.getStrUrl();
        //访问网络
        getFromIntnent(strUrl);
    }

    private void getFromIntnent(String id) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        HttpUtil.get(NetConfig.SERHELP_RULE, params, new HttpUtil.JsonHttpResponseUtil() {
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
                Gson gson = new Gson();
                HelpeRulerInfo mHelpeRulerInfo = gson.fromJson(response.toString(), HelpeRulerInfo.class);
                String helprule = mHelpeRulerInfo.getArr().getHelprule();

                //帮助详情
                String fdetail = helprule;
                mWeb_show.loadDataWithBaseURL("", getNewContent(fdetail), "text/html", "utf-8", "");
                //启用支持javascript
                WebSettings settings = mWeb_show.getSettings();
                settings.setJavaScriptEnabled(true);
                //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
                mWeb_show.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // TODO Auto-generated method stub
                        //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                        view.loadUrl(url);
                        return true;
                    }
                });
            }
        });
    }

    private String getNewContent(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }
}
