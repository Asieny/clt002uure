package com.jarvis.mytaobao.cart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.AtSceneActivity;
import com.guangxi.culturecloud.activitys.LoginActivity;
import com.guangxi.culturecloud.activitys.ShowsDetailActivityNew;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.Square;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cn.com.mytest.util.SPref;
import cz.msebera.android.httpclient.Header;

/**
 * 购物车主界面
 * 广场界面
 *
 * @author
 */
public class Cart_F extends Fragment {
    private TextView bt_cart_all, bt_cart_low, bt_cart_stock;
    private View show_cart_all, show_cart_low, show_cart_stock;
    private AllBaby_F   allBaby_F;
    private LowBaby_F   lowBaby_F;
    private StockBaby_F stockBaby_F;
    private boolean isDel = true;

    private View                mView;
    //    private SwipeRefreshLayout  mSwipe_refresh;//下拉控件
    private SmartRefreshLayout  mSmart_refresh;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView        mRecyclerView;
    private ArrayList           mArrayList;//广场数据
    private ProgressDialog      progressDialog;
    private        String mURLHeadString = NetConfig.IMG;
    private        String UserID         = NetConfig.UserID;
    private static int    actForResult   = 10086;
    private MyRecAdapter myRecdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.cart_f, null);
        initView();
        return mView;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        mSmart_refresh = (SmartRefreshLayout) mView.findViewById(R.id.smart_refresh);
        //        mSwipe_refresh = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);
        //        mSwipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        //            @Override
        //            public void onRefresh() {
        //                getIntnetData();
        //
        //            }
        //        });
        mArrayList = new ArrayList<>();
        getIntnetData();
        mSmart_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getIntnetData();
            }
        });
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
                //                mSwipe_refresh.setRefreshing(true);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
                //                mSwipe_refresh.setRefreshing(false);
                mSmart_refresh.finishRefresh();
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
                }
            }
        });
    }

    private void setIntentData() {
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (null==myRecdapter){
            myRecdapter= new MyRecAdapter(mArrayList);
            mRecyclerView.setAdapter(myRecdapter);
        }else {
            myRecdapter.notifyDataSetChanged();
        }
    }

    class MyRecAdapter extends RecyclerView.Adapter<MyRecAdapter.ViewHolder> {
        private List mData;
        public static final int XianChangType = 0;//类型现场
        public static final int DianZanType   = 1;//类型点赞活动
        public static final int footType      = 2;//类型03_尾部

        public MyRecAdapter(List data) {
            this.mData = data;
        }

        @Override
        public int getItemViewType(int position) {
//            if (position == (mData.size() - 1)) {
////                return footType;
//            }
            Square square = (Square) mData.get(position);
            String ftype = square.getFtype();
            if (ftype.equals("1")) {
                return XianChangType;//填充在现场布局
            } else if (ftype.equals("0")) {
                return DianZanType;//填充点赞布局
            }
            //但是不太可能走到这里
            return DianZanType;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            //根据类型去返回不同的view
            switch (viewType) {
                case XianChangType:
                    // 实例化展示的view
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_recyclerview_item_one, parent, false);
                    // 不让父控件忽略子控件的宽度
                    RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.setLayoutParams(params);
                    break;
                case DianZanType:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.square_recyclerview_item_two, parent, false);
                    // 不让父控件忽略子控件的宽度
                    RecyclerView.LayoutParams paramsZan = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.setLayoutParams(paramsZan);
                    break;
                case footType:
                    view = View.inflate(parent.getContext(), R.layout.footview_layout, null);
                    RecyclerView.LayoutParams paramsFoot = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    view.setLayoutParams(paramsFoot);
                    break;
            }
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            //TODO:将获取的数据填充
            if (position == mData.size() - 1) {
                return;
            }
            final Square square = (Square) mData.get(position);
            final String ftype = square.getFtype();

            if (ftype.equals("1")) {//现场数据填充
                String newHeadpicUrl = NetConfig.HEAD_IMG + square.getNewHeadpic();
                //用户头像
                ImageLoaderUtil.displayImageIcon(newHeadpicUrl, holder.img_log);
                holder.tv_name.setText(square.getUsername());
                String zan_date = square.getZan_date();
                //获取发表时间与现在时间的差
                String fromNow = judgeTimeByNow(zan_date);
                //拼接显示富文本
                String title = fromNow + "参加了" + "<font color='red'>" + square.getGc_type() + "</font>";
                Spanned spanned = Html.fromHtml(title);
                //小标题：时间参加。。。
                holder.tv_title.setText(spanned);
                //现场照，目前只显示第一张
                //                ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(0).getNewFpic(), holder.img_show);
                //现场照，显示多张
                List<Square.PicListBean> picList = square.getPicList();
                if (picList.size() == 0) {
                    holder.linear_total.setVisibility(View.GONE);
                } else if (picList.size() == 1) {
                    holder.linear_total.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(0).getNewFpic(), holder.img_show1);
                    holder.linear_more_pic.setVisibility(View.GONE);
                } else if (picList.size() == 2) {
                    holder.linear_total.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(0).getNewFpic(), holder.img_show1);
                    holder.linear_more_pic.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(1).getNewFpic(), holder.img_show2);
                } else if (picList.size() == 3) {
                    holder.linear_total.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(0).getNewFpic(), holder.img_show1);
                    holder.linear_more_pic.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(1).getNewFpic(), holder.img_show2);
                    holder.relative_more.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(2).getNewFpic(), holder.img_show3);
                    holder.tv_more.setVisibility(View.GONE);
                } else {
                    holder.linear_total.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(0).getNewFpic(), holder.img_show1);
                    holder.linear_more_pic.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(1).getNewFpic(), holder.img_show2);
                    holder.relative_more.setVisibility(View.VISIBLE);
                    ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + square.getPicList().get(2).getNewFpic(), holder.img_show3);
                    holder.tv_more.setVisibility(View.VISIBLE);
                    holder.tv_more.setText("+" + (picList.size() - 3));
                }
                //用户发表的内容
                holder.tv_comment.setText(square.getFcontent());
                //用户是否点赞
                int zan = square.getZan();
                int diszan = square.getDiszan();
                if (zan == 0) {
                    holder.img_zan.setImageResource(R.drawable.sh_activity_like_np);
                } else {
                    holder.img_zan.setImageResource(R.drawable.sh_activity_like_p);
                }
                if (diszan == 0) {
                    holder.img_cai.setImageResource(R.drawable.sh_activity_unlike_np);
                } else {
                    holder.img_cai.setImageResource(R.drawable.sh_activity_unlike_p);
                }
                //点赞数
                holder.tv_zan.setText("" + square.getGc_zanSum());
                //踩数
                holder.tv_cai.setText("" + square.getGc_disZanSum());
                holder.mLinear_detail.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AtSceneActivity.class);
                        intent.putExtra("ShowDetails", true);
                        AtSceneActivity.mSquare = (Square) mData.get(position);
                        startActivity(intent);
                    }
                });
            } else if (ftype.equals("0") || ftype.equals("3")) {//点赞活动条目数据填充
                String newHeadpicUrl = NetConfig.HEAD_IMG + square.getNewHeadpic();
                //用户头像
                ImageLoaderUtil.displayImageIcon(newHeadpicUrl, holder.img_log);
                holder.tv_name.setText(square.getUsername());
                String zan_date = square.getZan_date();
                //获取发表时间与现在时间的差
                String fromNow = judgeTimeByNow(zan_date);
                //拼接显示富文本
                String title = "";
                if (ftype.equals("0")) {
                    title = fromNow + "点赞了一个" + "<font color='blue'>" + "活动" + "</font>";
                } else if (ftype.equals("3")) {
                    String gc_type = square.getGc_type();
                    title = fromNow + "投票了一个" + "<font color='blue'>" + gc_type + "</font>";
                }
                Spanned spanned = Html.fromHtml(title);
                //小标题：时间点赞了一个活动。。。
                holder.tv_title.setText(spanned);
                if (ftype.equals("0")) {
                    ImageLoaderUtil.displayImageIcon(square.getGet_img_address() + square.getPic_address(), holder.img_show);
                } else if (ftype.equals("3")) {
                    List<Square.PicListBean> picList = square.getPicList();
                    String newFpic = picList.get(0).getNewFpic();
                    ImageLoaderUtil.displayImageIcon(square.getGet_img_address() + newFpic, holder.img_show);
                }
                //活动的名称或者现场内容
                String fname = "";
                if (ftype.equals("0")) {
                    fname = square.getActivity_name();
                } else if (ftype.equals("3")) {
                    fname = square.getFcontent();
                }
                holder.tv_show_name.setText(fname);
                //活动的时间缺少字段
                holder.tv_show_time.setText(zan_date);
                int zan = square.getZan();
                int diszan = square.getDiszan();
                if (zan == 0) {
                    holder.img_zan.setImageResource(R.drawable.sh_activity_like_np);
                } else {
                    holder.img_zan.setImageResource(R.drawable.sh_activity_like_p);
                }
                if (diszan == 0) {
                    holder.img_cai.setImageResource(R.drawable.sh_activity_unlike_np);
                } else {
                    holder.img_cai.setImageResource(R.drawable.sh_activity_unlike_p);
                }
                //点赞数
                holder.tv_zan.setText("" + square.getGc_zanSum());
                //踩数
                holder.tv_cai.setText("" + square.getGc_disZanSum());

                holder.mLinear_connect.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ftype.equals("0")) {
                            Intent intent = new Intent(getActivity(), ShowsDetailActivityNew.class);
                            Square square1 = (Square) mData.get(position);
                            String tb_name = square1.getTb_name();
                            String actId = square1.getActivity_id();
                            String url_address = square1.getUrl_address();
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
                            } else {
                                actUrl = url_address + "bigEventInfo";
                            }
                            intent.putExtra("modelId", 10);
                            intent.putExtra("collectActUrl", actUrl);
                            intent.putExtra("showImgUrl", square1.getGet_img_address());
                            intent.putExtra("filmRoom", actId);
                            intent.putExtra("url_address", url_address);
                            startActivity(intent);
                        } else if (ftype.equals("1")) {
                            Intent intent = new Intent(getActivity(), AtSceneActivity.class);
                            intent.putExtra("ShowDetails", true);
                            AtSceneActivity.mSquare = (Square) mData.get(position);
                            startActivity(intent);
                        }
                    }
                });
            }
            if (position == (mData.size() - 1)) {
                return;
            }

            holder.img_zan.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    zan_or_cai("0", square.getId(), holder.img_zan, holder.tv_zan);
                }
            });
            holder.img_cai.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    zan_or_cai("1", square.getId(), holder.img_cai, holder.tv_cai);
                }
            });

        }

        private void zan_or_cai(final String ftype, String z_zan_id, final ImageView img, final TextView textView) {
            String mSquareUrl = NetConfig.INSERT_ZANCOL;
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
                startActivityForResult(intent, actForResult);
            } else {
                RequestParams params = new RequestParams();
                params.put("userid", UserID);
                params.put("z_zan_id", z_zan_id);
                params.put("ftype", ftype);
                HttpUtil.get(mSquareUrl, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ProgressDialogUtil.startShow(getActivity(), "正在提交，请稍后");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        ProgressDialogUtil.hideDialog();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        if (statusCode == 200) {
                            Gson gson = new Gson();
                            ClubDetailInfo clubDetailInfo = gson.fromJson(jsonObject.toString(), ClubDetailInfo.class);
                            String result = clubDetailInfo.getArr().getResult();
                            String message = clubDetailInfo.getArr().getMessage();
                            if (result.equals("2")) {
                                int number = Integer.parseInt((String) textView.getText());
                                if ("0".equals(ftype)) {
                                    if ("操作成功".equals(message)) {
                                        img.setImageResource(R.drawable.sh_activity_like_p);
                                        textView.setText("" + (number + 1));
                                    } else if ("取消成功".equals(message)) {
                                        img.setImageResource(R.drawable.sh_activity_like_np);
                                        textView.setText("" + (number - 1));
                                    }
                                } else if ("1".equals(ftype)) {
                                    if ("操作成功".equals(message)) {
                                        img.setImageResource(R.drawable.sh_activity_unlike_p);
                                        textView.setText("" + (number + 1));
                                    } else if ("取消成功".equals(message)) {
                                        img.setImageResource(R.drawable.sh_activity_unlike_np);
                                        textView.setText("" + (number - 1));
                                    }
                                }
                            }
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView    img_log;       //用户头像
            TextView     tv_name;       //用户名称
            TextView     tv_title;      //小标题：时间，参加了我在现场
            ImageView    img_show;      //现场活动照//点赞活动照
            TextView     tv_comment;    //我在现场评论
            ImageView    img_zan;       //点赞图片
            ImageView    img_cai;       //踩图片
            TextView     tv_zan;        //点赞数
            TextView     tv_cai;        //踩数
            TextView     tv_show_name;//点赞活动的name
            TextView     tv_show_time;//点赞活动的time
            LinearLayout mLinear_detail;//参加现场类型的条目
            LinearLayout mLinear_connect;//点赞类型的条目

            ImageView      img_show1; //现场照1
            ImageView      img_show2; //现场照2
            ImageView      img_show3; //现场照2
            LinearLayout   linear_more_pic;//更多照片linear
            LinearLayout   linear_total;//总条目
            RelativeLayout relative_more;//更多
            TextView       tv_more;//显示更多文字

            public ViewHolder(View itemView) {
                super(itemView);
                img_log = (ImageView) itemView.findViewById(R.id.img_logo);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_title = (TextView) itemView.findViewById(R.id.tv_title);
                img_show = (ImageView) itemView.findViewById(R.id.img_show);
                tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
                img_zan = (ImageView) itemView.findViewById(R.id.img_zan);
                img_cai = (ImageView) itemView.findViewById(R.id.img_cai);
                tv_zan = (TextView) itemView.findViewById(R.id.tv_zan);
                tv_cai = (TextView) itemView.findViewById(R.id.tv_cai);

                tv_show_name = (TextView) itemView.findViewById(R.id.tv_show_name);
                tv_show_time = (TextView) itemView.findViewById(R.id.tv_show_time);
                mLinear_connect = (LinearLayout) itemView.findViewById(R.id.linear_connect);
                mLinear_detail = (LinearLayout) itemView.findViewById(R.id.linear_detail);

                img_show1 = (ImageView) itemView.findViewById(R.id.img_show1);
                img_show2 = (ImageView) itemView.findViewById(R.id.img_show2);
                img_show3 = (ImageView) itemView.findViewById(R.id.img_show3);
                linear_more_pic = (LinearLayout) itemView.findViewById(R.id.linear_more_pic);
                linear_total = (LinearLayout) itemView.findViewById(R.id.linear_total);
                relative_more = (RelativeLayout) itemView.findViewById(R.id.relative_more);
                tv_more = (TextView) itemView.findViewById(R.id.tv_more);
            }
        }
    }

    private String judgeTimeByNow(String date) {
        long millionSeconds = 0;
        String substring = date.substring(0, 19);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            //用户发表的时间/毫秒
            millionSeconds = sdf.parse(substring).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long nowTime = System.currentTimeMillis();
        long intervalTime = nowTime - millionSeconds;
        String spaceTime = getSpaceTime(intervalTime);
        return spaceTime;
    }

    public static String getSpaceTime(long time) {
        //间隔秒
        Long spaceSecond = time / 1000;
        //一分钟之内
        if (spaceSecond >= 0 && spaceSecond < 60) {
            return "刚刚";
        }
        //一小时之内
        else if (spaceSecond / 60 > 0 && spaceSecond / 60 < 60) {
            return spaceSecond / 60 + "分钟之前";
        }
        //一天之内
        else if (spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
            return spaceSecond / (60 * 60) + "小时之前";
        }
        //3天之内
        else if (spaceSecond / (60 * 60 * 24) > 0 && spaceSecond / (60 * 60 * 24) < 3) {
            return spaceSecond / (60 * 60 * 24) + "天之前";
        } else {
            //            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //            Date date = new Date(time);
            //            String dateStr = simpleDateFormat.format(date);
            //            return dateStr;
            return "3天之前";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (actForResult == requestCode) {
            getIntnetData();
        }
    }

    //	private void initView(View view) {

    //		bt_cart_all = (TextView) view.findViewById(R.id.bt_cart_all);
    //		bt_cart_low = (TextView) view.findViewById(R.id.bt_cart_low);
    //		bt_cart_stock = (TextView) view.findViewById(R.id.bt_cart_stock);
    //
    //		show_cart_all = view.findViewById(R.id.show_cart_all);
    //		show_cart_low = view.findViewById(R.id.show_cart_low);
    //		show_cart_stock = view.findViewById(R.id.show_cart_stock);
    //
    //		bt_cart_all.setOnClickListener(this);
    //		bt_cart_low.setOnClickListener(this);
    //		bt_cart_stock.setOnClickListener(this);
    //
    //		stockBaby_F = new StockBaby_F();
    //		addFragment(stockBaby_F);
    //		showFragment(stockBaby_F);
    //	}
    //
    //	@Override
    //	public void onClick(View v) {
    //		switch (v.getId()) {
    //		case R.id.tv_top_edit:
    //			if (allBaby_F!=null&&isDel) {
    //				removeFragment(allBaby_F);
    //				allBaby_F=null;
    //				allBaby_F=new AllBaby_F("删除");
    //				addFragment(allBaby_F);
    //				showFragment(allBaby_F);
    //				isDel=false;
    //				Data.Allprice_cart=0;
    //
    //			}else if (!isDel&&allBaby_F!=null) {
    //				removeFragment(allBaby_F);
    //				allBaby_F=null;
    //				allBaby_F=new AllBaby_F();
    //				addFragment(allBaby_F);
    //				showFragment(allBaby_F);
    //				isDel=true;
    //				Data.Allprice_cart=0;
    //			}
    //			break;
    //		case R.id.bt_cart_all:
    //			if (allBaby_F == null) {
    //				allBaby_F = new AllBaby_F();
    //				addFragment(allBaby_F);
    //				showFragment(allBaby_F);
    //			} else {
    //				showFragment(allBaby_F);
    //			}
    //			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Black));
    //			show_cart_low.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
    //			show_cart_stock.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
    //			break;
    //		case R.id.bt_cart_low:
    //			if (lowBaby_F == null) {
    //				lowBaby_F = new LowBaby_F();
    //				addFragment(lowBaby_F);
    //				showFragment(lowBaby_F);
    //			} else {
    //				showFragment(lowBaby_F);
    //			}
    //			show_cart_low.setBackgroundColor(getResources().getColor(R.color.bg_Black));
    //			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
    //			show_cart_stock.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
    //
    //			break;
    //		case R.id.bt_cart_stock:
    //			if (stockBaby_F == null) {
    //				stockBaby_F = new StockBaby_F();
    //				addFragment(stockBaby_F);
    //				showFragment(stockBaby_F);
    //			} else {
    //				showFragment(stockBaby_F);
    //			}
    //			show_cart_stock.setBackgroundColor(getResources().getColor(R.color.bg_Black));
    //			show_cart_all.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
    //			show_cart_low.setBackgroundColor(getResources().getColor(R.color.bg_Gray));
    //
    //			break;
    //
    //		default:
    //			break;
    //		}
    //	}
    //
    //	/** 添加Fragment **/
    //	public void addFragment(Fragment fragment) {
    //		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
    //		ft.add(R.id.show_cart_view, fragment);
    //		ft.commitAllowingStateLoss();
    //	}
    //	/** 删除Fragment **/
    //	public void removeFragment(Fragment fragment) {
    //		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
    //		ft.remove(fragment);
    //		ft.commitAllowingStateLoss();
    //	}
    //
    //	/** 显示Fragment **/
    //	public void showFragment(Fragment fragment) {
    //		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
    //		if (allBaby_F != null) {
    //			ft.hide(allBaby_F);
    //		}
    //		if (lowBaby_F != null) {
    //			ft.hide(lowBaby_F);
    //		}
    //		if (stockBaby_F != null) {
    //			ft.hide(stockBaby_F);
    //		}
    //
    //		ft.show(fragment);
    //		ft.commitAllowingStateLoss();
    //
    //	}

}
