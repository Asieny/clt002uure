package com.guangxi.culturecloud.http;

import android.widget.Toast;

import com.MyApplication;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * AsyncHttp 工具
 *
 * 
 */
public class HttpUtil
{
    private static AsyncHttpClient client = new AsyncHttpClient(); // 实例对象
    static
    {
        client.setTimeout(5000); // 设置链接超时，如果不设置，默认为10s

    }

    public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
    {
        client.get(urlString, res);
    }
    
    public static void get(String urlString, RequestParams params, AsyncHttpResponseHandler res) // url里面带参数
    {
        client.get(urlString, params, res);
    }
    
    public static void get(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
    {
        client.get(urlString, res);
    }
    
    public static void get(String urlString, RequestParams params, JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
    {
        client.get(urlString, params, res);
    }
    
    public static void get(String uString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
    {
        client.get(uString, bHandler);
    }
    
    public static void post(String uString, RequestParams params, JsonHttpResponseHandler res)
    {
        client.post(uString, params, res);
    }
    
    public static void post(String uString, RequestParams params, JsonHttpResponseUtil res)
    {
        client.post(uString, params, res);
    }
    
    public static AsyncHttpClient getClient()
    {
        return client;
    }
    
    public static class JsonHttpResponseUtil extends JsonHttpResponseHandler
    {
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse)
        {
            Toast.makeText(MyApplication.get().getBaseContext(), "网络异常", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
        {
            Toast.makeText(MyApplication.get().getBaseContext(), "数据异常", Toast.LENGTH_LONG).show();
        }
    }
}