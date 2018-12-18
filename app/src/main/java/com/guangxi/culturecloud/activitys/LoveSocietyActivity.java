package com.guangxi.culturecloud.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.http.HttpUtil;
import com.guangxi.culturecloud.model.Society;
import com.guangxi.culturecloud.model.UserInfo;
import com.guangxi.culturecloud.utils.ProgressDialogUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
 * @创建时间 2017/12/25 14:28
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class LoveSocietyActivity extends BaseActivity {

    private ListView       mLoveSocietyList;//社团条目展示
    private Button         mEnterCommunity;//加入社团
    private Button         mToTop;
    private ImageView      mIv_back;
    private ProgressDialog progressDialog;
    private List<Society>  mList; //存储社团信息对象
    private Society        mSociety;
    private String mURLHeadString  = NetConfig.IMG;
    private String mLoveSocietyUrl = NetConfig.LOVE_SOCIETY;
    private ImageView mImg_intnet;
    private        String UserID       = NetConfig.UserID;
    private static int    mRequestCode = 10000;
    private SmartRefreshLayout mSwipe_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lovesociety);
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mSwipe_refresh = (SmartRefreshLayout) findViewById(R.id.swipe_refresh);
        mLoveSocietyList = (ListView) findViewById(R.id.list_love_society);
        mEnterCommunity = (Button) findViewById(R.id.enter_community);
        mToTop = (Button) findViewById(R.id.to_top);
        mImg_intnet = (ImageView) findViewById(R.id.img_intnet);
    }

    private void initData() {
        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
        if (userInfo == null) {
            UserID = NetConfig.UserID;
        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
            UserID = NetConfig.UserID;
        } else {
            UserID = userInfo.member_id;
        }
        //获取网络数据
        getIntnetData();

        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwipe_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //获取网络数据
                getIntnetData();
            }
        });
    }

    private void getIntnetData() {
        HttpUtil.get(mLoveSocietyUrl, new HttpUtil.JsonHttpResponseUtil() {
            @Override
            public void onStart() {
                super.onStart();
                ProgressDialogUtil.startShow(LoveSocietyActivity.this,"正在加载，请稍后");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ProgressDialogUtil.hideDialog();
                mImg_intnet.setVisibility(View.GONE);
                mSwipe_refresh.finishRefresh();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                if (null == mList) {
                    mList = new ArrayList<>();
                } else {
                    mList.clear();
                }
                Gson gson = new Gson();
                Society society;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("arr");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject societyData = jsonArray.getJSONObject(i);
                        society = gson.fromJson(societyData.toString(), Society.class);
                        //设置完数据，放入list集合
                        mList.add(society);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mEnterCommunity.setVisibility(View.VISIBLE);
                mToTop.setVisibility(View.VISIBLE);
                final MyAdapter myAdapter = new MyAdapter();
                mLoveSocietyList.setAdapter(myAdapter);
                //设置条目点击，跳转社团详细展示
                mLoveSocietyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getBaseContext(), ClubDetailsActivity.class);
                        intent.putExtra("clubId", mList.get(position).getId());
                        startActivity(intent);
                    }
                });
                //跳转申请社团界面
                mEnterCommunity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo userInfo = SPref.getObject(getBaseContext(), UserInfo.class, "userinfo");
                        if (userInfo == null) {
                            UserID = NetConfig.UserID;
                        } else if (userInfo.member_id.trim().equals("") || userInfo.member_id.equals(NetConfig.UserID)) {
                            UserID = NetConfig.UserID;
                        } else {
                            UserID = userInfo.member_id;
                        }
                        if (UserID.equals(NetConfig.UserID)) {
                            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivityForResult(intent, mRequestCode);
                        } else {
                            Intent intent = new Intent(getBaseContext(), DetailsJoinActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                //跳转到listView的第一行
                mToTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoveSocietyList.smoothScrollToPosition(0);//跳转到第一条内容处
                    }
                });
            }
        });
    }

    class MyAdapter extends BaseAdapter {
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
                convertView = View.inflate(getBaseContext(), R.layout.list_item_community, null);
                viewHolder = new MyViewHolder();
                viewHolder.img_community = (ImageView) convertView.findViewById(R.id.img_community);
                viewHolder.img_club = (CircleImageView) convertView.findViewById(R.id.img_club);
                viewHolder.tv_praise = (TextView) convertView.findViewById(R.id.tv_praise);
                viewHolder.name_authentication = (TextView) convertView.findViewById(R.id.name_authentication);
                viewHolder.credentials_authentication = (TextView) convertView.findViewById(R.id.credentials_authentication);
                viewHolder.tv_clubName = (TextView) convertView.findViewById(R.id.tv_clubName);
                viewHolder.tv_Label_1 = (TextView) convertView.findViewById(R.id.tv_Label_1);
                viewHolder.tv_Label_2 = (TextView) convertView.findViewById(R.id.tv_Label_2);
                viewHolder.tv_Label_3 = (TextView) convertView.findViewById(R.id.tv_Label_3);
                viewHolder.tv_introduce = (TextView) convertView.findViewById(R.id.tv_introduce);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            //
            mSociety = mList.get(position);
            String newfirst_img = mSociety.getNewfirst_img();
            String url_img = mURLHeadString + newfirst_img;
            ImageLoaderUtil.displayImageIcon(url_img, viewHolder.img_community);
            String logo = mSociety.getlogo();
            String url_logo = mURLHeadString + logo;
            ImageLoaderUtil.displayImageIcon(url_logo, viewHolder.img_club);
            int smrz = mSociety.getSmrz();
            if (smrz == 0) {
                viewHolder.name_authentication.setText("未实名认证");
            } else {
                viewHolder.name_authentication.setText("实名认证");
            }
            int zzrz = mSociety.getZzrz();
            if (zzrz == 0) {
                viewHolder.credentials_authentication.setText("未资质认证");
            } else {
                viewHolder.credentials_authentication.setText("资质认证");
            }

            viewHolder.tv_praise.setText("" + mSociety.getSumJiaohua());//设置浇花数
            viewHolder.tv_clubName.setText(mSociety.getFname());    //设置每个条目的小标题
            String label1 = mSociety.getLabel1().trim();
            String label2 = mSociety.getLabel2().trim();
            String label3 = mSociety.getLabel3().trim();
            if (label1.equals("")) {
                viewHolder.tv_Label_1.setVisibility(View.GONE);
            } else {
                viewHolder.tv_Label_1.setText(label1);//设置标签1内容
            }
            if (label2.equals("")) {
                viewHolder.tv_Label_2.setVisibility(View.GONE);
            } else {
                viewHolder.tv_Label_2.setText(label2);//设置标签2内容
            }
            if (label3.equals("")) {
                viewHolder.tv_Label_3.setVisibility(View.GONE);
            } else {
                viewHolder.tv_Label_3.setText(label3);//设置标签3内容
            }
            String society_detail = mSociety.getDetail_title_sx();
            //            String s = society_detail.replaceAll("<p>", "");
            Spanned spanned = Html.fromHtml(society_detail);
            viewHolder.tv_introduce.setText(spanned);//设置社团简介
            return convertView;
        }
    }

    class MyViewHolder {
        ImageView img_community;//社团图片
        ImageView img_club;//社团唱片图
        TextView  tv_praise;//获赞数
        TextView  tv_clubName;//社团名称
        TextView  tv_Label_1;//社团标签1
        TextView  tv_Label_2;//社团标签2
        TextView  tv_Label_3;//社团标签3
        TextView  tv_introduce;//社团介绍
        TextView  name_authentication;   //实名认证
        TextView  credentials_authentication;    //资质认证
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            initData();
        }
    }
}
