package com.guangxi.culturecloud.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvis.MyView.MyListView;
import com.guangxi.culturecloud.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/24 15:13
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class SpaceIntangibleheritageFragment extends Fragment {
    private View mRootView;
    private MyListView mMyListview;
    private ArrayList mArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_space_intangibleheritage, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mMyListview = (MyListView) mRootView.findViewById(R.id.mylistview_intangibleheritage);

        mArrayList=new ArrayList();
        mArrayList.add("你好");
        mArrayList.add("你好");
        mArrayList.add("你好");
        mArrayList.add("你好");
    }

    private void initData() {
        MylistviewAdapter mylistviewAdapter = new MylistviewAdapter(mArrayList);
        mMyListview.setAdapter(mylistviewAdapter);
        mMyListview.setFocusable(false);
    }

    class MylistviewAdapter extends BaseAdapter{
        List mList;

        public MylistviewAdapter(List mList){
            this.mList=mList;
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
            if (convertView==null){
                convertView = View.inflate(getActivity(),R.layout.list_item_intangibleheritage,null);
                myViewholder=new MyViewholder();
                myViewholder.img_show= (ImageView) convertView.findViewById(R.id.img_show);
                myViewholder.tv_show_name= (TextView) convertView.findViewById(R.id.tv_show_name);
                myViewholder.tv_level= (TextView) convertView.findViewById(R.id.tv_level);
                myViewholder.tv__place= (TextView) convertView.findViewById(R.id.tv__place);
                myViewholder.tv_years= (TextView) convertView.findViewById(R.id.tv_years);
                convertView.setTag(myViewholder);
            }else {
                myViewholder = (MyViewholder) convertView.getTag();
            }

            return convertView;
        }

        class MyViewholder{
            ImageView img_show;//非遗活动图片
            TextView tv_show_name;//活动名称
            TextView tv_level;//级别（国家级）
            TextView tv__place;//地址区域
            TextView tv_years;//年代

        }
    }

}
