package com.guangxi.culturecloud.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guangxi.culturecloud.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/25 9:53
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class CulturalAttractionsFragment extends Fragment {

    private View      mRootView;
    private ListView  mListview_view_spot;
    private ArrayList mArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_space_cultural_attractions, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mListview_view_spot = (ListView) mRootView.findViewById(R.id.listview_view_spot);
        mArrayList = new ArrayList();
        mArrayList.add("nihao");
        mArrayList.add("nihao");
        mArrayList.add("nihao");
        mArrayList.add("nihao");
        mArrayList.add("nihao");
        mArrayList.add("nihao");
        mArrayList.add("nihao");
    }

    private void initData() {

        MylistviewAdapter mylistviewAdapter = new MylistviewAdapter(mArrayList);
        mListview_view_spot.setAdapter(mylistviewAdapter);
    }

    class MylistviewAdapter extends BaseAdapter {
        List mList;

        public MylistviewAdapter(List mList) {
            this.mList = mList;
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
            MyViewholder myViewholder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.list_item_space_museum, null);
                myViewholder = new MyViewholder();
                myViewholder.img_museum_show = (ImageView) convertView.findViewById(R.id.img_museum_show);
                myViewholder.tv_museum_title = (TextView) convertView.findViewById(R.id.tv_museum_title);
                myViewholder.tv_museum_place = (TextView) convertView.findViewById(R.id.tv_museum_place);
                myViewholder.tv_museum_act_num = (TextView) convertView.findViewById(R.id.tv_museum_act_num);
                convertView.setTag(myViewholder);
            } else {
                myViewholder = (MyViewholder) convertView.getTag();
            }

            return convertView;
        }

        class MyViewholder {
            ImageView img_museum_show;//博物馆展示照
            TextView  tv_museum_title;//博物馆名称
            TextView  tv_museum_place;//博物馆地址
            TextView  tv_museum_act_num;//博物馆活动数
        }
    }
}
