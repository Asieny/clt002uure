package com.guangxi.culturecloud.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.activitys.AtSceneActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.Square;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/17 13:19
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class AtSceneRankingFragment extends Fragment implements View.OnClickListener {
    private View              mRootView;
    private Button            mBt_join_red;
    private Button            mBt_to_top;
    private RecyclerView      mRecyclerView_comment;
    private ArrayList         mArrayList;//排行榜的条目数据
    private LinearLayout      mLinear_gen;
    private PhotoviewFragment photoviewFragment;
    private ArrayList<String> imgUrlList; //点击对应的图片地址信息
    private ProgressDialog    progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_at_scene_ranking, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mRecyclerView_comment = (RecyclerView) mRootView.findViewById(R.id.recyclerView_comment);
        mLinear_gen = (LinearLayout) mRootView.findViewById(R.id.linear_gen);

        Activity atSceneActivity = getActivity();
        if (atSceneActivity instanceof AtSceneActivity) {
            mBt_join_red = (Button) getActivity().findViewById(R.id.bt_join_red);
            mBt_join_red.setVisibility(View.GONE);
        }
        mArrayList = new ArrayList();
        //        mArrayList.add("你好");
        //        mArrayList.add("你好");
        //        mArrayList.add("你好");
        //        mArrayList.add("你好");
    }

    private void initData() {
        getIntnetData();
    }

    private void getIntnetData() {
        String mSquareUrl = NetConfig.SERRANKING_GCACTIVITY_LIST;
        RequestParams params = new RequestParams();
        params.put("gc_type", "1");
        HttpUtil.get(mSquareUrl, params, new JsonHttpResponseHandler() {
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
                Square square;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("arr");
                    mArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject squareData = jsonArray.getJSONObject(i);
                        square = gson.fromJson(squareData.toString(), Square.class);
                        //设置完数据，放入list集合
                        mArrayList.add(square);
                    }
                    //                    mArrayList.add("footType");
                    setIntentData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("LoveSocietyActivity", "json解析出现了问题");
                }
            }
        });
    }

    private void setIntentData() {
        mLinear_gen.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView_comment.setLayoutManager(linearLayoutManager);
        MyCommentRecAdapter myCommentRecAdapter = new MyCommentRecAdapter(mArrayList);
        mRecyclerView_comment.setAdapter(myCommentRecAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //设置空点击，防止点击渗透
            case R.id.linear_gen:
                break;
        }
    }

    public class MyCommentRecAdapter extends RecyclerView.Adapter<MyCommentRecAdapter.CommemtViewHolder> {
        private List mData;

        public MyCommentRecAdapter(ArrayList data) {
            this.mData = data;
        }

        @Override
        public CommemtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_atscene_ranking_comment, parent, false);
            // 实例化viewholder
            CommemtViewHolder viewHolder = new CommemtViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CommemtViewHolder holder, final int position) {
            // 绑定数据
            //TODO:
            Square mItemSquare = (Square) mData.get(position);
            if (position >= 3) {
                holder.tv_number.setBackgroundResource(R.drawable.bg_normol_round_gray);
            }
            holder.tv_number.setText("" + (position + 1));
            ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG +mItemSquare.getPicList().get(0).getFpic(),holder.mImg_show);
            ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG+mItemSquare.getNewHeadpic(),holder.mCircleImg);
            holder.tv_uer_name.setText(mItemSquare.getUsername());
            holder.tv_content.setText(mItemSquare.getFcontent());
            holder.tv_zan_num.setText(""+mItemSquare.getGc_zanSum());

            holder.mImg_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity atSceneActivity = getActivity();
                    if (atSceneActivity instanceof AtSceneActivity) {
                        mBt_to_top = (Button) getActivity().findViewById(R.id.bt_to_top);
                        mBt_to_top.setVisibility(View.GONE);
                    }
                    //TODO:跳转图片可放大显示
                    FragmentTransaction mFt = getFragmentManager().beginTransaction();
                    //展示图片信息
                    if (photoviewFragment == null) {
                        photoviewFragment = new PhotoviewFragment();
                    }
                    //进行fragment操作:
                    mFt.add(R.id.frame_initial, photoviewFragment, "photoviewFragment");
                    mFt.addToBackStack(null);
                    Square mSquare = (Square) mData.get(position);
                    List<Square.PicListBean> picList = mSquare.getPicList();
                    if (imgUrlList == null) {
                        imgUrlList = new ArrayList<String>();
                    } else {
                        imgUrlList.clear();
                    }
                    for (int i = 0; i < picList.size(); i++) {
                        imgUrlList.add(picList.get(i).getFpic());
                    }
                    photoviewFragment.setImgUrlData(imgUrlList);
                    //提交事务
                    mFt.commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class CommemtViewHolder extends RecyclerView.ViewHolder {
            TextView  tv_number;  //名次
            TextView  tv_uer_name;  //姓名
            TextView  tv_content;  //内容
            TextView  tv_zan_num;  //获赞数
            ImageView mImg_show;
            CircleImageView mCircleImg;
            public CommemtViewHolder(View itemView) {
                super(itemView);
                tv_number = (TextView) itemView.findViewById(R.id.tv_number);
                tv_uer_name = (TextView) itemView.findViewById(R.id.tv_uer_name);
                tv_content = (TextView) itemView.findViewById(R.id.tv_content);
                tv_zan_num = (TextView) itemView.findViewById(R.id.tv_zan_num);
                mImg_show = (ImageView) itemView.findViewById(R.id.img_show);
                mCircleImg = (CircleImageView) itemView.findViewById(R.id.circleImg);
            }
        }
    }
}
