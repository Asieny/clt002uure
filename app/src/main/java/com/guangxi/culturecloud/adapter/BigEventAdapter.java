package com.guangxi.culturecloud.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangxi.culturecloud.global.NetConfig;
import com.guangxi.culturecloud.model.BigEventInfo;
import com.guangxi.culturecloud.R;

import cn.com.mytest.adapter.AbsAdapter;
import cn.com.mytest.util.ImageLoaderUtil;

import static com.guangxi.culturecloud.R.id.tv_address;
import static com.guangxi.culturecloud.R.id.tv_name;

/**
 * Created by zww on 2017/12/19.
 */

public class BigEventAdapter extends AbsAdapter<BigEventInfo> {
    public BigEventAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        if (currentView == null) {
            currentView = getInflater().inflate(R.layout.adapter_listview_tao, parent, false);
        }
        TextView tvNumber=(TextView) currentView.findViewById(R.id.tv_number);
        TextView tvLabel1=(TextView) currentView.findViewById(R.id.tv_label1);
        TextView tvLabel2=(TextView) currentView.findViewById(R.id.tv_label2);
        TextView tvLabel3=(TextView) currentView.findViewById(R.id.tv_label3);
        TextView tvName=(TextView) currentView.findViewById(tv_name);
        TextView tvAddress=(TextView) currentView.findViewById(tv_address);
        ImageView iv_pic=(ImageView) currentView.findViewById(R.id.iv_adapter_list_pic);

        ImageLoaderUtil.displayImageIcon(NetConfig.IMG+getItem(position).newpic, iv_pic);
        if (getItem(position).price==0){
            tvNumber.setText("免费");
        }else {
            tvNumber.setText(getItem(position).price+"/张");
        }
        tvLabel1.setText(getItem(position).label1);
        tvLabel2.setText(getItem(position).label2);
        tvLabel3.setText(getItem(position).label3);
        tvName.setText(getItem(position).fname);
        String fdate = getItem(position).fdate;
        String faddress = getItem(position).faddress;
        tvAddress.setText(fdate+" | "+faddress);

        return currentView;
    }

    public String getId(int position){
        return getItem(position).id;
    }
    public String getTableName(int position){
        return getItem(position).tb_name;
    }
    public String getType(int position){
        return getItem(position).tb_type;
    }
}
