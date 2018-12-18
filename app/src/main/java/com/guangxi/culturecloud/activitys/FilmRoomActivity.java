package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.FilmShowInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;
import cz.msebera.android.httpclient.Header;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/2 11:33
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class FilmRoomActivity extends BaseActivity {

    private SwipeRefreshLayout  mSwipe_refresh;//下拉刷新
    private RecyclerView        mRecyclerView; //条目展示
    private TextView            mArea; //选择地区
    private TextView            mSort; //选择排序排序
    private TextView            mPayKind;  //付费方式
    private LinearLayout        mLinear_title;
    private FrameLayout         mFrameLayout;//待填充的布局
    private LinearLayoutManager mLayoutManager;
    private View                mView;     //填充的布局
    private ListView            mLv_area;   //地区选择
    private ListView            mLv_area_detail; //地区详细地块选择
    private String[] mStrings    = {"全广西", "北海", "桂林", "南宁", "贺州", "柳州", "河池"};
    private String[] mStringSort = {"智能排序", "热门排序", "最新上线", "即将结束", "离我最近"};
    private String[] mStringPay  = {"全部", "免费", "在线预订"};
    private ArrayList<String> mStringList;//地区详细地址记录
    private int mSelectedColor = 0;//记录点击时的条目
    private MyListViewAdapter myListAdapter;
    private MyListViewAdapter myListAdapter2;
    private MyRecAdapter      mMyAdapter;   // RecyclerView的适配器
    private ImageView         mIv_back;
    private ProgressDialog    progressDialog;
    private List              mFileShowInfoList;

    private String FilmActUrl     = NetConfig.THEATER_LIST;
    private String mURLHeadString = NetConfig.IMG;
    private       String       mFilmId;
    private       FilmShowInfo mFilmShowInfo;
    public static FilmShowInfo markShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_room);
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mSwipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinear_title = (LinearLayout) findViewById(R.id.linear_title);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_choice);

        mArea = (TextView) findViewById(R.id.tv_area);
        mSort = (TextView) findViewById(R.id.tv_sort);
        mPayKind = (TextView) findViewById(R.id.tv_pay);

        mStringList = new ArrayList<>();
        mStringList.add("北海");
        mStringList.add("桂林");
        mStringList.add("南宁");
        mStringList.add("贺州");
        mStringList.add("柳州");
        mStringList.add("河池");
    }

    private void initData() {
        //设置回退键
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取网络数据
        getIntnetData();

        //下拉刷新改变数据
        mSwipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //请求网络，更新数据//刷新列表
                getIntnetData();
                // mSwipe_refresh.setRefreshing(false);
            }
        });
    }

    private void getIntnetData() {
        HttpUtil.get(FilmActUrl, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(FilmRoomActivity.this, "正在加载，请稍后");
                mSwipe_refresh.setRefreshing(true);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
                mSwipe_refresh.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                mFileShowInfoList = new ArrayList<>();
                Gson gson = new Gson();
                FilmShowInfo filmShowInfo;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("arr");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject filmShowInfoData = jsonArray.getJSONObject(i);
                        filmShowInfo = gson.fromJson(filmShowInfoData.toString(), FilmShowInfo.class);
                        //设置完数据，放入list集合
                        mFileShowInfoList.add(filmShowInfo);
                        //设置数据
                        setDataAndListener();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDataAndListener() {
        //配置RecyclerView
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mMyAdapter == null) {
            mMyAdapter = new MyRecAdapter(mFileShowInfoList);
            mRecyclerView.setAdapter(mMyAdapter);
            //上拉加载
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    //获取最后一个条目的position
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItemPosition == mStringList.size() - 1) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //判断网络数据是否是最后一条，是则不请求网络，不是请求，添加，刷新
                                //TODO:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMyAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }).start();
                    }
                }
            });
        } else {
            mMyAdapter.notifyDataSetChanged();
        }

        //显示地区详细搜索项
        mArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏搜索title，显示搜索详情项
                mLinear_title.setVisibility(View.INVISIBLE);
                mFrameLayout.setVisibility(View.VISIBLE);
                //动态填充view，展示详细搜索选项
                if (mView != null) {
                    mFrameLayout.removeView(mView);
                }
                mView = LayoutInflater.from(getBaseContext()).inflate(R.layout.choice_area_detail, null);
                mFrameLayout.addView(mView);
                ImageView mImg_up = (ImageView) mFrameLayout.findViewById(R.id.img_hidde);
                mLv_area = (ListView) mView.findViewById(R.id.lv_area);
                mLv_area_detail = (ListView) mView.findViewById(R.id.lv_area_detail);
                mLv_area_detail.setVisibility(View.VISIBLE);
                //设置两个列表的数据
                myListAdapter = new MyListViewAdapter(mStrings);
                mLv_area.setAdapter(myListAdapter);
                myListAdapter2 = new MyListViewAdapter(mStringList);
                mLv_area_detail.setAdapter(myListAdapter2);

                mLv_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            mArea.setText(mStrings[position]);
                            mFrameLayout.setVisibility(View.INVISIBLE);
                            mLinear_title.setVisibility(View.VISIBLE);
                            //设置RecyclerView的数据
                            mMyAdapter.notifyDataSetChanged();
                        }
                        //记录选中的条目
                        mSelectedColor = position;
                        myListAdapter.notifyDataSetChanged();
                        mStringList.add("哪" + position);
                        myListAdapter2.notifyDataSetChanged();
                    }
                });
                mLv_area_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mArea.setText(mStringList.get(position));
                        mFrameLayout.setVisibility(View.INVISIBLE);
                        mLinear_title.setVisibility(View.VISIBLE);
                        //设置RecyclerView的数据
                        for (int i = mFileShowInfoList.size(); i > mFileShowInfoList.size() - 2; i--) {
                            mFileShowInfoList.add(0, mFileShowInfoList.get(i - 1));
                        }
                        mMyAdapter.notifyDataSetChanged();
                    }
                });

                //图片设置隐藏详细搜索项
                mImg_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFrameLayout.setVisibility(View.INVISIBLE);
                        mLinear_title.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        //显示排序详细搜索项
        mSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏搜索title，显示搜索详情项
                mLinear_title.setVisibility(View.INVISIBLE);
                mFrameLayout.setVisibility(View.VISIBLE);
                //动态填充view，展示详细搜索选项
                if (mView != null) {
                    mFrameLayout.removeView(mView);
                }
                mView = LayoutInflater.from(getBaseContext()).inflate(R.layout.choice_area_detail, null);
                mFrameLayout.addView(mView);
                mLv_area = (ListView) mView.findViewById(R.id.lv_area);
                if (mLv_area_detail != null) {
                    mLv_area_detail.setVisibility(View.GONE);
                }
                ImageView mImg_up = (ImageView) mFrameLayout.findViewById(R.id.img_hidde);
                mSelectedColor = 0;
                myListAdapter = new MyListViewAdapter(mStringSort);
                mLv_area.setAdapter(myListAdapter);
                mLv_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mSort.setText(mStringSort[position]);
                        mFrameLayout.setVisibility(View.INVISIBLE);
                        mLinear_title.setVisibility(View.VISIBLE);
                        //设置RecyclerView的数据
                        mMyAdapter.notifyDataSetChanged();
                    }
                });
                //图片设置隐藏详细搜索项
                mImg_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFrameLayout.setVisibility(View.INVISIBLE);
                        mLinear_title.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        //显示筛选详细搜索项
        mPayKind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //隐藏搜索title，显示搜索详情项
                mLinear_title.setVisibility(View.INVISIBLE);
                mFrameLayout.setVisibility(View.VISIBLE);
                //动态填充view，展示详细搜索选项
                if (mView != null) {
                    mFrameLayout.removeView(mView);
                }
                mView = LayoutInflater.from(getBaseContext()).inflate(R.layout.choice_area_detail, null);
                mFrameLayout.addView(mView);
                mLv_area = (ListView) mView.findViewById(R.id.lv_area);
                if (mLv_area_detail != null) {
                    mLv_area_detail.setVisibility(View.GONE);
                }
                ImageView mImg_up = (ImageView) mFrameLayout.findViewById(R.id.img_hidde);
                mSelectedColor = 0;
                myListAdapter = new MyListViewAdapter(mStringPay);
                mLv_area.setAdapter(myListAdapter);
                mLv_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //TODO:修改mRecyclerView中的数据排序
                        mPayKind.setText(mStringPay[position]);
                        mFrameLayout.setVisibility(View.INVISIBLE);
                        mLinear_title.setVisibility(View.VISIBLE);
                        //TODO:刷新RecyclerView的数据
                        mMyAdapter.notifyDataSetChanged();

                    }
                });
                //图片设置隐藏详细搜索项
                mImg_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFrameLayout.setVisibility(View.INVISIBLE);
                        mLinear_title.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


    public class MyRecAdapter extends RecyclerView.Adapter<MyRecAdapter.ViewHolder> {

        private List mData;

        public MyRecAdapter() {
        }

        public MyRecAdapter(List data) {
            this.mData = data;
        }

        public void updateData(ArrayList<String> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_introdce_item, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            //TODO:
            mFilmShowInfo = (FilmShowInfo) mData.get(position);
            String newpic = mURLHeadString + mFilmShowInfo.getNewpic();
            ImageLoaderUtil.displayImageIcon(newpic, holder.mImg);
            String label1 = mFilmShowInfo.getLabel1().trim();
            if (label1.equals("")) {
                holder.mLabel1.setVisibility(View.GONE);
            } else {
                holder.mLabel1.setText(label1);
            }
            String label2 = mFilmShowInfo.getLabel2().trim();
            if (label2.equals("")) {
                holder.mLabel2.setVisibility(View.GONE);
            } else {
                holder.mLabel2.setText(label2);
            }
            String label3 = mFilmShowInfo.getLabel3().trim();
            if (label3.equals("")) {
                holder.mLabel3.setVisibility(View.GONE);
            } else {
                holder.mLabel3.setText(label3);
            }
            float price = mFilmShowInfo.getPrice();
            if (price == 0) {
                holder.mPayKind.setText("免费");
            } else {
                holder.mPayKind.setText("" + price + "元/每人");
            }
            String fname = mFilmShowInfo.getFname();
            holder.mShowName.setText(fname);
            String fdate = mFilmShowInfo.getFdate();
            String substring = fdate.substring(0, 10);
            String faddress = mFilmShowInfo.getFaddress();
            if (fdate.equals("")) {
                holder.mTimePlace.setText(faddress);
            } else {
                holder.mTimePlace.setText(substring + " | " + faddress);
            }

            holder.mLl_film_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), ShowsDetailActivityNew.class);
                    FilmShowInfo info = (FilmShowInfo) mData.get(position);
                    mFilmId = info.getId();
                    markShow = (FilmShowInfo) mData.get(position);
                    intent.putExtra("filmRoom", mFilmId);
                    intent.putExtra("modelId", 7);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            //            return mData.size();
            //            return 10;
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView    mImg; //话剧显示图片
            //            Button       mOrder;  //是否订阅
            TextView     mLabel1;  //标签1
            TextView     mLabel2;  //标签2
            TextView     mLabel3;  //标签3
            TextView     mPayKind; //付费种类
            TextView     mShowName;//活动的名称
            TextView     mTimePlace;//时间和地点
            LinearLayout mLl_film_item;

            public ViewHolder(View itemView) {
                super(itemView);
                mLl_film_item = (LinearLayout) itemView.findViewById(R.id.ll_film_item);
                mImg = (ImageView) itemView.findViewById(R.id.img_show_picture);
                //                mOrder = (Button) itemView.findViewById(R.id.bt_order);
                mLabel1 = (TextView) itemView.findViewById(R.id.tv_label_1);
                mLabel2 = (TextView) itemView.findViewById(R.id.tv_label_2);
                mLabel3 = (TextView) itemView.findViewById(R.id.tv_label_3);
                mPayKind = (TextView) itemView.findViewById(R.id.tv_isfree);
                mShowName = (TextView) itemView.findViewById(R.id.tv_show_name);
                mTimePlace = (TextView) itemView.findViewById(R.id.tv_time_place);
            }
        }
    }

    class MyListViewAdapter extends BaseAdapter {
        String[]     mString = new String[]{};
        List<String> mList   = new ArrayList<>();

        public MyListViewAdapter(String[] mStrings) {
            this.mString = mStrings;
        }

        public MyListViewAdapter(ArrayList mArrayList) {
            this.mList = mArrayList;
        }

        @Override
        public int getCount() {
            if (mString.length != 0) {
                return mString.length;
            }
            if (mList.size() != 0) {
                return mList.size();
            }
            return 0;
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
            convertView = View.inflate(getBaseContext(), R.layout.list_item_club_type, null);
            TextView area_item = (TextView) convertView.findViewById(R.id.tv_list_type);
            if (mString.length != 0) {
                area_item.setText(mString[position]);
                area_item.setBackgroundColor(Color.WHITE);
                area_item.setTextColor(Color.GRAY);
                if (position != 0) {
                    if (mSelectedColor == position) {
                        area_item.setBackgroundColor(Color.GRAY);
                        area_item.setTextColor(Color.WHITE);
                    }
                }
            }

            if (mList.size() != 0) {
                area_item.setText(mList.get(position));
                area_item.setTextColor(Color.WHITE);
                area_item.setBackgroundColor(Color.GRAY);
            }
            return convertView;
        }
    }
}
