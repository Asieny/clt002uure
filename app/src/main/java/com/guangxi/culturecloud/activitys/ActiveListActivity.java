package com.guangxi.culturecloud.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.model.VenueDetailsInfo;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;

/**
 * @创建者 AndyYan
 * @创建时间 2018/2/5 12:33
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ActiveListActivity extends BaseActivity {
    private ListView mListview_active;
    public static List   mListEntryBeen = new ArrayList();//活动数据集合
    private       String mURLHeadString = NetConfig.IMG;
    private ImageView mIv_back;
    private String    mUrl_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_list);
        Intent intent = getIntent();
        mUrl_address = intent.getStringExtra("url_address");
        mURLHeadString = intent.getStringExtra("get_img_address");
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mListview_active = (ListView) findViewById(R.id.listview_active);
    }

    private void initData() {
        mIv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MyShowAdapter myShowAdapter = new MyShowAdapter((ArrayList) mListEntryBeen);
        mListview_active.setAdapter(myShowAdapter);
        mListview_active.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VenueDetailsInfo.ArrBean.ListEntryBean listEntryBean = (VenueDetailsInfo.ArrBean.ListEntryBean) mListEntryBeen.get(position);
                String tb_name = listEntryBean.getTb_name();
                Intent intent = new Intent(getBaseContext(), ShowsDetailActivityNew.class);
//                String url_address = listEntryBean.getUrl_address();
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
                intent.putExtra("showImgUrl", mURLHeadString);
                intent.putExtra("filmRoom", listEntryBean.getId());
                intent.putExtra("url_address", mUrl_address);
                startActivity(intent);
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
            ImageLoaderUtil.displayImageIcon(mURLHeadString + newpic, myShowHolder.img_show_pic);//活动图片
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
}
