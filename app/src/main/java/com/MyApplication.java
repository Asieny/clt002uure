package com;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.location.BDLocation;
import com.guangxi.culturecloud.model.CityAreaCodeInfo;
import com.guangxi.culturecloud.utils.ToolLocation;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.framework.AbsApp;
import cn.sharesdk.framework.ShareSDK;


/**
 * zww
 */
public class MyApplication extends AbsApp {
    public static Context applicationContext;
    public static String cityName = "";
    public static List<CityAreaCodeInfo.ArrBean> mCityAreaCode;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        @SuppressWarnings("deprecation")
        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(getApplicationContext()).memoryCache(new LruMemoryCache(2 * 1024 * 1024)).discCache(new UnlimitedDiscCache(cacheDir)).build();
        ImageLoader.getInstance().init(config);
        ToolLocation.requestLocation(getApplicationContext(), new ToolLocation.InterfaceBDLocation() {

            @Override
            public void onLocationSuccess(BDLocation location) {
                cityName = location.getCity();
                if ("".equals(cityName) || null == cityName) {
                    cityName = "南宁市";
                }
            }
        }, false);

        mCityAreaCode = new ArrayList<>();
        //初始化ShareSDK
        ShareSDK.initSDK(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
