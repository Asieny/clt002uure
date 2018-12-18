package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/17 9:55
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class AtSceneHomeFragment extends Fragment {
    private View         mRootView;
    private Button       mBt_join_red;
    private Button       mBt_to_top;
    private RecyclerView mRecyclerView_title;
    private RecyclerView mRecyclerView_comment;
    private String[] mStringsTitle = {"全部", "长宁艺术文化中心", "中华艺术馆", "大剧院", "东方艺术中心", "大众社区", "文化社区", "其他"};
    private ArrayList<Square>       mArrayList;//首页条目数据
    private PhotoviewFragment       photoviewFragment;
    private VoteWorksDetailFragment voteWorksDetailFragment;//具体某个投票界面
    private ProgressDialog          progressDialog;
    private ArrayList<String> mImgUrlList  = new ArrayList<>();//图片地址信息
    private String            UserID       = NetConfig.UserID;//游客id
    private String            markerShowId = "";
    private TakePartInSceneFragment mTakePartInSceneFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_at_scene_home, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mRecyclerView_title = (RecyclerView) mRootView.findViewById(R.id.recyclerView_title);
        mRecyclerView_comment = (RecyclerView) mRootView.findViewById(R.id.recyclerView_comment);

        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            mBt_join_red.setVisibility(View.VISIBLE);
            mBt_join_red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction mFt = fragmentManager.beginTransaction();
                    //首先展示投票界面
                    mTakePartInSceneFragment = new TakePartInSceneFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame_tab, mTakePartInSceneFragment, "mTakePartInSceneFragment");
                    //添加到回退栈
                    mFt.addToBackStack(null);
                    //提交事务
                    mFt.commit();
                    mBt_join_red.setVisibility(View.GONE);
                }
            });
        }
    }

    private void initData() {
        mArrayList = new ArrayList<Square>();
        StaggeredGridLayoutManager staggeredManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView_title.setLayoutManager(staggeredManager);
        MyTitleRecAdapter myTitleRecAdapter = new MyTitleRecAdapter(mStringsTitle);
        mRecyclerView_title.setAdapter(myTitleRecAdapter);

        //访问网络
        getIntnetData();
    }

    private void getIntnetData() {
        getOrSetUserID();
        String mSquareUrl = NetConfig.GCZAN_LIST;
        RequestParams param = new RequestParams();
        param.put("userid", UserID);
        HttpUtil.get(mSquareUrl, param, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(getActivity(), "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Gson gson = new Gson();
                //隐藏没有网络图片
                ImageView img_no_intnet = (ImageView) getActivity().findViewById(R.id.img_no_intnet);
                if (img_no_intnet != null) {
                    img_no_intnet.setVisibility(View.GONE);
                }
                Square square;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("arr");
                    mArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject squareData = jsonArray.getJSONObject(i);
                        square = gson.fromJson(squareData.toString(), Square.class);

                        //设置完数据，放入list集合
                        if (square.getFtype().equals("1")) {
                            mArrayList.add(square);
                        }
                    }
                    setIntentData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("LoveSocietyActivity", "json解析出现了问题");
                }
            }
        });
    }

    private void setIntentData() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView_comment.setLayoutManager(gridLayoutManager);
        MyCommentRecAdapter myCommentRecAdapter = new MyCommentRecAdapter(mArrayList);
        mRecyclerView_comment.setAdapter(myCommentRecAdapter);
        mRecyclerView_comment.setNestedScrollingEnabled(false);

    }

    public class MyCommentRecAdapter extends RecyclerView.Adapter<MyCommentRecAdapter.CommemtViewHolder> {

        private List<Square> mData;

        public MyCommentRecAdapter(ArrayList<Square> data) {
            this.mData = data;
        }

        @Override
        public CommemtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_atscene_home_comment, parent, false);
            // 实例化viewholder
            CommemtViewHolder viewHolder = new CommemtViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final CommemtViewHolder holder, final int position) {
            // 绑定数据
            //TODO:
            final Square mItemSquare = mData.get(position);
            ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + mItemSquare.getNewHeadpic(), holder.img_record);
            holder.tv_uer_name.setText(mItemSquare.getUsername());
            holder.tv_user_speek.setText(mItemSquare.getFcontent());
            List<Square.PicListBean> picList = mItemSquare.getPicList();
            if (picList.size() == 0) {
                holder.img_usr_show.setVisibility(View.GONE);
            } else {
                ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + mItemSquare.getPicList().get(0).getFpic(), holder.img_usr_show);
            }
            int zan = mItemSquare.getZan();
            if (zan == 0) {
                holder.img_zan.setImageResource(R.drawable.sh_activity_like_n);
            } else {
                holder.img_zan.setImageResource(R.drawable.sh_activity_like_p);
            }
            holder.tv_zan_number.setText("" + mItemSquare.getGc_zanSum());

            holder.img_usr_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity atSceneActivity = getActivity();
                    if (atSceneActivity instanceof AtSceneActivity) {
                        mBt_to_top = (Button) getActivity().findViewById(R.id.bt_to_top);
                        mBt_to_top.setVisibility(View.GONE);
                    }

                    FragmentTransaction mFt = getFragmentManager().beginTransaction();
                    //展示图片信息
                    //                    if (photoviewFragment == null) {
                    //                        photoviewFragment = new PhotoviewFragment();
                    //                    }
                    photoviewFragment = new PhotoviewFragment();
                    //进行fragment操作:
                    mFt.add(R.id.frame_initial, photoviewFragment, "photoviewFragment");
                    photoviewFragment.setMarkFromInfo("AtSceneHomeFragment");
                    mFt.addToBackStack(null);
                    Square square = mData.get(position);
                    List<Square.PicListBean> picList = square.getPicList();
                    mImgUrlList.clear();
                    for (int i = 0; i < picList.size(); i++) {
                        mImgUrlList.add(picList.get(i).getFpic());
                    }
                    photoviewFragment.setImgUrlData(mImgUrlList);
                    //提交事务
                    mFt.commit();
                    mBt_join_red.setVisibility(View.GONE);
                }
            });
            holder.bt_canvassing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBt_join_red.setVisibility(View.GONE);
                    FragmentTransaction mFt = getFragmentManager().beginTransaction();
                    //展示投票界面
                    if (voteWorksDetailFragment == null) {
                        voteWorksDetailFragment = new VoteWorksDetailFragment();
                    }
                    //进行fragment操作:
                    mFt.add(R.id.frame_tab, voteWorksDetailFragment, "HomeVoteWorksDetailFragment");
                    voteWorksDetailFragment.setMarkFromInfo("AtSceneHomeFragment");
                    Square square = mData.get(position);
                    voteWorksDetailFragment.setVoteData(square);
                    //提交事务
                    mFt.addToBackStack(null);
                    mFt.commit();
                }
            });
            holder.linear_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接提交网络投票
                    markerShowId = mItemSquare.getId();
                    addZan(holder.tv_zan_number, holder.img_zan);
                }
            });
            holder.img_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class CommemtViewHolder extends RecyclerView.ViewHolder {
            CircleImageView img_record;
            ImageView       img_usr_show;
            ImageView       img_zan;
            TextView        tv_uer_name;
            TextView        tv_user_speek;
            TextView        tv_zan_number;
            Button          bt_canvassing;
            LinearLayout    linear_zan;

            public CommemtViewHolder(View itemView) {
                super(itemView);
                img_record = (CircleImageView) itemView.findViewById(R.id.img_record);
                img_usr_show = (ImageView) itemView.findViewById(R.id.img_usr_show);
                img_zan = (ImageView) itemView.findViewById(R.id.img_zan);
                tv_uer_name = (TextView) itemView.findViewById(R.id.tv_uer_name);
                tv_user_speek = (TextView) itemView.findViewById(R.id.tv_user_speek);
                bt_canvassing = (Button) itemView.findViewById(R.id.bt_canvassing);
                linear_zan = (LinearLayout) itemView.findViewById(R.id.linear_zan);
                tv_zan_number = (TextView) itemView.findViewById(R.id.tv_zan_number);
            }
        }
    }

    private void addZan(final TextView tv_zan_number, final ImageView img_zan) {
        getOrSetUserID();
        if (UserID.equals(NetConfig.UserID)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            String insertZanCol = NetConfig.INSERT_ZANCOL;
            RequestParams param = new RequestParams();
            param.put("userid", UserID);
            param.put("z_zan_id", markerShowId);
            param.put("ftype", "0");
            HttpUtil.get(insertZanCol, param, new HttpUtil.JsonHttpResponseUtil() {
                @Override
                public void onStart() {
                    super.onStart();
                    ProgressDialogUtil.startShow(getActivity(), "正在上传，请稍后");
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    ProgressDialogUtil.hideDialog();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Gson gson = new Gson();
                    ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    ClubDetailInfo.ArrBean arr = clubDetailInfo.getArr();
                    String message = arr.getMessage();
                    if (statusCode == 200) {
                        String result = arr.getResult();
                        if (result.equals("2")) {
                            String text = (String) tv_zan_number.getText();
                            int num = Integer.parseInt(text);
                            if ("操作成功".equals(message)) {
                                img_zan.setImageResource(R.drawable.sh_activity_like_p);
                                tv_zan_number.setText("" + (num + 1));
                            } else if ("取消成功".equals(message)) {
                                img_zan.setImageResource(R.drawable.sh_activity_like_n);
                                tv_zan_number.setText("" + (num - 1));
                            }
                        }
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getOrSetUserID() {
        UserInfo userInfo = SPref.getObject(getActivity(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
    }

    public class MyTitleRecAdapter extends RecyclerView.Adapter<MyTitleRecAdapter.ViewHolder> {

        private String[] mData;

        public MyTitleRecAdapter(String[] data) {
            this.mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_text_item, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            //TODO:
            String text_string = mData[position];
            holder.mLabel.setText(text_string);
            if (position == 0) {
                holder.mLabel.setBackgroundResource(R.drawable.bg_normol_red);
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mLabel;  //标签

            public ViewHolder(View itemView) {
                super(itemView);
                mLabel = (TextView) itemView.findViewById(R.id.tv_label);
            }
        }
    }
}
