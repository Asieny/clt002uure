package com.javis.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guangxi.culturecloud.global.NetConfig;
import com.jarvis.mytaobao.Data.RecommendBean;
import com.guangxi.culturecloud.R;

import java.util.List;

import cn.com.mytest.util.ImageLoaderUtil;

public class Adapter_ListView_tao extends BaseAdapter {
	private Context context;
	/**图片*/
	private List<RecommendBean> recommendBean;
	
	public Adapter_ListView_tao(Context context, List<RecommendBean> recommendBean){
		this.context=context;
		this.recommendBean =recommendBean;
	}
	
	@Override
	public int getCount() {
		return recommendBean.size();
	}

	@Override
	public RecommendBean getItem(int arg0) {
		return recommendBean.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View currentView, ViewGroup arg2) {
		HolderView holderView=null;
		if (currentView==null) {
			holderView=new HolderView();
			currentView=LayoutInflater.from(context).inflate(R.layout.adapter_listview_tao, null);
			holderView.tvNumber=(TextView) currentView.findViewById(R.id.tv_number);
			holderView.tvLabel1=(TextView) currentView.findViewById(R.id.tv_label1);
			holderView.tvLabel2=(TextView) currentView.findViewById(R.id.tv_label2);
			holderView.tvLabel3=(TextView) currentView.findViewById(R.id.tv_label3);
			holderView.tvName=(TextView) currentView.findViewById(R.id.tv_name);
			holderView.tvAddress=(TextView) currentView.findViewById(R.id.tv_address);
			holderView.iv_pic=(ImageView) currentView.findViewById(R.id.iv_adapter_list_pic);


			currentView.setTag(holderView);
		}else {
			holderView=(HolderView) currentView.getTag();
		}
		ImageLoaderUtil.displayImageIcon(NetConfig.IMG+getItem(position).getImage(), holderView.iv_pic);
		double number = getItem(position).getNumber();
		if (number==0){
			holderView.tvNumber.setText("免费");
		}else {
			holderView.tvNumber.setText(number+"/张");
		}
		holderView.tvLabel1.setText(getItem(position).getLabel1());
		holderView.tvLabel2.setText(getItem(position).getLabel2());
		holderView.tvLabel3.setText(getItem(position).getLabel3());
		holderView.tvName.setText(getItem(position).getName());
		String fdate = getItem(position).getFdate();
		holderView.tvAddress.setText(fdate+" | "+getItem(position).getAddress());

		return currentView;
	}
	
	
	public class HolderView {
		private ImageView iv_pic;
		private TextView tvNumber;
		private TextView tvLabel1;
		private TextView tvLabel2;
		private TextView tvLabel3;
		private TextView tvName;
		private TextView tvAddress;



	}

}
