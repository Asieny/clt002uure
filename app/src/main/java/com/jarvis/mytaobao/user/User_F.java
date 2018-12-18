package com.jarvis.mytaobao.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guangxi.culturecloud.activitys.AboutCultureActivity;
import com.guangxi.culturecloud.activitys.LoginActivity;
import com.guangxi.culturecloud.activitys.MyOrderListActivity;
import com.guangxi.culturecloud.activitys.SettingAvtivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.R;
import com.javis.Adapter.Adapter_GridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;

import butterknife.ButterKnife;
import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * 我的淘宝主界面
 *
 * @author http://yecaoly.taobao.com
 */
public class User_F extends Fragment implements OnClickListener {
    ImageView    imgFace;
    TextView     goLogin;
    TextView     userName;
    TextView     ordernum;
    LinearLayout myorder;
    private Adapter_GridView adapter_GridView;
    //资源文件
    private int[]    pic_path           = {R.drawable.user_3, R.drawable.user_4, R.drawable.user_5, R.drawable.user_6, R.drawable.user_7};
    private String[] pic_path_life_name = {"", "", "", "", ""};


    private LinearLayout ll_user_life;
    private LinearLayout ll_user_store;
    private LinearLayout ll_user_opinion;
    private LinearLayout ll_user_setting;
    private UserInfo     userInfo;

    private int          orderNum;
    private int          collection_Num;
    private int          comment_Num;
    private String       totoal_points;
    private LinearLayout mLl_user_about;
    private String UserID = NetConfig.UserID;
    private LinearLayout mMycollection;//我的收藏
    private LinearLayout mMycomment;    //我的评论
    private LinearLayout mMypoints;     //我的积分
    private TextView     mCollection_num;//收藏数
    private TextView     mComment_num;  // 评论数
    private TextView     mTv_points;    // 积分数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.user_f, null);
        initView(view);
        ButterKnife.inject(this, view);
        return view;
    }


    private void initView(View view) {
        mLl_user_about = (LinearLayout) view.findViewById(R.id.ll_user_about);
        ll_user_life = (LinearLayout) view.findViewById(R.id.ll_user_life);
        ll_user_store = (LinearLayout) view.findViewById(R.id.ll_user_store);
        ll_user_opinion = (LinearLayout) view.findViewById(R.id.ll_user_opinion);
        ll_user_setting = (LinearLayout) view.findViewById(R.id.ll_user_setting);
        imgFace = (ImageView) view.findViewById(R.id.img_face);
        goLogin = (TextView) view.findViewById(R.id.go_Login);
        userName = (TextView) view.findViewById(R.id.user_Name);
        ordernum = (TextView) view.findViewById(R.id.ordernum);
        myorder = (LinearLayout) view.findViewById(R.id.myorder);

        mMycollection = (LinearLayout) view.findViewById(R.id.mycollection);
        mMycomment = (LinearLayout) view.findViewById(R.id.mycomment);
        mMypoints = (LinearLayout) view.findViewById(R.id.mypoints);
        mCollection_num = (TextView) view.findViewById(R.id.tv_collection_num);
        mComment_num = (TextView) view.findViewById(R.id.comment_num);
        mTv_points = (TextView) view.findViewById(R.id.tv_points);
        mMycollection.setOnClickListener(this);
        mMycomment.setOnClickListener(this);
        mMypoints.setOnClickListener(this);

        ll_user_life.setOnClickListener(this);
        ll_user_store.setOnClickListener(this);
        ll_user_opinion.setOnClickListener(this);
        ll_user_setting.setOnClickListener(this);
        goLogin.setOnClickListener(this);
        myorder.setOnClickListener(this);
        //    	my_gridView_user.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //    	adapter_GridView=new Adapter_GridView(getActivity(), pic_path,pic_path_life_name);
        //    	my_gridView_user.setAdapter(adapter_GridView);
        //    	my_gridView_user.setOnItemClickListener(new OnItemClickListener() {
        //
        //			@Override
        //			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        //					long arg3) {
        //				//进入本机拥有传感器界面
        //				Intent intent=new Intent(getActivity(),HelloSensor.class);
        //				startActivity(intent);
        //
        //			}
        //		});
        //注册eventbus监听
        EventBus.getDefault().register(this);
        //获得保存的图片的路径
        String path = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/icon_bitmap/UserHeadIcon.jpg";
        File file = new File(path);
        //判断是否登录保存过头像
        if (file.exists()) {
            //工具类有缓存，清除，防止显示错误图片
            ImageLoaderUtil.clean();
            ImageLoaderUtil.displayImageRoundIcon("file:/" + path, imgFace);
        }
        mLl_user_about.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutCultureActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        if (null != userInfo && !userInfo.member_id.equals("")) {
            goLogin.setVisibility(View.GONE);
            userName.setVisibility(View.VISIBLE);
            userName.setText(userInfo.username);
            getAllData();
        } else {
            goLogin.setVisibility(View.VISIBLE);
            userName.setVisibility(View.GONE);
            ordernum.setText("" + 0);
            mCollection_num.setText("" + 0);
            mComment_num.setText("" + 0);
            mTv_points.setText("" + 0+"分");
        }
    }

    private void getAllData() {
        String url = NetConfig.SEARCHORDERLIST + "member_id=" + userInfo.member_id + "&fstatus=";
        HttpUtil.get(url, new HttpUtil.JsonHttpResponseUtil() {
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
                orderNum = jsonObject.optInt("sumOrder");
                ordernum.setText(orderNum + "");
                collection_Num = jsonObject.optInt("sumFavourite");
                mCollection_num.setText(collection_Num + "");
                comment_Num = jsonObject.optInt("sumComment");
                mComment_num.setText(comment_Num + "");
                totoal_points = jsonObject.optString("totoal_points");
                mTv_points.setText(totoal_points+"分");
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        switch (arg0.getId()) {
            case R.id.ll_user_life:
                //判断用户是否已登录
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    //进入实名认证
                    Intent intent1 = new Intent(getActivity(), ProvideReallyNameActivity.class);
                    startActivity(intent1);
                }
                break;
            case R.id.ll_user_store:
                //进入刮刮乐界面
                //                Intent intent3 = new Intent(getActivity(), User_life.class);
                //                startActivity(intent3);

                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    //进入资质：个人，公司，社团认证界面
                    Intent intent3 = new Intent(getActivity(), ProvideQualificationsActivity.class);
                    startActivity(intent3);
                }
                break;
            case R.id.ll_user_opinion:
                //意见反馈界面
                Intent intent4 = new Intent(getActivity(), User_opinion.class);
                startActivity(intent4);
                break;
            case R.id.ll_user_setting:
                //设置界面
                if (userInfo != null) {
                    Intent intent8 = new Intent(getActivity(), SettingAvtivity.class);
                    startActivity(intent8);
                } else {
                    Intent intent5 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent5);
                }
                break;
            case R.id.go_Login:
                //登录界面
                Intent intent5 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent5);
                break;
            case R.id.myorder:
                //订单界面
                if (userInfo != null) {
                    Intent intent6 = new Intent(getActivity(), MyOrderListActivity.class);
                    startActivity(intent6);
                } else {
                    Intent intent7 = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent7);
                }
                break;
            case R.id.mycollection:
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    //进入个人收藏界面
                    Intent intent3 = new Intent(getActivity(), PersonalCollectionsActivity.class);
                    intent3.putExtra("userid", UserID);
                    startActivity(intent3);
                }
                break;
            case R.id.mycomment:
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    //进入个人评论界面
                    Intent intent3 = new Intent(getActivity(), PersonalCommentActivity.class);
                    intent3.putExtra("userid", UserID);
                    startActivity(intent3);
                }
                break;
            case R.id.mypoints:
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    //进入个人积分
                    Intent intent3 = new Intent(getActivity(), PersonalPointsActivity.class);
                    startActivity(intent3);
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    //订阅方法，当接收到事件的时候，会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("OUTLogin")) {
            ImageLoaderUtil.displayLocalImage(R.drawable.iman, imgFace);
            return;
        }
        if (messageEvent.getMessage().startsWith("h")) {
            ImageLoaderUtil.displayImageRoundIcon(messageEvent.getMessage(), imgFace);
            return;
        }
        ImageLoaderUtil.displayImageRoundIcon("file://" + messageEvent.getMessage(), imgFace);
    }
}
