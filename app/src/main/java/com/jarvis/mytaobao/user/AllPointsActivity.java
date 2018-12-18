package com.jarvis.mytaobao.user;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guangxi.culturecloud.R;
import com.guangxi.culturecloud.activitys.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/3/20 16:19
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class AllPointsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView          mIv_back;
    private ListView           mListview_all_points;
    private SmartRefreshLayout mSmart_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_points);
        initView();
        initData();
    }

    private void initView() {
        mIv_back = (ImageView) findViewById(R.id.iv_back);
        mSmart_refresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        mListview_all_points = (ListView) findViewById(R.id.listview_all_points);
    }

    private void initData() {
        mIv_back.setOnClickListener(this);
        List list = new ArrayList();
        list.add("哈哈哈");
        list.add("哈哈哈");
        list.add("哈哈哈");
        MyAdapter myAdapter = new MyAdapter(list);
        mListview_all_points.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
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
                convertView = View.inflate(AllPointsActivity.this, R.layout.list_item_point, null);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_plus_point = (TextView) convertView.findViewById(R.id.tv_plus_point);
                viewHolder.tv_explain = (TextView) convertView.findViewById(R.id.tv_explain);
                viewHolder.tv_complete_time = (TextView) convertView.findViewById(R.id.tv_complete_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }

            return convertView;
        }
    }

    class MyViewHolder {
        TextView tv_title;
        TextView tv_plus_point;
        TextView tv_explain;
        TextView tv_complete_time;
    }
}
