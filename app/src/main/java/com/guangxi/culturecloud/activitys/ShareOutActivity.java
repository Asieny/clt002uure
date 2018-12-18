package com.guangxi.culturecloud.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guangxi.culturecloud.R;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/1 9:15
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ShareOutActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mRelative;
    private ImageView      mShare_wechat;//分享到微信
    private ImageView      mShare_qq;
    private Button mBt_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_out);
        initView();
        initData();
    }

    private void initView() {
        mRelative = (RelativeLayout) findViewById(R.id.relative);
        mShare_wechat = (ImageView) findViewById(R.id.img_share_wechat);
        mShare_qq = (ImageView) findViewById(R.id.img_share_qq);
        mBt_cancel = (Button) findViewById(R.id.bt_cancel);
        //        applyBlur();
    }

//    private void applyBlur() {
//        //        mRelative.setBackground();
//        if (BlurBuilder.isBlurFlag()) {
//            iv_shared_layout.setVisibility(View.VISIBLE);
//        }
//    }

    private void initData() {
        mShare_wechat.setOnClickListener(this);
        mBt_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_share_wechat:

                break;
            case R.id.bt_cancel:
                finish();
                break;
        }
    }
}
