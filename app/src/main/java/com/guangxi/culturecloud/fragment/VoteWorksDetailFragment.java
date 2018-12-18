package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MyApplication;
import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.AtSceneActivity;
import com.guangxi.culturecloud.activitys.LoginActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.Square;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onekeyshare.OnekeyShare;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/16 20:43
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class VoteWorksDetailFragment extends Fragment implements View.OnClickListener {

    private View              mRootView;
    private TextView          mTv_vote_title;
    private TextView          mTv_works_comment;
    private ImageView         mImg_head;
    private ImageView         mImg_zan;
    private TextView          mTv_writer_name;
    private TextView          mTv_zan_number;
    private Button            mBt_vote;
    private Button            mBt_share;
    private PhotoviewFragment photoviewFragment;
    private LinearLayout      mLinear_ding;
    private String markFromInfo = "";
    private Button mBt_join_red;
    private Button mBt_to_top;
    private Square mSceneData;//传递过来的需投票信息
    private String mURLHeadString = NetConfig.IMG;
    private String UserID         = NetConfig.UserID;
    private ProgressDialog    progressDialog;
    private RecyclerView      mRecyclerView_show_pic;
    private ArrayList<String> mArrayList;//图片地址信息
    private GridLayoutManager mGridManager;
    private String ImageUrlOrPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521093612719&di=6e6a175f275d0efc26956315d4b07582&imgtype=0&src=http%3A%2F%2Fnews.sznews.com%2Fimages%2Fattachement%2Fjpg%2Fsite3%2F20171013%2FIMG6c0b840b679945782523231.jpg";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_vote_works_detail, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mLinear_ding = (LinearLayout) mRootView.findViewById(R.id.linear_ding);
        mTv_vote_title = (TextView) mRootView.findViewById(R.id.tv_vote_title);
        mTv_works_comment = (TextView) mRootView.findViewById(R.id.tv_works_comment);
        mImg_head = (ImageView) mRootView.findViewById(R.id.img_head);
        mImg_zan = (ImageView) mRootView.findViewById(R.id.img_zan);
        mTv_writer_name = (TextView) mRootView.findViewById(R.id.tv_writer_name);
        mTv_zan_number = (TextView) mRootView.findViewById(R.id.tv_zan_number);
        mBt_vote = (Button) mRootView.findViewById(R.id.bt_vote);
        mBt_share = (Button) mRootView.findViewById(R.id.bt_share);

        mRecyclerView_show_pic = (RecyclerView) mRootView.findViewById(R.id.recyclerView_show_pic);
        if (mSceneData != null) {
            setDataToView();
            AtSceneActivity activity = (AtSceneActivity) getActivity();
            ImageView img_no_intnet = (ImageView) activity.findViewById(R.id.img_no_intnet);
            if (img_no_intnet != null) {
                img_no_intnet.setVisibility(View.GONE);
            }
        }
    }

    //设置投票界面数据，在提交fragment之前要设置.
    public void setVoteData(Square mSceneData) {
        this.mSceneData = mSceneData;
    }

    private void setDataToView() {
        String username = mSceneData.getUsername();
        mTv_vote_title.setText("请为“" + username + "”发布的作品投票");
        mTv_works_comment.setText(mSceneData.getFcontent());
        ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + mSceneData.getNewHeadpic(), mImg_head);
        mTv_writer_name.setText(username);
        int zan = mSceneData.getZan();
        if (zan == 0) {
            mImg_zan.setImageResource(R.drawable.sh_activity_like_np);
        } else {
            mImg_zan.setImageResource(R.drawable.sh_activity_like_p);
        }
        mTv_zan_number.setText("" + mSceneData.getGc_zanSum());
        List<Square.PicListBean> picList = mSceneData.getPicList();
        if (picList.size() == 0) {
            mRecyclerView_show_pic.setVisibility(View.GONE);
        } else {
            if (picList.size() > 3) {
                mGridManager = new GridLayoutManager(getActivity(), 3);
            } else {
                mGridManager = new GridLayoutManager(getActivity(), picList.size());
            }
            mRecyclerView_show_pic.setLayoutManager(mGridManager);
            mArrayList = new ArrayList<String>();
            for (int i = 0; i < picList.size(); i++) {
                mArrayList.add(String.valueOf(picList.get(i).getNewFpic()));
            }
            MyAdapter myAdapter = new MyAdapter(mArrayList);
            mRecyclerView_show_pic.setAdapter(myAdapter);
        }
    }

    private void initData() {
        mLinear_ding.setOnClickListener(this);
        //mImg_works_show.setOnClickListener(this);
        mImg_zan.setOnClickListener(this);
        mBt_vote.setOnClickListener(this);
        mBt_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //            case R.id.img_works_show:
            //                Activity atSceneActivity = getActivity();
            //                if (atSceneActivity instanceof AtSceneActivity) {
            //                    mBt_to_top = (Button) getActivity().findViewById(R.id.bt_to_top);
            //                    mBt_to_top.setVisibility(View.GONE);
            //                }
            //                //展示图片信息
            //                FragmentTransaction mFt = getFragmentManager().beginTransaction();
            //                if (photoviewFragment == null) {
            //                    photoviewFragment = new PhotoviewFragment();
            //                }
            //                //进行fragment操作:
            //                mFt.add(R.id.frame_initial, photoviewFragment, "photoviewFragment");
            //                mFt.addToBackStack(null);
            //                //提交事务
            //                mFt.commit();
            //                break;
            case R.id.img_zan:
                //给作者点赞
                VoteForWriter("0");
                break;
            case R.id.bt_vote:
                //连接网络，投票,成功改变点赞信息
                //给作者投票。
                VoteForWriter("2");
                break;
            case R.id.bt_share:
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    OnekeyShare oks = new OnekeyShare();
                    oks.setCallback(new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                            //分享完成后提交分享积分
                            sendIntnetShare();
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {
                            Toast.makeText(getActivity(), "分享失败" + throwable, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {

                        }
                    });
                    //关闭sso授权
                    oks.disableSSOWhenAuthorize();
                    String url = "http://220.248.107.62:8084/whyapi/index.html?id=" + "40288a60609c3d2e01609c589c520018" + "&tb_type=" + "泥城影剧院排片-帕丁顿熊22";
                    // title标题，微信、QQ和QQ空间等平台使用
                    oks.setTitle("文化云·我在现场");
                    // titleUrl QQ和QQ空间跳转链接
                    oks.setTitleUrl(url);
                    // text是分享文本，所有平台都需要这个字段
                    oks.setText(mSceneData.getGc_type());
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    //                    oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                    if (ImageUrlOrPath != null && ImageUrlOrPath.contains("/sdcard/")) {
                        //imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        oks.setImagePath(ImageUrlOrPath);
                    } else if (ImageUrlOrPath != null) {
                        oks.setImageUrl(ImageUrlOrPath); //网络地址
                    }
                    // url在微信、微博，Facebook等平台中使用
                    oks.setUrl(url);
                    // comment是我对这条分享的评论，仅在人人网使用
                    //                oks.setComment("我是测试评论文本");
                    // site是分享此内容的网站名称，仅在QQ空间使用
                    oks.setSite(getString(R.string.app_name));
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                    oks.setSiteUrl(url);
                    oks.setDialogMode();
                    oks.setSilent(false);
                    // 启动分享GUI
                    oks.show(getActivity());
                }
                break;
            case R.id.linear_ding:
                break;
        }
    }

    //分享完后，
    private void sendIntnetShare() {
        RequestParams params = new RequestParams();
        params.put("user_id", UserID);
        params.put("activity_id", mSceneData.getId());
        HttpUtil.get(NetConfig.INSERT_SHARE, params, new HttpUtil.JsonHttpResponseUtil() {
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
                ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                String message = clubDetailInfo.getMessage();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void VoteForWriter(String type) {
        UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        if (UserID.equals(NetConfig.UserID)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            //发送投票请求
            getVote(type);
        }
    }

    private void getVote(final String type) {
        String insertZanCol = NetConfig.INSERT_ZANCOL;
        String z_zan_id = mSceneData.getId();
        RequestParams params = new RequestParams();
        params.put("userid", UserID);
        params.put("z_zan_id", z_zan_id);
        params.put("ftype", type);
        params.put("area_name", MyApplication.cityName);
        HttpUtil.get(insertZanCol, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(getActivity(), "正在提交投票请求，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    //隐藏没有网络图片
                    ImageView img_no_intnet = (ImageView) getActivity().findViewById(R.id.img_no_intnet);
                    if (img_no_intnet != null) {
                        img_no_intnet.setVisibility(View.GONE);
                    }
                    Gson gson = new Gson();
                    ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    ClubDetailInfo.ArrBean arr = clubDetailInfo.getArr();
                    String result = arr.getResult();
                    String message = arr.getMessage();
                    //                    if (result.equals("2") && message.equals("操作成功")) {
                    //                        if (type.equals("0")) {
                    //                            mTv_zan_number.setText("" + (mSceneData.getGc_zanSum() + 1));
                    //                        }
                    //                    }
                    if (result.equals("2")) {
                        int number = Integer.parseInt((String) mTv_zan_number.getText());
                        if ("0".equals(type)) {//赞
                            if ("操作成功".equals(message)) {
                                mImg_zan.setImageResource(R.drawable.sh_activity_like_p);
                                mTv_zan_number.setText("" + (number + 1));
                            } else if ("取消成功".equals(message)) {
                                mImg_zan.setImageResource(R.drawable.sh_activity_like_np);
                                mTv_zan_number.setText("" + (number - 1));
                            }
                        } else if ("2".equals(type)) {//投票

                        }
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setMarkFromInfo(String markFromInfo) {
        this.markFromInfo = markFromInfo;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            if (markFromInfo.equals("AtSceneHomeFragment")) {
                if (!mBt_join_red.isShown()) {
                    mBt_join_red.setVisibility(View.VISIBLE);
                }
            }
            markFromInfo = "";
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<String> mData;

        public MyAdapter(ArrayList<String> data) {
            this.mData = data;
        }

        @Override
        public int getItemCount() {

            return mData.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_recycler_newitem, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            //             绑定数据
            ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + mData.get(position), holder.mImg);
            holder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity atSceneActivity = getActivity();
                    if (atSceneActivity instanceof AtSceneActivity) {
                        mBt_to_top = (Button) getActivity().findViewById(R.id.bt_to_top);
                        mBt_to_top.setVisibility(View.GONE);
                    }
                    //展示图片信息
                    FragmentTransaction mFt = getFragmentManager().beginTransaction();
                    photoviewFragment = new PhotoviewFragment();
                    photoviewFragment.setImgUrlData(mArrayList);
                    photoviewFragment.setViewPagerCurrentItem(position);
                    //进行fragment操作:
                    mFt.add(R.id.frame_initial, photoviewFragment, "photoviewFragment");
                    mFt.addToBackStack(null);
                    //提交事务
                    mFt.commit();
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mImg;
            Button    bt_over;

            public ViewHolder(View itemView) {
                super(itemView);
                mImg = (ImageView) itemView.findViewById(R.id.img_picture_show);
                bt_over = (Button) itemView.findViewById(R.id.bt_over);
            }
        }
    }

    //判断是游客id还是用户id
    public void getOrSetUserID() {
        UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
    }
}
