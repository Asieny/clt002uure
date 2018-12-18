package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.AdmireInfo;
import com.guangxi.culturecloud.model.ClubDetailInfo;
import com.guangxi.culturecloud.model.CommentInfo;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.model.VenueDetailsInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.jarvis.MyView.MyListView;
import com.jarvis.mytaobao.home.MapNavigationActivity;
import com.loopj.android.http.RequestParams;
import com.onekeyshare.OnekeyShare;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
 * @创建时间 2018/2/1 10:17
 * @描述 ${TODO}
 * @更新者 BaseActivity
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class VenueDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView                mIv_back;
    private ScrollView               mScrollView;
    private ProgressDialog           progressDialog;
    private VenueDetailsInfo.ArrBean neddAarr;
    private MyListView               mListview_venue_show;//场馆活动条目
    private TextView                 mTv_show_num;//总共活动信息number
    private WebView                  mWeb_detail;//场馆详情web
    private TextView                 mTv_collection_num;//藏品总数
    private RecyclerView             mRecyclerView_collection_pic;//藏品的图片展示墙
    private RecyclerView             mRecyclerView_head_pic;//点赞者的头像
    private MyListView               mLv_talk;//用户评论
    private ArrayList<String>        mHeadUrlList;//点赞头像地址集合
    private ArrayList                mShowList;//用户评论集合
    private ImageView                mImg_venue_picture;//场馆图片
    private String businessID     = "402881e86151d691016151d9cddf0005";//接口参数-标识
    private String tb_name        = "z_library";//接口参数-表名
    private String mHeadURLString = NetConfig.IMG;
    private TextView                                             mTv_label_1;
    private TextView                                             mTv_label_2;
    private TextView                                             mTv_label_3;
    private TextView                                             mTv_pay_kind;
    private TextView                                             mTv_venue_name;
    private TextView                                             mTv_place;
    private RelativeLayout                                       mRelative_to_mapview;
    private TextView                                             mTv_time;//开放时间
    private TextView                                             mTv_phone;
    private ArrayList<VenueDetailsInfo.ArrBean.ListEntryBean>    mListEntryBeen;//场馆活动详细信息
    private ArrayList<VenueDetailsInfo.ArrBean.ListGoodsBean>    mListGoodsBean;//场馆收藏品信息
    private ArrayList<VenueDetailsInfo.ArrBean.ListPlayroomBean> mListPlayroomBeen;//场馆活动室详细信息
    private TextView                                             mTv_to_talk;
    private ImageView                                            mImg_dianzan;
    private ImageView                                            mImg_shoucang;
    private Button                                               mBt_ask;
    private String  UserID     = NetConfig.UserID;
    private String  mShowId    = "";
    private boolean isDianZan  = false;
    private boolean isShouCang = false;
    private String        showName;
    private ImageView     mImg_no_intnet;
    private TextView      mTv_number_zan;//点赞总数
    private MyRecAdapter  mMyRecHeadAdapter;
    private TextView      mTv_comment;//用户评论数
    private TextView      mTv_more_talk;//更多评论
    private MyTalkAdapter mMyTalkAdapter;
    private double mLng = 120.99037;//经度
    private double mLat = 32.079313;//纬度
    private ImageView    mImg_share;
    private LinearLayout mLinear_act;//场馆活动总条目
    private LinearLayout mLinear_collection;//藏品总条目
    private LinearLayout mLinear_zanlist;//点赞总条目
    private LinearLayout mLinear_talk;//评论总条目
    private LinearLayout mLinear_actroom;//活动室总条目
    private TextView     mTv_artroom_num;//活动室数量
    private MyListView   mListview_actroom;//活动室分条目
    private String businessList = NetConfig.BUSINESS_INFO;
    private String mFromKind;
    private String mUrl_address;//服务器地址
    private int    mLoginRequestCode   = 1001;//启动登录页
    private int    mCommentRequestCode = 1006;//启动评论页
    private String ImageUrlOrPath      = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1521093612719&di=6e6a175f275d0efc26956315d4b07582&imgtype=0&src=http%3A%2F%2Fnews.sznews.com%2Fimages%2Fattachement%2Fjpg%2Fsite3%2F20171013%2FIMG6c0b840b679945782523231.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);
        Intent intent = getIntent();
        mUrl_address = NetConfig.URL_HEAD_ADDRESS;
        businessID = intent.getStringExtra("businessID");
        tb_name = intent.getStringExtra("tb_name");

        mFromKind = intent.getStringExtra("fromKind");
        if ("10".equals(mFromKind)) {
            mUrl_address = intent.getStringExtra("url_address");
            mHeadURLString = intent.getStringExtra("get_img_address");
            businessList = mUrl_address + "businessInfo";
        }
        initView();
    }

    private void initView() {
        mImg_share = (ImageView) findViewById(R.id.img_share);
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mImg_venue_picture = (ImageView) findViewById(R.id.img_venue_picture);
        mTv_label_1 = (TextView) findViewById(R.id.tv_label_1);
        mTv_label_2 = (TextView) findViewById(R.id.tv_label_2);
        mTv_label_3 = (TextView) findViewById(R.id.tv_label_3);
        mTv_pay_kind = (TextView) findViewById(R.id.tv_pay_kind);
        mTv_venue_name = (TextView) findViewById(R.id.tv_venue_name);
        mRelative_to_mapview = (RelativeLayout) findViewById(R.id.relative_to_mapview);
        mTv_place = (TextView) findViewById(R.id.tv_place);
        mTv_time = (TextView) findViewById(R.id.tv_time);
        TextView tv_time_warning = (TextView) findViewById(R.id.tv_time_warning);
        mTv_phone = (TextView) findViewById(R.id.tv_phone);
        mLinear_act = (LinearLayout) findViewById(R.id.linear_act);
        mTv_show_num = (TextView) findViewById(R.id.tv_show_num);
        mListview_venue_show = (MyListView) findViewById(R.id.listview_venue_show);
        mWeb_detail = (WebView) findViewById(R.id.web_detail);
        mLinear_collection = (LinearLayout) findViewById(R.id.linear_collection);
        mTv_collection_num = (TextView) findViewById(R.id.tv_collection_num);
        mRecyclerView_collection_pic = (RecyclerView) findViewById(R.id.recyclerView_collection_pic);
        mLinear_zanlist = (LinearLayout) findViewById(R.id.linear_zanlist);
        mTv_number_zan = (TextView) findViewById(R.id.tv_number_zan);
        mRecyclerView_head_pic = (RecyclerView) findViewById(R.id.recyclerView_head_pic);
        mTv_comment = (TextView) findViewById(R.id.tv_comment);
        mLinear_talk = (LinearLayout) findViewById(R.id.linear_talk);
        mTv_more_talk = (TextView) findViewById(R.id.tv_more_talk);
        mLv_talk = (MyListView) findViewById(R.id.lv_talk);
        mTv_to_talk = (TextView) findViewById(R.id.tv_to_talk);
        mImg_dianzan = (ImageView) findViewById(R.id.img_dianzan);
        mImg_shoucang = (ImageView) findViewById(R.id.img_shoucang);
        mBt_ask = (Button) findViewById(R.id.bt_ask);
        mImg_no_intnet = (ImageView) findViewById(R.id.img_no_intnet);
        mLinear_actroom = (LinearLayout) findViewById(R.id.linear_actroom);
        mTv_artroom_num = (TextView) findViewById(R.id.tv_artroom_num);
        mListview_actroom = (MyListView) findViewById(R.id.listview_actroom);
        getIntentData();
    }

    //    @Override
    //    protected void onResume() {
    //        super.onResume();
    //        getIntentData();
    //    }

    private void getIntentData() {
        if (mHeadUrlList == null) {
            mHeadUrlList = new ArrayList<>();
        } else {
            mHeadUrlList.clear();
        }
        if (mShowList == null) {
            mShowList = new ArrayList();
        } else {
            mShowList.clear();
        }
        if (mListEntryBeen == null) {
            mListEntryBeen = new ArrayList<>();
        } else {
            mListEntryBeen.clear();
        }
        if (mListGoodsBean == null) {
            mListGoodsBean = new ArrayList<>();
        } else {
            mListGoodsBean.clear();
        }
        if (mListPlayroomBeen == null) {
            mListPlayroomBeen = new ArrayList<>();
        } else {
            mListPlayroomBeen.clear();
        }
        getOrSetUserID();
        getIntnetData();
    }

    private void getIntnetData() {//NetConfig.BUSINESS_INFO
        RequestParams params = new RequestParams();
        params.put("id", businessID);
        params.put("tb_name", tb_name);
        params.put("member_id", UserID);
        HttpUtil.get(businessList, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(VenueDetailsActivity.this,"正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getBaseContext());
                    return;
                }
                Gson gson = new Gson();
                VenueDetailsInfo venueDetailsInfo = gson.fromJson(response.toString(), VenueDetailsInfo.class);
                VenueDetailsInfo.ArrBean arr = venueDetailsInfo.getArr();
                neddAarr = arr;
                setData(arr);
                //获取点赞信息和评论列表
                getZanAndTalkData();
            }
        });
    }

    private void getZanAndTalkData() {
        RequestParams params = new RequestParams();
        params.put("Activity_id", businessID);
        HttpUtil.get(mUrl_address + "zanList", params, new HttpUtil.JsonHttpResponseUtil() {
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
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getBaseContext());
                    return;
                }
                Gson gson = new Gson();
                AdmireInfo venueDetailsInfo = gson.fromJson(response.toString(), AdmireInfo.class);
                int countZan = venueDetailsInfo.getCountZan();
                List<AdmireInfo.ArrBean> arr = venueDetailsInfo.getArr();
                mTv_number_zan.setText("共" + countZan + "人赞过");
                if (mHeadUrlList != null) {
                    mHeadUrlList.clear();
                }
                for (int i = 0; i < arr.size(); i++) {
                    mHeadUrlList.add(arr.get(i).getHeadpic());
                }
                //没有点赞，隐藏整块条目
                if (mHeadUrlList.size() == 0) {
                    mLinear_zanlist.setVisibility(View.GONE);
                } else {
                    mLinear_zanlist.setVisibility(View.VISIBLE);
                    mMyRecHeadAdapter.notifyDataSetChanged();
                }
                //获取评论列表
                getTalkData();
            }
        });
    }

    private void getTalkData() {
        RequestParams params = new RequestParams();
        params.put("Activity_id", businessID);
        HttpUtil.get(mUrl_address + "commentList", params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode != 200) {
                    ToastUtils.makeShortText("网络错误", getBaseContext());
                    return;
                }
                mImg_no_intnet.setVisibility(View.GONE);
                Gson gson = new Gson();
                CommentInfo venueDetailsInfo = gson.fromJson(response.toString(), CommentInfo.class);
                List<CommentInfo.ArrBean> arr = venueDetailsInfo.getArr();
                mTv_comment.setText("共" + arr.size() + "条评论");
                if (arr.size() < 20) {
                    mTv_more_talk.setVisibility(View.GONE);
                }
                if (mShowList != null) {
                    mShowList.clear();
                }
                for (int i = 0; i < arr.size(); i++) {
                    mShowList.add(arr.get(i));
                }
                //没有评论，隐藏评论整体
                if (mShowList.size() == 0) {
                    mLinear_talk.setVisibility(View.GONE);
                } else {
                    mLinear_talk.setVisibility(View.VISIBLE);
                    mMyTalkAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setData(VenueDetailsInfo.ArrBean arr) {
        String label1 = arr.getLabel1();
        String label2 = arr.getLabel2();
        String label3 = arr.getLabel3();
        if (label1 == null || label1.equals("")) {
            mTv_label_1.setVisibility(View.GONE);
        } else {
            mTv_label_1.setText(label1);
        }
        if (label2 == null || label2.equals("")) {
            mTv_label_2.setVisibility(View.GONE);
        } else {
            mTv_label_2.setText(label2);
        }
        if (label3 == null || label3.equals("")) {
            mTv_label_3.setVisibility(View.GONE);
        } else {
            mTv_label_3.setText(label3);
        }
        String newpic = arr.getNewpic();
        ImageLoaderUtil.displayImageIcon(mHeadURLString + newpic, mImg_venue_picture);
        double price = arr.getPrice();
        if (price == 0) {
            mTv_pay_kind.setText("免费");
        } else {
            mTv_pay_kind.setText("" + price + "元/人");
        }
        mTv_venue_name.setText(arr.getBusiness_name());
        mTv_place.setText(arr.getBusiness_address());
        mTv_time.setText(arr.getOpen_time());
        mTv_phone.setText(arr.getTelephone());
        //场馆活动信息
        List<VenueDetailsInfo.ArrBean.ListEntryBean> listEntry = arr.getListEntry();
        for (int i = 0; i < listEntry.size(); i++) {
            mListEntryBeen.add(listEntry.get(i));
        }
        String actString = "共" + "<font color='blue'>" + mListEntryBeen.size() + "</font>" + "个活动";
        Spanned spanned = Html.fromHtml(actString);
        mTv_show_num.setText(spanned);
        //没有活动，将整个活动展示条目隐藏
        if (mListEntryBeen.size() == 0) {
            mLinear_act.setVisibility(View.GONE);
        }
        //场馆藏品信息
        List<VenueDetailsInfo.ArrBean.ListGoodsBean> listGoods = arr.getListGoods();
        for (int i = 0; i < listGoods.size(); i++) {
            mListGoodsBean.add(listGoods.get(i));
        }
        String collectionString = "共" + "<font color='blue'>" + mListGoodsBean.size() + "</font>" + "个藏品";
        Spanned spanned2 = Html.fromHtml(collectionString);
        mTv_collection_num.setText(spanned2);
        //没有藏品，将整个藏品展示条目隐藏
        if (mListGoodsBean.size() == 0) {
            mLinear_collection.setVisibility(View.GONE);
        }
        //活动室信息
        //获取网络活动室数据mListPlayroom
        List<VenueDetailsInfo.ArrBean.ListPlayroomBean> listPlayroom = arr.getListPlayroom();
        if (listPlayroom != null) {
            for (int i = 0; i < listPlayroom.size(); i++) {
                mListPlayroomBeen.add(listPlayroom.get(i));
            }
        }
        String actRoomString = "共" + "<font color='blue'>" + mListPlayroomBeen.size() + "</font>" + "个活动室";
        Spanned spanned3 = Html.fromHtml(actRoomString);
        mTv_artroom_num.setText(spanned3);
        if (mListPlayroomBeen.size() == 0) {
            mLinear_actroom.setVisibility(View.GONE);
        }
        //场馆详情
        String bussiness_detail = arr.getBussiness_detail();
        if (bussiness_detail == null || bussiness_detail.equals("")) {

        } else {
            mWeb_detail.loadDataWithBaseURL("", getNewContent(bussiness_detail), "text/html", "utf-8", "");
        }
        //是否点赞，是否收藏
        int isZan = arr.getIsZan();
        if (isZan == 0) {
            mImg_dianzan.setImageResource(R.drawable.ic_zan_select1);
            isDianZan = false;
        } else {
            mImg_dianzan.setImageResource(R.drawable.ic_zan_select2);
            isDianZan = true;
        }
        int isFavourite = arr.getIsFavourite();
        if (isFavourite == 0) {
            mImg_shoucang.setImageResource(R.drawable.ic_star_selected1);
            isShouCang = false;
        } else {
            mImg_shoucang.setImageResource(R.drawable.ic_star_selected2);
            isShouCang = true;
        }
        mShowId = arr.getId();
        showName = "z_business";
        mLat = arr.getLat();
        mLng = arr.getLng();
        initData();
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        mImg_share.setOnClickListener(this);
        //显示更多活动
        if (mListEntryBeen.size() > 0) {
            mTv_show_num.setOnClickListener(this);
        }
        //场馆活动
        MyShowAdapter myShowAdapter = new MyShowAdapter(mListEntryBeen);
        mListview_venue_show.setAdapter(myShowAdapter);
        mListview_venue_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VenueDetailsInfo.ArrBean.ListEntryBean listEntryBean = (VenueDetailsInfo.ArrBean.ListEntryBean) mListEntryBeen.get(position);
                String tb_name = listEntryBean.getTb_name();
                Intent intent = new Intent(getBaseContext(), ShowsDetailActivityNew.class);
                String actUrl = "";
                if (tb_name.equals("z_arts")) {
                    actUrl = mUrl_address + "searchArtsInfo";
                } else if (tb_name.equals("z_library")) {
                    actUrl = mUrl_address + "searchLibraryInfo";
                } else if (tb_name.equals("z_museum")) {
                    actUrl = mUrl_address + "searchMuseumInfo";
                } else if (tb_name.equals("z_Sports")) {
                    actUrl = mUrl_address + "searchSportsInfo";
                } else if (tb_name.equals("z_Nation")) {
                    actUrl = mUrl_address + "nationInfo";
                } else if (tb_name.equals("z_Culture")) {
                    actUrl = mUrl_address + "searchCultureInfo";
                } else if (tb_name.equals("z_Theater")) {
                    actUrl = mUrl_address + "searchTheaterInfo";
                } else if (tb_name.equals("z_community")) {
                    actUrl = mUrl_address + "searchCommunityInfo";
                } else {
                    actUrl = mUrl_address + "bigEventInfo";
                }
                intent.putExtra("modelId", 10);
                intent.putExtra("collectActUrl", actUrl);
                intent.putExtra("filmRoom", listEntryBean.getId());
                intent.putExtra("showImgUrl", mHeadURLString);
                intent.putExtra("url_address", mUrl_address);
                startActivity(intent);
            }
        });

        //藏品
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_collection_pic.setLayoutManager(linearLayoutManager);
        MyRecAdapter myRecCollectionAdapter = new MyRecAdapter(mListGoodsBean, "藏品");
        mRecyclerView_collection_pic.setAdapter(myRecCollectionAdapter);
        //        mRecyclerView_collection_pic.setNestedScrollingEnabled(false);

        //显示更多活动室
        if (mListEntryBeen.size() > 0) {
            mTv_artroom_num.setOnClickListener(this);
        }
        //活动室
        MyActRoomAdapter actRoomAdapter = new MyActRoomAdapter(mListPlayroomBeen);
        mListview_actroom.setAdapter(actRoomAdapter);
        mListview_actroom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转活动室详情
                Intent intent = new Intent(getBaseContext(), ActRoomDetailActivity.class);
                VenueDetailsInfo.ArrBean.ListPlayroomBean listPlayroomBean = mListPlayroomBeen.get(position);
                intent.putExtra("actRoomID", listPlayroomBean.getId());
                intent.putExtra("url_address", mUrl_address);
                intent.putExtra("get_img_address", mHeadURLString);
                startActivity(intent);
            }
        });
        //点赞用户头像
        LinearLayoutManager LayoutManagerHeadPic = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView_head_pic.setLayoutManager(LayoutManagerHeadPic);
        mMyRecHeadAdapter = new MyRecAdapter(mHeadUrlList, "头像");
        mRecyclerView_head_pic.setAdapter(mMyRecHeadAdapter);
        //用户评论
        mMyTalkAdapter = new MyTalkAdapter(mShowList);
        mLv_talk.setAdapter(mMyTalkAdapter);

        mTv_to_talk.setOnClickListener(this);
        mImg_dianzan.setOnClickListener(this);
        mImg_shoucang.setOnClickListener(this);
        mBt_ask.setOnClickListener(this);
        mRelative_to_mapview.setOnClickListener(this);
        mTv_collection_num.setOnClickListener(this);
        mScrollView.scrollTo(0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.img_share:
                getOrSetUserID();
                if (UserID.equals(NetConfig.UserID)) {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivityForResult(intent, mLoginRequestCode);
                    //                    startActivity(intent);
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
                            Toast.makeText(VenueDetailsActivity.this, "分享失败" + throwable, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {

                        }
                    });
                    //关闭sso授权
                    oks.disableSSOWhenAuthorize();
                    String url = "http://220.248.107.62:8084/whyapi/index.html?id=" + mShowId + "&tb_type=" + showName;
                    // title标题，微信、QQ和QQ空间等平台使用
                    oks.setTitle(mTv_venue_name.getText() + "");
                    // titleUrl QQ和QQ空间跳转链接
                    oks.setTitleUrl(url);
                    // text是分享文本，所有平台都需要这个字段
                    oks.setText(mTv_venue_name.getText() + "");
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
                    oks.show(VenueDetailsActivity.this);
                }
                break;
            case R.id.tv_show_num:
                if (mListEntryBeen.size() >= 1) {
                    Intent activeIntent = new Intent(this, ActiveListActivity.class);
                    ActiveListActivity.mListEntryBeen.clear();
                    for (int i = 0; i < mListEntryBeen.size(); i++) {
                        ActiveListActivity.mListEntryBeen.add(mListEntryBeen.get(i));
                    }
                    activeIntent.putExtra("url_address", mUrl_address);
                    activeIntent.putExtra("get_img_address", mHeadURLString);
                    startActivity(activeIntent);
                }
                break;
            case R.id.tv_artroom_num:
                //TODO:跳转活动室列表
                if (mListPlayroomBeen.size() >= 1) {
                    Intent actRoomIntent = new Intent(this, ActRoomListActivity.class);
                    if (ActRoomListActivity.mListPlayroomBeen != null) {
                        ActRoomListActivity.mListPlayroomBeen.clear();
                    } else {
                        ActRoomListActivity.mListPlayroomBeen = new ArrayList<>();
                    }
                    for (int i = 0; i < mListPlayroomBeen.size(); i++) {
                        ActRoomListActivity.mListPlayroomBeen.add(mListPlayroomBeen.get(i));
                    }
                    actRoomIntent.putExtra("url_address", mUrl_address);
                    actRoomIntent.putExtra("get_img_address", mHeadURLString);
                    startActivity(actRoomIntent);
                }
                break;
            case R.id.relative_to_mapview:
                Intent intent = new Intent(getBaseContext(), MapNavigationActivity.class);
                intent.putExtra("mLng", mLng);
                intent.putExtra("mLat", mLat);
                startActivity(intent);
                break;
            case R.id.tv_collection_num:
                if (mListGoodsBean.size() >= 1) {
                    Intent goodsIntent = new Intent(this, CollectionMoreActivity.class);
                    CollectionMoreActivity.mListGoodsBean.clear();
                    for (int i = 0; i < mListGoodsBean.size(); i++) {
                        CollectionMoreActivity.mListGoodsBean.add(mListGoodsBean.get(i));
                    }
                    goodsIntent.putExtra("url_address", mUrl_address);
                    goodsIntent.putExtra("showImgUrl", mHeadURLString);
                    startActivity(goodsIntent);
                }
                break;
            case R.id.tv_to_talk:
                //写评论
                writeTalk();
                break;
            case R.id.img_dianzan:
                //点赞
                giveAdmire();
                break;
            case R.id.img_shoucang:
                //收藏
                collectVenue();
                break;
            case R.id.bt_ask:
                if (mTv_phone.getText().equals("")) {
                    ToastUtils.makeShortText("该场馆没有电话咨询", getBaseContext());
                    return;
                }
                //弹出dialog
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setIcon(R.drawable.icon_phone);      //设置图标
                dialog.setTitle("将为您自动跳转拨号页面");    //设置标题
                dialog.setCancelable(false);        //点击旁边不消失
                dialog.setMessage(mTv_phone.getText());
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mTv_phone.getText()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });//设置确定键
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "复制电话号码", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(mTv_phone.getText());
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//关闭对话框
                    }
                });
                dialog.show();
                break;
        }
    }

    //分享完后，
    private void sendIntnetShare() {
        RequestParams params = new RequestParams();
        params.put("user_id", UserID);
        params.put("activity_id", mShowId);
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
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //收藏
    private void collectVenue() {
        getOrSetUserID();
        if (UserID.equals(NetConfig.UserID)) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (isShouCang == false) {
                String insertFavourite = mUrl_address + "insertFavourite";
                RequestParams params = new RequestParams();
                params.put("member_id", UserID);
                params.put("Activity_id", mShowId);
                params.put("Activity_name", neddAarr.getBusiness_name());
                params.put("area_code", "");
                params.put("area_name", neddAarr.getArea_name());
                params.put("pic_address", neddAarr.getNewpic());
                params.put("tb_name", neddAarr.getTb_name());
                params.put("faddress", neddAarr.getBusiness_address());
                params.put("begin_time", neddAarr.getOpen_time());
                params.put("end_time", "");
                params.put("favourite_type", "2");
                params.setUseJsonStreamer(true);
                postToIntnet(insertFavourite, params, "收藏");
            } else {
                String cancelFavourite = mUrl_address + "cancelFavourite";
                RequestParams params = new RequestParams();
                params.put("member_id", UserID);
                params.put("Activity_id", mShowId);
                params.put("area_name", neddAarr.getArea_name());
                params.setUseJsonStreamer(true);
                //                getSendToIntnet(cancelFavourite, params, "取消收藏");
                postcancelFavToIntnet(cancelFavourite, params);
            }
        }
    }

    //点赞
    private void giveAdmire() {
        getOrSetUserID();
        if (UserID.equals(NetConfig.UserID)) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (isDianZan == false) {
                //点赞
                String insertZan = mUrl_address + "insertZan";
                RequestParams params = new RequestParams();
                params.put("userid", UserID);
                params.put("Activity_id", mShowId);
                params.put("tb_name", showName);
                params.put("area_name", neddAarr.getArea_name());
                params.put("ftype", "2");
                params.put("Activity_name", neddAarr.getBusiness_name());
                params.put("begin_time", neddAarr.getOpen_time());
                params.put("pic_address", neddAarr.getNewpic());
                params.setUseJsonStreamer(true);
                postToIntnet(insertZan, params, "点赞");
            } else {
                //取消点赞
                String cancelZan = mUrl_address + "cancelZan";
                RequestParams params = new RequestParams();
                params.put("member_id", UserID);
                params.put("Activity_id", mShowId);
                getSendToIntnet(cancelZan, params, "取消点赞");
            }
        }
    }

    private void writeTalk() {
        getOrSetUserID();
        if (UserID.equals(NetConfig.UserID)) {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            Intent mIntent = new Intent(getBaseContext(), SendCommentActivity.class);
            mIntent.putExtra("showId", mShowId);
            mIntent.putExtra("fromKind", "2");
            mIntent.putExtra("Activity_name", neddAarr.getBusiness_name());
            mIntent.putExtra("tb_name", neddAarr.getTb_name());
            mIntent.putExtra("fmain_company", neddAarr.getBusiness_name());
            mIntent.putExtra("area_name", neddAarr.getArea_name());
            mIntent.putExtra("url_address", mUrl_address);
            startActivityForResult(mIntent, mCommentRequestCode);
            //            startActivity(mIntent);
        }
    }

    private void postcancelFavToIntnet(String url, RequestParams params) {
        HttpUtil.post(url, params, new HttpUtil.JsonHttpResponseUtil() {
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
                if (statusCode == 200) {
                    Gson gson = new Gson();
                    ClubDetailInfo clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    String result = clubDetailInfo.getResult();
                    if (result.equals("2")) {
                        mImg_shoucang.setImageResource(R.drawable.ic_star_selected1);
                        isShouCang = !isShouCang;
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getSendToIntnet(String url, RequestParams params, final String kind) {
        HttpUtil.get(url, params, new HttpUtil.JsonHttpResponseUtil() {
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
                if (statusCode == 200) {
                    ClubDetailInfo clubDetailInfo;
                    Gson gson = new Gson();
                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    String result = clubDetailInfo.getResult();
                    if (result.equals("2")) {
                        if (kind.equals("取消点赞")) {
                            mImg_dianzan.setImageResource(R.drawable.ic_zan_select1);
                            isDianZan = !isDianZan;
                            //刷新点赞头像列表
                            getZanAndTalkData();
                        }
                        if (kind.equals("取消收藏")) {
                            mImg_shoucang.setImageResource(R.drawable.ic_star_selected1);
                            isShouCang = !isShouCang;
                        }
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void postToIntnet(String url, RequestParams params, final String kind) {
        HttpUtil.post(url, params, new HttpUtil.JsonHttpResponseUtil() {
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
                if (statusCode == 200) {
                    ClubDetailInfo clubDetailInfo;
                    Gson gson = new Gson();
                    clubDetailInfo = gson.fromJson(response.toString(), ClubDetailInfo.class);
                    String result = clubDetailInfo.getResult();
                    if (result.equals("2")) {
                        if (kind.equals("点赞")) {
                            mImg_dianzan.setImageResource(R.drawable.ic_zan_select2);
                            isDianZan = !isDianZan;
                            //刷新点赞头像列表
                            getZanAndTalkData();
                        }
                        if (kind.equals("收藏")) {
                            mImg_shoucang.setImageResource(R.drawable.ic_star_selected2);
                            isShouCang = !isShouCang;
                        }
                    }
                    String message = clubDetailInfo.getMessage();
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //场馆活动展示条目设配器
    class MyShowAdapter extends BaseAdapter {
        List mList;

        MyShowAdapter(ArrayList arrayList) {
            this.mList = arrayList;
        }

        @Override
        public int getCount() {
            return mList.size() > 3 ? 3 : mList.size();
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
            MyShowHolder myShowHolder;
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(), R.layout.list_item_venue_show, null);
                myShowHolder = new MyShowHolder();
                myShowHolder.img_show_pic = (ImageView) convertView.findViewById(R.id.img_show_pic);
                myShowHolder.tv_show_title = (TextView) convertView.findViewById(R.id.tv_show_title);
                myShowHolder.tv_kind = (TextView) convertView.findViewById(R.id.tv_kind);
                myShowHolder.tv_interval_time = (TextView) convertView.findViewById(R.id.tv_interval_time);
                myShowHolder.tv_isfree = (TextView) convertView.findViewById(R.id.tv_isfree);
                convertView.setTag(myShowHolder);
            } else {
                myShowHolder = (MyShowHolder) convertView.getTag();
            }
            VenueDetailsInfo.ArrBean.ListEntryBean mListEntryBean = (VenueDetailsInfo.ArrBean.ListEntryBean) mList.get(position);
            String newpic = mListEntryBean.getNewpic();
            ImageLoaderUtil.displayImageIcon(mHeadURLString + newpic, myShowHolder.img_show_pic);//活动图片
            myShowHolder.tv_show_title.setText(mListEntryBean.getFname());//活动名
            myShowHolder.tv_kind.setText(mListEntryBean.getFtype());//活动类别
            String end_date = mListEntryBean.getEnd_date();
            myShowHolder.tv_interval_time.setText(end_date);//活动时间
            double price = mListEntryBean.getPrice();//活动价格
            if (price == 0) {
                myShowHolder.tv_isfree.setText("免费");
            } else {
                myShowHolder.tv_isfree.setText("" + price + "元/每人");
            }
            return convertView;
        }

        class MyShowHolder {
            ImageView img_show_pic;//活动图片
            TextView  tv_show_title;//活动名
            TextView  tv_kind;      //活动类别
            TextView  tv_interval_time;//开始结束时间
            TextView  tv_isfree;//活动是否免费
        }
    }

    //活动室
    public class MyActRoomAdapter extends BaseAdapter {

        List mList;

        MyActRoomAdapter(ArrayList arrayList) {
            this.mList = arrayList;
        }

        @Override
        public int getCount() {
            return mList.size() > 3 ? 3 : mList.size();
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
            MyActRoomHolder myShowHolder;
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(), R.layout.list_item_venue_actroom, null);
                myShowHolder = new MyActRoomHolder();
                myShowHolder.img_act_room_pic = (ImageView) convertView.findViewById(R.id.img_act_room_pic);
                myShowHolder.tv_act_room_title = (TextView) convertView.findViewById(R.id.tv_act_room_title);
                myShowHolder.tv_act_room_lage = (TextView) convertView.findViewById(R.id.tv_act_room_lage);
                myShowHolder.tv_isfree = (TextView) convertView.findViewById(R.id.tv_isfree);
                convertView.setTag(myShowHolder);
            } else {
                myShowHolder = (MyActRoomHolder) convertView.getTag();
            }
            VenueDetailsInfo.ArrBean.ListPlayroomBean mListPlayroom = (VenueDetailsInfo.ArrBean.ListPlayroomBean) mList.get(position);
            String newpic = mListPlayroom.getNewpic();
            ImageLoaderUtil.displayImageIcon(mHeadURLString + newpic, myShowHolder.img_act_room_pic);//活动图片
            myShowHolder.tv_act_room_title.setText(mListPlayroom.getPlayroom_name());//活动室名
            myShowHolder.tv_act_room_lage.setText(mListPlayroom.getPlayroom_area());//活动室大小
            double price = Double.parseDouble(mListPlayroom.getPlayroom_price());//活动室价格
            if (price == 0) {
                myShowHolder.tv_isfree.setText("免费");
            } else {
                myShowHolder.tv_isfree.setText("收费");
            }
            return convertView;
        }

        class MyActRoomHolder {
            ImageView img_act_room_pic;//活动室图片
            TextView  tv_act_room_title;//活动室名
            TextView  tv_act_room_lage;      //活动室大小
            TextView  tv_isfree;//活动是否免费
        }
    }

    public class MyRecAdapter extends RecyclerView.Adapter<MyRecAdapter.ViewHolder> {

        private List   mData;
        private String ItemKind;

        public MyRecAdapter(List data, String ItemKind) {
            this.mData = data;
            this.ItemKind = ItemKind;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            if (ItemKind.equals("藏品")) {
                // 实例化展示的view
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.museum_collection_item, parent, false);
            } else {
                // 实例化展示的view
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.museum_head_pic_item, parent, false);
            }
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            if (ItemKind.equals("藏品")) {
                VenueDetailsInfo.ArrBean.ListGoodsBean mListGoodsBeanDetail = (VenueDetailsInfo.ArrBean.ListGoodsBean) mData.get(position);
                String newGoods_file = mListGoodsBeanDetail.getNewGoods_file();
                ImageLoaderUtil.displayImageIcon(mHeadURLString + newGoods_file, holder.img_goods);
                String goods_name = mListGoodsBeanDetail.getGoods_name();
                holder.tv_goods_name.setText(goods_name);
                holder.linear_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VenueDetailsInfo.ArrBean.ListGoodsBean mListGoodsBeanDetail2 = (VenueDetailsInfo.ArrBean.ListGoodsBean) mData.get(position);
                        //点击显示详细藏品信息
                        Intent intent = new Intent(getBaseContext(), CollectionDetailsActivity.class);
                        intent.putExtra("tb_name", mListGoodsBeanDetail2.getTb_name());
                        intent.putExtra("business_id", mListGoodsBeanDetail2.getBusiness_id());
                        intent.putExtra("which", position);
                        intent.putExtra("url_address", mUrl_address);
                        intent.putExtra("showImgUrl", mHeadURLString);
                        startActivity(intent);
                    }
                });
            } else {
                //填充头像
                String headUrl = (String) mData.get(position);
                ImageLoaderUtil.displayImageRoundIcon(NetConfig.HEAD_IMG + headUrl, holder.img_head);

            }
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linear_item;//整个条目
            ImageView    img_head;//用户头像
            ImageView    img_goods;//藏品图片
            TextView     tv_goods_name;//藏品名称

            public ViewHolder(View itemView) {
                super(itemView);
                linear_item = (LinearLayout) itemView.findViewById(R.id.linear_item);
                img_head = (ImageView) itemView.findViewById(R.id.img_head);
                img_goods = (ImageView) itemView.findViewById(R.id.img_goods);
                tv_goods_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
            }
        }
    }

    //场馆评论条目设配器
    class MyTalkAdapter extends BaseAdapter {
        List mList;

        MyTalkAdapter(ArrayList arrayList) {
            this.mList = arrayList;
        }

        @Override
        public int getCount() {
            return mList.size() > 20 ? 20 : mList.size();
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
            MyShowHolder myShowHolder;
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(), R.layout.list_item_talk, null);
                myShowHolder = new MyShowHolder();
                myShowHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
                myShowHolder.img_comment = (ImageView) convertView.findViewById(R.id.img_comment);
                myShowHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                myShowHolder.tv_speek_time = (TextView) convertView.findViewById(R.id.tv_speek_time);
                myShowHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
                convertView.setTag(myShowHolder);
            } else {
                myShowHolder = (MyShowHolder) convertView.getTag();
            }
            CommentInfo.ArrBean arrBean = (CommentInfo.ArrBean) mList.get(position);
            String headpic = arrBean.getHeadpic();
            String username = arrBean.getUsername();
            String comment_pic = arrBean.getComment_pic();
            String comment_date = arrBean.getComment_date().substring(0, 19);
            String comment_content = arrBean.getComment_content();
            Spanned spanned = Html.fromHtml(comment_content);
            ImageLoaderUtil.displayImageRoundIcon(NetConfig.HEAD_IMG + headpic, myShowHolder.img_head);
            myShowHolder.tv_name.setText(username);
            myShowHolder.tv_speek_time.setText(comment_date);
            myShowHolder.tv_comment.setText(spanned);
            if (comment_pic.equals("")) {
                myShowHolder.img_comment.setVisibility(View.GONE);
            } else {
                myShowHolder.img_comment.setVisibility(View.VISIBLE);
                ImageLoaderUtil.displayImageIcon(mHeadURLString + "photo/" + comment_pic, myShowHolder.img_comment);
            }
            return convertView;
        }

        class MyShowHolder {
            ImageView img_head;//评论用户头像
            ImageView img_comment;//用户评论图片
            TextView  tv_name;//评论用户名
            TextView  tv_speek_time;//评论时间
            TextView  tv_comment;//评论内容
        }
    }

    private String getNewContent(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        Log.d("VACK", doc.toString());
        return doc.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mLoginRequestCode) {
            getIntentData();
        }
        if (requestCode == mCommentRequestCode) {
            //获取评论列表
            getTalkData();
        }
    }

    //判断是游客id还是用户id
    public void getOrSetUserID() {
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
    }
}
