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
import com.guangxi.culturecloud.model.VenueDetailsInfo;

import java.util.ArrayList;
import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/28 11:31
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class ActRoomListActivity extends BaseActivity implements View.OnClickListener {
    public static ArrayList<VenueDetailsInfo.ArrBean.ListPlayroomBean> mListPlayroomBeen;
    private       ImageView                                            mIv_back;
    private       TextView                                             mLogin;
    private       ListView                                             mListview_active;
    private       String                                               mUrl_address;
    private       String                                               mGet_img_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_list);
        Intent intent = getIntent();
        mUrl_address = intent.getStringExtra("url_address");
        mGet_img_address = intent.getStringExtra("get_img_address");
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mLogin = (TextView) findViewById(R.id.login);
        mListview_active = (ListView) findViewById(R.id.listview_active);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        mLogin.setText("相关活动室");
        MyActRoomAdapter actRoomAdapter = new MyActRoomAdapter(mListPlayroomBeen);
        mListview_active.setAdapter(actRoomAdapter);
        mListview_active.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转活动室详情
                Intent intent = new Intent(getBaseContext(), ActRoomDetailActivity.class);
                VenueDetailsInfo.ArrBean.ListPlayroomBean listPlayroomBean = mListPlayroomBeen.get(position);
                intent.putExtra("actRoomID", listPlayroomBean.getId());
                intent.putExtra("url_address", mUrl_address);
                intent.putExtra("get_img_address", mGet_img_address);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
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
            ImageLoaderUtil.displayImageIcon(mGet_img_address + newpic, myShowHolder.img_act_room_pic);//活动图片
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
}
