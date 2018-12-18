package com.guangxi.culturecloud.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.VenueDetailsActivity;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.PersonalFavAndComInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/20 10:00
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CollectionMusFragment extends Fragment {

    private View           mRootView;
    private ListView       mListview_act;
    private String         mKindFrom;//类型：收藏、评论
    private String         typeFrom;
    private List           list;
    private ProgressDialog progressDialog;
    private String UserID = NetConfig.UserID;
    private SmartRefreshLayout mSwipe_refresh;
    private String favMusUrl = "";//访问的网络地址
    private MyAdapter myAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_collection_act, null);
        initView();
        return mRootView;
    }

    private void initView() {
        mSwipe_refresh = (SmartRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        mListview_act = (ListView) mRootView.findViewById(R.id.listview_act);

        mSwipe_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //访问网络
                getDataFromIntent(favMusUrl);
            }
        });
        mListview_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转馆类详情
//                if ("Collection".equals(mKindFrom)) {
                    PersonalFavAndComInfo.ArrBean bean = (PersonalFavAndComInfo.ArrBean) list.get(position);
                    Intent intent = new Intent(getActivity(), VenueDetailsActivity.class);
                    String businessID = bean.getActivity_id();
                    String tb_name = bean.getTb_name();
                    intent.putExtra("fromKind", "10");
                    intent.putExtra("businessID", businessID);
                    intent.putExtra("tb_name", tb_name);
                    intent.putExtra("url_address", bean.getUrl_address());
                    intent.putExtra("get_img_address", bean.getGet_img_address());
                    startActivity(intent);
//                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if ("Collection".equals(mKindFrom)) {
            typeFrom = "2";
            favMusUrl = NetConfig.MAINFAVOURITELIST;
        }
        if ("Comment".equals(mKindFrom)) {
            typeFrom = "2";
            favMusUrl = NetConfig.MAINCOMMENTLIST;
        }
        //访问网络
        getDataFromIntent(favMusUrl);
    }

    private void getDataFromIntent(String favMusUrl) {
        if (null == list) {
            list = new ArrayList();
        } else {
            list.clear();
        }
        RequestParams params = new RequestParams();
        params.put("userid", UserID);
        params.put("favourite_type", typeFrom);
        params.put("comment_type", typeFrom);
        HttpUtil.get(favMusUrl, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(getActivity(), "正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
                mSwipe_refresh.finishRefresh();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getActivity());
                    return;
                }
                Gson gson = new Gson();
                PersonalFavAndComInfo personalFavAndComInfo = gson.fromJson(response.toString(), PersonalFavAndComInfo.class);
                List<PersonalFavAndComInfo.ArrBean> arr = personalFavAndComInfo.getArr();
                for (int i = 0; i < arr.size(); i++) {
                    list.add(arr.get(i));
                }
                initData();
            }
        });
    }

    private void initData() {
        if (null==myAdapter){
            myAdapter= new MyAdapter(list);
            mListview_act.setAdapter(myAdapter);
        }else {
            myAdapter.notifyDataSetChanged();
        }
    }

    public void setKind(String kind) {
        this.mKindFrom = kind;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }

    class MyAdapter extends BaseAdapter {
        List mList;

        public MyAdapter(List list) {
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new MyViewHolder();
                if ("Collection".equals(mKindFrom)) {
                    convertView = View.inflate(getActivity(), R.layout.list_item_collection, null);
                    viewHolder.img_show = (ImageView) convertView.findViewById(R.id.img_show);
                    viewHolder.tv_show_name = (TextView) convertView.findViewById(R.id.tv_show_name);
                    viewHolder.tv_show_time = (TextView) convertView.findViewById(R.id.tv_show_time);
                    viewHolder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
                }
                if ("Comment".equals(mKindFrom)) {
                    convertView = View.inflate(getActivity(), R.layout.list_item_comment, null);
                    viewHolder.tv_kind_name = (TextView) convertView.findViewById(R.id.tv_kind_name);
                    viewHolder.linear_place = (LinearLayout) convertView.findViewById(R.id.linear_place);
                    viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
                    viewHolder.tv_comment_time = (TextView) convertView.findViewById(R.id.tv_comment_time);
                    viewHolder.img_comment = (ImageView) convertView.findViewById(R.id.img_comment);
                }
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            PersonalFavAndComInfo.ArrBean bean = (PersonalFavAndComInfo.ArrBean) mList.get(position);
            if ("Collection".equals(mKindFrom)) {
                viewHolder.tv_show_time.setVisibility(View.GONE);
                String pic_address = bean.getPic_address();
                String imgUrl = bean.getGet_img_address() + pic_address;
                String activity_name = bean.getActivity_name();
                String begin_time = bean.getBegin_time();
                String faddress = bean.getFaddress();

                ImageLoaderUtil.displayImageIcon(imgUrl, viewHolder.img_show);
                viewHolder.tv_show_name.setText(activity_name);
                viewHolder.tv_show_time.setText(begin_time);
                viewHolder.tv_place.setText(faddress);
            }
            if ("Comment".equals(mKindFrom)) {
                viewHolder.linear_place.setVisibility(View.GONE);
                String activity_name = bean.getActivity_name();
                String fmain_company = bean.getFmain_company();
                String comment_content = bean.getComment_content();
                String comment_date = bean.getComment_date();
                String comment_pic = bean.getComment_pic();

                viewHolder.tv_kind_name.setText(activity_name);
                viewHolder.tv_comment.setText(comment_content);
                String substring = comment_date.substring(0, 16);
                viewHolder.tv_comment_time.setText(substring);
                if (null == comment_pic || comment_pic.equals("")) {
                    viewHolder.img_comment.setVisibility(View.GONE);
                } else {
                    viewHolder.img_comment.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + comment_pic, viewHolder.img_comment);
                }
            }
            return convertView;
        }
    }

    class MyViewHolder {
        ImageView img_show;
        TextView  tv_show_name;
        TextView  tv_show_time;
        TextView  tv_place;

        LinearLayout linear_place;  //地址
        TextView     tv_kind_name;  //评论的活动标题
        TextView     tv_comment;    //评论内容
        ImageView    img_comment; //评论图
        TextView     tv_comment_time;//评论时间
    }
}
