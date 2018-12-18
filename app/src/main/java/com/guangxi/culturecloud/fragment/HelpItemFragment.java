package com.guangxi.culturecloud.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guangxi.culturecloud.R;

/**
 * @创建者 AndyYan
 * @创建时间 2018/1/9 15:39
 * @描述 ${TODO}
 * @更新者 $Author$
 * @更新时间 $Date$
 * @更新描述 ${TODO}
 */
public class HelpItemFragment extends Fragment {
    private View                       mRootView;
    private ListView mListView;
    private String[] mStrings = {"活动预定流程","场馆活动室预定流程","如何取消订单","注册流程"};
//    private String[] mWebUrl = {"abc.html","asd.html","asd.html","fg.html","sad.html"};
    private String mStringUrl;
    private ImageView mImg_back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_help_item, null);
        initView();
        initData();
        return mRootView;
    }

    private void initView() {
        mImg_back = (ImageView)mRootView.findViewById(R.id.img_back);
        mListView = (ListView) this.mRootView.findViewById(R.id.list_item);
    }

    private void initData() {
        MyFragAdapter myFragAdapter = new MyFragAdapter(mStrings);
        mListView.setAdapter(myFragAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mStringUrl=mWebUrl[position];
                mStringUrl=""+position;
                //替换fragment//展示web帮助
                HelpItemWebFragment helpWebFragment = new HelpItemWebFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction mFt = fragmentManager.beginTransaction();
                //进行fragment操作:
                mFt.add(R.id.frame, helpWebFragment, "helpItemWebFragment");
                mFt.addToBackStack("helpItemWebFragment");
                //提交事务
                mFt.commit();
            }
        });
        mImg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
    public String getStrUrl(){
        return mStringUrl;
    }

    class MyFragAdapter extends BaseAdapter{
        private String[] mData;
        public MyFragAdapter(String[] mStrings){
            this.mData=mStrings;
        }

        @Override
        public int getCount() {
            return mData==null?0:mData.length;
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
                convertView=View.inflate(getActivity(),R.layout.list_item_help,null);
                viewHolder = new MyViewHolder();
                viewHolder.tv_help_which= (TextView) convertView.findViewById(R.id.tv_help_which);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyViewHolder) convertView.getTag();
            }
            viewHolder.tv_help_which.setText(mData[position]);

            return convertView;
        }
        class MyViewHolder {
            TextView tv_help_which;
        }
    }
}
