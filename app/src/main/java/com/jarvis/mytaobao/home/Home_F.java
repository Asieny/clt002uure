package com.jarvis.mytaobao.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.MyApplication;
import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.AtSceneActivity;
import com.guangxi.culturecloud.activitys.BigActiveActivity;
import com.guangxi.culturecloud.activitys.ChooseCityActivity;
import com.guangxi.culturecloud.activitys.CultureCrowdFundingActivity;
import com.guangxi.culturecloud.activitys.CultureVolunteerActivity;
import com.guangxi.culturecloud.activitys.FilmRoomActivity;
import com.guangxi.culturecloud.activitys.LoveSocietyActivity;
import com.guangxi.culturecloud.activitys.ShowsDetailActivityNew;
import com.guangxi.culturecloud.activitys.YanXueActivity;
import com.guangxi.culturecloud.adapter.BigEventAdapter;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.BannerInfo;
import com.guangxi.culturecloud.model.BigEventInfo;
import com.guangxi.culturecloud.model.Society;
import com.guangxi.culturecloud.utils.ToastUtils;
import com.jarvis.MyView.MyGridView;
import com.javis.Adapter.Adapter_GridView;
import com.javis.Adapter.Adapter_GridView_hot;
import com.javis.ab.view.AbOnItemClickListener;
import com.javis.ab.view.AbSlidingPlayView;
import com.loopj.android.http.RequestParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.framework.AbsFragment;
import cn.com.mytest.util.ImageLoaderUtil;
import cz.msebera.android.httpclient.Header;

/**
 * 首页
 *
 * @author http://yecaoly.taobao.com
 */
public class Home_F extends AbsFragment {
    //顶部标题栏
    private TextView tv_top_title, iv_shao;
    //分类的九宫格
    //    private GridView              gridView_classify;
    private Adapter_GridView      adapter_GridView_classify;
    private Adapter_GridView_icon adapter_GridView_icon;
    private Adapter_GridView_hot  adapter_GridView_hot;
    //首页轮播
    private AbSlidingPlayView     viewPager;
    //扫一扫
    //	private ImageView iv_shao;
    // 分类九宫格的资源文件
    //    private int[]    pic_path_classify      = {R.drawable.bigevent, R.drawable.library, R.drawable.art_gallery,
    //            R.drawable.museum, R.drawable.gymnasium, R.drawable.national_culture_museum, R.drawable.cultural_artistic_museum, R.drawable.film_room, R.drawable.cultural_volunteers, R.drawable.love_society, R.drawable.crowd_raising, R.drawable.others};
    private int[]    pic_path_classify      = {R.drawable.secret__01, R.drawable.secret__02, R.drawable.secret__03,
            R.drawable.secret__04, R.drawable.secret__05, R.drawable.secret__10, R.drawable.secret__06, R.drawable.secret__11, R.drawable.secret__07, R.drawable.secret__08, R.drawable.secret__09, R.drawable.secret__12};
    //
    private String[] pic_path_classify_name = {"大活动", "图书馆", "美术馆", "博物馆", "体育场馆", "民族文化", "文化艺术馆", "影剧院", "文化志愿者", "爱社团", "活动众筹", "我在现场"};

    private int[]    pic_path_icon1      = {R.drawable.secret__01, R.drawable.secret__02, R.drawable.secret__03,
            R.drawable.secret__04, R.drawable.secret__05, R.drawable.secret__10, R.drawable.secret__06, R.drawable.secret__11, R.drawable.secret__13, R.drawable.secret__07, R.drawable.secret__08, R.drawable.secret__09};
    private int[]    pic_path_icon2      = {R.drawable.secret__14, R.drawable.secret__15, R.drawable.secret__15};
    private String[] pic_path_icon_name1 = {"大活动", "图书馆", "美术馆", "博物馆", "体育场馆", "民族文化", "文化艺术馆", "影剧院", "社区活动", "文化志愿者", "爱社团", "活动众筹"};
    private String[] pic_path_icon_name2 = {"我在现场", "铜鼓奖", "研学"};
    private ArrayList pic_path_icon;
    private ArrayList pic_path_icon_name;

    // 热门市场的资源文件
    private int[] pic_path_hot = {R.drawable.menu_1, R.drawable.menu_2, R.drawable.menu_3, R.drawable.menu_4, R.drawable.menu_5, R.drawable.menu_6};
    /**
     * 存储首页轮播的界面
     */
    private ArrayList<View> allListView;
    //翻页图标
    private ArrayList<View> iconListView;
    /**
     * 首页轮播的界面的资源
     */
    private int[] resId = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4};
    private ArrayList<String> bannerUrl;
    private ListView          listView_tao;

    private String cityName;
    final int REQUEST_CODE_CITY   = 1001;
    final int REQUEST_CODE_YANXUE = 1986;//跳转研学的请求码
    private String city, cityCode;

    private BigEventAdapter    bigEventAdapter;
    private List<BigEventInfo> data;
    private ViewPager          mViewPager_icon;//翻页图标
    private MyGridView         mMy_icon_gridview;
    private ImageView          mImg_banner1;
    private ImageView          mImg_banner2;
    private ProgressDialog     progressDialog;
    private View               mView;
    private SmartRefreshLayout mSwipe_refresh;//智能刷新控件

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.home_f, null);
        initView(mView);
        return mView;
    }

    private void initView(final View view) {
        iv_shao = (TextView) view.findViewById(R.id.iv_shao);
        //		ToolLocation.requestLocation(getActivity(), new ToolLocation.InterfaceBDLocation() {
        //
        //			@Override
        //			public void onLocationSuccess(BDLocation location) {
        //				cityName = location.getCity();
        //
        //			}
        //		}, false);
        iv_shao.setText(MyApplication.cityName);
        mSwipe_refresh = (SmartRefreshLayout) view.findViewById(R.id.swipe_refresh);
        tv_top_title = (TextView) view.findViewById(R.id.tv_top_title);
        mViewPager_icon = (ViewPager) view.findViewById(R.id.viewPager_icon);//翻页图标
        mImg_banner1 = (ImageView) view.findViewById(R.id.img_banner1);
        mImg_banner2 = (ImageView) view.findViewById(R.id.img_banner2);
        viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
        listView_tao = (ListView) view.findViewById(R.id.listView_tao);
        iv_shao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ChooseCityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CITY);
            }
        });
        mSwipe_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //其他balabala
                otherAllAction(view);
            }
        });
        initIconBanner();
        //根据定位地址，修改链接head；
        //获取地址请求头
        getUrlHead(MyApplication.cityName, view);
    }

    private void getUrlHead(String cityName, final View view) {
        RequestParams params = new RequestParams();
        params.put("area", cityName);
        HttpUtil.get(NetConfig.FIRSTURL, params, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                showProgressDialog("正在加载，请稍后");
                super.onStart();
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
                super.onFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (statusCode == 200) {
                    String ress = response.toString();
                    if (null != ress && !ress.equals("") && !ress.equals("{}")) {
                        Gson gson = new Gson();
                        Society urlAddressInfo = gson.fromJson(response.toString(), Society.class);
                        try {
                            Society.ApiAddressBean apiAddress = urlAddressInfo.getApiAddress();
                            String url_address = apiAddress.getUrl_address();
                            String get_img_address = apiAddress.getGet_img_address();
                            //修改所有链接
                            changeAllLinks(url_address, get_img_address);
                        } catch (Exception e) {
                            ToastUtils.makeShortText("跳转省会城市资讯", getActivity());
                        }
                    }
                }
                //其他balabala
                otherAllAction(view);
            }
        });
    }

    private void otherAllAction(View view) {
        //设置播放方式为顺序播放
        viewPager.setPlayType(1);
        //设置播放间隔时间
        viewPager.setSleepTime(3000);
        //从网络获取轮播图
        getBanner();
        //        gridView_classify.setOnItemClickListener(new OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //
        //                if (position == 0) {
        //                    Intent bigActive = new Intent(getActivity(), BigActiveActivity.class);
        //                    bigActive.putExtra("bigUrl", NetConfig.BIGEVENT_LIST);
        //                    bigActive.putExtra("mModelId", 0);
        //                    bigActive.putExtra("actName", "大活动");
        //                    startActivity(bigActive);
        //                }
        //                if (position == 1) {
        //                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
        //                    //                    library.putExtra("type", 0);
        //                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
        //                    library.putExtra("bigUrl", NetConfig.LIBRARYACTIVITYLIST);
        //                    library.putExtra("mModelId", 1);
        //                    library.putExtra("actName", "图书馆");
        //                    startActivity(library);
        //                }
        //                if (position == 2) {
        //                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
        //                    //                    library.putExtra("type", 1);
        //                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
        //                    library.putExtra("bigUrl", NetConfig.ARTSACTIVITYLIST);
        //                    library.putExtra("mModelId", 2);
        //                    library.putExtra("actName", "美术馆");
        //                    startActivity(library);
        //                }
        //                if (position == 3) {
        //                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
        //                    //                    library.putExtra("type", 2);
        //                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
        //                    library.putExtra("bigUrl", NetConfig.MUSEUMACTIVITYLIST);
        //                    library.putExtra("mModelId", 3);
        //                    library.putExtra("actName", "博物馆");
        //                    startActivity(library);
        //                }
        //                if (position == 4) {
        //                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
        //                    //                    library.putExtra("type", 3);
        //                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
        //                    library.putExtra("bigUrl", NetConfig.SPORTLIST);
        //                    library.putExtra("mModelId", 4);
        //                    library.putExtra("actName", "体育场馆");
        //                    startActivity(library);
        //                }
        //                if (position == 5) {
        //                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
        //                    //                    library.putExtra("type", 4);
        //                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
        //                    library.putExtra("bigUrl", NetConfig.NATIONLIST);
        //                    library.putExtra("mModelId", 5);
        //                    library.putExtra("actName", "民族文化");
        //                    startActivity(library);
        //                }
        //                if (position == 6) {
        //                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
        //                    //                    library.putExtra("type", 5);
        //                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
        //                    library.putExtra("bigUrl", NetConfig.CULTUREACTIVITYLIST);
        //                    library.putExtra("mModelId", 6);
        //                    library.putExtra("actName", "文化艺术馆");
        //                    startActivity(library);
        //                }
        //                if (position == 7) {
        //                    Intent cultureVolunteer = new Intent(getActivity(), FilmRoomActivity.class);
        //                    startActivity(cultureVolunteer);
        //                }
        //                if (position == 8) {
        //                    Intent cultureVolunteer = new Intent(getActivity(), CultureVolunteerActivity.class);
        //                    startActivity(cultureVolunteer);
        //                }
        //                if (position == 9) {
        //                    Intent intentLoveSociety = new Intent(getActivity(), LoveSocietyActivity.class);
        //                    startActivity(intentLoveSociety);
        //                }
        //                if (position == 10) {
        //                    Intent intentLoveSociety = new Intent(getActivity(), CultureCrowdFundingActivity.class);
        //                    startActivity(intentLoveSociety);
        //                }
        //                if (position == 11) {
        //                    Intent intentLoveSociety = new Intent(getActivity(), AtSceneActivity.class);
        //                    intentLoveSociety.putExtra("ShowDetails", false);
        //                    startActivity(intentLoveSociety);
        //                }
        //            }
        //        });

        bigEventAdapter = new BigEventAdapter(getActivity());
        if (null == data) {
            data = new ArrayList<>();
        } else {
            data.clear();
        }
        HttpUtil.get(NetConfig.RECOMMND_LIST, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mSwipe_refresh.finishRefresh();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                JSONArray array = jsonObject.optJSONArray("arr");
                if (array != null && array.length() > 0) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.optJSONObject(i);
                        BigEventInfo firstInfo = new BigEventInfo().fromJson(obj.toString());
                        data.add(firstInfo);
                    }
                    bigEventAdapter.setDataSource(data);
                }

            }
        });
        listView_tao.setAdapter(bigEventAdapter);
        listView_tao.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent = new Intent(getActivity(), ShowsDetailActivityNew.class);
                BigEventAdapter adapter = (BigEventAdapter) listView_tao.getAdapter();
                intent.putExtra("filmRoom", adapter.getId(position));
                intent.putExtra("activity_name", adapter.getTableName(position));
                intent.putExtra("type", adapter.getType(position));
                intent.putExtra("modelId", 0);
                startActivity(intent);
            }
        });
    }

    public void getBanner() {
        String url = NetConfig.BANNERLIST;
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
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200) {
                    Gson gson = new Gson();
                    BannerInfo bannerInfo = gson.fromJson(response.toString(), BannerInfo.class);
                    List<BannerInfo.ArrBean> arr = bannerInfo.getArr();
                    initViewPager(arr);
                }
            }
        });
    }

    private void initViewPager(List<BannerInfo.ArrBean> arr) {
        if (null == bannerUrl) {
            bannerUrl = new ArrayList<>();
        } else {
            bannerUrl.clear();
        }
        for (int i = 0; i < arr.size(); i++) {
            bannerUrl.add(arr.get(i).getNewpic());
        }

        if (null == allListView) {
            allListView = new ArrayList<View>();
        } else {
            allListView.clear();
        }
        for (int i = 0; i < bannerUrl.size(); i++) {
            //导入ViewPager的布局
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pic_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
            ImageLoaderUtil.displayImageIcon(NetConfig.IMG + bannerUrl.get(i), imageView);
            //            imageView.setImageResource(resId[i]);
            allListView.add(view);
        }
        viewPager.removeAllViews();
        viewPager.addViews(allListView);
        //开始轮播
        if (viewPager != null) {
            viewPager.stopPlay();
        }
        viewPager.startPlay();
        viewPager.setOnItemClickListener(new AbOnItemClickListener() {
            @Override
            public void onClick(int position) {
                //跳转到详情界面
                //                Intent intent = new Intent(getActivity(), BabyActivity.class);
                //                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CITY:
                if (data != null) {
                    city = data.getStringExtra("city") + "市";
                    cityCode = data.getStringExtra("cityCode");
                    iv_shao.setText(city);
                    //                    MyApplication.cityName = city;
                    //根据修改地址，修改链接head；
                } else {
                    city = "南宁市";
                    iv_shao.setText("南宁市");
                }
                //获取地址请求头
                getUrlHead(city, mView);
                break;
            default:
                break;
        }
    }

    private void initIconBanner() {
        if (null == iconListView) {
            iconListView = new ArrayList<View>();
        } else {
            iconListView.clear();
        }

        for (int i = 0; i < 2; i++) {
            //导入iconPager的布局
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.icon_pager, null);
            mMy_icon_gridview = (MyGridView) view.findViewById(R.id.my_icon_gridview);
            pic_path_icon = new ArrayList();
            pic_path_icon_name = new ArrayList();
            for (int n = 0; n < pic_path_icon1.length; n++) {
                pic_path_icon.add(pic_path_icon1[n]);
            }
            for (int n = 0; n < pic_path_icon_name1.length; n++) {
                pic_path_icon_name.add(pic_path_icon_name1[n]);
            }
            adapter_GridView_icon = new Adapter_GridView_icon(getActivity(), pic_path_icon, pic_path_icon_name);
            mMy_icon_gridview.setAdapter(adapter_GridView_icon);
            iconListView.add(view);
            //设置图标点击事件
            setIconClick(mMy_icon_gridview);
        }
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(iconListView);
        mViewPager_icon.setAdapter(myViewPagerAdapter);
        mViewPager_icon.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (pic_path_icon != null) {
                    pic_path_icon.clear();
                }
                if (pic_path_icon_name != null) {
                    pic_path_icon_name.clear();
                }
                if (position == 0) {
                    mImg_banner1.setImageResource(R.drawable.bg_banner_round_blue);
                    mImg_banner2.setImageResource(R.drawable.bg_banner_round_gray);
                    for (int n = 0; n < pic_path_icon1.length; n++) {
                        pic_path_icon.add(pic_path_icon1[n]);
                    }
                    for (int n = 0; n < pic_path_icon_name1.length; n++) {
                        pic_path_icon_name.add(pic_path_icon_name1[n]);
                    }
                } else if (position == 1) {
                    mImg_banner1.setImageResource(R.drawable.bg_banner_round_gray);
                    mImg_banner2.setImageResource(R.drawable.bg_banner_round_blue);
                    for (int n = 0; n < pic_path_icon2.length; n++) {
                        pic_path_icon.add(pic_path_icon2[n]);
                    }
                    for (int n = 0; n < pic_path_icon_name2.length; n++) {
                        pic_path_icon_name.add(pic_path_icon_name2[n]);
                    }
                }
                adapter_GridView_icon.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setIconClick(MyGridView my_icon_gridview) {
        my_icon_gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_grid_name = (TextView) view.findViewById(R.id.tv_adapter_grid_name);
                String name = String.valueOf(tv_grid_name.getText());
                if ("大活动".equals(name)) {
                    Intent bigActive = new Intent(getActivity(), BigActiveActivity.class);
                    bigActive.putExtra("bigUrl", NetConfig.BIGEVENT_LIST);
                    bigActive.putExtra("mModelId", 0);
                    bigActive.putExtra("actName", "大活动");
                    startActivity(bigActive);
                }
                if ("图书馆".equals(name)) {
                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
                    //                    library.putExtra("type", 0);
                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
                    library.putExtra("bigUrl", NetConfig.LIBRARYACTIVITYLIST);
                    library.putExtra("mModelId", 1);
                    library.putExtra("actName", "图书馆");
                    startActivity(library);
                }
                if ("美术馆".equals(name)) {
                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
                    //                    library.putExtra("type", 1);
                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
                    library.putExtra("bigUrl", NetConfig.ARTSACTIVITYLIST);
                    library.putExtra("mModelId", 2);
                    library.putExtra("actName", "美术馆");
                    startActivity(library);
                }
                if ("博物馆".equals(name)) {
                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
                    //                    library.putExtra("type", 2);
                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
                    library.putExtra("bigUrl", NetConfig.MUSEUMACTIVITYLIST);
                    library.putExtra("mModelId", 3);
                    library.putExtra("actName", "博物馆");
                    startActivity(library);
                }
                if ("体育场馆".equals(name)) {
                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
                    //                    library.putExtra("type", 3);
                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
                    library.putExtra("bigUrl", NetConfig.SPORTLIST);
                    library.putExtra("mModelId", 4);
                    library.putExtra("actName", "体育场馆");
                    startActivity(library);
                }
                if ("民族文化".equals(name)) {
                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
                    //                    library.putExtra("type", 4);
                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
                    library.putExtra("bigUrl", NetConfig.NATIONLIST);
                    library.putExtra("mModelId", 5);
                    library.putExtra("actName", "民族文化");
                    startActivity(library);
                }
                if ("文化艺术馆".equals(name)) {
                    //                    Intent library = new Intent(getActivity(), LibraryActivity.class);
                    //                    library.putExtra("type", 5);
                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
                    library.putExtra("bigUrl", NetConfig.CULTUREACTIVITYLIST);
                    library.putExtra("mModelId", 6);
                    library.putExtra("actName", "文化艺术馆");
                    startActivity(library);
                }
                if ("影剧院".equals(name)) {
                    Intent cultureVolunteer = new Intent(getActivity(), FilmRoomActivity.class);
                    startActivity(cultureVolunteer);
                }
                if ("文化志愿者".equals(name)) {
                    Intent cultureVolunteer = new Intent(getActivity(), CultureVolunteerActivity.class);
                    startActivity(cultureVolunteer);
                }
                if ("爱社团".equals(name)) {
                    Intent intentLoveSociety = new Intent(getActivity(), LoveSocietyActivity.class);
                    startActivity(intentLoveSociety);
                }
                if ("活动众筹".equals(name)) {
                    Intent intentLoveSociety = new Intent(getActivity(), CultureCrowdFundingActivity.class);
                    startActivity(intentLoveSociety);
                }
                if ("我在现场".equals(name)) {
                    Intent intentLoveSociety = new Intent(getActivity(), AtSceneActivity.class);
                    intentLoveSociety.putExtra("ShowDetails", false);
                    startActivity(intentLoveSociety);
                }
                if ("社区活动".equals(name)) {
                    Intent library = new Intent(getActivity(), BigActiveActivity.class);
                    library.putExtra("bigUrl", NetConfig.COMMUNITY_ACTIVITY_LIST);
                    library.putExtra("mModelId", 8);
                    library.putExtra("actName", "社区活动");
                    startActivity(library);
                }
                if ("铜鼓奖".equals(name)) {
                    ToastUtils.makeShortText("内容正在维护中", getActivity());
                }
                if ("研学".equals(name)) {
                    Intent yanXueAct = new Intent(getActivity(), YanXueActivity.class);
                    startActivityForResult(yanXueAct, REQUEST_CODE_YANXUE);
                    //                    startActivity(yanXueAct);
                }
            }
        });
    }


    /**
     * 自定义类实现PagerAdapter，填充显示数据
     */
    class MyViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> mArrayList;

        public MyViewPagerAdapter(ArrayList<View> arrayList) {
            this.mArrayList = arrayList;
        }

        // 显示多少个页面
        @Override
        public int getCount() {
            return mArrayList == null ? 0 : mArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        // 初始化显示的条目对象
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //
            //            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //            LayoutInflater inflater = LayoutInflater.from(getActivity());
            //            View view = inflater.inflate(R.layout.photoview_only, null);
            //            view.setLayoutParams(lp);
            //            //设置viewpager中子view显示的数据
            //            PhotoView img_show = (PhotoView) view.findViewById(R.id.photoview);
            //            ImageLoaderUtil.displayImageIcon(NetConfig.HEAD_IMG + mArrayList.get(position), img_show);

            // 添加到ViewPager容器
            container.addView(mArrayList.get(position));
            // 返回填充的View对象
            return mArrayList.get(position);
        }

        // 销毁条目对象
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class Adapter_GridView_icon extends BaseAdapter {
        private Context      context;
        private List         data;
        private List<String> names;

        public Adapter_GridView_icon(Context context, List data, List<String> names) {

            this.context = context;
            this.data = data;
            this.names = names;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View currentView, ViewGroup arg2) {
            HolderView holderView = null;
            if (currentView == null) {
                holderView = new HolderView();
                currentView = LayoutInflater.from(context).inflate(R.layout.adapter_grid_home, null);
                holderView.iv_pic = (ImageView) currentView.findViewById(R.id.iv_adapter_grid_pic);
                holderView.tv_name = (TextView) currentView.findViewById(R.id.tv_adapter_grid_name);
                currentView.setTag(holderView);
            } else {
                holderView = (HolderView) currentView.getTag();
            }


            holderView.iv_pic.setImageResource((Integer) data.get(position));
            holderView.tv_name.setText(names.get(position));

            return currentView;
        }


        public class HolderView {
            private ImageView iv_pic;
            private TextView  tv_name;
        }
    }

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setInverseBackgroundForced(false);//对话框后面的窗体不获得焦点
        progressDialog.setCanceledOnTouchOutside(false);//旁击不消失
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void changeAllLinks(String url_address, String get_img_address) {
        NetConfig.IMG = get_img_address;
        NetConfig.URL_HEAD_ADDRESS = url_address;
        NetConfig.URL_CHANGE_UPLOAD_BASE64 = url_address + "uploadBase64";//提交图片接口
        //        NetConfig.INSERTPIC = url_address + "insertPic";//提交头像
        NetConfig.BANNERLIST = url_address + "bannerList";//首页轮播图地址
        //        NetConfig.REGISTER = url_address + "register";//用户注册
        //        NetConfig.CHECK_MESSAGE = url_address + "checkMessage";//用户发送短信验证码
        //        NetConfig.LOGIN = url_address + "login?";//用户登录
        //        NetConfig.MODIFYPSW = url_address + "modifyPsw?";//修改密码
        NetConfig.LIBRARYTYPELIST = url_address + "libraryTypeList";//图书馆分类
        NetConfig.ARTSTYPELIST = url_address + "artsTypeList"; //美术馆分类
        NetConfig.LIBRARY_Detail = url_address + "searchLibraryInfo";//图书馆详情
        NetConfig.ARTS_Detail = url_address + "searchArtsInfo";//美术馆详情
        NetConfig.NATION_TYPE_LIST = url_address + "nationTypeList";//民族文化分类
        NetConfig.SPORT_TYPE_LIST = url_address + "sportsTypeList";//体育场馆分类
        NetConfig.NATION_Detail = url_address + "nationInfo";//民族文化详情
        NetConfig.SPORT_Detail = url_address + "searchSportsInfo";//体育馆详情
        NetConfig.NATIONLIST = url_address + "nationList?";//民族文化GET
        NetConfig.SPORTLIST = url_address + "sportsActivityList?";//体育场馆GET
        NetConfig.CULTURETYPELIST = url_address + "cultureTypeList";//文化艺术分类
        NetConfig.CULTURE_Detail = url_address + "searchCultureInfo";//文化艺术详情
        NetConfig.COMMUNITY_ACTIVITY_LIST = url_address + "communityActivityList";//社区活动列表
        NetConfig.COMMUNITY_DETAIL = url_address + "searchCommunityInfo";//社区活动详情
        NetConfig.MUSEUMTYPELIST = url_address + "museumTypeList";//博物馆分类
        NetConfig.MUSEUM_Detail = url_address + "searchMuseumInfo";//博物馆详情
        NetConfig.FILM_Detail = url_address + "searchTheaterInfo";//影剧院详情
        NetConfig.LIBRARYACTIVITYLIST = url_address + "libraryActivityList?";//图书馆列表
        NetConfig.ARTSACTIVITYLIST = url_address + "artsActivityList?";//美术馆
        NetConfig.CULTUREACTIVITYLIST = url_address + "cultureActivityList?";//文化艺术
        NetConfig.MUSEUMACTIVITYLIST = url_address + "museumActivityList?";//博物馆列表
        NetConfig.CROWDFUNDINGLIST = url_address + "crowdfundingList?";//活动众筹
        NetConfig.SEARCHCROWDFUNDINGINFO = url_address + "searchCrowdfundingInfo?";//活动众筹详情
        //        NetConfig.SEARCHORDERLIST = url_address + "searchOrderList?";//订单列表
        //        NetConfig.SERACTIVITYINFO = url_address + "serActivityInfo";//订单列表里的活动详情
        NetConfig.RECOMMND_LIST = url_address + "recommendList";//推荐列表GET
        NetConfig.RECOMMND_INFO = url_address + "recommendInfo";
        NetConfig.BIGEVENT_LIST = url_address + "bigEventList";//大活动GET
        NetConfig.BIGEVENT_INFO = url_address + "bigEventInfo";//大活动详情
        NetConfig.THEATER_LIST = url_address + "theaterActivityList";//影剧院
        NetConfig.SEARCH_THEATER_INFO = url_address + "searchTheaterInfo";//活动详情
        NetConfig.ZAN_LIST = url_address + "zanList";//点赞列表接口
        NetConfig.COMMENT_LIST = url_address + "commentList";//评论列表
        NetConfig.INSERT_WHYORDER = url_address + "insertWhyOrder.do";//提交订单
        //        NetConfig.INSERT_WHYORDER_OK = url_address + "whyOrderNotify.do";//成功提交订单POST
        //        NetConfig.CANCEL_WHYORDER = url_address + "cancleWhyOrder.do";//取消提交订单POST
        NetConfig.INSERT_ZAN = url_address + "insertZan";//点赞接口
        NetConfig.CANCEL_ZAN = url_address + "cancelZan";//取消点赞
        NetConfig.INSERT_FAVOURITE = url_address + "insertFavourite";//关注
        NetConfig.CANCEL_FAVOURITE = url_address + "cancelFavourite";//取消关注
        NetConfig.INSERTOTHERFAVOURITE = url_address + "insertOtherFavourite";//客户收藏(除活动和馆类之外调用)
        NetConfig.LOVE_SOCIETY = url_address + "loveSocietyList";//爱社团
        NetConfig.LOVE_SOCIETY_INFO = url_address + "loveSocietyInfo";//爱社团社团详情
        NetConfig.INSERT_JIAOHUA = url_address + "insertJiaohua";//浇花
        NetConfig.INSERT_LOVESOCIETY = url_address + "insertLoveSociety";//入驻文化云社团
        NetConfig.VOLUNTEER_ACTIVITYLIST = url_address + "volunteerActivityList";//志愿者
        NetConfig.SEARCH_VOLUNTEERINFO = url_address + "searchVolunteerInfo";//志愿者招聘详情
        NetConfig.REGISTER_VOLUNTEER = url_address + "registerVolunteer";//注册志愿者
        NetConfig.BUSINESS_LIST = url_address + "businessList";//文化空间，商家列表
        NetConfig.BUSINESS_INFO = url_address + "businessInfo";//商家详情
        NetConfig.BUSINESS_PLAYROOM = url_address + "businessplayroom";//商家活动室详情
        NetConfig.BUSINESS_GOODSLIST = url_address + "businessGoodsList";
        //        NetConfig.GCZAN_LIST = url_address + "gcZanList";//广场列表
        //        NetConfig.INSERT_ZANCOL = url_address + "insertZanCol";//点赞讨厌投票
        //        NetConfig.SERRANKING_GCACTIVITY_LIST = url_address + "serRankingGcActivityList";//广场活动排行榜
        //        NetConfig.INSERT_GC = url_address + "insertGc";//广场发布
        //        NetConfig.INSERT_SMRZ = url_address + "insertSmrz";//实名认证
        NetConfig.INSERT_ZZRZPERSONNEL = url_address + "insertZzrzPersonnel";//个人资质认证
        NetConfig.INSERT_ZZRZST = url_address + "insertZzrzSt";//社团资质认证
        NetConfig.INSERT_ZZRZCOMPANY = url_address + "insertZzrzCompany";//公司资质认证
        //        NetConfig.BACK_PSW = url_address + "backPsw";//找回密码
        //        NetConfig.INSERT_HELP = url_address + "insertHelp";//帮助和反馈
        //        NetConfig.INSERT_JOIN = url_address + "insertJoin";//申请入驻
        NetConfig.INSERT_COMMENT = url_address + "insertComment";//提交评论
    }
}
