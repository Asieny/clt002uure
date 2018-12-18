package com.guangxi.culturecloud.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.model.LibraryInfo;
import com.guangxi.culturecloud.R;

import cn.com.mytest.adapter.AbsAdapter;
import cn.com.mytest.util.ImageLoaderUtil;

/**
 * Created by zww on 2017/12/19.
 */

public class LibraryAdapter extends AbsAdapter<LibraryInfo> {
    public LibraryAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(R.layout.item_list_library, parent, false);
        }
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
        ImageView img_face = (ImageView) convertView.findViewById(R.id.img_face);

        tv_name.setText(getItem(position).fname);
        tv_address.setText("地点：" + getItem(position).faddress);
        ImageLoaderUtil.displayImageIcon(NetConfig.IMG+getItem(position).newpic, img_face);
        tv_time.setText("时间：" + getItem(position).fdate);

        return convertView;
    }
}
