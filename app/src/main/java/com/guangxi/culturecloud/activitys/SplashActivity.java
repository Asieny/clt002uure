package com.guangxi.culturecloud.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.AppConstants;
import com.guangxi.culturecloud.utils.SpUtils;
import com.jarvis.mytaobao.home.Main_FA;


/**
 * @desc 启动页
 * zww
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // 如果不是第一次启动app，则正常显示启动屏
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                enterHomeActivity();
            }
        }, 2000);
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, Main_FA.class);
        startActivity(intent);
        finish();
    }
}
