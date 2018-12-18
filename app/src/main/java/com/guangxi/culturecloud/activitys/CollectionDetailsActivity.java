package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.GoodsDetailsInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/5 13:44
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CollectionDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView      mIv_back;
    private ImageView      mImg_goods;
    private ImageView      img_no_intnet;
    private TextView       mTv_name;
    private TextView       mTv_time;
    private TextView       mTv_type;
    private TextView       mTv_place;
    private WebView        mWeb_detail;
    private String         tb_name;
    private String         id;
    private ProgressDialog progressDialog;
    private int            mWhich;
    private String mURLHeadString = NetConfig.IMG;
    private String url_address="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        Intent intent = getIntent();
        tb_name = intent.getStringExtra("tb_name");
        id = intent.getStringExtra("business_id");
        mWhich = intent.getIntExtra("which", 0);

        url_address = intent.getStringExtra("url_address");
        mURLHeadString = intent.getStringExtra("showImgUrl");
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mImg_goods = (ImageView) findViewById(R.id.img_goods);
        img_no_intnet = (ImageView) findViewById(R.id.img_no_intnet);
        mTv_name = (TextView) findViewById(R.id.tv_name);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        mTv_type = (TextView) findViewById(R.id.tv_type);
        mTv_place = (TextView) findViewById(R.id.tv_place);
        mWeb_detail = (WebView) findViewById(R.id.web_detail);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        getIntnetData();

        //        VenueDetailsInfo.ArrBean.ListGoodsBean listGoodsBean = CollectionMoreActivity.mListGoodsBean.get(mWhich);
        //        String newGoods_file = listGoodsBean.getNewGoods_file();
        //        ImageLoaderUtil.displayImageIcon(mURLHeadString + newGoods_file, mImg_goods);
        //        String goods_time = listGoodsBean.getGoods_time();
        //        String goods_type = listGoodsBean.getGoods_type();
        //        String goods_business = listGoodsBean.getGoods_business();
        //        mTv_time.setText(goods_time);
        //        mTv_type.setText(goods_type);
        //        mTv_place.setText(goods_business);
        //        String goods_introduce = listGoodsBean.getGoods_introduce();
        //        mWeb_detail.loadDataWithBaseURL("", getNewContent(goods_introduce), "text/html", "utf-8", "");
        //        //启用支持javascript
        //        WebSettings settings = mWeb_detail.getSettings();
        //        settings.setJavaScriptEnabled(true);
        //        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        //        mWeb_detail.setWebViewClient(new WebViewClient() {
        //            @Override
        //            public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //                // TODO Auto-generated method stub
        //                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        //                view.loadUrl(url);
        //                return true;
        //            }
        //        });
    }

    private void getIntnetData() {
//        String businessList = NetConfig.BUSINESS_GOODSLIST;
        RequestParams params = new RequestParams();
        params.put("tb_name", tb_name);
        params.put("id", id);
        HttpUtil.get(url_address+"businessGoodsList", params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(CollectionDetailsActivity.this,"正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getBaseContext());
                    return;
                }
                img_no_intnet.setVisibility(View.GONE);
                Gson gson = new Gson();
                GoodsDetailsInfo goodsDetailsInfo = gson.fromJson(response.toString(), GoodsDetailsInfo.class);
                List<GoodsDetailsInfo.ArrBean> arr = goodsDetailsInfo.getArr();
                GoodsDetailsInfo.ArrBean arrBean = arr.get(mWhich);
                setData(arrBean);
            }
        });
    }

    private void setData(GoodsDetailsInfo.ArrBean arrBean) {
        String newGoods_file = arrBean.getNewGoods_file();
        ImageLoaderUtil.displayImageIcon(mURLHeadString + newGoods_file, mImg_goods);
        String goods_time = arrBean.getGoods_time();
        String goods_type = arrBean.getGoods_type();
        String goods_business = arrBean.getGoods_business();
        String goods_name = arrBean.getGoods_name();
        mTv_name.setText(goods_name);
        mTv_time.setText(goods_time);
        mTv_type.setText(goods_type);
        mTv_place.setText(goods_business);
        String goods_introduce = arrBean.getGoods_introduce();
        mWeb_detail.loadDataWithBaseURL("", getNewContent(goods_introduce), "text/html", "utf-8", "");
        //启用支持javascript
        WebSettings settings = mWeb_detail.getSettings();
        settings.setJavaScriptEnabled(true);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWeb_detail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
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

}
