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
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.ShowsDetailActivityNew;
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
 * @创建时间 2018/3/20 9:42
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CollectionActFragment extends Fragment {
    private View           mRootView;
    private ListView       mListview_act;
    private String         mKindFrom;//录入是从收藏过来，还是评论过来
    private ProgressDialog progressDialog;
    private String         typeFrom;//判断是从收藏过来，还是评论过来
    private String UserID = NetConfig.UserID;
    private List               list;
    private SmartRefreshLayout mSwipe_refresh;
    private String favActUrl = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_collection_act, null);
        initView();
        return mRootView;
    }

    private void initView() {
        mListview_act = (ListView) mRootView.findViewById(R.id.listview_act);
        mSwipe_refresh = (SmartRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //访问网络
            if ("Collection".equals(mKindFrom)) {
                typeFrom = "1";
                favActUrl = NetConfig.MAINFAVOURITELIST;
            }
            if ("Comment".equals(mKindFrom)) {
                typeFrom = "1";
                favActUrl = NetConfig.MAINCOMMENTLIST;
            }
            //访问网络
            getDataFromIntent(favActUrl);
        }
    }

    private void getDataFromIntent(String favActUrl) {
        list = new ArrayList();
        RequestParams params = new RequestParams();
        params.put("userid", UserID);
        params.put("favourite_type", typeFrom);
        params.put("comment_type", typeFrom);
        HttpUtil.get(favActUrl, params, new HttpUtil.JsonHttpResponseUtil() {
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
                //                mSwipe_refresh.finishLoadMore();
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
        MyAdapter myAdapter = new MyAdapter(list);
        mListview_act.setAdapter(myAdapter);
        //根据类别，判断是否有点击事件
//        if ("Collection".equals(mKindFrom)) {
            mListview_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //跳转活动详情
                    PersonalFavAndComInfo.ArrBean bean = (PersonalFavAndComInfo.ArrBean) list.get(position);
                    String url_address = bean.getUrl_address();
                    String actId = bean.getActivity_id();
                    String tb_name = bean.getTb_name();
                    Intent intent = new Intent(getActivity(), ShowsDetailActivityNew.class);
                    String actUrl = "";
                    if (tb_name.equals("z_arts")) {
                        actUrl = url_address + "searchArtsInfo";
                    } else if (tb_name.equals("z_library")) {
                        actUrl = url_address + "searchLibraryInfo";
                    } else if (tb_name.equals("z_museum")) {
                        actUrl = url_address + "searchMuseumInfo";
                    } else if (tb_name.equals("z_Sports")) {
                        actUrl = url_address + "searchSportsInfo";
                    } else if (tb_name.equals("z_Nation")) {
                        actUrl = url_address + "nationInfo";
                    } else if (tb_name.equals("z_Culture")) {
                        actUrl = url_address + "searchCultureInfo";
                    } else if (tb_name.equals("z_Theater")) {
                        actUrl = url_address + "searchTheaterInfo";
                    } else if (tb_name.equals("z_community")) {
                        actUrl = url_address + "searchCommunityInfo";
                    } else {
                        actUrl = url_address + "bigEventInfo";
                    }
                    intent.putExtra("modelId", 10);
                    intent.putExtra("collectActUrl", actUrl);
                    intent.putExtra("showImgUrl", bean.getGet_img_address());
                    intent.putExtra("filmRoom", actId);
                    intent.putExtra("url_address", url_address);
                    startActivity(intent);
                }
            });
//        }
//        if ("Comment".equals(mKindFrom)) {
//            mListview_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    //暂不做处理
//                }
//            });
//        }
        mSwipe_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //访问网络
                getDataFromIntent(favActUrl);
            }
        });
        //        mSwipe_refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
        //            @Override
        //            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //                //TODO：访问网络
        //            }
        //        });
    }

    //设置界面类别（评论还是收藏）
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
                    viewHolder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
                    viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
                    viewHolder.img_comment = (ImageView) convertView.findViewById(R.id.img_comment);
                    viewHolder.tv_comment_time = (TextView) convertView.findViewById(R.id.tv_comment_time);
                }
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            PersonalFavAndComInfo.ArrBean bean = (PersonalFavAndComInfo.ArrBean) mList.get(position);
            if ("Collection".equals(mKindFrom)) {
                String pic_address = bean.getPic_address();
                String imgUrl = bean.getGet_img_address() + pic_address;
                String activity_name = bean.getActivity_name();
                String begin_time = bean.getBegin_time();
                String end_time = bean.getEnd_time();
                String faddress = bean.getFaddress();

                ImageLoaderUtil.displayImageIcon(imgUrl, viewHolder.img_show);
                viewHolder.tv_show_name.setText(activity_name);
                if (begin_time.equals(end_time)) {
                    viewHolder.tv_show_time.setText(begin_time);
                } else {
                    viewHolder.tv_show_time.setText(begin_time + " - " + end_time);
                }
                viewHolder.tv_place.setText(faddress);
            }
            if ("Comment".equals(mKindFrom)) {
                String activity_name = bean.getActivity_name();
                String fmain_company = bean.getFmain_company();
                String comment_content = bean.getComment_content();
                String comment_date = bean.getComment_date();
                String comment_pic = bean.getComment_pic();

                viewHolder.tv_kind_name.setText(activity_name);
                viewHolder.tv_place.setText(fmain_company);
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
        ImageView img_show; //活动图
        TextView  tv_show_name; //活动名称
        TextView  tv_show_time;//活动时间
        TextView  tv_place;     //活动地点

        TextView  tv_kind_name;  //评论的活动标题
        TextView  tv_comment;    //评论内容
        ImageView img_comment; //评论图
        TextView  tv_comment_time;//评论时间
    }
}
